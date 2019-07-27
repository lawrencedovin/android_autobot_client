<img src="https://github.com/alexako/android_autobot_client/blob/develop/app/src/main/res/mipmap-xxxhdpi/autobot_icon.png" width="200" />

# AutoBot Android Client

Java 4 final project

## Set up

### API Keys

You'll need to acquire API keys for two different services. One for [Pusher API](https://pusher.com) and [Google Maps Android SDK API](https://cloud.google.com/maps-platform/maps/?apis=maps).

Add the keys into the `gradle.properties` file found in the `.gradle` directory:


  * **Mac**:
   `/Users/<Your Username>/.gradle`

  * **Linux**:
   `/home/<Your Username>/.gradle`

  * **Windows**:
    `C:\Users\<Your Username>\.gradle`


The `gradle.properties` file should look something like this:
```bash
PUSHER_API_KEY="YOUR_PUSHER_API_KEY"
GOOGLE_MAPS_API_KEY="YOUR_GOOGLE_MAPS_API_KEY"
```

#### Note:

>   * Create the `gradle.properties` file in the `.gradle` directory if it does not exist.
>   * Be sure to include the double quotes `"` around the API key.
>   * There is another `gradle.properties` file inside the project directory. DO NOT ENTER THE KEYS IN THAT FILE. 
>    For example, `/Users/alex/.gradle/gradle.properties` should be the file where you add the keys.
