package com.vinicius.whatsapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.vinicius.whatsapp.R;
import com.vinicius.whatsapp.model.Contato;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = findViewById(R.id.tb_conversa);

        //Recuperar dados passados entre Ativity
        Intent i = getIntent();
        if(i != null) contato = (Contato) i.getSerializableExtra("contato");


        //Configurr toolbar
        toolbar.setTitle(contato.getNome());
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

    }
}
