# UPI Voice Announcer

Automatically announces incoming UPI payment notifications on your Android device using Text-to-Speech (TTS).

## Features

- 🔊 **Automatic Voice Announcements** - Announces incoming UPI payments as they arrive
- 📱 **Multi-UPI Support** - Works with Google Pay, PhonePe, Paytm, BHIM, Amazon Pay
- 🎯 **Smart Detection** - Filters failed, pending, and cancelled payments
- 🔒 **Privacy-First** - No data collection, runs locally on your device
- 🌙 **Dark Mode Support** - Beautiful Material Design 3 UI
- ⚡ **Duplicate Prevention** - Prevents duplicate announcements within 5 seconds
- 🎨 **Customizable** - Adjust announcement format and volume

## Supported UPI Apps

- Google Pay
- PhonePe
- Paytm
- BHIM
- Amazon Pay

## Requirements

- Android 7.0 (API level 24) or higher
- Android Studio 2024.1.1 or later
- Gradle 8.9
- Java 11 or higher

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/dimple9416/UPIVoiceAnnouncer.git
cd UPIVoiceAnnouncer
```

### 2. Open in Android Studio
- Open Android Studio
- Select "Open an existing Android Studio project"
- Navigate to the cloned `UPIVoiceAnnouncer` directory
- Wait for Gradle sync to complete

### 3. Build the Project
```bash
./gradlew build
```

### 4. Install on Device
Connect an Android device via USB and run:
```bash
./gradlew installDebug
```

## Running Tests

```bash
./gradlew test
```

## Building Release APK

```bash
./gradlew assembleRelease
```

The release APK will be generated at `app/build/outputs/apk/release/`.

## Usage

1. **Open the App** - Launch UPI Voice Announcer from your app drawer
2. **Enable Notification Listener** - Click "Enable Now" if not already enabled
3. **Grant Permission** - Go to Settings → Notification access and enable UPI Voice Announcer
4. **Test TTS** - Click "Test TTS" button to hear a sample announcement
5. **Receive Payments** - When a UPI payment is received, it will be automatically announced

## Project Structure

```
UPIVoiceAnnouncer/
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/upivoiceannouncer/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── NotificationListener.kt
│   │   │   │   ├── PaymentParser.kt
│   │   │   │   ├── NumberToWords.kt
│   │   │   │   ├── TTSManager.kt
│   │   │   │   ├── DuplicateGuard.kt
│   │   │   │   ├── PaymentEventBus.kt
│   │   │   │   ├── PrefsManager.kt
│   │   │   │   └── AppRegistry.kt
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   └── activity_main.xml
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   └── values-night/
│   │   │   │       └── themes.xml
│   │   │   └── AndroidManifest.xml
│   │   └── test/java/com/example/upivoiceannouncer/
│   │       └── PaymentParserTest.kt
│   └── build.gradle.kts
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── .gitignore
└── README.md
```

## Key Components

### MainActivity
- Displays notification listener status
- "Enable Now" button to grant notification access
- "Test TTS" button to test voice announcements
- Shows real-time payment events via EventBus

### NotificationListener
- Extends `NotificationListenerService`
- Captures incoming notifications from UPI apps
- Extracts and announces payment information
- Prevents duplicate announcements (5-second window)

### PaymentParser
- Extracts amount and app name from notification text
- Converts amount to Indian English words
- Filters failed/pending/cancelled payments
- Supports all major UPI apps

### TTSManager
- Wrapper for Android TextToSpeech engine
- Handles initialization and error cases
- Supports volume control and speech rate adjustment

### DuplicateGuard
- SharedPreferences-based duplicate detection
- 5-second duplicate prevention window
- Auto-cleanup after 1 minute

## Troubleshooting

### App Crashes on Startup
- Ensure notification listener permission is granted
- Check that TTS engine is installed on your device

### TTS Not Speaking
- Go to Settings → Sound & Vibration → Text-to-Speech Output
- Ensure a TTS engine is selected and installed

### Notifications Not Detected
- Verify the app has notification access granted
- Some UPI apps may have different notification formats
- Check logcat for debug messages

## Contributing

Contributions are welcome! Please feel free to submit pull requests.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Disclaimer

This app requires notification access to function. It does not store, transmit, or use your payment data for any purpose other than local text-to-speech announcement. All processing happens locally on your device.

---

**Made with ❤️ for the Android community**