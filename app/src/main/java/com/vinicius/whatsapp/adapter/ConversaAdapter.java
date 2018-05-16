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
import com.vinicius.whatsapp.model.Conversa;

import java.util.ArrayList;
import java.util.List;

public class ConversaAdapter extends ArrayAdapter<Conversa> {

    private ArrayList<Conversa> conversas;
    private Context context;

    public ConversaAdapter(@NonNull Context c, @NonNull ArrayList<Conversa> objects) {
        super(c, 0, objects);
        this.context = c;
        this.conversas = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        //Virifica se a lista esta preenchida
        if(conversas != null){

            //Inicializar objetivo para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Montar view a partir do xml
            view = inflater.inflate(R.layout.lista_adapter, parent, false);

            //Recupera elemento para exibição
            TextView titulo = view.findViewById(R.id.tv_titulo);
            TextView subtitulo = view.findViewById(R.id.tv_subtitulo);

            Conversa conversa = conversas.get(position);
            titulo.setText(conversa.getNome());
            subtitulo.setText(conversa.getMensagem());
        }

        return view;
    }
}
