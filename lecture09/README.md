# Lecture 09: Android Location-Aware Applications

This lecture covers location-aware Android applications, including accessing device location and integrating Google Maps.

## Projects Overview

This directory contains **four** sample Android applications demonstrating location-aware features:

### MyLocation Apps
1. **09-1_MyLocation** - Basic location tracking using Jetpack Compose
2. **09-2_MyLocation-MDC** - Basic location tracking using Material Design Components (Fragments + ViewBinding)

### Google Maps Apps
3. **09-3_GoogleMaps** - Google Maps integration using **Jetpack Compose**
4. **09-4_GoogleMaps-MDC** - Google Maps integration using **Material Design Components** (Fragments + ViewBinding + Navigation XML)

## Implementation Details

### 09-3_GoogleMaps (Jetpack Compose)
This project demonstrates Google Maps integration using modern Android development with Jetpack Compose:
- **UI Framework**: Jetpack Compose (declarative UI)
- **Architecture**: `MainActivity` with `MainScreen` composable
- **Maps Library**: Maps Compose library (`com.google.maps.android:maps-compose`)
- **Key Features**:
  - Declarative map setup with `GoogleMap` composable
  - Runtime location permission handling with `rememberLauncherForActivityResult`
  - Custom map styling (day/night themes)
  - Camera positioning and markers using Compose state

### 09-4_GoogleMaps-MDC (Material Design Components)
This project demonstrates Google Maps integration using traditional Android Views:
- **UI Framework**: XML layouts with ViewBinding
- **Architecture**: `MainActivity` + `MainFragment` with Navigation Component
- **Maps Library**: Classic Google Maps SDK (`com.google.android.gms:play-services-maps`)
- **Key Features**:
  - `SupportMapFragment` for map display
  - Activity Result API for runtime location permissions
  - Navigation graph for fragment management
  - Window insets handling for proper UI positioning
  - Custom map styling (day/night themes)

## Setup Instructions

### Prerequisites
Both Google Maps projects require a Google Maps API key:

1. **Get an API Key**:
   - Visit the [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select an existing one
   - Enable the "Maps SDK for Android" API
   - Create credentials (API key) for Android
   - Restrict the API key to your app's package name and SHA-1 certificate fingerprint

2. **Configure the API Key**:

   #### For 09-3_GoogleMaps (Compose):
   Add to `local.properties`:
   ```properties
   MAPS_API_KEY=your_api_key_here
   ```

   #### For 09-4_GoogleMaps-MDC (Views):
   Add to `local.properties`:
   ```properties
   MAPS_API_KEY=your_api_key_here
   ```

### Running the Projects

1. Open the desired project in Android Studio
2. Ensure your `local.properties` file contains the API key
3. Sync Gradle files
4. Run on a physical device or emulator with Google Play Services
5. Grant location permissions when prompted

## Choosing Between Implementations

**Use 09-3_GoogleMaps (Compose)** if you want to:
- Learn modern Android development with Jetpack Compose
- Use declarative UI patterns
- Integrate maps into a Compose-based app
- Take advantage of Compose state management

**Use 09-4_GoogleMaps-MDC (Views)** if you want to:
- Work with traditional Android Views and XML layouts
- Use the Navigation Component for fragment management
- Maintain compatibility with existing View-based apps
- Learn the classic Android development approach

## Key Concepts Covered

- **Runtime Permissions**: Handling ACCESS_FINE_LOCATION permission at runtime
- **Location Services**: Accessing device GPS and location data
- **Google Maps SDK**: Displaying interactive maps
- **Map Customization**: Custom styling, markers, and camera control
- **UI Patterns**: Both modern (Compose) and traditional (Views/Fragments) approaches
- **Material Design**: Integration with Material 3 components

## Additional Resources

- [Google Maps Platform Documentation](https://developers.google.com/maps/documentation/android-sdk/overview)
- [Maps Compose Documentation](https://github.com/googlemaps/android-maps-compose)
- [Location and Context Documentation](https://developer.android.com/training/location)
- [Android Permissions Guide](https://developer.android.com/training/permissions/requesting)
