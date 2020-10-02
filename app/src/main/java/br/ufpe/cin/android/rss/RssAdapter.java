package br.ufpe.cin.android.rss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.prof.rssparser.Article;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RssAdapter extends RecyclerView.Adapter <ItemRssViewHolder> {
    List<Article> noticias;
    Context context;

    //adapter criado para tratar os dados trazidos da activity principal e
    //separar as notícias em seus repectivos campos na tela de feed
    public RssAdapter(Context c, List<Article> noticias) {
        this.noticias = noticias;
        this.context = c;
    }

    public int getCount() {
        return noticias.size();
    }

    public Object getItem(int i) {
        return noticias.get(i);
    }

    @NonNull
    @Override
    public ItemRssViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //criando um inflater para exibir novos itens
        LayoutInflater inflater = LayoutInflater.from(context);
        //inflando o layout definido em linha.xml (cards)
        View v = inflater.inflate(R.layout.linha, parent, false);
        //criando um viewholdedr, que usará as informações exibidas na tela para executar ações no app
        ItemRssViewHolder viewHolder = new ItemRssViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRssViewHolder viewHolder, int i) {
        //criando um novo Article com os dados da notícia do xml
        Article noticia = noticias.get(i);
        //definindo os campos a serem exibidos na tela: titulo, imagem e data da publicação
        viewHolder.titulo.setText(noticia.getTitle());
        //customizei a exibição para fazer a data ficar melhor de ser lida, com uma substring
        viewHolder.data.setText("Publicado em: " + noticia.getPubDate().substring(0, 22));
        //salvando o link da noticia em um textview do viewholder que vai ser invisível
        viewHolder.url.setText(noticia.getLink());
        //usando a biblioteca Picasso para obter as imagens anexadas as noticias e exebi-las no layout
        String url = noticias.get(i).getImage();
        Picasso.with(context)
                .load(url)
                .into(viewHolder.image);
    }

    @Override
    public long getItemId(int i) {
        return noticias.get(i).hashCode();
    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }
}
