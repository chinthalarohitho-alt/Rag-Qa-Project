package com.rag.pages;

import com.microsoft.playwright.Page;

public class BasePage {
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public String getPageTitle() {
        return page.title();
    }

    public String getPageUrl() {
        return page.url();
    }

    public void waitForPageLoad() {
        page.waitForLoadState();
    }
}
