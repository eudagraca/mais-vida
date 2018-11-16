package mz.co.vida.helpers;


import android.content.Context;
import android.content.SharedPreferences;

//Grava o usuario no celular
public class Preferencias {

    private Context context;
    private SharedPreferences preferences;
    private int MODE= 0;
    private SharedPreferences.Editor  editor;

    private final String CHAVE_IDENTIFICADOR= "identificarUsuarioNaApp";
    private final String CHAVE_NOME ="nomeUsuario";


    public Preferencias(Context context) {

        this.context = context;
        String NOME_ARQUIVO = "VIDA.Preferencias";
        preferences = context.getSharedPreferences(NOME_ARQUIVO,MODE);

        editor = preferences.edit();
        editor.commit();
    }


    public void gravarUsuario(String identificarUsuarioNaApp, String nomeUsuario){
        editor.putString(CHAVE_IDENTIFICADOR, identificarUsuarioNaApp);
        editor.putString(CHAVE_NOME, nomeUsuario);
        editor.commit();
    }

    public String getIdentificador(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome(){
        return preferences.getString(CHAVE_NOME, null);
    }
}
