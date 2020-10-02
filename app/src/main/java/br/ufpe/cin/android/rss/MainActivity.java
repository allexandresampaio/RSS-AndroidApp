package br.ufpe.cin.android.rss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.prof.rssparser.Article;
import com.prof.rssparser.Channel;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //private final String RSS_FEED = "https://g1.globo.com/dynamo/rss2.xml";
    private String urlFeed;
    private Toolbar mTopToolbar;
    //definindo a recyclerview
    RecyclerView conteudoRSS;
    List<Article> noticias;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conteudoRSS = findViewById(R.id.conteudoRSS);
        mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);

        //instanciando a recyclerview no contexto atual
        //conteudoRSS = new RecyclerView(this);
        //organizando o recyclerview
        //tamanho fixo
        conteudoRSS.setHasFixedSize(true);
        //estrutura vertical
        conteudoRSS.setLayoutManager(new LinearLayoutManager(this));
        //conteudoRSS.setAdapter(new RssAdapter(this, noticias));
        //  setContentView(conteudoRSS);

        //recuperando as informações de feed padrão via sharedPreferences
        //pegando do arquivo user_preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences.Editor editor = preferences.edit();
        //urlFeed = preferences.getString(RSS_FEED, getString(R.string.feed_padrao));
        //editor.putString("feed1", getString(R.string.feed1));
        //editor.putString("feed2", getString(R.string.feed2));
        //editor.putString("feed3", getString(R.string.feed3));
        //editor.apply();
/*
        //se existe a chave rssfeed no arquivo supramencionado,
        if(preferences.contains("rssfeed")){
            //urlFeed vai receber esse valor ou o feed padrao definido no xml
            urlFeed = preferences.getString("rssfeed", getString(R.string.feed_padrao));
        } else {//se não houver essa chave
            //coloca-se o valor definido no xml como feed padrao na chave rssfeed
            editor.putString("rssfeed", getString(R.string.feed_padrao));
            editor.apply();
            //urlFeed vai receber esse valor ou o feed padrao definido no xml
            urlFeed = preferences.getString("rssfeed", getString(R.string.feed_padrao));
        }
        //prefs = getSharedPreferences(getString(R.string.feed_padrao), MODE_PRIVATE);
        //instanciando a minha prefs
        //prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //pega o valor na string cuja chave é a definida em Preferencias Activity -> RSS_FEED e coloca em urlFeed
        //se nao tiver nada salvo, ele cria e seta com o segundo argumento
        //urlFeed = prefs.toString();
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            // User chose the "Settings" item, show the app settings UI...
            startActivity(new Intent(
                    this, PreferenciasActivity.class));
            return true;
        } else {
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        urlFeed = preferences.getString(PreferenciasActivity.RSS_FEED, getString(R.string.feed_padrao));
        Parser p = new Parser.Builder().build();
        p.onFinish(
                new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Channel channel) {
                        noticias = channel.getArticles();
                        runOnUiThread(
                                () -> {
                                    RssAdapter adapter = new RssAdapter(
                                            getApplicationContext(),
                                            noticias
                                    );
                                    conteudoRSS.setAdapter(adapter);
                                    //seta o conteúdo da view apenas após pegar o array de noticias
                                    //setContentView(conteudoRSS);
                                }
                        );
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.e("RSS_APP",e.getMessage());
                    }
                }
        );
        //pegando o valor passado na SharedPreference pra usar na execução
        p.execute(urlFeed);
    }
    protected void onResume() {
        super.onResume();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PreferenciasActivity.RSS_FEED, getString(R.string.feed_padrao));
    }

    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed;
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, StandardCharsets.UTF_8);
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }
}