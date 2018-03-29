package com.vinicius.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.vinicius.whatsapp.R;
import com.vinicius.whatsapp.helper.Preferencias;

import java.util.HashMap;

public class ValidadorActivity extends AppCompatActivity {

    private EditText codigoValidacao;
    private Button validar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        codigoValidacao = findViewById(R.id.edit_cod_validacao);
        validar = findViewById(R.id.button_validar);

        //Mascara para com codigo validacao
        SimpleMaskFormatter simpleMaskCodigoValidacao = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher maskTextCodigoValidacao = new MaskTextWatcher(codigoValidacao, simpleMaskCodigoValidacao);

        codigoValidacao.addTextChangedListener(maskTextCodigoValidacao);

        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //recuperar dados das preferencias do usuario
                Preferencias preferencias = new Preferencias(ValidadorActivity.this);
                HashMap<String, String> usuario = preferencias.getDadosUsuario();

                String tokenGerado = usuario.get("token");
                String tokenDigitado = codigoValidacao.getText().toString();

                if(tokenDigitado.equals(tokenGerado)){
                    Toast.makeText(ValidadorActivity.this, "Token Validado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ValidadorActivity.this, "Token Invalido!", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}
