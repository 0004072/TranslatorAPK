package com.hsenidmobile.romeosierra.translator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hsenidmobile.romeosierra.translator.exceptions.CharacterLimitExceededException;
import com.hsenidmobile.romeosierra.translator.model.*;

public class MainActivity extends AppCompatActivity{
    LanguageManager langMgr;
    private EditText txt_in, txt_out;
    private Spinner cmb_lang_in, cmb_lang_out;
    private YandexRunner runner;
    private Button btn_translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        langMgr = new LanguageManager();
        runner = new YandexRunner(this, langMgr);
        txt_in = (EditText) findViewById(R.id.txt_in);
        txt_out = (EditText) findViewById(R.id.txt_out);
        cmb_lang_in = (Spinner) findViewById(R.id.cmb_lang_in);
        cmb_lang_out = (Spinner) findViewById(R.id.cmb_lang_out);
        btn_translate = (Button) findViewById(R.id.btn_tr);

        runner.getLangs("en", cmb_lang_in, cmb_lang_out);

        btn_translate.setOnClickListener(view -> {
            String txt_in_content = txt_in.getText().toString();
            if(txt_in_content.length() < 1)
                return;

            Object cmb_lang_out_val = cmb_lang_out.getSelectedItem();
            if(cmb_lang_out_val.toString().equals("--Auto--")) {
                Toast.makeText(MainActivity.this, "Please select output language!", Toast.LENGTH_SHORT).show();
                cmb_lang_out.requestFocus();
                return;
            }
            String lang_out = langMgr.getOutput_lang();

            String lang_in = null;
            Object cmb_lang_in_val = cmb_lang_in.getSelectedItem();
            if(!cmb_lang_in_val.toString().equals("--Auto--")){
                lang_in = langMgr.getInput_lang();
            }

            try {
                runner.translate(txt_in_content, lang_in, lang_out, txt_out);
            } catch (CharacterLimitExceededException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        txt_out.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(txt_out.getText().length() < 10)
                    return;
                Object cmb_lang_in_val = cmb_lang_in.getSelectedItem();
                String hint = "";
                if(!cmb_lang_in_val.toString().equals("--Auto--"))
                    hint = cmb_lang_in_val.toString();
                runner.detectLang(txt_out.getText().toString(), hint);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cmb_lang_in.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                langMgr.setInput_lang(langMgr.getLangCode(cmb_lang_in.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cmb_lang_out.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                langMgr.setOutput_lang(langMgr.getLangCode(cmb_lang_out.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
