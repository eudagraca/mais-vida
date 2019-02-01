package mz.co.vida.entities;

import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.activities.Profile_UpdadeActivity;
import mz.co.vida.utils.MyUtils;

public class Usuario {

    private String user_id;
    private String nome;
    private String contacto;
    private String sexo;
    private String email;
    private String senha;
    private String provincia;
    private String unidadeProxima;
    private String tipo_sangue;
    private String disponibilidade;
    private String estado;
    private String foto;

    public Usuario() { }


    public void gravar(){
        DatabaseReference dbRef = ConfiguracaoFirebase.getFirebase();
        dbRef.child("Usuario").child(String.valueOf(getUser_id())).setValue(toMap());
    }

    private StorageReference storageReference;


    public void update(String key){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        databaseReference.child("Usuario").child(key).updateChildren(toMapUpdate());
    }

    @Exclude
    private Map <String, Object> toMap(){
        HashMap<String, Object> hashMapUser = new HashMap<>();
        hashMapUser.put("user_id", getUser_id());
        hashMapUser.put("nome", getNome());
        hashMapUser.put("email", getEmail());
        hashMapUser.put("foto", getFoto());
        hashMapUser.put("senha", getSenha());
        hashMapUser.put("contacto", getContacto());
        hashMapUser.put("sexo", getSexo());
        hashMapUser.put("provincia", getProvincia());
        hashMapUser.put("unidadeProxima", getUnidadeProxima());
        hashMapUser.put("tipo_sangue", getTipo_sangue());
        hashMapUser.put("disponibilidade", getDisponibilidade());
        hashMapUser.put("estado", getEstado());
        return hashMapUser;
    }

    @Exclude
    private Map <String, Object> toMapFoto(){
        HashMap<String, Object> hashMapUser = new HashMap<>();
        hashMapUser.put("foto", getFoto());
        return hashMapUser;
    }


    @Exclude
    private Map <String, Object> toMapUpdate(){
        HashMap<String, Object> hashMapUser = new HashMap<>();
        hashMapUser.put("contacto", getContacto());
        hashMapUser.put("sexo", getSexo());
        hashMapUser.put("provincia", getProvincia());
        hashMapUser.put("unidadeProxima", getUnidadeProxima());
        hashMapUser.put("disponibilidade", getDisponibilidade());
        hashMapUser.put("estado", getEstado());
        return hashMapUser;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    private String getUser_id() {
        return user_id;
    }

    public void setUidUser(String uidUser) {
        this.user_id = uidUser;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getUnidadeProxima() {
        return unidadeProxima;
    }

    public void setUnidadeProxima(String unidadeProxima) {
        this.unidadeProxima = unidadeProxima;
    }

    public String getTipo_sangue() {
        return tipo_sangue;
    }

    public void setTipo_sangue(String tipo_sangue) {
        this.tipo_sangue = tipo_sangue;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
    }


    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}