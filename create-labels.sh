#!/bin/bash

echo "Setting up labels for JavaDump repository..."

# Function to create or update label
create_or_update_label() {
    local name="$1"
    local color="$2"
    local description="$3"

    echo "Processing label: $name"
    gh label create "$name" --color "$color" --description "$description" --force 2>/dev/null

    if [ $? -eq 0 ]; then
        echo "✅ $name - created/updated"
    else
        echo "❌ $name - failed"
    fi
}

# Create/update all labels
create_or_update_label "examples" "0E8A16" "Related to example code and demonstrations"
create_or_update_label "good first issue" "7057FF" "Good for newcomers"
create_or_update_label "help wanted" "008672" "Extra attention is needed"
create_or_update_label "security" "D73A4A" "Security-related features"
create_or_update_label "showcase" "FBCA04" "Showcase/demo examples"
create_or_update_label "tracking" "D4C5F9" "Meta issues tracking progress"
create_or_update_label "priority: high" "B60205" "High priority"
create_or_update_label "priority: medium" "FBCA04" "Medium priority"
create_or_update_label "priority: low" "C2E0C6" "Low priority"

echo ""
echo "🎉 Label setup complete!"
echo ""
echo "Current labels:"
gh label list