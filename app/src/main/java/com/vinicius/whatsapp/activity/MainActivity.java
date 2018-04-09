package com.vinicius.whatsapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vinicius.whatsapp.Adapter.TabAdapter;
import com.vinicius.whatsapp.R;
import com.vinicius.whatsapp.config.ConfiguracaoFireBase;
import com.vinicius.whatsapp.helper.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);

        slidingTabLayout = findViewById(R.id.stl_tabs);
        viewPager = findViewById(R.id.vp_pagina);

        //Configurar sliding tabs
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        //Configurar adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_sair:
                deslogaUsuario();
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_adiciona:
                abreCadastroContato();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void abreCadastroContato(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        //Configurações do Dialog
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("E-mail do usuário");
        alertDialog.setCancelable(false);

        EditText editText = new EditText(MainActivity.this);
        alertDialog.setView(editText);

        //Configurar botões
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.create();
        alertDialog.show();

    }

    public void deslogaUsuario(){
        autenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
