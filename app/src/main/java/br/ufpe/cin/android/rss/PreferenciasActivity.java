package br.ufpe.cin.android.rss;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class PreferenciasActivity extends AppCompatActivity {
    //essa variável será utilizada na main ativity para ser guardada como a shared preference do usuário
    //ela identifica o que está sendo selecionado na tela e altera esse valor automaticamente
    public static final String RSS_FEED = "rssfeed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //quando a activity é criada, o layout activity_preferências é exibido
        setContentView(R.layout.activity_preferencias);
        //um novo fragmento é criado, do tipo PrefsFragment
        PrefsFragment prefsFrag = new PrefsFragment();
        //o campo identificado por "settings" no layout, será substituído pelo fragmento definido anteriormente
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings,prefsFrag)
                .commit();
    }
}