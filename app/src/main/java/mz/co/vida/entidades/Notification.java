package mz.co.vida.entidades;

import java.util.Date;

public class Notification {

    private String uid;
    private String nome;
    private String estadoDescrito;
    private Date dataNotif;
    private String tipoSangue;

    public Notification() {

    }

    public Notification(String nome, String estadoDescrito, Date dataNotif, String tipoSangue, String uid) {
        this.nome = nome;
        this.estadoDescrito = estadoDescrito;
        this.dataNotif = dataNotif;
        this.tipoSangue = tipoSangue;
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEstadoDescrito() {
        return estadoDescrito;
    }

    public void setEstadoDescrito(String estadoDescrito) {
        this.estadoDescrito = estadoDescrito;
    }

    public Date getDataNotif() {
        return dataNotif;
    }

    public void setDataNotif(Date dataNotif) {
        this.dataNotif = dataNotif;
    }

    public String getTipoSangue() {
        return tipoSangue;
    }

    public void setTipoSangue(String tipoSangue) {
        this.tipoSangue = tipoSangue;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
