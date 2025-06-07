# mDepUnit

[![codecov](https://codecov.io/gh/crittje-zerofifty/m-dep-unit/graph/badge.svg?token=EM5SUREB71)](https://codecov.io/gh/crittje-zerofifty/m-dep-unit)

## Motivation

Maven POM files can become complex and difficult to manage, especially when working with multiple team members on the same project. Without proper validation and enforcement of standards, POM files can quickly become messy and inconsistent, leading to dependency conflicts, versioning issues, and other problems that are difficult to debug.

mDepUnit was created to address these challenges by providing a way to verify your POM agreements early in the 
development process (at test time). By defining and enforcing rules for your POM files, you can ensure consistency 
across your projects and prevent common issues before they occur.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Installation

Add mDepUnit to your project as a dependency:

```xml
<dependency>
    <groupId>nl.zerofifty-it</groupId>
    <artifactId>mdepunit</artifactId>
    <version>[version]</version>
    <scope>test</scope>
</dependency>
```

## Usage

mDepUnit provides a fluent API for validating POM files. Here are some examples of how to use it:

### Basic Validation

```java
// Validate that dependencies don't have explicit version tags
analyzePom()
    .checking("dependencies.dependency")
    .shouldNot()
    .haveTag("version")
    .validate();
```

### Checking for Specific Values

```java
// Validate that modules contain a specific value
analyzePom()
    .checking("modules.module")
    .should()
    .containValue("article")
    .validate();
```

### Combining Validations with AND

```java
// Validate that dependencies have a groupId tag AND contain a specific value
analyzePom()
    .checking("dependencies.dependency")
    .should()
    .haveTag("groupId")
    .and()
    .containValue("org.mockito")
    .validate();
```

### Combining Validations with OR

```java
// Validate that dependencies have a groupId tag OR contain a specific value
analyzePom()
    .checking("dependencies.dependency")
    .should()
    .haveTag("groupId")
    .or()
    .containValue("org.mockito")
    .validate();
```

### Module-Specific Validations

```java
// Validate that modules contain a specific value
analyzePom()
    .checkingModule()
    .should()
    .containValue("article")
    .validate();

// Validate that a specific module exists and has a pom.xml file
analyzePom()
    .checkingModule()
    .should()
    .containModuleNode("test-module")
    .and()
    .moduleExists("test-module")
    .validate();
```

### Specify the POM file
You might want to target a specific pom file. That is possible. Start the analyzer as follows:
```java
// change 
analyzePom() // uses the pom of working directory
// to 
analyzePom("yourPomLocation.xml")
```

## Validation
There are two validation methods:
```java
.validate();
// and
.validate("A custom message to display when validation rules are violated");
```
By using the default `validate()` method a generic message is thrown that fails your test. However, if you need more 
motivation to the validation rule you can add your reasoning as a `String`. This is printed in addition to the 
generic message the fluent API supplies.

For example:
```java
// Validate that dependencies don't have explicit version tags
analyzePom("pom.xml")
    .checking("dependencies.dependency")
    .shouldNot()
    .haveTag("version")
    .validate("Because we have decided to have versions only in Dependency Management, See ADR-001 for more information");
```

## Integration with Unit Tests

You can integrate mDepUnit validations into your unit tests to ensure that your POM files comply with your team's standards. Here's an example:

```java
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static nl.zerofiftyit.mdepunit.core.validation.InitAnalyzer.analyzePom;

public class PomValidationTest {

    @Test
    public void testPomCompliance() throws IOException {
        // Ensure dependencies don't have explicit versions
        analyzePom()
            .checking("dependencies.dependency")
            .shouldNot()
            .haveTag("version")
            .validate();

        // Ensure all dependencies have groupId and artifactId
        analyzePom()
            .checking("dependencies.dependency")
            .should()
            .haveTag("groupId")
            .and()
            .haveTag("artifactId")
            .validate();
    }
}
```

By running these tests as part of your build process, you can catch POM issues early and ensure consistency across your project.

## Benefits

- **Early Detection**: Catch POM issues at test time rather than runtime
- **Consistency**: Enforce team standards for POM structure and content
- **Automation**: Integrate POM validation into your CI/CD pipeline
- **Flexibility**: Create custom validation rules for your specific project needs

## Contributing

Before submitting a Pull Request, please read our [PR Policy](.github/PULL_REQUEST_POLICY.md) to understand the requirements and process for contributing to this project.

## Deployment to Maven Central

This project is configured to automatically deploy to Maven Central when changes are merged to the main branch. For more information on how this works and how to set up the required secrets, see the [Maven Central Deployment](.github/MAVEN_CENTRAL_DEPLOYMENT.md) documentation.

## License

This project is licensed under the [MIT License](LICENSE). See the LICENSE file in the repository root for the full license text.
