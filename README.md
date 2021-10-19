<h1 align="center">Snap Creative kit Sample</h1>
<p align="center">An android app that demonstrates Snapchat's creative kit integration with Jetpack compose</p>

# 💻 Requirements
------------
- To try out this sample app, you need to use [Android Studio Arctic Fox](https://developer.android.com/studio).
- You can clone this repository.

🧬 Sample
------------

| Project | Sample |
| :--- | --- |
| Android app that demonstrates use of Snapkit's Creative kit with new Jetpack Compose and latest Android Studio version.<br><br> What it does? <br><br> • Share Image on SnapChat <br>• Share Image with Sticker on SnapChat <br>• Open Lens on SnapChat <br>• Jetpack compose UI<br>• Light theme<br>• Lifecycle and Stateful<br>• No XML <br><br>  | <img src="video/creative kit demo.gif" width="280" alt="Creative kit sample demo">|
|  |  |

# Intructions
- Add your `client id` from Snapkit [Developer Portal](https://kit.snapchat.com/manage/) inside [strings.xml](https://github.com/Horizon733/snap-loginkit-sample/blob/master/app/src/main/res/values/strings.xml)
``` xml
<string name="snapchat_client_id">Your client id</string>
```
- Make sure to change project name in `AndroidManifest.xml`
```xml
<application ...>
...
  <meta-data
      android:name="com.snapchat.kit.sdk.clientId"
      android:value="@string/snapchat_client_id" />
  <provider
      android:authorities="${applicationId}.fileprovider"
      android:name="androidx.core.content.FileProvider"
      android:exported="false"
      android:grantUriPermissions="true">
      <meta-data
          android:name="android.support.FILE_PROVIDER_PATHS"
          android:resource="@xml/file_paths"
          />
  </provider>
...
</application>
```
- Create `xml` directory in `res` 
- Create `file_paths.xml` file in `xml` directory.
- Add following code snippet for using it as `cache directory` for storing images
```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <cache-path name="tmp" path="/" />
</paths>
```
- Add your own lens uuid from [my lenses](https://my-lenses.snapchat.com/lenses)
  - procedure for capturing lens uuid
    - [Click here](https://my-lenses.snapchat.com/lenses) to login to check out your lenses
    - Now, as shown in below image, click on `chain` icon to copy link for your lens
    - <img src="https://user-images.githubusercontent.com/57827233/137531014-b075f3dd-26a5-4874-8f2b-f90c0e02c5a1.png">
    - Paste the same link in `new tab` you will be able to find uuid as below image shows
    <img src="https://user-images.githubusercontent.com/57827233/137532975-170f5582-07af-4a5d-8a98-603f89ba54ca.png">
- As you found your uuid, go inside `MainActivity.kt` find function `getLens()` and add your uuid
```kotlin
fun getLens(){
...
  val snapLensContent = SnapLensContent.createSnapLensContent(
          "your lens uuid",
          launchData
      )
...
```
- These are the only steps to do.

## Please give this repo a Star, if this helps you!

