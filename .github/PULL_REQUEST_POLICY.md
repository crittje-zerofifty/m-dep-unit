# Pull Request Policy

## Overview
This document outlines the policy for submitting and reviewing Pull Requests (PRs) for the mDepUnit project. Following these guidelines ensures a smooth and efficient contribution process.

## Requirements

### 1. Ticket-Driven Development
- Every PR must be associated with a GitHub issue ticket
- Include the issue number in the PR title using the format: `[#123] Brief description of changes`
- Reference the issue in the PR description using the format: `Fixes #123` or `Relates to #123`

### 2. Review Process
- All PRs must be reviewed and approved by at least one project maintainer
- No PR should be merged without approval, even for minor changes
- Maintainers should provide constructive feedback when requesting changes

### 3. PR Content Guidelines
- Keep PRs focused on a single issue or feature
- Break large changes into smaller, more manageable PRs when possible
- Include appropriate tests for new features or bug fixes
- Ensure all CI checks pass before requesting review

### 4. PR Description
- Provide a clear description of the changes made
- Explain the reasoning behind implementation decisions
- Include steps to test the changes if applicable
- List any potential side effects or areas of concern

### 5. Code Quality
- Follow the project's coding standards and style guidelines
- Ensure code is properly documented if required for API docs
- Address all code review comments before merging

## Merge Process
Once a PR has been approved and all requirements are met, it can be merged into the main branch. The CI/CD pipeline will automatically:

1. Build the project
2. Run tests
3. Generate code coverage reports
4. Update badges and documentation as needed

## Additional Resources
- [GitHub's documentation on pull requests](https://docs.github.com/en/github/collaborating-with-pull-requests)
- [How to write a good commit message](https://chris.beams.io/posts/git-commit/)
