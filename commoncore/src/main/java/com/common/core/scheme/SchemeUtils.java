package com.common.core.scheme;

import android.net.Uri;
import android.text.TextUtils;

import com.common.log.MyLog;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lan on 16/10/26.
 *
 * @module scheme
 * @description 该类中的方法与具体业务无关，只是提供简化的代码逻辑
 */
public class SchemeUtils {
    public static long getLong(Uri uri, String key, long defaultValue) {
        try {
            return Long.valueOf(uri.getQueryParameter(key));
        } catch (Exception e) {
            MyLog.e(e);
        }
        return defaultValue;
    }

    public static int getInt(Uri uri, String key, int defaultValue) {
        try {
            String s = uri.getQueryParameter(key);
            if(s!=null){
                return Integer.valueOf(s);
            }
        } catch (Exception e) {
            MyLog.e(e);
        }
        return defaultValue;
    }

    public static String getString(Uri uri, String key) {
        try {
            return uri.getQueryParameter(key);
        } catch (Exception e) {
            MyLog.e(e);
        }
        return "";
    }

    public static boolean getBoolean(@NotNull Uri uri, @NotNull String key, boolean b) {
        try {
            String s = uri.getQueryParameter(key);
            if(s!=null){
                return Boolean.parseBoolean(s);
            }
        } catch (Exception e) {
            MyLog.e(e);
        }
        return b;
    }

    public static String join(String url,Map<String, Object> params, Integer requestCode) {
        Uri uri = Uri.parse(url);

        StringBuilder sb = new StringBuilder();
        sb.append(uri.getScheme()).append("://").append(uri.getHost()).append(uri.getPath());
        HashMap<String,Object> paramsMap = new HashMap();
        if(requestCode!=0){
            paramsMap.put("requestCode",requestCode);
        }
        Set<String> keys = uri.getQueryParameterNames();
        for(String key:keys){
            Object value = uri.getQueryParameter(key);
            paramsMap.put(key,value);
        }
        if(params!=null){
            paramsMap.putAll(params);
        }
        if(!paramsMap.isEmpty()){
            sb.append("?");
            for(String key:paramsMap.keySet()){
                sb.append(key).append("=").append(paramsMap.get(key)).append("&");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }

    public static HashMap<String,Object> getParams(Uri uri) {
        HashMap<String,Object> paramsMap = new HashMap();
        Set<String> keys = uri.getQueryParameterNames();
        for(String key:keys){
            Object value = uri.getQueryParameter(key);
            paramsMap.put(key,value);
        }
        return paramsMap;
    }

}
