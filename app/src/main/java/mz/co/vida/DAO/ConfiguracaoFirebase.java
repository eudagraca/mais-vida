package mz.co.vida.DAO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {
    private static DatabaseReference baseDadosReferencia;
    private static FirebaseAuth authFirebase;

    public static DatabaseReference getFirebase(){
        if(baseDadosReferencia==null){
            baseDadosReferencia = FirebaseDatabase.getInstance().getReference();
        }
        return baseDadosReferencia;
    }

    public static  FirebaseAuth getFirebaseAuth(){
        if (authFirebase==null){
            authFirebase = FirebaseAuth.getInstance();
        }
        return authFirebase;
    }

}
