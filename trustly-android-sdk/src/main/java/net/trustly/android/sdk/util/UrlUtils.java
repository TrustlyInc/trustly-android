package net.trustly.android.sdk.util;

import android.net.Uri;
import android.util.Base64;

import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class UrlUtils {

    private static final String EMPTY = "";
    private static final String AMPERSAND_CHAR = "&";
    private static final String EQUALS_CHAR = "=";
    private static final String URL = "url";
    private static final String REQUEST_SIGNATURE = "requestSignature=.*";
    private static final String SEPARATOR = "\\.";

    protected UrlUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static Set<String> getQueryParameterNames(Uri uri) {
        String query = uri.getEncodedQuery();
        if (query == null) return Collections.emptySet();
        Set<String> names = new LinkedHashSet<>();
        int start = 0;
        do {
            int next = query.indexOf(AMPERSAND_CHAR, start);
            int end = (next == -1) ? query.length() : next;
            int separator = query.indexOf(EQUALS_CHAR, start);
            if (separator > end || separator == -1) {
                separator = end;
            }
            String name = query.substring(start, separator);
            names.add(Uri.decode(name));
            // Move start to end of name.
            start = end + 1;
        } while (start < query.length());
        return Collections.unmodifiableSet(names);
    }

    public static Map<String, String> getQueryParametersFromUrl(String url) {
        Uri uri = Uri.parse(url);
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put(URL, url.replaceAll(REQUEST_SIGNATURE, EMPTY));
        Set<String> queryParametersKeys = UrlUtils.getQueryParameterNames(uri);
        for (String queryParameterKey : queryParametersKeys) {
            queryParameters.put(queryParameterKey, uri.getQueryParameter(queryParameterKey));
        }
        return queryParameters;
    }

    public static String getParameterString(Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (sb.length() > 0) sb.append(AMPERSAND_CHAR);
            sb.append(urlEncode(entry.getKey()));
            sb.append(EQUALS_CHAR);
            sb.append(urlEncode(entry.getValue()));
        }
        return sb.toString();
    }

    @SuppressWarnings("deprecation")
    private static String urlEncode(String str) {
        if (str == null) return EMPTY;
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return URLEncoder.encode(str);
        }
    }

    public static String getJsonFromParameters(Map<String, String> parameters) {
        return buildJsonObjectSecond(parameters).toString();
    }

    private static JsonObject buildJsonObjectSecond(Map<String, String> data) {
        JsonObject json = new JsonObject();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String[] keys = entry.getKey().split(SEPARATOR);
            JsonObject current = json;
            for (int i = 0; i < keys.length; ++i) {
                String key = keys[i];
                if (i == keys.length - 1) {
                    current.addProperty(key, entry.getValue());
                } else {
                    if (!current.has(key)) {
                        current.add(key, new JsonObject());
                    }
                    current = current.getAsJsonObject(key);
                }
            }
        }
        return json;
    }

    public static String encodeStringToBase64(String value) {
        return Base64.encodeToString(value.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
    }

}
