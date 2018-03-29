package com.vinicius.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.vinicius.whatsapp.Manifest;
import com.vinicius.whatsapp.R;
import com.vinicius.whatsapp.helper.Permissao;
import com.vinicius.whatsapp.helper.Preferencias;

import java.util.HashMap;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText nome;
    private EditText telefone;
    private EditText codPais;
    private EditText codArea;
    private Button cadastrar;

    //Lista de Permissões Necessarias para Activity
    private String[] permissoesNecessarias = new String[]{
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Perdir Permissões
        Permissao.validaPermissoes(1, this, permissoesNecessarias);

        //Componentes da View
        nome = findViewById(R.id.edit_nome);
        telefone = findViewById(R.id.edit_telefone);
        codPais = findViewById(R.id.edit_cod_pais);
        codArea = findViewById(R.id.edit_cod_area);
        cadastrar = findViewById(R.id.button_cadastrar);

        //Definir as mascaras
        SimpleMaskFormatter simpleMaskCodPais = new SimpleMaskFormatter("+NN");
        SimpleMaskFormatter simpleMaskCodArea = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("NNNNN-NNNN");

        MaskTextWatcher maskCodPais = new MaskTextWatcher(codPais, simpleMaskCodPais);
        MaskTextWatcher maskCodArea = new MaskTextWatcher(codArea, simpleMaskCodArea);
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone, simpleMaskTelefone);

        codPais.addTextChangedListener(maskCodPais);
        codArea.addTextChangedListener(maskCodArea);
        telefone.addTextChangedListener(maskTelefone);

        cadastrar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String nomeUsuario = nome.getText().toString();
                String telefoneCompleto =
                                codPais.getText().toString() +
                                codArea.getText().toString()+
                                telefone.getText().toString();

                String telefoneSemformatacao = telefoneCompleto.replace("+","");
                telefoneSemformatacao = telefoneSemformatacao.replace("-","");

                //Gerar token
                Random randomico = new Random();
                int numeroRandomico = randomico.nextInt(9999 - 1000) + 1000;
                String token = String.valueOf(numeroRandomico);
                String mensagemEnvio = "WhatsApp Código de Confirmação: " + token;
                //Log.i("TOKEN", "T: " + token);

                //Telefone do emulador
                telefoneSemformatacao = "5554";

                //Salavar os dados para validação
                Preferencias preferencias = new Preferencias(LoginActivity.this);
                preferencias.salvarUsuarioPreferencias(nomeUsuario, telefoneSemformatacao, token);

                //HashMap<String, String> usuario = preferencias.getDadosUsuario();
                //Log.i("TOKEN", "Nome: " + usuario.get("nome") + " Fone: " + usuario.get("telefone"));

                //Envio de SMS
                boolean enviaSms = enviaSMS("+" + telefoneSemformatacao, mensagemEnvio);

                if(enviaSms){
                    Intent intent = new Intent(LoginActivity.this, ValidadorActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Problema ao inviar o SMS, tente novamente", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    //Envio SMS
    private boolean enviaSMS(String telefone, String mensagem){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int resultado: grantResults){
            if(resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar esse app, é necessarios aceitar as permissões");

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
