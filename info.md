# RAG Test Automation Framework

A specialized test automation framework built to validate the accuracy and reliability of AI agents and Retrieval-Augmented Generation (RAG) systems. The framework ensures that AI responses are factually correct based on crawled website data using **Java**, **Playwright**, and **Cucumber (BDD)**.

## Technical Challenges

*   **Validating Non-Deterministic AI Responses**: Implemented flexible validation logic to verify factual correctness in naturally phrased AI outputs without requiring exact string matches.
*   **Testing RAG Memory & Context**: Designed multi-turn test scenarios to ensure the AI maintains session context and performs accurate reasoning across product categories.
*   **AI Safety & Reliability Testing**: Developed test suites to detect hallucinations (non-existent products) and verify resilience against basic prompt injection attempts.

## Key Projects Features

*   **Automated Knowledge Validation**: Verifies the AI's understanding of products, pricing, and category structures crawled from the target website.
*   **BDD-Driven AI Testing**: Leveraged Cucumber to define human-readable test cases for complex AI behaviors, enabling clear communication of expected AI performance.
*   **Unified Quality Reporting**: Integrated **Allure Report** to provide a visual overview of test results across various AI capabilities, from product knowledge to security.

## How to Run Tests with Tags

To run scenarios with a specific tag, use:
```bash
mvn test -Dcucumber.filter.tags="@tagName"
```

### Available Tags:
`@CrawlValidation` `@CategoryKnowledge` `@ProductKnowledge` `@CartKnowledge` `@Memory` `@Reasoning` `@Hallucination` `@Negative` `@Complex` `@Security` `@Performance`

### Logical Tag Expressions:
*   **OR Logic** (Runs tests with *at least one* of the tags):
    ```bash
    mvn test -Dcucumber.filter.tags="@Security or @Reasoning"
    ```
*   **AND Logic** (Runs tests that have *both* tags):
    ```bash
    mvn test -Dcucumber.filter.tags="@ProductKnowledge and @Performance"
    ```
*   **NOT Logic** (Excludes tests with a specific tag):
    ```bash
    mvn test -Dcucumber.filter.tags="not @Security"
    ```



## Tech Stack
`Java` `Playwright` `BDD (Cucumber)` `TestNG` `Maven` `Allure Reports`
