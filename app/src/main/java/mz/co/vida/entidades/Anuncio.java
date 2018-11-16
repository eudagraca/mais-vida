package mz.co.vida.entidades;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;
import mz.co.vida.DAO.ConfiguracaoFirebase;

public class Anuncio {
    private int quantSanguinea;
    private String dataDoacao;
    private String comentario;
    private String nome;
    private String localizacao;
    private String id;
    private String estado;
    private String tipo_sangue;


    public Anuncio() {
    }

    public void gravar(){
        DatabaseReference dbRef = ConfiguracaoFirebase.getFirebase();
        dbRef.child("anuncios").child(String.valueOf(getId())).setValue(this);
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapUser = new HashMap<>();

        hashMapUser.put("nome", getNome());
        hashMapUser.put("data", getDataDoacao());
        hashMapUser.put("quantidade", getQuantSanguinea());
        hashMapUser.put("comentario", getComentario());
        hashMapUser.put("localizacao", getLocalizacao());
        hashMapUser.put("estado", getEstado());
        hashMapUser.put("tipo_sangue", getTipo_sangue());

        return hashMapUser;
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getTipo_sangue() {
        return tipo_sangue;
    }

    public void setTipo_sangue(String tipo_sangue) {
        this.tipo_sangue = tipo_sangue;
    }
}