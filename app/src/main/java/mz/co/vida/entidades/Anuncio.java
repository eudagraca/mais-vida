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
    private Boolean feedback;
    private String id;


    public Anuncio() {
    }

    public void gravar(){
        DatabaseReference dbRef = ConfiguracaoFirebase.getFirebase();
        dbRef.child("anuncios").child(String.valueOf(getId())).setValue(this);
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapUser = new HashMap<>();

        hashMapUser.put("data", getDataDoacao());
        hashMapUser.put("quantidade", getQuantSanguinea());
        hashMapUser.put("comentario", getComentario());

        return hashMapUser;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getFeedback() {
        return feedback;
    }

    public void setFeedback(Boolean feedback) {
        this.feedback = feedback;
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

    public void setQuantSanguinea(String quantSanguinea) {
    }
}