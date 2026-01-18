# Contributing to JavaDump

We're thrilled you're considering contributing to JavaDump! This project thrives on community collaboration, and we appreciate your interest in making it better. Before diving in, please take a moment to review these guidelines to ensure a smooth contribution process.

## 🎯 Contribution Philosophy

At JavaDump, we believe in:
- **Pragmatic excellence** over academic perfection
- **Developer experience** as our primary metric
- **Zero-dependency** philosophy as a core tenet
- **Educational value** in our code and documentation

## 📋 Before You Start

### Quick Self-Check
- [ ] Have you searched existing issues/PRs to avoid duplication?
- [ ] Does your change align with JavaDump's vision of simple, zero-dependency debugging?
- [ ] Are you prepared to maintain your contribution long-term?

### Scope Considerations
We prioritize contributions that:
- Improve performance without sacrificing readability
- Add functionality while maintaining the simple API
- Fix bugs affecting real-world usage
- Enhance documentation and examples
- Add comprehensive test coverage

## 🛠 Development Workflow

### 1. Environment Setup

```bash
# Clone and navigate
git clone https://github.com/programmerjide/javadump.git
cd javadump

# Verify Java compatibility (Java 17+ required)
java --version

# Build and run sanity checks
mvn clean verify

# Run all tests (should pass)
mvn test
```

### 2. Implementation Standards

#### Code Organization
```
src/main/java/io/github/programmerjide/javadump/
├── core/          # Core dumping logic
├── formatter/     # Output formatters
├── analyzer/      # Data analysis utilities
├── config/        # Configuration models
└── util/          # Shared utilities
```

#### Naming Conventions
- **Methods**: `verbNoun()` - `formatHtml()`, `analyzeDependencies()`
- **Classes**: `NounVerb` - `HtmlFormatter`, `DiffAnalyzer`
- **Variables**: Descriptive, avoiding abbreviations
- **Packages**: Reverse DNS notation, lowercase

#### Code Quality Metrics
```bash
# Run quality checks
mvn clean compile
mvn spotbugs:check
mvn pmd:check
mvn checkstyle:check
```

### 3. Testing Requirements

#### Test Structure
```java
/**
 * Tests should follow this pattern:
 * 1. Given - Setup test conditions
 * 2. When - Execute the behavior
 * 3. Then - Assert expected outcomes
 */
@DisplayName("ClassName Behavior Tests")
class ClassNameTest {
    
    @Test
    @DisplayName("should [expected behavior] when [condition]")
    void test_specific_behavior() {
        // Arrange
        var input = createTestData();
        
        // Act
        var result = unitUnderTest.process(input);
        
        // Then
        assertThat(result)
            .hasExpectedProperty()
            .matches(expectedPattern());
    }
}
```

#### Coverage Requirements
```bash
# Minimum coverage thresholds
mvn clean test jacoco:check
# Line coverage ≥ 85%
# Branch coverage ≥ 75%
# Complexity coverage ≥ 80%
```

### 4. Performance Considerations

All contributions must pass performance benchmarks:
```bash
# Run microbenchmarks
mvn clean test -Dtest=BenchmarkTest

# Key metrics to watch:
# - Memory allocation per operation
# - Throughput (ops/second)
# - 99th percentile latency
```

## 📝 Pull Request Process

### PR Checklist
- [ ] **Title**: Descriptive, under 50 characters
- [ ] **Description**:
    - Context and motivation
    - Technical approach
    - Testing performed
    - Performance impact
    - Breaking changes (if any)
- [ ] **Linked Issues**: Closes #issue-number
- [ ] **Tests**: All new and existing tests pass
- [ ] **Documentation**: Updated README/Javadoc as needed
- [ ] **Changelog**: Entry added for user-facing changes

### Review Expectations
- **Typical timeline**: 2-5 business days for initial review
- **Review rounds**: Expect 1-3 iterations
- **Required approvals**: 1 maintainer approval minimum
- **CI must pass**: All checks green before merge

## 🔍 Code Review Guidelines

### What We Look For
```java
// ✅ Good: Clear, maintainable, efficient
public String formatValue(Object value) {
    if (value == null) return "null";
    return value.toString();
}

// ❌ Avoid: Clever but confusing
public String f(Object v) {
    return v == null ? "null" : v.toString();
}
```

### Review Focus Areas
1. **Correctness**: Does it work as intended?
2. **Performance**: Any unnecessary allocations or complexity?
3. **Maintainability**: Will others understand this in 6 months?
4. **Testing**: Are edge cases covered?
5. **Documentation**: Is the "why" explained?

## 🚀 Release Process

### Versioning (SemVer)
- **MAJOR**: Breaking API changes
- **MINOR**: Backward-compatible features
- **PATCH**: Backward-compatible bug fixes

### Release Checklist
```bash
# Pre-release validation
mvn clean deploy -DskipTests -P release
# 1. All tests pass
# 2. Documentation updated
# 3. Changelog complete
# 4. Performance benchmarks stable
# 5. Backward compatibility verified
```

## 🧪 Experimental Features

For significant new functionality:
1. Start with an **RFC** (Request for Comments) issue
2. Gather community feedback (2-week minimum)
3. Implement behind feature flags
4. Graduate to stable after 1 minor release

## 🆘 Getting Help

### Before Asking
- [ ] Check existing documentation
- [ ] Search closed issues
- [ ] Review code examples

### Support Channels
1. **GitHub Issues**: Bug reports and feature requests
2. **Discussion Forum**: Design discussions (coming soon)
3. **Stack Overflow**: Tag with `[javadump]`

## 📚 Learning Resources

- [Architecture Decision Records](./docs/adr/)
- [Performance Guidelines](./docs/performance/)
- [Extension Points](./docs/extensions/)
- [Testing Strategy](./docs/testing/)

## 🙏 Acknowledgments

Every contributor is recognized in:
- Project README (Major contributors)
- Release notes (Per-release contributions)
- CHANGELOG.md (All contributions)

---

**Remember**: The best contributions are those that make JavaDump more useful for developers while maintaining its core philosophy of simplicity and zero-dependency excellence.

Happy coding! 🚀

*The JavaDump Maintainers*