package net.trustly.android.sdk.util;

import android.net.Uri;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class UrlUtils {

    private UrlUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static Set<String> getQueryParameterNames(Uri uri) {
        String query = uri.getEncodedQuery();
        if (query == null) {
            return Collections.emptySet();
        }

        Set<String> names = new LinkedHashSet<>();
        int start = 0;
        do {
            int next = query.indexOf('&', start);
            int end = (next == -1) ? query.length() : next;

            int separator = query.indexOf('=', start);
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
        Uri uri1 = Uri.parse(url);
        Set<String> queryParametersKeys = UrlUtils.getQueryParameterNames(uri1);
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("url", url.replaceAll("requestSignature=.*", ""));
        for (String queryParameterKey : queryParametersKeys) {
            queryParameters.put(queryParameterKey, uri1.getQueryParameter(queryParameterKey));
        }

        return queryParameters;
    }

    public static String getParameterString(Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(urlEncode(entry.getKey()));
            sb.append("=");
            sb.append(urlEncode(entry.getValue()));
        }
        return sb.toString();
    }

    private static String urlEncode(String str) {
        if (str == null) return "";
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            return URLEncoder.encode(str);
        }
    }

}
