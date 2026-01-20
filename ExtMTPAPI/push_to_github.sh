#!/bin/bash

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║         GitHub Push Script - ExtMTPush SMS Gateway            ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Check if repository URL is provided
if [ -z "$1" ]; then
    echo "❌ Error: Repository URL required"
    echo ""
    echo "Usage:"
    echo "  ./push_to_github.sh <repository-url>"
    echo ""
    echo "Example:"
    echo "  ./push_to_github.sh https://github.com/Arunkumarsgithub/ExtMTPush-SpringBoot.git"
    echo ""
    echo "Or with SSH:"
    echo "  ./push_to_github.sh git@github.com:Arunkumarsgithub/ExtMTPush-SpringBoot.git"
    echo ""
    exit 1
fi

REPO_URL="$1"

echo "📋 Git Status"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
git status --short
echo ""

echo "📊 Repository Details"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Files committed: $(git ls-files | wc -l)"
echo "Branch: $(git branch --show-current)"
echo "Remote URL: $REPO_URL"
echo ""

# Check if remote already exists
if git remote | grep -q "origin"; then
    echo "⚠️  Remote 'origin' already exists. Removing..."
    git remote remove origin
fi

echo "🔗 Adding Remote Repository"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
git remote add origin "$REPO_URL"

if [ $? -eq 0 ]; then
    echo "✅ Remote added successfully"
else
    echo "❌ Failed to add remote"
    exit 1
fi

echo ""
echo "📤 Pushing to GitHub"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Pushing to: $REPO_URL"
echo ""

# Check current branch
BRANCH=$(git branch --show-current)

# Push to GitHub
git push -u origin "$BRANCH"

if [ $? -eq 0 ]; then
    echo ""
    echo "╔════════════════════════════════════════════════════════════════╗"
    echo "║                  ✅ SUCCESS!                                   ║"
    echo "╚════════════════════════════════════════════════════════════════╝"
    echo ""
    echo "Repository successfully pushed to GitHub!"
    echo ""
    echo "🔗 Repository URL:"
    echo "   $REPO_URL"
    echo ""
    echo "📊 What was pushed:"
    echo "   - $(git ls-files | wc -l) files"
    echo "   - Complete source code"
    echo "   - All documentation"
    echo "   - Postman collection"
    echo "   - Setup scripts"
    echo ""
    echo "🎯 Next Steps:"
    echo "   1. Visit your repository on GitHub"
    echo "   2. Add topics/tags (spring-boot, sms-gateway, java-17)"
    echo "   3. Update description"
    echo "   4. Share with team!"
    echo ""
else
    echo ""
    echo "╔════════════════════════════════════════════════════════════════╗"
    echo "║                  ❌ PUSH FAILED                                ║"
    echo "╚════════════════════════════════════════════════════════════════╝"
    echo ""
    echo "Common issues:"
    echo "  1. Authentication required - Use Personal Access Token"
    echo "  2. Repository doesn't exist - Create it on GitHub first"
    echo "  3. Permission denied - Check repository access"
    echo ""
    echo "See GITHUB_PUSH_GUIDE.md for detailed instructions"
    exit 1
fi

