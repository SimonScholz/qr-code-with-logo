# Contributing

Thank you for considering contributing to **qr-code-with-logo**! Whether it's reporting bugs,
suggesting enhancements, improving documentation, or submitting code changes, your contributions
are greatly appreciated.

By participating in this project, you agree to abide by our [Code of Conduct](CODE_OF_CONDUCT.md).

## Ways to contribute

- **Report a bug** — open an issue using the *Bug report* template.
- **Request a feature** — open an issue using the *Feature request* template.
- **Improve the docs** — fixes to `README.adoc` or module READMEs are very welcome.
- **Submit code** — see the workflow below.

## Project layout

The root Gradle build includes four modules:

- **`qr-code`** — the core library (raster `BufferedImage` output via zxing). **Java 8 toolchain.** Published.
- **`qr-code-svg`** — SVG output via Apache Batik. Depends on `:qr-code`. **Java 8 toolchain.** Published.
- **`qr-code-app`** — Swing desktop GUI for designing QR codes. Java 17 toolchain.
- **`kotlin-sample`** — runnable examples of the API.

> `java-sample/` is a **separate, standalone Gradle build** with its own `gradlew`. It consumes the
> *published* artifacts from Maven Central to verify Java interop, so changes to `:qr-code` are only
> visible there after publishing. It is not part of the root build.

## Development setup

You need a JDK to run Gradle (the build uses toolchains to compile the published modules against
Java 8). Run everything through the Gradle wrapper:

```bash
./gradlew build                  # build + test all modules
./gradlew test                   # run all tests
./gradlew :qr-code:test          # one module's tests
./gradlew :qr-code-app:run       # launch the Swing app
```

To work on `java-sample`, `cd java-sample` and use its own `./gradlew`.

## Coding standards

- Formatting is enforced by **Spotless** (ktlint). `spotlessCheck` runs as part of `build`, so run
  the formatter before pushing:

  ```bash
  ./gradlew spotlessApply
  ```

- **Keep the published modules Java 8 compatible.** Do not introduce APIs newer than Java 8 in
  `qr-code` or `qr-code-svg`. The `qr-code-app` module targets Java 17.
- Public API follows the `*Factory` → `*Api` interface → internal implementation pattern. Anything
  under an `internal` package is implementation detail and must not become part of the published
  contract — keep new internals there.
- For Java interop, keep the `QrCodeConfig` primary constructor defaults and the `Builder` defaults
  in sync, and annotate accordingly (`@JvmStatic`, `@JvmOverloads`).

## Submitting changes

1. Fork the repository and clone your fork.
2. Create a branch for your feature or fix (e.g. `feature/custom-dot-shape`).
3. Make your changes and add tests where it makes sense.
4. Run `./gradlew spotlessApply` and `./gradlew build` and make sure both pass.
5. Commit with clear, descriptive messages.
6. Push your branch and open a pull request against `main`, describing what you changed and why.
   Link any related issue.

## Reporting security issues

Please do **not** open public issues for security vulnerabilities. Follow the process in
[SECURITY.md](SECURITY.md) instead.

If you have any questions, feel free to open an issue. Thank you for your contributions!
