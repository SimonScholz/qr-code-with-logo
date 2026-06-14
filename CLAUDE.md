# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

A Kotlin/JVM library (built on Google's zxing) for generating customizable QR codes — with logos, borders, custom dot shapes, colors, and positional-square styling — as raster `BufferedImage`s or SVG. Published to Maven Central as `io.github.simonscholz:qr-code-with-logo` and `...-svg`. Also ships a Swing desktop app for building QR codes interactively.

## Modules

The root Gradle build (`settings.gradle.kts`) includes four modules:

- **`qr-code`** — the core library. Produces raster `BufferedImage` QR codes via zxing. Java 8 toolchain. Published.
- **`qr-code-svg`** — SVG output via Apache Batik. Depends on `:qr-code`. Java 8 toolchain. Published.
- **`qr-code-app`** — Swing desktop GUI (MigLayout + Eclipse Core Databinding) for designing QR codes; can emit Kotlin/Java code (via KotlinPoet) reproducing the configured QR code. Java 17 toolchain. Packaged with shadow (fat jar), jlink, and GraalVM native-image.
- **`kotlin-sample`** — runnable example `main` functions demonstrating the API.

`java-sample/` is **a separate standalone Gradle build** (its own `gradlew` and `settings.gradle.kts`) that consumes the *published* artifacts rather than the local projects — it verifies Java interop and is not part of the root build. The CI matrix builds it against JDK 8 and 21.

## Common commands

Run from the repo root (the root build excludes `java-sample`):

```bash
./gradlew build                  # build + test all modules
./gradlew test                   # all tests
./gradlew :qr-code:test          # one module's tests
./gradlew :qr-code:test --tests "io.github.simonscholz.qrcode.CreateQrCodeTest"   # single test class
./gradlew spotlessApply          # auto-format (ktlint 1.8.0); spotlessCheck to verify only
./gradlew :qr-code-app:run       # launch the Swing app
./gradlew :qr-code-app:shadowJar # build runnable fat jar
```

Formatting is enforced by Spotless (`spotlessCheck` runs as part of `build`), so run `spotlessApply` before considering work done.

To work on `java-sample`, `cd java-sample` and use its own `./gradlew`; it pulls the library from Maven Central (snapshot repo configured in its `build.gradle.kts`), so changes to `:qr-code` are only visible there after publishing.

## Architecture & conventions

**Public API pattern (both library modules).** Each library exposes a `*Factory` object → `*Api` interface → internal implementation:
- `QrCodeFactory.createQrCodeApi()` returns a `QrCodeApi`; the impl lives in `qrcode.internal.api.QrCodeApiImpl`.
- `QrCodeSvgFactory` / `QrCodeSvgApi` mirror this for SVG.
- Everything under an `internal` package is implementation detail and not part of the published contract. Keep new internals there.

**Configuration objects.** `QrCodeConfig` (and its nested color/logo/border/positional-square configs) is the single input passed to the API. It provides both a primary constructor with defaults **and** a fluent `Builder`. This dual form exists for Java interop — annotate accordingly (`@JvmStatic`, `@JvmOverloads`) and keep the constructor defaults and Builder defaults in sync when adding parameters.

**QR content types.** `qrcode.types` (`VCard`, `VEvent`, `EpcGiroCode`, `SimpleTypes`) are helpers that build the *text payload* string encoded into a QR code (e.g. vCard, calendar event, SEPA giro/banking codes); they don't draw anything.

**Customization extension point.** `QrCodeDotStyler` controls dot rendering; `QrCodeDotShape` provides the built-in shapes. The SVG module reuses the core `QrCodeConfig` and adds `QrSvgLogoConfig` so an SVG file (not just a raster `Image`/base64) can be used as the logo.

**Java compatibility.** The two published library modules target **Java 8** deliberately; don't introduce APIs newer than that in `qr-code` / `qr-code-svg`. The app targets Java 17.

## Native image notes

`qr-code-app` supports GraalVM native-image compilation (`reflection-config.json`, `resource-config.json`, and the `nativeDist` task that bundles fontconfig). The AWT/font setup is fragile — see `qr-code-app/README.md` for the agent-run → `metadataCopy` → `nativeCompile` workflow and known limitations.

## Dependencies & versions

All dependency versions are centralized in `gradle/libs.versions.toml` (Gradle version catalog). The root build version is set in `gradle.properties`. The `ben-manes-versions` plugin (`./gradlew dependencyUpdates`) reports updates, configured to reject non-stable candidates.
