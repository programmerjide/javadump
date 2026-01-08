# Contributing to JavaDump

Thank you for your interest in contributing! 🎉

## How to Contribute

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Make your changes
4. Write tests for your changes
5. Ensure all tests pass: `mvn test`
6. Commit your changes: `git commit -m 'Add amazing feature'`
7. Push to your fork: `git push origin feature/amazing-feature`
8. Open a Pull Request

## Development Setup

```bash
# Clone your fork
git clone https://github.com/programmerjide/javadump.git
cd javadump

# Build the project
mvn clean install

# Run tests
mvn test

# Run with coverage
mvn clean test jacoco:report
```

## Code Style

- Follow standard Java conventions
- Write clear, self-documenting code
- Add JavaDoc for all public methods
- Keep methods small and focused
- Write tests for new features

## Testing

- All new features must include tests
- Aim for 80%+ code coverage
- Use JUnit 5 and AssertJ

## Questions?

Feel free to open an issue for any questions!
