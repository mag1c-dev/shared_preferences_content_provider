package com.zhgwu.shared_preferences_content_provider;

import static com.zhgwu.shared_preferences_content_provider.Constants.*;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import io.flutter.plugin.common.JSONUtil;

public class SharedPreferencesContentProvider extends ContentProvider {

    private static final String SHARE_PREFERENCES = "_____share_preferences_____";
    private static final String TAG = SharedPreferencesContentProvider.class.getName();
    private SharedPreferences prefs;

    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            prefs = getContext().getSharedPreferences(SHARE_PREFERENCES, Context.MODE_PRIVATE);
        }
        Log.d(TAG, "context is null");
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        Bundle bundle = new Bundle();
        switch (method) {
            case GET_METHOD: {
                try {
                    Boolean value = get(arg, Boolean.class);
                    bundle.putBoolean(arg, value);
                } catch (ClassCastException ignored) {
                }
                try {
                    Integer value = get(arg, Integer.class);
                    bundle.putInt(arg, value);
                } catch (ClassCastException ignored) {
                }
                try {
                    Double value = get(arg, Double.class);
                    bundle.putDouble(arg, value);
                } catch (ClassCastException ignored) {
                }
                try {
                    String value = get(arg, String.class);
                    bundle.putString(arg, value);
                } catch (ClassCastException ignored) {
                }
                break;
            }

            case PUT_STRING_METHOD: {
                put(arg, extras.getString(arg));
                break;
            }

            case PUT_BOOL_METHOD: {
                put(arg, extras.getBoolean(arg));
                break;
            }

            case PUT_INT_METHOD: {
                put(arg, extras.getInt(arg));
                break;
            }

            case PUT_DOUBLE_METHOD: {
                put(arg, extras.getDouble(arg));
                break;
            }

            case GET_ALL_METHOD: {
                bundle.putString("data", new JSONObject(prefs.getAll()).toString());
                break;
            }

            case REMOVE_METHOD: {
                prefs.edit().remove(arg).apply();
                break;
            }

            case REMOVE_ALL_METHOD: {
                prefs.edit().clear().apply();
                break;
            }

        }

        return bundle;
    }


    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> anonymousClass) {
        if (anonymousClass == String.class) {
            return (T) prefs.getString(key, "");
        } else if (anonymousClass == Boolean.class) {
            return (T) Boolean.valueOf(prefs.getBoolean(key, false));
        } else if (anonymousClass == Double.class) {
            return (T) Double.valueOf(Float.valueOf(prefs.getFloat(key, 0)).toString());
        } else if (anonymousClass == Integer.class) {
            return (T) Integer.valueOf(prefs.getInt(key, 0));
        } else {
            return null;
        }
    }

    public <T> void put(String key, T data) {
        SharedPreferences.Editor editor = prefs.edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Double) {
            editor.putFloat(key, ((Double) data).floatValue());
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        }
        editor.apply();
    }
}
