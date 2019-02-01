package mz.co.vida.entities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;
import mz.co.vida.DAO.ConfiguracaoFirebase;

public class Anuncio {
    private int quantSanguinea;
    private String dataDoacao;
    private String descricao;
    private String nome;
    private String provincia;
    private String id;
    private String estado;
    private String tipo_sangue;
    private String contacto;



    public Anuncio() {
    }

    public boolean gravar(){
        DatabaseReference dbRef = ConfiguracaoFirebase.getFirebase();
        return dbRef.child("anuncios").child(String.valueOf(getId())).setValue(toMap()).isSuccessful();
    }

    @Exclude
    private Map<String, Object> toMap(){
        HashMap<String, Object> hashMapUser = new HashMap<>();

        hashMapUser.put("nome", getNome());
        hashMapUser.put("data", getDataDoacao());
        hashMapUser.put("quantSanguinea", getQuantSanguinea());
        hashMapUser.put("descricao", getDescricao());
        hashMapUser.put("provincia", getProvincia());
        hashMapUser.put("estado", getEstado());
        hashMapUser.put("tipo_sangue", getTipo_sangue());
        hashMapUser.put("contacto", getContacto());

        return hashMapUser;
    }

    private String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public int getQuantSanguinea() {
        return quantSanguinea;
    }

    public void setQuantSanguinea(int quantSanguinea) {
        this.quantSanguinea = quantSanguinea;
    }

    public String getDataDoacao() {
        return dataDoacao;
    }

    public void setDataDoacao(String dataDoacao) {
        this.dataDoacao = dataDoacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    private String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    private String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    private String getTipo_sangue() {
        return tipo_sangue;
    }

    public void setTipo_sangue(String tipo_sangue) {
        this.tipo_sangue = tipo_sangue;
    }
}