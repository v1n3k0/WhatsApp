package com.vinicius.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.vinicius.whatsapp.Manifest;
import com.vinicius.whatsapp.R;
import com.vinicius.whatsapp.config.ConfiguracaoFireBase;
import com.vinicius.whatsapp.helper.Base64Custom;
import com.vinicius.whatsapp.helper.Permissao;
import com.vinicius.whatsapp.helper.Preferencias;
import com.vinicius.whatsapp.model.Usuario;

import java.util.HashMap;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private ProgressBar progressBar;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUsuario;
    private String identificadorUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificaUsuarioLogado();

        email = findViewById(R.id.edit_login_email);
        senha = findViewById(R.id.edit_login_senha);
        botaoLogar = findViewById(R.id.button_login_logar);
        progressBar = findViewById(R.id.progressBar);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                botaoLogar.setEnabled(false);

                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());

                validaLogin();
            }
        });
    }

    //Verifica se usuario já esta logado no app
    private void verificaUsuarioLogado(){
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    //Faz o login do usuario
    private void validaLogin(){

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();

        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    //Preferencias preferencias = new Preferencias(LoginActivity.this);
                    identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());

                    firebase = ConfiguracaoFireBase.getFirebase()
                            .child("Usuarios")
                            .child(identificadorUsuario);

                    valueEventListenerUsuario  = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);

                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                            preferencias.salvarDados(identificadorUsuario, usuarioRecuperado.getNome());

                            abrirTelaPrincipal();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    firebase.addListenerForSingleValueEvent(valueEventListenerUsuario);

                    //abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(LoginActivity.this, "Erro ao fazer login", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
                botaoLogar.setEnabled(true);
            }
        });
    }

    //Abre a View principal
    private void abrirTelaPrincipal(){

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //Abrir View CadastroUsuario
    public void abrirCadastroUsuario(View view){

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

}