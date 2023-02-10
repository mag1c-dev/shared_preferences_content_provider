package com.zhgwu.shared_preferences_content_provider;

import static com.zhgwu.shared_preferences_content_provider.Constants.*;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * SharedPreferencesContentProviderPlugin
 */
public class SharedPreferencesContentProviderPlugin implements FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler {
    private MethodChannel channel;
    private EventChannel eChannel;
    private Context context;
    private String authority;
    private BroadcastReceiver receiver;

    private static final String TAG = "SharedPrefCPPlugin";

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "shared_preferences_content_provider");
        channel.setMethodCallHandler(this);
        eChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), "shared_preferences_content_provider_event");
        eChannel.setStreamHandler(this);
        context = flutterPluginBinding.getApplicationContext();
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if(!call.method.equals(INIT_METHOD)){
            if (authority == null) {
                result.error("ERROR_NOT_INIT", "SharePreferenceContentProvider must be init before use", null);
            }
        }
        switch (call.method) {
            case INIT_METHOD : {
                authority = call.argument("authority");
                result.success("INIT SUCCESS");
                Log.d(TAG, "INIT SUCCESS");
                break;
            }
            case GET_METHOD : {
                result.success(get(call.argument("key")));
                Log.d(TAG, "GET SUCCESS");
                break;
            }
            case PUT_STRING_METHOD : {
                putString(call.argument("key"), call.argument("value"));
                result.success("PUT STRING SUCCESS");
                Log.d(TAG, "PUT SUCCESS");

                break;
            }
            case PUT_DOUBLE_METHOD : {
                putDouble(call.argument("key"), call.argument("value"));
                result.success("PUT DOUBLE SUCCESS");
                Log.d(TAG, "PUT SUCCESS");

                break;
            }
            case PUT_BOOL_METHOD : {
                putBool(call.argument("key"), call.argument("value"));
                result.success("PUT BOOL SUCCESS");
                Log.d(TAG, "PUT SUCCESS");
                break;
            }
            case PUT_INT_METHOD : {
                putInt(call.argument("key"), call.argument("value"));
                result.success("PUT INT SUCCESS");
                Log.d(TAG, "PUT SUCCESS");
                break;
            }
            case GET_ALL_METHOD : {
                result.success(getAll());
                Log.d(TAG, "GET ALL SUCCESS");
                break;
            }
            case REMOVE_METHOD : {
                call(REMOVE_METHOD, null, null);
                result.success("REMOVE SUCCESS");
                Log.d(TAG, "REMOVE SUCCESS");
                break;
            }
            case REMOVE_ALL_METHOD : {
                call(REMOVE_ALL_METHOD, null, null);
                result.success("REMOVE ALL SUCCESS");
                Log.d(TAG, "REMOVE ALL SUCCESS");
                break;
            }
        }
    }


    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }


    private void putString(String key, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        put(PUT_STRING_METHOD, key, bundle);
    }


    private void putDouble(String key, Double value) {
        Bundle bundle = new Bundle();
        bundle.putDouble(key, value);
        put(PUT_DOUBLE_METHOD, key, bundle);
    }

    private void putBool(String key, Boolean value) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(key, value);
        put(PUT_BOOL_METHOD, key, bundle);
    }

    private void putInt(String key, Integer value) {
        Bundle bundle = new Bundle();
        bundle.putInt(key, value);
        put(PUT_INT_METHOD, key, bundle);
    }


    private void put(String method, String key, Bundle bundle) {
        if (receiver!=null){
            Intent intent = new Intent();
            intent.setAction(ACTION_DATA_UPDATE);
            bundle.putString("key", key);
            intent.putExtras(bundle);
            context.sendBroadcast(intent);
            Log.d(TAG, "SEND BROAD CAST SUCCESS");
        }
        call(method, key, bundle);

    }


    private Object get(String key) {
        return call(GET_METHOD, key, null).get(key);
    }

    private Object getAll() {
        return call(GET_ALL_METHOD, null, null).get("data");
    }

    private Bundle call(String method, String arg, Bundle extras) {
        Uri uri = Uri.parse("content://" + authority);
        ContentResolver cr = context.getContentResolver();
        Bundle bundle;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            bundle = cr.call(authority, method, arg, extras);
        } else {
            bundle = cr.call(uri, method, arg, extras);
        }
        return bundle;
    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Map<String, Object> res = new HashMap<>();
                res.put("key", intent.getExtras().get("key"));
                res.put("value", intent.getExtras().get((String) intent.getExtras().get("key")));
                if (arguments != null){
                    if (arguments.equals(intent.getExtras().get("key"))){
                        events.success(res);
                        Log.d(TAG, "RECEIVER "+res);
                    }
                } else {
                    events.success(res);
                    Log.d(TAG, "RECEIVER "+res);
                }

            }
        };
        context.registerReceiver(receiver, new IntentFilter(ACTION_DATA_UPDATE));
        Log.d(TAG, "LISTENING...");
    }

    @Override
    public void onCancel(Object arguments) {
        context.unregisterReceiver(receiver);
        receiver = null;
        Log.d(TAG, "STOP LISTEN");
    }

}
