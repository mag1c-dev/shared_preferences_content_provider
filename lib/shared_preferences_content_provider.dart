import 'dart:convert';

import 'package:flutter/services.dart';

class SharedPreferencesContentProvider {
  static const MethodChannel _channel =
      MethodChannel('shared_preferences_content_provider');

  /// Set Content provider URI and Authority
  ///
  static Future<dynamic> init({String? providerAuthority}) {
    return _channel.invokeMethod('init', {'authority': providerAuthority});
  }

  /// Saves a boolean [value] to persistent storage under the specified content provider.
  ///
  static Future<dynamic> putBool(String key, bool value) {
    return _setValue('Bool', key, value);
  }

  /// Saves an integer [value] to persistent storage under the specified content provider.
  ///
  /// If [value] is null, this is equivalent to calling [remove()] on the [key].
  static Future<dynamic> putInt(String key, int? value) {
    return _setValue('Int', key, value);
  }

  /// Saves a double [value] to persistent storage under the specified content provider.
  ///
  /// If [value] is null, this is equivalent to calling [remove()] on the [key].
  static Future<dynamic> putDouble(String key, double? value) {
    return _setValue('Double', key, value);
  }

  /// Saves a string [value] to persistent storage under the specified content provider.
  ///
  /// If [value] is null, this is equivalent to calling [remove()] on the [key].
  static Future<dynamic> putString(String key, String? value) {
    return _setValue('String', key, value);
  }

  /// Reads a value of any type from persistent storage under the specified content provider.
  ///
  /// If the persistent storage does not contains [key], then [null] will be returned
  static Future<dynamic> get(String key) {
    return _channel.invokeMethod('get', {'key': key});
  }

  /// Reads all key-value pairs from persistent storage under the specified content provider.
  static Future<Map<String, dynamic>> getAll() async {
    final allPrefs = await _channel.invokeMethod('getAll');
    return jsonDecode(allPrefs);
  }

  /// Removes an entry from persistent storage under the specified specified content provider.
  static Future<dynamic> remove(String key) {
    return _channel.invokeMethod('remove', {'key': key});
  }

  /// Removes all entry from persistent storage under the specified specified content provider.
  static Future<dynamic> removeAll() {
    return _channel.invokeMethod('removeAll');
  }

  static Future<dynamic> _setValue(
      String valueType, String key, Object? value) {
    return _channel.invokeMethod('put$valueType', {
      'key': key,
      'value': value,
    });
  }
}
