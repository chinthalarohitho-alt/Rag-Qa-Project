package com.rag.hooks;

import com.microsoft.playwright.Page;
import com.rag.factory.PlaywrightFactory;
import com.rag.utils.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.util.Properties;

public class ApplicationHooks {
    private PlaywrightFactory pf;
    private Page page;
    private ConfigReader cr;
    private Properties prop;

    @Before(order = 0)
    public void getProperty() {
        cr = new ConfigReader();
        prop = cr.init_prop();
    }

    @Before(order = 1)
    public void launchBrowser() {
        pf = new PlaywrightFactory();
        page = pf.initBrowser(prop);
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed() && page != null) {
                String screenshotName = scenario.getName().replaceAll(" ", "_");
                byte[] sourcePath = page.screenshot(new com.microsoft.playwright.Page.ScreenshotOptions().setFullPage(true));
                scenario.attach(sourcePath, "image/png", screenshotName);
            }
        } catch (Exception e) {
            System.err.println("Error taking screenshot: " + e.getMessage());
        } finally {
            if (pf != null) {
                pf.closeBrowser();
            }
        }
    }
}
