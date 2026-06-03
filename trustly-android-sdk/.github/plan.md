# DEV-301885 - Simple Implementation Plan
**Component:** Trustly Android SDK (`TrustlyStorageClient`)  
**Goal:** Store personalization data securely, recover safely on failures, and keep checkout flow uninterrupted.

---

## 1) What We Are Building (Plain Language)

We are adding a secure storage path for last-used-bank data:

1. Encrypt data before storing it.
2. Store encrypted data in a Trustly cookie scope.
3. Read and decrypt it when needed.
4. If anything fails, return `null` and continue checkout.

---

## 2) Important Platform Rules

Keep these constraints in mind during implementation and QA:

- SDK `minSdkVersion` is 19.
- Android Keystore AES key generation is supported on API 23+.
- For API 19-22, secure cookie personalization is unavailable by design and must fail gracefully.
- Cross-app cookie reuse is best-effort only.
- Keystore keys are app-scoped, so deterministic decrypt across independent merchant apps is not guaranteed.

---

## 3) Build Scope

### A. Crypto Layer
File: `TrustlyCryptoEngine.kt`

- Use `Cipher.getInstance("AES/GCM/NoPadding")`.
- Use `KeyStore.getInstance("AndroidKeyStore")` with alias `TrustlyStorageKey`.
- Use 12-byte IV and 128-bit GCM tag.
- API:
  - `encrypt(plainText: String): String?`
  - `decrypt(encodedPayload: String): String?`
- Payload format: URL-safe Base64 of `[IV || CIPHERTEXT_WITH_TAG]`.
- Handle explicitly:
  - `KeyPermanentlyInvalidatedException`
  - `AEADBadTagException`
- On API 19-22: return `null` and log sanitized warning.

### B. Storage Layer
File: `TrustlyStorageClient.kt`

- Public API:
  - `setItem(key: String, value: String): Boolean`
  - `getItem(key: String): String?`
- Use `CookieManager` on Trustly storage URL.
- Cookie attributes: `Secure`, `HttpOnly`, `SameSite=Strict`, `Max-Age=31536000`, `Path=/`.
- `setItem` flow:
  1. normalize key,
  2. encrypt value,
  3. write cookie,
  4. flush,
  5. return success/failure.
- `getItem` flow:
  1. read cookie header,
  2. parse target key,
  3. decrypt,
  4. if tampered, return `null` and clear that key.
- Catch and degrade safely:
  - `SQLiteFullException`
  - `IOException`
  - `IllegalStateException`
  - `SecurityException`

### C. SDK Wiring Layer
File: `LastUsedBankManager.kt`

- Add feature toggle (`cross-app storage ON/OFF`).
- Write to new storage path when enabled.
- Read from new storage path first when enabled.
- Always keep local SharedPreferences as fallback.
- Never block checkout if storage path fails.

---

## 4) Execution Plan (Step-by-Step)

### Step 1 - Finalize Crypto Engine
**Done criteria:**
- AES-GCM + AndroidKeyStore used correctly.
- API 19-22 fallback returns `null`.
- Explicit crypto exception handling implemented.

**How to validate:**
1. Review code in `TrustlyCryptoEngine.kt` and confirm:
   - `Cipher.getInstance("AES/GCM/NoPadding")`
   - `KeyStore.getInstance("AndroidKeyStore")`
   - 12-byte IV and `GCMParameterSpec(128, iv)`
2. Run unit tests to ensure compilation and no regressions:
   ```zsh
   cd "/Users/andre.guedes/Documents/trustly-android/trustly-android-sdk"
   ./gradlew testDebugUnitTest --no-daemon
   ```
3. On emulator/device, run crypto instrumented test class and verify pass:
   ```zsh
   cd "/Users/andre.guedes/Documents/trustly-android/trustly-android-sdk"
   ./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=net.trustly.android.sdk.util.storage.TrustlyCryptoEngineInstrumentedTest --no-daemon
   ```
4. Expected result: API >= 23 encrypt/decrypt roundtrip works; API 19-22 returns `null` without crash.

### Step 2 - Finalize Storage Client
**Done criteria:**
- Cookie write/read/decrypt logic complete.
- Secure cookie attributes present.
- Tamper cleanup for single key works.

**How to validate:**
1. Review code in `TrustlyStorageClient.kt` and confirm:
   - cookie attributes include `Secure`, `HttpOnly`, `SameSite=Strict`, `Max-Age=31536000`, `Path=/`
   - `setItem` encrypts before write
   - `getItem` decrypts and clears only affected key on tamper failure
2. Run unit tests:
   ```zsh
   cd "/Users/andre.guedes/Documents/trustly-android/trustly-android-sdk"
   ./gradlew testDebugUnitTest --no-daemon
   ```
3. Run storage instrumented test class:
   ```zsh
   cd "/Users/andre.guedes/Documents/trustly-android/trustly-android-sdk"
   ./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=net.trustly.android.sdk.util.storage.TrustlyStorageClientInstrumentedTest --no-daemon
   ```
4. Expected result: same-app set/get succeeds on API >= 23, tampered payload returns `null`, and fallback remains safe on API < 23.

### Step 3 - Wire into Last-Used-Bank Flow
**Done criteria:**
- Feature toggle implemented.
- Storage-first + local fallback behavior active.
- Existing user flows remain stable.

**How to validate:**
1. Review `LastUsedBankManager.kt` and confirm:
   - feature toggle exists
   - read path checks storage client first when enabled
   - local SharedPreferences fallback remains
2. Run unit tests for manager behavior:
   ```zsh
   cd "/Users/andre.guedes/Documents/trustly-android/trustly-android-sdk"
   ./gradlew testDebugUnitTest --no-daemon
   ```
3. Verify existing SDK flows still pass current instrumentation suite (or a focused suite for lightbox/widget paths):
   ```zsh
   cd "/Users/andre.guedes/Documents/trustly-android/trustly-android-sdk"
   ./gradlew connectedDebugAndroidTest --no-daemon
   ```
4. Expected result: no regressions in return/cancel/widget flows; storage failures do not block checkout.

### Step 4 - Unit Tests (`src/test`)
**Minimum cases:**
- key parsing/normalization,
- empty/missing key behavior,
- UTF-8 roundtrip,
- failure isolation paths.

**How to validate:**
1. Confirm test classes exist and cover minimum cases (`TrustlyStorageClientTest`, `TrustlyCryptoEngineTest`, manager tests).
2. Run unit test task:
   ```zsh
   cd "/Users/andre.guedes/Documents/trustly-android/trustly-android-sdk"
   ./gradlew testDebugUnitTest --no-daemon
   ```
3. Expected result: build successful and all storage/crypto unit tests pass.

### Step 5 - Instrumented Tests (`src/androidTest`)
**Minimum cases:**
- API >= 23 encrypt/decrypt runtime,
- tampered payload returns `null`,
- cookie set/get/flush path,
- API 19-22 safe fallback.

**How to validate:**
1. Start an emulator/device and ensure it is online in `adb devices`.
2. Run focused storage/crypto instrumentation tests:
   ```zsh
   cd "/Users/andre.guedes/Documents/trustly-android/trustly-android-sdk"
   ./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=net.trustly.android.sdk.util.storage.TrustlyCryptoEngineInstrumentedTest,net.trustly.android.sdk.util.storage.TrustlyStorageClientInstrumentedTest --no-daemon
   ```
3. (Optional) Run full instrumentation suite for regression confidence:
   ```zsh
   cd "/Users/andre.guedes/Documents/trustly-android/trustly-android-sdk"
   ./gradlew connectedDebugAndroidTest --no-daemon
   ```
4. Expected result: focused tests pass on connected devices; API-specific behavior matches design.

### Step 6 - Manual QA
**Minimum checks:**
- same-app happy path,
- cleartext prevention,
- tamper handling,
- storage/sandbox failure fallback,
- cross-app behavior documented as best-effort.

**How to validate:**
1. Same-app happy path: save value and confirm it is read back in checkout flow.
2. Cleartext check: inspect cookie data/logs and verify no plaintext personalization payload.
3. Tamper test: modify stored cookie payload and confirm flow returns `null` without crash.
4. Failure fallback: simulate restricted profile/storage pressure and confirm checkout still proceeds.
5. Cross-app test (best-effort): run App A/App B scenario and document observed behavior by device/API/browser.
6. Expected result: graceful degradation in all failure cases and no checkout blocking.

---

## 5) Acceptance Criteria

Implementation is complete when all of the following are true:

1. No plaintext personalization value is stored in cookies.
2. Encryption/decryption works on API 23+ and fails safely on API 19-22.
3. Storage failures do not break checkout.
4. Unit and instrumented tests pass for new storage/crypto flows.
5. Cross-app limitations are documented clearly (best-effort only).

---

## 6) Final Verification Checklist

- [ ] `Cipher.getInstance("AES/GCM/NoPadding")` is present.
- [ ] `KeyStore.getInstance("AndroidKeyStore")` is present.
- [ ] 12-byte IV + 128-bit tag enforced.
- [ ] `KeyPermanentlyInvalidatedException` and `AEADBadTagException` handled explicitly.
- [ ] No broad crypto catch-all used for normal flow.
- [ ] `TrustlyStorageClient` handles cookie/runtime failures gracefully.
- [ ] `LastUsedBankManager` keeps fallback path and feature toggle.
- [ ] Unit tests pass.
- [ ] Instrumented tests pass.
- [ ] Documentation reflects best-effort cross-app behavior.

---
*Simplified plan focused on execution clarity and delivery milestones.*
