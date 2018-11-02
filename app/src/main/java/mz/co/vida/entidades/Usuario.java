package mz.co.vida.entidades;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import DAO.ConfiguracaoFirebase;

public class Usuario {

    private String uidUser;
    private String nome;

    private String telefone;
    private String sexo;
    private String email;
    private String senha;
    private String provincia;
    private String unidadeProxima;
    private String tipoSanguineo;
    private String disponibilidade;
    private String estado;
    private String foto;



    public Usuario() {

    }

    public void gravar(){
        DatabaseReference dbRef = ConfiguracaoFirebase.getFirebase();
        dbRef.child("Usuario").child(String.valueOf(getUidUser())).setValue(this);
       // dbRef.child("foto de perfil").setValue(getFoto());
    }
    @Exclude
    public Map <String, Object> toMap(){
        HashMap<String, Object> hashMapUser = new HashMap<>();

       // hashMapUser.put("id", getUidUser());
        hashMapUser.put("nome", getNome());
        hashMapUser.put("email", getEmail());
        hashMapUser.put("senha", getSenha());
        hashMapUser.put("telefone", getTelefone());
        hashMapUser.put("sexo", getSexo());
        hashMapUser.put("localizacao", getProvincia());
        hashMapUser.put("unidadeProxima", getUnidadeProxima());
        hashMapUser.put("tipoSanguineo", getTipoSanguineo());
        hashMapUser.put("disponibilidade", getDisponibilidade());
        hashMapUser.put("estado", getEstado());
        hashMapUser.put("fotoPerfil", getFoto());
        return hashMapUser;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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

    public String getTipoSanguineo() {
        return tipoSanguineo;
    }

    public void setTipoSanguineo(String tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
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