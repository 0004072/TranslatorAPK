package com.hsenidmobile.romeosierra.translator.model;

/**
 * Created by kanchana on 5/15/17.
 */

import com.loopj.android.http.*;

public class YandexClient {
    private static final String BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
