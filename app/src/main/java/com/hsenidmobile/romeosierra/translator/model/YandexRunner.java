package com.hsenidmobile.romeosierra.translator.model;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hsenidmobile.romeosierra.translator.exceptions.CharacterLimitExceededException;
import com.loopj.android.http.*;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by kanchana on 5/15/17.
 */

public class YandexRunner {
    private Context context;
    private String apiKey;
    private LanguageManager languageManager;

    public YandexRunner(Context context, LanguageManager languageManager) {
        this.context = context;
        this.languageManager = languageManager;
        this.apiKey = "trnsl.1.1.20170509T020247Z.08232df9c0a87744.2abd5ac2178ffe7f5623b078c0271147d8580b88";
    }

    public void detectLang(String sourceText, String hint){
        RequestParams params = new RequestParams();
        try {
            params.put("text", URLEncoder.encode(sourceText, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.put("key", this.apiKey);
        params.put("hint", languageManager.getLangCode(hint));

        YandexClient.get("detect", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JsonElement root = new JsonParser().parse(new String(responseBody));
                    String langCode_detected = root.getAsJsonObject().get("lang").getAsString();
                    languageManager.setInput_lang(langCode_detected);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getLangs(String ui, Spinner ... spinners){
        RequestParams params = new RequestParams();
        params.put("ui", ui);
        params.put("key", this.apiKey);

        YandexClient.get("getLangs", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JsonElement root = new JsonParser().parse(new String(responseBody));
                Type mapType = new TypeToken<HashMap<String, String>>(){}.getType();
                Map<String, String> langList = new Gson().fromJson(root.getAsJsonObject().get("langs").getAsJsonObject(), mapType);
                languageManager.addLanguages(langList);
                try {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, languageManager.getLanguageNames());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    for(Spinner s : spinners){
                        s.setAdapter(adapter);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void translate(String text, @Nullable String lang_in, String lang_out, @Nullable EditText txt_out) throws CharacterLimitExceededException{
        if(text.length() > 10000)
            throw new CharacterLimitExceededException("Length of input string exceeds 10000 which is the maximum allowed!");

        RequestParams params = new RequestParams();
        params.put("key", this.apiKey);
        params.put("text", text);
        String direction = lang_out;
        if(lang_in != null)
            direction = String.format("%s-%s", lang_in, direction);
        params.put("lang", direction);
        params.put("format", "plain");

        YandexClient.get("translate", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(txt_out != null){
                    try {
                        JsonElement root = new JsonParser().parse(new String(responseBody));
                        String[] outputText = new Gson().fromJson(root.getAsJsonObject().get("text").getAsJsonArray(), String[].class);
                        txt_out.setText(outputText[0]);
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
