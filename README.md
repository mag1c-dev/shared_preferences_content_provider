# shared_preferences_content_provider

[![pub package](https://img.shields.io/pub/v/shared_preferences_content_provider.svg)](https://pub.dev/packages/shared_preference_content_provider)

Shared preference supporting Android content provider

> Note: Only support Android

## Usage

To use this plugin, add `shared_preferences_content_provider` as a [dependency in your pubspec.yaml file](https://flutter.io/platform-plugins/).

### Example


#### Add this to AndroidManifest.xml

##### Host app (Provider app):

```xml
<manifest>
    <application>
         <provider
           android:name="com.zhgwu.shared_preferences_content_provider.SharedPreferencesContentProvider"
           android:authorities="com.zhgwu.shared_preferences_content_provider.example"
           android:readPermission="shared_preferences.permission.READ_TOKEN"
           android:writePermission="shared_preferences.permission.WRITE_DATA"
           android:enabled="true"
           android:exported="true" />
    </application>

   
</manifest>
```


##### Consumer app:

```xml
<manifest>
    <queries>
        <package android:name="com.zhgwu.shared_preferences_content_provider_example" />  <!--host app package-->
    </queries>
    <uses-permission android:name="shared_preference.permission.WRITE_DATA" />
    <uses-permission android:name="shared_preference.permission.READ_DATA" />
</manifest>
```


```dart
// The content provider must be set up first
await SharedPreferencesContentProvider.init(
    providerAuthority:
    'com.zhgwu.shared_preferences_content_provider.example',  //authority provider in AndroidManifest.xml
);
```

```dart
// Put values
await SharedPreferencesContentProvider.putBool('MY_BOOL_KEY', true);
await SharedPreferencesContentProvider.putString('MY_STRING_KEY', 'STRING_VALUE');
await SharedPreferencesContentProvider.putInt('MY_INT_KEY', 42);
await SharedPreferencesContentProvider.putDouble('MY_DOUBLE_KEY', 9.9);
```

```dart
// Get values
bool boolValue = await SharedPreferencesContentProvider.get('MY_BOOL_KEY');
String stringValue = await SharedPreferencesContentProvider.get('MY_STRING_KEY');
int intValue = await SharedPreferencesContentProvider.get('MY_INT_KEY');
double doubleValue = await SharedPreferencesContentProvider.get('MY_DOUBLE_KEY');
```


