package com.vinicius.whatsapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vinicius.whatsapp.R;
import com.vinicius.whatsapp.model.Contato;

import java.util.ArrayList;

public class ContatoAdapter extends ArrayAdapter<Contato>{

    private ArrayList<Contato> contatos;
    private Context context;

    public ContatoAdapter(@NonNull Context c, @NonNull ArrayList<Contato> objects) {
        super(c, 0, objects);

        this.contatos = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        //Verifica se a lista está vazia
        if(contatos != null){
            //inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Montar view a partir do xml
            view = inflater.inflate(R.layout.lista_adapter, parent, false);

            //Recupera elemento para exibição
            TextView nomeContato = view.findViewById(R.id.tv_titulo);
            TextView emailContato = view.findViewById(R.id.tv_subtitulo);

            Contato contato = contatos.get(position);
            nomeContato.setText(contato.getNome());
            emailContato.setText(contato.getEmail());

        }

        return view;
    }
}
