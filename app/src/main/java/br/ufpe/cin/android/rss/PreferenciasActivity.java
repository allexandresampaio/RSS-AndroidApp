package br.ufpe.cin.android.rss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import android.os.Bundle;

public class PreferenciasActivity extends AppCompatActivity {

    public static final String RSS_FEED = "feed_padrao";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);
        //Após criar o fragmento, use o código abaixo para exibir
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings,new PrefsFragment())
                .commit();



    }



}