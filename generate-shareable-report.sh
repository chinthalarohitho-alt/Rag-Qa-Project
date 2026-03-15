#!/bin/bash

# Path to the local Allure binary downloaded by Maven
ALLURE_BIN="./.allure/allure-2.25.0/bin/allure"
RESULTS_DIR="allure-results"
OUTPUT_DIR="allure-report-shareable"

# Check if Allure results exist
if [ ! -d "$RESULTS_DIR" ]; then
    echo "Error: Results directory '$RESULTS_DIR' not found."
    echo "Please run your tests first (e.g., 'mvn test') to generate results."
    exit 1
fi

# Try to find Allure binary if the hardcoded path fails
if [ ! -f "$ALLURE_BIN" ]; then
    echo "Allure binary not found at $ALLURE_BIN. Searching..."
    ALLURE_BIN=$(find .allure -name allure | head -n 1)
fi

# If still not found, try to download via Maven
if [ -z "$ALLURE_BIN" ] || [ ! -f "$ALLURE_BIN" ]; then
    echo "Allure CLI not found. Running 'mvn allure:install'..."
    mvn allure:install
    ALLURE_BIN=$(find .allure -name allure | head -n 1)
fi

if [ -z "$ALLURE_BIN" ] || [ ! -f "$ALLURE_BIN" ]; then
    echo "Error: Could not find or install Allure CLI."
    exit 1
fi

echo "Generating shareable Allure report..."
$ALLURE_BIN generate "$RESULTS_DIR" -o "$OUTPUT_DIR" --single-file --clean

if [ $? -eq 0 ]; then
    echo "-------------------------------------------------------"
    echo "Success! Report generated in: $OUTPUT_DIR/index.html"
    echo "You can now share this single HTML file with anyone."
    echo "They can open it directly in their browser without a server."
    echo "-------------------------------------------------------"
else
    echo "Error: Failed to generate Allure report."
    exit 1
fi
