package com.hsenidmobile.romeosierra.translator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by kanchana on 5/15/17.
 */

public class LanguageManager {
    private Map<String, String> languages;
    private String input_lang;
    private String output_lang;

    public String getInput_lang() {
        return input_lang;
    }

    public void setInput_lang(String input_lang) {
        this.input_lang = input_lang;
    }

    public String getOutput_lang() {
        return output_lang;
    }

    public void setOutput_lang(String output_lang) {
        this.output_lang = output_lang;
    }

    public LanguageManager() {
        this.languages = new HashMap<>();
        this.languages.put("auto", "--Auto--");
    }

    public Map<String, String> getLanguages() {
        return languages;
    }

    public void setLanguages(Map<String, String> languages) {
        this.languages = languages;
    }

    public void addLanguage(String key, String value){
        this.languages.put(key, value);
    }

    public void addLanguages(Map<String, String> langs){
        for(Map.Entry<String, String> entry : langs.entrySet()){
            this.addLanguage(entry.getKey(), entry.getValue());
        }
    }

    public String getLanguageName(String key){
        return this.languages.get(key);
    }

    public Set<String> getLangCodes(){
        return this.languages.keySet();
    }
    
    public String getLangCode(String lang){
        String langCode = null;
        for (Map.Entry<String, String> entry : languages.entrySet()) {
            if(entry.getValue().equals(lang)){
                langCode = entry.getKey();
            }
        }
        return langCode;
    }

    public List<String> getLanguageNames(){
        List<String> langs = new ArrayList<>();
        for (Map.Entry<String, String> entry : languages.entrySet()) {
            langs.add(entry.getValue());
        }
        langs.sort(String::compareTo);
        return langs;
    }
}
