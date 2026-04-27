# Trustly Android SDK v3.7.0 to v4.2.0 Migration Guide for Merchant Teams

## Who This Guide Is For

This document is for merchant Android engineers upgrading an existing Trustly Android SDK v3.7.0 integration to v4.2.0.

It is written as an implementation guide, not a codebase audit.

## What Changes for Your App

### High-impact changes

1. SDK dependency updates from 3.7.0 to 4.2.0.
2. v4 encapsulates secure-browser redirect handling inside the SDK:
   - `TrustlyCustomTabsManagerActivity`
   - `TrustlyRedirectActivity`
3. Merchant apps can remove manual redirect Activity implementations and set only the URL scheme resource:
    - `<string name="trustly_url_scheme">url_scheme</string>`
4. Callback routing is internally refactored through `TrustlyEvents`, while merchant-facing callback registration remains on `Trustly`/`TrustlyView`.

### What stays mostly the same

1. Core integration object remains `TrustlyView` implementing `Trustly`.
2. Main flow methods remain:
   - `selectBankWidget(establishData)`
   - `establish(establishData)`
   - `onBankSelected(...)`
   - `onReturn(...)`
   - `onCancel(...)`
   - `setListener(...)`
3. Required establish base fields remain the same (`accessId`, `merchantId`, `merchantReference`, `returnUrl`, `cancelUrl`, `requestSignature`).

---

## Migration at a Glance

### v3.7.0 to v4.2.0 mapping

| v3.7.0 pattern | v4.2.0 pattern |
|---|---|
| `implementation 'net.trustly:trustly-android-sdk:3.7.0'` | `implementation 'net.trustly:trustly-android-sdk:4.2.0'` |
| Deprecated `Trustly.Instance.create(context)` helper exists | Use `TrustlyView(context)` directly |
| `TrustlyView` contains env-based constructors (for example `TrustlyView(context, env)`) | Constructor simplified to `TrustlyView(context, attrs?)`; pass environment in establish data |
| Custom tabs launch handled directly in v3 utility path | v4 uses SDK activities for secure-browser flow and redirect callback handling |
| Callback invocation handled directly in `TrustlyView` | Callback registration unchanged; dispatch refactored via `TrustlyEvents` |

---

## Step-by-Step Upgrade Plan

## 1. Update SDK dependency to v4.2.0

Change your app dependency:

```gradle
dependencies {
    implementation 'net.trustly:trustly-android-sdk:4.2.0'
}
```

Keep this PR small: dependency bump and build verification first.

## 2. Verify build tooling compatibility

Observed SDK-side tooling deltas between these versions:

1. Android Gradle Plugin: `8.11.0` -> `8.12.0`
2. Gradle wrapper: `8.14.2` -> `8.14.3`
3. targetSdkVersion in SDK project: `34` -> `36`

For merchant apps, align your Android toolchain to a modern AGP/Gradle combination compatible with your project and CI.

## 3. Keep using `TrustlyView` + `Trustly` flow methods

Your host-side integration pattern remains:

1. Set callbacks (`onBankSelected`, `onReturn`, `onCancel`, optional `setListener`).
2. Render bank widget (`selectBankWidget`).
3. Launch light-box (`establish`) when bank/provider is selected.

No `TrustlyPanel` API was found in either v3 or v4 repository.

## 4. Validate deep-link return behavior in v4

SDK 4.2.0 encapsulates redirect flow with SDK-owned activities.

Action items:

1. Delete your manual redirect Activity class from the merchant app, if present.
2. Add your scheme to `strings.xml`:

```xml
<string name="trustly_url_scheme">url_scheme</string>
```

3. Choose one deep-link strategy in establish payload:
    - URL scheme: `metadata.urlScheme`
    - App Links: `metadata.deepLinkUrl`
4. `metadata.urlScheme` and `metadata.deepLinkUrl` are mutually exclusive. Send only one, never both.
5. App Links require additional configuration (for example, AndroidManifest intent filters, Digital Asset Links, and domain association validation).
6. `metadata.deepLinkUrl` (App Links) is new and also requires Trustly-side configuration/enablement.
7. Contact Trustly Support to enable App Links before rollout.
8. Test return-to-app behavior on real devices.

## 5. Re-test callback behavior end-to-end

Run full flow tests for:

1. Success path (`onReturn`).
2. Cancel/failure path (`onCancel`).
3. Bank selection (`onBankSelected`).
4. Telemetry/events (`setListener`).

---

## Task 1: API Changes

## A. Trustly / related API differences

### Public interfaces

v3 interface style:

- Java `interface Trustly`
- Java `interface TrustlyCallback<T, V>`
- Java `interface TrustlyListener`
- Deprecated factory helper via `Trustly.Instance.create(context)`

v4 interface style:

- Kotlin `interface Trustly`
- Kotlin `fun interface TrustlyCallback<T, V>`
- Kotlin `fun interface TrustlyListener`
- Added internal `TrustlyEvents` abstraction for callback/event dispatch

### Behavioral notes

1. Merchant-facing callback registration points are still on `Trustly`/`TrustlyView`.
2. `hybrid(...)` is documented as deprecated in v4 and marked as not available in future versions.
3. `proceedToChooseAccount()` remains available in both versions.

## B. build.gradle / dependency differences

### Dependency string

v3:

```gradle
implementation 'net.trustly:trustly-android-sdk:3.7.0'
```

v4:

```gradle
implementation 'net.trustly:trustly-android-sdk:4.2.0'
```

### SDK project-level technical deltas (useful for migration planning)

1. `com.android.library` plugin: `8.11.0` -> `8.12.0`
2. `targetSdkVersion`: `34` -> `36`
3. Gradle wrapper distribution: `8.14.2` -> `8.14.3`

## C. AndroidManifest permissions

### Permission changes

No required `uses-permission` entries were found in either SDK manifest.

### Component changes in v4

v4 introduces manifest-declared activities for secure browser + redirect callback handling:

1. `TrustlyCustomTabsManagerActivity`
2. `TrustlyRedirectActivity`

`TrustlyRedirectActivity` is configured with a browsable intent filter and scheme from `@string/trustly_url_scheme`.

Merchant implication: with SDK 4.2.0 redirect flow encapsulation, manual merchant redirect Activities are no longer required.

---

## Task 2: Integration Example (Before and After)

Note: these repositories do not include a merchant app/example module under `/app` or `/example`. The snippets below represent merchant-side integration using the SDK public API and launch flow behavior from the SDK source.

### Before (v3.7.0 style)

```kotlin
val trustlyView = TrustlyView(this)

trustlyView
    .onBankSelected { _, bankData ->
        // Continue to light-box after bank selection
        trustlyView.establish(bankData)
    }
    .onReturn { _, returnParams ->
        // Success flow
    }
    .onCancel { _, cancelParams ->
        // Cancel or failure flow
    }
    .selectBankWidget(establishData)
```

### After (v4.2.0 style)

```kotlin
val trustlyView = TrustlyView(this)

trustlyView
    .onBankSelected { _, bankData ->
        // Continue to light-box after bank selection
        trustlyView.establish(bankData)
    }
    .onReturn { _, returnParams ->
        // Success flow
    }
    .onCancel { _, cancelParams ->
        // Cancel or failure flow
    }
    .setListener { eventName, eventDetails ->
        // Optional telemetry
    }
    .selectBankWidget(establishData)
```

### What changed under the hood for light-box launch

1. v3 can open custom tabs directly from `TrustlyView` path.
2. v4 encapsulates secure-browser flow through SDK activities (`TrustlyCustomTabsManagerActivity` and `TrustlyRedirectActivity`) and dispatches return/cancel callbacks.

Merchant callback usage remains effectively the same. In v4, remove any manual redirect Activity and configure `trustly_url_scheme` in `strings.xml`.

If using App Links, send `metadata.deepLinkUrl` instead of `metadata.urlScheme` (mutually exclusive).

`metadata.deepLinkUrl` is a new option and requires Trustly-side enablement. Reach out to Trustly Support before using it in production.

---

## Task 3: Callback Logic Migration

## Did listener handling move to `TrustlyCallback`?

It was already callback-based in v3 and stays callback-based in v4.

What changed is the internal dispatch model:

1. v3: callback handlers are invoked directly from `TrustlyView` flow paths.
2. v4: callback handlers are still registered on `TrustlyView`, but dispatch is funneled through `TrustlyEvents` and activity-based return handling for secure browser flows.

## Migration guidance for success/error listeners

### v3 style

```kotlin
trustlyView
    .onReturn { trustly, params ->
        // Handle success
    }
    .onCancel { trustly, params ->
        // Handle cancel/error
    }
```

### v4 style

```kotlin
trustlyView
    .onReturn { trustly, params ->
        // Handle success
    }
    .onCancel { trustly, params ->
        // Handle cancel/error
    }
```

### Practical migration notes

1. Keep your existing `onReturn` and `onCancel` wiring.
2. If moving Java integration code to Kotlin, use lambda-friendly `fun interface` callbacks.
3. In v4, treat callback `trustly` argument as nullable in Kotlin callback signatures.
4. In SDK 4.2.0, delete manual merchant redirect Activity classes.
5. Deep-link keys are mutually exclusive: send either `metadata.urlScheme` or `metadata.deepLinkUrl`, not both.
6. If using App Links (`metadata.deepLinkUrl`), complete required Android App Links setup before rollout.
7. `metadata.deepLinkUrl` is new and requires Trustly-side configuration; contact Trustly Support to enable it.
8. Keep `setListener` for diagnostics and funnel events.

---

## Merchant Team Implementation Checklist

## Code migration

1. Update dependency to `4.2.0`.
2. Remove any usage of deprecated factory helper (`Trustly.Instance.create(...)`) and instantiate `TrustlyView` directly.
3. Keep callback registrations on `TrustlyView`.
4. Confirm establish flow still triggers from bank selection.

## App configuration

1. Delete any manual redirect Activity class previously used for Trustly callback routing.
2. Add your scheme in `strings.xml`:

```xml
<string name="trustly_url_scheme">url_scheme</string>
```

3. Confirm deep-link strategy is valid and mutually exclusive in establish data:
    - URL scheme via `metadata.urlScheme`, or
    - App Links via `metadata.deepLinkUrl`.
4. If using App Links, complete required platform setup (intent filters, Digital Asset Links, and verified domain configuration).
5. If using App Links (`metadata.deepLinkUrl`), contact Trustly Support to enable Trustly-side configuration before production rollout.
6. Validate return-to-app behavior from secure browser on physical devices.
7. Ensure your release manifest merge does not break SDK-declared activities.

## QA and release

1. Test happy path end-to-end.
2. Test cancel path end-to-end.
3. Test transient network failures and recovery.
4. Test app background/foreground transitions during authentication.
5. Monitor return success rate and cancellation rate after rollout.

---

## Regression Test Matrix (Recommended)

| Scenario | Expected Result |
|---|---|
| Widget loads | Banks are displayed and selectable |
| Bank selected | `onBankSelected` fires once with provider payload |
| Light-box success | `onReturn` fires and checkout success flow continues |
| User cancel/failure | `onCancel` fires and fallback UX appears |
| Custom tab return | App regains control and flow is resumed |
| Foreground/background transition | Flow resumes without dead-end state |

---

## Rollout Strategy for Merchant Teams

1. Ship behind a feature flag if possible.
2. Enable in internal builds first.
3. Roll out to a small production cohort.
4. Monitor conversion, return success rate, and cancel/failure trends.
5. Expand after stability is confirmed.

---

## Reference Files

This repository does not vendor the v3 and v4 SDK source trees side-by-side under
`trustly-android-v3/...` and `trustly-android-v4/...`, so the previous workspace-relative
reference links were removed to avoid broken links for readers.

If you want to restore this section later, replace it with verified links to real locations
such as release tags, branches, or commit permalinks for the corresponding SDK versions.
