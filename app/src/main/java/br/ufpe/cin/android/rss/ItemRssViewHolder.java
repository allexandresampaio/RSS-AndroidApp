package br.ufpe.cin.android.rss;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
//o holder guarda as referências para os objetos
public class ItemRssViewHolder extends RecyclerView.ViewHolder {
    TextView titulo = null;
    //criando novas variáveis para armazenar os dados novos de exibição na tela
    ImageView image = null;
    TextView data = null;
    TextView url = null;

    public ItemRssViewHolder(View itemCard) {
        super(itemCard);
        this.titulo = itemCard.findViewById(R.id.titulo);
        //novos campos do construtor, buscando as referências
        this.image = itemCard.findViewById(R.id.imagem);
        this.data = itemCard.findViewById(R.id.dataPublicacao);
        this.url = itemCard.findViewById(R.id.url);
        //como o card é clicável, aqui dispara-se um intent quando ele é clicado
        itemCard.setOnClickListener(
                v -> {
                    //define-se a url relacionada com o card clicado
                    String uri = url.getText().toString();
                    //define-se o intent e seu conteúdo/parâmetros
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(uri));
                    //flag solicitada para abrir intent em outra aplicação
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent é disparado a partir do contexto atual
                    itemCard.getContext().startActivity(intent);

                }
        );
    }
}
