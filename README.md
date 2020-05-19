# TrailFinder
An app that finds hiking trails near a user's location. Uses the Hiking Project Data API to locate nearby trails and displays their location using the Google Maps SDK.

## Instructions
In order to run this project locally in Android Studio, you will need a [Hiking Project Data API Key](https://www.hikingproject.com/data) and at least one [Google Maps Platform API Key](https://developers.google.com/maps/documentation/android-sdk/get-api-key). The keys should be stored in the project level's `gradle.properties` file. The file should contain the following String variables:
*  `HIKING_PROJECT_DATA_API_KEY`
*  `GOOGLE_MAPS_API_KEY_DEBUG`
*  `GOOGLE_MAPS_API_KEY_RELEASE` (optional)

## Screenshots
![](https://github.com/williamspete321/TrailFinder/tree/master/app/src/main/res/raw/trail-finder-main.png)

## Libraries
*  [Architecture Components](https://developer.android.com/topic/libraries/architecture) for data persistance
*  [Data Binding](https://developer.android.com/topic/libraries/data-binding/index.html) for binding UI components to data 
*  [Google Maps SDK](https://developers.google.com/maps/documentation/android-sdk/intro) for displaying trail's location
*  [Material Design](https://developer.android.com/guide/topics/ui/look-and-feel) for visual effects
*  [Retrofit](https://square.github.io/retrofit/) for REST API communication
*  [Gson](https://github.com/google/gson) for JSON deserialization
*  [Picasso](https://github.com/square/picasso) for image loading
*  [Timber](https://github.com/JakeWharton/timber) for logging
