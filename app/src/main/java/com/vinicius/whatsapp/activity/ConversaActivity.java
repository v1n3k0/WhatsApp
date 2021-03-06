package com.vinicius.whatsapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.vinicius.whatsapp.R;
import com.vinicius.whatsapp.adapter.MensagemAdapter;
import com.vinicius.whatsapp.config.ConfiguracaoFireBase;
import com.vinicius.whatsapp.helper.Base64Custom;
import com.vinicius.whatsapp.helper.Preferencias;
import com.vinicius.whatsapp.model.Contato;
import com.vinicius.whatsapp.model.Conversa;
import com.vinicius.whatsapp.model.Mensagem;

import java.util.ArrayList;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Contato contato;
    private EditText editMensagem;
    private ImageButton btMensagem;
    private DatabaseReference firebase;
    private ListView listView;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter <Mensagem> adapter;
    private ValueEventListener valueEventListenerMensagem;

    //dados do destinatario
    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;

    //dados do remetente
    private String idUsuarioRemetente;
    private String nomeUsuarioRemetente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = findViewById(R.id.tb_conversa);
        editMensagem = findViewById(R.id.edit_mensagem);
        btMensagem = findViewById(R.id.bt_enviar);
        listView = findViewById(R.id.lv_conversas);

        //dados do usuario logado
        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioRemetente = preferencias.getIdentificador();
        nomeUsuarioRemetente = preferencias.getNome();

        //Recuperar dados passados entre Ativity
        Intent i = getIntent();
        if(i != null){
            contato = (Contato) i.getSerializableExtra("contato");
            idUsuarioDestinatario = Base64Custom.codificarBase64(contato.getEmail());
            nomeUsuarioDestinatario = contato.getNome();
        }

        //Configurar toolbar
        toolbar.setTitle(contato.getNome());
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Monta listView e adapter
        mensagens = new ArrayList<>();
        adapter = new MensagemAdapter(ConversaActivity.this, mensagens);
        listView.setAdapter(adapter);

        //Recuperar mensagens do firebase
        firebase = ConfiguracaoFireBase.getFirebase()
                .child("Mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

        //Criar listener para mensagens
        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensagens.clear();

                //recuperara mensagem
                for( DataSnapshot dados: dataSnapshot.getChildren()){
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener(valueEventListenerMensagem);

        //Enviar mensagem
        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoMensagem = editMensagem.getText().toString();

                if(textoMensagem.isEmpty() ){
                    Toast.makeText(ConversaActivity.this, "Digite uma mensagem para enviar!", Toast.LENGTH_LONG).show();
                }else {

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRemetente);
                    mensagem.setMensagem(textoMensagem);

                    //Salvar mensagem para remetente
                    Boolean retornoMensagemRemetente = salvaMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                    if(!retornoMensagemRemetente){
                        Toast.makeText(ConversaActivity.this, "Problema ao salvar mensagem, tente novamente!", Toast.LENGTH_LONG).show();
                    }else{
                        //Salvar mensagem para destinatario
                        Boolean retornoMensagemDestinatario = salvaMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);
                        if(!retornoMensagemDestinatario){
                            Toast.makeText(ConversaActivity.this, "Problema ao salvar mensagem para o destinatário, tente novamente!", Toast.LENGTH_LONG).show();
                        }
                    }

                    //Salvar Conversa para remtente
                    Conversa conversa = new Conversa();
                    conversa.setIdUsuario(idUsuarioRemetente);
                    conversa.setNome(nomeUsuarioRemetente);
                    conversa.setMensagem(textoMensagem);
                    Boolean retornoConversaRemetente = salvaConversa(idUsuarioRemetente, idUsuarioDestinatario, conversa);

                    if(!retornoConversaRemetente){
                        Toast.makeText(ConversaActivity.this, "Problema ao salvar conversa, tente novamente!", Toast.LENGTH_LONG).show();
                    }else{
                        //Salvar Conversa para destinatario
                        conversa = new Conversa();
                        conversa.setIdUsuario(idUsuarioDestinatario);
                        conversa.setNome(nomeUsuarioDestinatario);
                        conversa.setMensagem(textoMensagem);
                        Boolean retornoConversaDestinatario = salvaConversa(idUsuarioDestinatario, idUsuarioRemetente, conversa);

                        if(!retornoConversaDestinatario){
                            Toast.makeText(ConversaActivity.this, "Problema ao salvar conversa para o destinatário, tente novamente!", Toast.LENGTH_LONG).show();
                        }
                    }


                    editMensagem.setText("");
                }
            }
        });

    }

    private boolean salvaMensagem(String idDestinatario, String idUsuarioRemetente, Mensagem mensagem){
        try{

            firebase = ConfiguracaoFireBase.getFirebase().child("Mensagens");

            firebase.child(idUsuarioRemetente)
                    .child(idDestinatario)
                    .push()
                    .setValue(mensagem);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean salvaConversa(String idDestinatario, String idUsuarioRemetente, Conversa conversa){
        try {
            firebase = ConfiguracaoFireBase.getFirebase().child("Conversas");
            firebase.child(idUsuarioRemetente)
                    .child(idDestinatario)
                    .setValue(conversa);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagem);
    }
}