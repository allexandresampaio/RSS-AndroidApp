package br.ufpe.cin.android.rss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

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
    private String urlFeed;
    //criando variável para manipular a toolbar do app
    private Toolbar mTopToolbar;
    //definindo a recyclerview para incluir os cards com as notícias do feed
    RecyclerView conteudoRSS;
    //criando uma lista de notícias para guardar cada item capturado do XML
    List<Article> noticias;
    //definindo instância para manipulação das preferências do usuário
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //definindo o layout inicial da aplicação
        setContentView(R.layout.activity_main);
        //buscando o widget onde deve ser incluído o feed
        conteudoRSS = findViewById(R.id.conteudoRSS);
        //buscando o widget referente à toolbar
        mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        //organizando o recyclerview em tamanho fixo e estrutura vertical
        conteudoRSS.setHasFixedSize(true);
        conteudoRSS.setLayoutManager(new LinearLayoutManager(this));
        //pegando o arquivo padrão de preferências do usuário armazenado para ser manipulado
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflando o botão na toolbar para ser usado como ponto de acesso das preferências
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //definindo o que ocorre ao ser selecionada uma das opções da lista exibida ao clicar no botão da toolbar
        //como só há uma opção, a iteração ocorrerá apenas sobre ela
        //caso a opção selecionada seja o botão com o id action_settings...
        if (item.getItemId() == R.id.action_settings) {
            //iniciará uma nova Activity por meio de um intent que exibirá a tela de configurações
            startActivity(new Intent(this, PreferenciasActivity.class));
            return true;
        } else {
                //caso algo dê errado, a superclasse deverá tomar conta
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //busca nas preferências do usuário qual a url definida para o feed e salva na variável
        //em caso negativo, exibirá o feed padrão do G1, indicado no arquivo de values - strings
        urlFeed = preferences.getString(PreferenciasActivity.RSS_FEED, getString(R.string.feed_padrao));
        Parser p = new Parser.Builder().build();
        p.onFinish(
                new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Channel channel) {
                        //após o parsing da URL, salva na lista de notícias cada notícia extraída do xml
                        noticias = channel.getArticles();
                        //abre uma thread para criar um novo adapter e entregar a ele a lista de notícias
                        runOnUiThread(
                                () -> {
                                    RssAdapter adapter = new RssAdapter(
                                            getApplicationContext(),
                                            noticias
                                    );
                                    //alterando o widget da tela para mostrar o feed de noticias selecionado
                                    conteudoRSS.setAdapter(adapter);
                                    //disparando a função que salva o feed no banco de dados
                                    salvarNoticiasNoDB();
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
        //quando a activity é iniciada por meio de um retorno à tela principal
        //por exemplo, quando o usuário volta da tela de preferências para a tela principal
        super.onResume();
        //definindo um editor para gravar preferências
        SharedPreferences.Editor editor = preferences.edit();
        //busca na activity que manipula a tela de preferências o valor definido para o feed do usuario e salva como shared preference
        editor.putString(PreferenciasActivity.RSS_FEED, getString(R.string.feed_padrao));
    }

    private void salvarNoticiasNoDB(){
        //inicia-se uma nova thread para salvar as noticias do feed no banco de dados de forma assíncrona
        new Thread(() -> {
            //itera sobre o array de notícias para salvá-las uma a uma
            for (int i=0; i<noticias.size(); i++){
                //separa cada item da notícia em uma variável
                String link = noticias.get(i).getLink();
                String titulo = noticias.get(i).getTitle();
                String descricao = noticias.get(i).getDescription();
                String categorias = noticias.get(i).getCategories().toString();
                String data = noticias.get(i).getContent();
                //instancia uma nova entidade de notícia
                Noticia noticia = new Noticia(link, titulo, descricao, categorias, data);
                //busca a instância atual de banco de dados
                NoticiasDB bd = NoticiasDB.getInstance(this);
                //busca a interfacec para manipulação do banco de dados
                NoticiasDAO dao = bd.obterDAO();
                //insere a notícia no banco de dados
                dao.inserirNoticia(noticia);
            }
        }).start();
    }

}