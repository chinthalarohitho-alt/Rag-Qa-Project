package com.rag.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class AIChatPage extends BasePage {
    private String websiteInputPlaceholder = "https://yourwebsite.com";
    private String buildAgentButtonText = "Build my Agent";
    private String chatInputPlaceholder = "Start a new message";

    public AIChatPage(Page page) {
        super(page);
    }

    public void initializeWonderchat(String targetWebsite) {
        page.navigate("https://app.wonderchat.io/try");
        page.getByPlaceholder(websiteInputPlaceholder).fill(targetWebsite);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(buildAgentButtonText)).click();
        
        System.out.println("Waiting for Wonderchat agent to be built...");
        try {
            // Wait for the chat input to appear, signaling readiness. 
            // Training can take up to ~30-60 seconds.
            page.getByPlaceholder(chatInputPlaceholder).waitFor(new Locator.WaitForOptions().setTimeout(90000));
            // Add a small delay for safety as WebSocket might still be connecting
            page.waitForTimeout(3000); 
        } catch(Exception e) {
            throw new RuntimeException("Timeout waiting for Wonderchat agent to finish training.", e);
        }
    }

    public void askQuestion(String question) {
        Locator input = page.getByPlaceholder(chatInputPlaceholder);
        input.fill(question);
        page.keyboard().press("Enter");
        
        // Wait a small amount of time for the AI response bubble to appear
        page.waitForTimeout(1000); 
    }

    public String getLastResponse() {
        Locator msgContainer = page.locator("#msg-container");
        
        try {
            msgContainer.waitFor(new Locator.WaitForOptions().setTimeout(60000));
        } catch(Exception e) {
            throw new RuntimeException("Timeout waiting for AI response container.");
        }
        
        // Handle streaming response by polling the total text length
        String previousText = "";
        String currentText = "";
        int stableRetries = 0;
        int totalLimit = 60; // Max 60 seconds
        
        for (int i = 0; i < totalLimit; i++) {
            currentText = msgContainer.innerText();
            
            // If it's still "Searching knowledge base", keep waiting regardless of stability
            if (currentText.toLowerCase().contains("searching knowledge base")) {
                page.waitForTimeout(1000);
                continue;
            }
            
            // Once "Searching..." is gone, wait for the response to stabilize (streaming)
            // Ensure the text is not empty and has actually changed from the placeholder
            if (currentText.equals(previousText) && !currentText.trim().isEmpty() && !currentText.toLowerCase().contains("searching knowledge base")) {
                stableRetries++;
            } else {
                stableRetries = 0;
            }
            
            if (stableRetries >= 3) {
                break;
            }
            
            previousText = currentText;
            page.waitForTimeout(1000);
        }
        
        return currentText;
    }
}
