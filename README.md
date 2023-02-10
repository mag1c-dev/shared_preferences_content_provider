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
           android:authorities="your_authority"
           android:readPermission="shared_preferences.permission.READ_DATA"
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
        <package android:name="com.example.host" />  <!--host app package-->
    </queries>
    <uses-permission android:name="shared_preferences.permission.WRITE_DATA" />
    <uses-permission android:name="shared_preferences.permission.READ_DATA" />
</manifest>
```


```dart
// The content provider must be set up first
await SharedPreferencesContentProvider.init(
    providerAuthority:
    'your_authority',  //authority provider in AndroidManifest.xml of host app
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
final bool boolValue = await SharedPreferencesContentProvider.get('MY_BOOL_KEY');
final String stringValue = await SharedPreferencesContentProvider.get('MY_STRING_KEY');
final int intValue = await SharedPreferencesContentProvider.get('MY_INT_KEY');
final double doubleValue = await SharedPreferencesContentProvider.get('MY_DOUBLE_KEY');
```
or
```dart
final Map<String,dynamic> data = await SharedPreferencesContentProvider.getAll();
```

```dart
// Listen when value change value of key ('123') change
SharedPreferencesContentProvider.listen((event) {
    print(event);
  }, 
  key: '123', // If you want to listen when have any change, remove this
);
```


