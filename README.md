# Flappy Bird — Reverse Engineering Project

![Lint](https://github.com/SSMGAlt/Flappy-Bird-RE/actions/workflows/lint.yml/badge.svg?branch=main)
![Build](https://github.com/SSMGAlt/Flappy-Bird-RE/actions/workflows/build.yml/badge.svg?branch=main)

A full source reconstruction of Flappy Bird version 1.3 (versionCode 4) for Android, reverse engineered from the original APK published by DotGears in 2013. All game logic has been translated from Dalvik bytecode (Smali) to Java, the binary AndroidManifest and resource table have been decoded, and the five JNI functions in `libandengine.so` have been reconstructed to C source.

This project is a technical study. No original source code was obtained. The decompiled output belongs to DotGears.

---

## Technical overview

| Property | Value |
|---|---|
| Original package | `com.dotgears.flappybird` |
| Original version | 1.3 (versionCode 4) |
| Original developer | DotGears |
| Game engine | AndEngine GLES2 (nicolasgramlich) |
| Original language | Java 1.6 |
| Original build system | Eclipse ADT + Apache Ant |
| Original minimum SDK | API 8 (Android 2.2 Froyo) |

This reconstruction targets Android API 8 through API 36 and adds `arm64-v8a` and `x86_64` ABI support alongside the original `armeabi-v7a` and `x86` targets. All four native libraries are compiled from the reconstructed C source via CMake; the original prebuilt `armeabi` binary is not reused. The original ad SDK (`com.google.ads`) has been removed entirely.

---

## Repository structure

```
.
├── .github/
│   └── workflows/
│       ├── build.yml          Release build CI (unsigned APK)
│       └── lint.yml           Android Lint CI
├── app/
│   ├── CMakeLists.txt         CMake build for libandengine.so
│   ├── build.gradle
│   ├── libs/
│   │   └── andengine.jar      AndEngine GLES2 JAR (must be provided, see below)
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── assets/
│       │   ├── gfx/
│       │   │   └── atlas.png              Single 512x512 texture atlas
│       │   └── sounds/
│       │       ├── sfx_die.ogg
│       │       ├── sfx_hit.ogg
│       │       ├── sfx_point.ogg
│       │       ├── sfx_swooshing.ogg
│       │       └── sfx_wing.ogg
│       ├── cpp/
│       │   └── andengine_jni.c            Reconstructed JNI source
│       ├── java/com/dotgears/
│       │   ├── AnimatedEntity.java
│       │   ├── AnimationState.java
│       │   ├── AtlasRegion.java
│       │   ├── BlinkAnimation.java
│       │   ├── Button.java
│       │   ├── FlappyScene.java
│       │   ├── FloatingScore.java
│       │   ├── GameActivity.java
│       │   ├── GameObjectPool.java
│       │   ├── GameObject.java
│       │   ├── MathTables.java
│       │   ├── Medal.java
│       │   ├── NumberRenderer.java
│       │   ├── Renderer.java
│       │   ├── ScoreNumberRenderer.java
│       │   ├── ScorePanel.java
│       │   ├── Tweener.java
│       │   └── flappy/
│       │       ├── Bird.java
│       │       ├── FontNumberRenderer.java
│       │       ├── GameOverPanel.java
│       │       ├── GameScene.java
│       │       ├── GetReadyPanel.java
│       │       ├── SplashRunnable.java
│       │       └── SplashScreen.java
│       └── res/
│           ├── raw/
│           │   └── atlas.txt              Sprite region definitions
│           └── ...
├── build.gradle
├── gradle.properties
├── gradle/wrapper/
│   └── gradle-wrapper.properties
└── settings.gradle
```

---

## Prerequisites

| Dependency | Version | Notes |
|---|---|---|
| JDK | 21 | Temurin recommended |
| Android SDK | API 36 | `platforms;android-36` |
| Android Build Tools | 36.0.0 | `build-tools;36.0.0` |
| NDK | r27b (`27.2.12479018`) | Pinned in `build.gradle` |
| CMake | 3.22.1 | Available via SDK Manager |
| Gradle | 8.13 | Declared in `gradle-wrapper.properties` |
| AndEngine GLES2 | GLES2-AnchorCenter branch | Must be built from source; see below |

---

## Building from source

### 1. Clone the repository

```
git clone https://github.com/SSMGAlt/Flappy-Bird-RE.git
cd Flappy-Bird-RE
```

### 2. Generate the Gradle wrapper

The binary wrapper JAR is not committed. Run this once with a local Gradle 8.13 installation:

```
gradle wrapper --gradle-version 8.13
```

### 3. Provide the AndEngine JAR

AndEngine GLES2 is not published to any package registry. Build it from source and copy the output JAR to `app/libs/andengine.jar`.

```
git clone https://github.com/nicolasgramlich/AndEngine.git --branch GLES2-AnchorCenter andengine
cd andengine
# Import into Android Studio as a library module, or build with ant jar
# Copy the resulting JAR to <project root>/app/libs/andengine.jar
```

The `app/libs/` directory is tracked by Git. Once the JAR is present, it is picked up automatically by the `fileTree` dependency declaration.

### 4. Configure Play Games credentials

Open `app/src/main/res/values/strings.xml` and replace the placeholder values:

```xml
<string name="app_id">YOUR_PLAY_GAMES_APP_ID</string>
<string name="leaderboard_id">YOUR_LEADERBOARD_ID</string>
```

These are required at compile time due to the `meta-data` entry in the manifest. The game runs without valid credentials; the leaderboard button will silently no-op on unauthenticated sessions.

### 5. Install SDK components

```
$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager \
    "platforms;android-36" \
    "build-tools;36.0.0" \
    "ndk;27.2.12479018" \
    "cmake;3.22.1"
```

### 6. Build

Debug build:

```
./gradlew assembleDebug
```

Release build (unsigned):

```
./gradlew assembleRelease
```

Output APK: `app/build/outputs/apk/release/app-release-unsigned.apk`

To sign a release build, configure a keystore in `app/build.gradle` under `signingConfigs` and reference it in the `release` build type, or use `apksigner` against the unsigned output.

---

## Continuous integration

Two workflows run on every push and pull request to `main`.

**Lint** — runs `gradle lint -Pci=true`. The `-Pci` flag enables `abortOnError`, causing the workflow to fail on any lint error. The HTML and XML reports are uploaded as artifacts on every run, including failures.

**Build** — installs SDK platform 36, NDK r27b, and CMake 3.22.1 via `sdkmanager`, then runs `gradle assembleRelease`. The unsigned APK is uploaded as a 90-day artifact named `flappybird-release-unsigned`.

Both workflows require `app/libs/andengine.jar` to be present in the repository for compilation to succeed.

---

## Deobfuscation reference

The original APK applied ProGuard renaming to the `com.dotgears` package. The table below maps every obfuscated class name to its reconstructed name.

| Obfuscated | Reconstructed | Role |
|---|---|---|
| `com.dotgears.a` | `AdShowRunnable` | Removed — original ad runnable |
| `com.dotgears.b` | `AdHideRunnable` | Removed — original ad runnable |
| `com.dotgears.c` | `FlappyScene` | AndEngine `Scene` and touch listener |
| `com.dotgears.d` | `AnimationState` | Per-animation frame and timing state |
| `com.dotgears.e` | `BlinkAnimation` | Bird eye-blink one-shot animation |
| `com.dotgears.f` | `Button` | Tap-tested sprite button |
| `com.dotgears.g` | `Renderer` | Atlas loader, sprite batcher, event queue |
| `com.dotgears.h` | `NumberRenderer` | Bitmap-font digit renderer |
| `com.dotgears.i` | `AtlasRegion` | One parsed sprite region |
| `com.dotgears.j` | `MathTables` | Trig LUTs, 22 easing LUTs, xorshift PRNG |
| `com.dotgears.k` | `Medal` | Medal sprite with shimmer effect |
| `com.dotgears.l` | `ScoreNumberRenderer` | Fixed-width atlas digit display |
| `com.dotgears.m` | `GameObject` | Base game object |
| `com.dotgears.n` | `GameObjectPool` | Ring pool of 30 `GameObject` instances |
| `com.dotgears.o` | `FloatingScore` | Animated floating score popup |
| `com.dotgears.p` | `ScorePanel` | Score, best, and medal result panel |
| `com.dotgears.q` | `AnimatedEntity` | Sprite animation controller |
| `com.dotgears.r` | `Tweener` | Value tweener with 22 easing modes |
| `com.dotgears.flappy.a` | `Bird` | Bird entity: physics, rotation, death |
| `com.dotgears.flappy.b` | `FontNumberRenderer` | Large in-game score font |
| `com.dotgears.flappy.c` | `GameScene` | Game loop, pipes, collision, scoring |
| `com.dotgears.flappy.d` | `SplashRunnable` | Starts `GameActivity` from splash |
| `com.dotgears.flappy.e` | `GameOverPanel` | Game over text panel |
| `com.dotgears.flappy.f` | `GetReadyPanel` | Get ready and tutorial panel |

---

## Native library

`libandengine.so` from the original APK contains exactly five JNI functions. The reconstructed C source is at `app/src/main/cpp/andengine_jni.c` and is compiled by CMake for all four supported ABIs.

| Symbol | Purpose |
|---|---|
| `Java_org_andengine_opengl_GLES20Fix_glVertexAttribPointer` | Calls `glVertexAttribPointer` via NIO direct buffer pointer |
| `Java_org_andengine_opengl_GLES20Fix_glDrawElements` | Calls `glDrawElements` via NIO direct buffer pointer |
| `Java_org_andengine_opengl_util_BufferUtils_jniPut` | Bulk float copy into a `DirectByteBuffer` |
| `Java_org_andengine_opengl_util_BufferUtils_jniAllocateDirect` | `malloc` wrapped as `NewDirectByteBuffer` |
| `Java_org_andengine_opengl_util_BufferUtils_jniFreeDirect` | `free` of a `DirectByteBuffer` backing allocation |

The original APK shipped `armeabi`, `armeabi-v7a`, and `x86` prebuilts. This project does not reuse the original binaries. All four ABIs are built from source; `armeabi` is dropped because NDK r17 and later do not support it and the ABI has no meaningful device population remaining.

---

## Modifications from the original

- **Build system**: Eclipse ADT + Ant replaced by Gradle 8.13 and AGP 8.9.0.
- **Ad SDK**: `com.google.ads` (legacy standalone AdMob) removed entirely. No replacement.
- **Play Games**: `GameHelper` / old `GamesClient` API replaced by Play Games Services v2 (`PlayGames.getGamesSignInClient`, `PlayGames.getLeaderboardsClient`).
- **SoundPool**: Uses `SoundPool.Builder` with `USAGE_GAME` audio attributes on API 21 and above; falls back to the deprecated constructor on older devices.
- **Screen adaptation**: Camera dimensions are computed from actual display size at runtime. The game world fills the screen on any aspect ratio without letterboxing. All game coordinate constants (`GROUND_Y`, panel positions, pipe bounds) derive from `GameScene.VIRTUAL_WIDTH` and `GameScene.VIRTUAL_HEIGHT` rather than fixed pixel values.
- **ABI expansion**: `arm64-v8a` and `x86_64` added.
- **Bug fixes**: `Renderer.setScore()` was pushing event ID 0 instead of 4; `NumberRenderer` was using the wrong part index when parsing sprite names with multi-segment prefixes (e.g. `number_score_00`); sounds were loaded from `R.raw` instead of the `assets/sounds/` path where they actually reside.

---

## License

The reconstructed source code in this repository is released under the MIT License.

The original Flappy Bird game, its artwork, sounds, and game design are the intellectual property of DotGears. This repository does not grant any rights to that material.
