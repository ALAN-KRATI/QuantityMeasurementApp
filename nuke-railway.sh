#!/bin/bash
# Nuke all Railway projects

RED='\033[0;31m'
NC='\033[0m'

echo -e "${RED}⚠️  DELETING ALL RAILWAY PROJECTS${NC}"
echo ""

# Get project IDs
PROJECTS=$(railway list 2>/dev/null | grep -E '^[a-f0-9-]{36}')

if [ -z "$PROJECTS" ]; then
    echo "No projects found"
    exit 0
fi

echo "Projects to delete:"
echo "$PROJECTS"
echo ""
read -p "Type 'NUKE' to delete all: " confirm

if [ "$confirm" = "NUKE" ]; then
    echo "$PROJECTS" | while read -r id name; do
        if [ -n "$id" ]; then
            echo "Deleting $id ($name)..."
            # Try different delete methods
            yes | railway delete "$id" 2>/dev/null || \
            railway delete "$id" --yes 2>/dev/null || \
            echo "Could not delete $id - try manually"
        fi
    done
    echo "Done!"
else
    echo "Cancelled"
fi
