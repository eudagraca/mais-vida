package DAO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {
    private static DatabaseReference baseDadosReferencia;
    private static FirebaseAuth authFirebase;
    private static StorageReference fireStorage;

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
