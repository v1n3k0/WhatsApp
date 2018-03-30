package com.vinicius.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfiguracaoFireBase {
    private static DatabaseReference referenceFireBase;
    private static FirebaseAuth autenticacao;

    public static DatabaseReference getFirebase(){
        if(referenceFireBase == null){
            referenceFireBase = FirebaseDatabase.getInstance().getReference();
        }

        return referenceFireBase;
    }

    public static FirebaseAuth getFirebaseAutenticacao(){
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

}
