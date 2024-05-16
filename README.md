[![License](https://badgen.net/badge/license/MIT/blue?icon=kotlin)](https://github.com/TrustlyInc/trustly-android/blob/main/LICENSE)
[![Java Version](https://badgen.net/badge/java/8/orange?icon=java)](https://www.oracle.com/java/technologies/javase/jdk8-naming.html)
[![Platform](https://badgen.net/badge/Android/34/green?icon=java)](https://developer.android.com/about/versions/14)
[![Gradle Plugin](https://badgen.net/badge/gradle/v8.0/green?icon=groovy)](https://gradle.org/whats-new/gradle-8)
[![Maven Central Version](https://badgen.net/badge/maven/v1.0.0/yellow?icon=java)](https://central.sonatype.com/artifact/net.trustly/android-sdk/3.2.1)

# Trustly Android SDK

This repository contains the source code for the Trustly Android SDK, including interfaces and views that integrated with the web-based Trustly user interface module.

## Requirements
---
* Android 4.4 (API level 19) and above
* [Gradle](https://gradle.org/releases/) 8+
* [AndroidX](https://developer.android.com/jetpack/androidx/) (as of v11.0.0)

### Documentation and Guides

[Get started](https://amer.developers.trustly.com/payments/docs/getting-started) with Trustly concepts and sandbox environment testing.
Understand the functions and user flows of [Trustly UI](https://amer.developers.trustly.com/payments/docs/sdk).

## Installation
---

### Maven Central

TrustlySDK is available through [Maven Central](https://search.maven.org). To install
it, include the dependency below in your `build.gradle` file.

```
dependencies {
    implementation 'net.trustly:trustly-android-sdk:3.2.1'
}
```

## Usage

Quickly start integrating this SDK with the [Android Quickstart](https://amer.developers.trustly.com/payments/docs/android-quickstart) guide.

## Versions
___

| VERSION   | DESCRIPTION   | BRANCH |
| :-------: | :-----------  | :----------- |
3.2.1     | Removing allow backup flag from Application | *main*
3.2.0     | Support for Android API 19 | *main*
3.1.1     | Google Chrome app required message | *main*
3.1.0     | Widget adjustment by it's content | *main*
3.0.1     | Add Gradle configuration to release version | *main*
3.0.0     | Add Maven Central package manager support | *main*


## License
___

TrustlySDK is available under the MIT license. See the LICENSE file for more info.
[MIT License](https://github.com/TrustlyInc/trustly-android/blob/main/LICENSE)