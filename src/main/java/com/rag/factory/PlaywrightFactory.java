package com.rag.factory;

import com.microsoft.playwright.*;
import java.util.Properties;

public class PlaywrightFactory {
    Playwright playwright;
    Browser browser;
    BrowserContext browserContext;
    Page page;
    Properties prop;

    private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
    private static ThreadLocal<Page> tlPage = new ThreadLocal<>();
    private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();

    public static Playwright getPlaywright() {
        return tlPlaywright.get();
    }

    public static Browser getBrowser() {
        return tlBrowser.get();
    }

    public static BrowserContext getBrowserContext() {
        return tlBrowserContext.get();
    }

    public static Page getPage() {
        return tlPage.get();
    }

    public Page initBrowser(Properties prop) {
        String browserName = prop.getProperty("browser").trim();
        boolean headless = Boolean.parseBoolean(prop.getProperty("headless").trim());

        // System.out.println("Browser name is : " + browserName);

        if (getPlaywright() == null) {
            tlPlaywright.set(Playwright.create());
        }

        switch (browserName.toLowerCase()) {
            case "chromium":
                tlBrowser.set(getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless)));
                break;
            case "firefox":
                tlBrowser.set(getPlaywright().firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless)));
                break;
            case "safari":
                tlBrowser.set(getPlaywright().webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless)));
                break;
            case "chrome":
                tlBrowser.set(getPlaywright().chromium()
                        .launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(headless)));
                break;
            default:
                System.out.println("Please pass the right browser name......");
                break;
        }

        Browser.NewContextOptions options = new Browser.NewContextOptions()
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                .setViewportSize(1280, 800);
        tlBrowserContext.set(getBrowser().newContext(options));
        tlPage.set(getBrowserContext().newPage());
        
        // Removed console/error listeners as they generate excessive logs
        getPage().navigate(prop.getProperty("url").trim());
        getPage().waitForLoadState(com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED);
        
        return getPage();
    }

    public void closeBrowser() {
        if (getPage() != null) {
            getPage().close();
        }
        if (getBrowserContext() != null) {
            getBrowserContext().close();
        }
        if (getBrowser() != null) {
            getBrowser().close();
        }
        if (getPlaywright() != null) {
            getPlaywright().close();
        }

        tlPage.remove();
        tlBrowserContext.remove();
        tlBrowser.remove();
        tlPlaywright.remove();
    }
}
