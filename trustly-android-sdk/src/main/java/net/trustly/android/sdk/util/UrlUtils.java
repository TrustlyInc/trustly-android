package net.trustly.android.sdk.util;

import android.net.Uri;
import android.util.Base64;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
        try {
            JSONObject jsonObject = buildJsonObject(parameters);
            return jsonObject.toString().replace("\"nameValuePairs\":{", "")
                    .replace(" }\n},", "}")
                    .replace("\\/", "/");
        } catch (JSONException e) {
            return new Gson().toJson(parameters);
        }
    }

    public static JSONObject buildJsonObject(Map<String, String> data) throws JSONException {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, String> e : data.entrySet()) {
            String[] keys = e.getKey().split("\\.");
            JSONObject current = json;
            for (int i = 0; i < keys.length; ++i) {
                String key = keys[i];
                try {
                    current = current.getJSONObject(key);
                } catch (JSONException ex) {
                    if (i == keys.length - 1) {
                        current.put(key, e.getValue());
                    } else {
                        JSONObject tmp = new JSONObject();
                        current.put(key, tmp);
                        current = tmp;
                    }
                }
            }
        }
        return json;
    }

    public static String encodeStringToBase64(String value) {
        return Base64.encodeToString(value.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
    }

}
