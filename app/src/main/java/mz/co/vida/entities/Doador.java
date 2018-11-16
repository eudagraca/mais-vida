package mz.co.vida.entities;

public class Doador {

    public Doador(){}

    public String uidUser;
    public String nome;
    public String provincia;
    public String tipo_sangue;

    public Doador(String uidUser, String nome, String tipo_sangue, String provincia) {
        this.uidUser = uidUser;
        this.nome = nome;
        this.tipo_sangue = tipo_sangue;
        this.provincia = provincia;
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

    public String getTipo_sangue() {
        return tipo_sangue;
    }

    public void setTipo_sangue(String tipo_sangue) {
        this.tipo_sangue = tipo_sangue;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
}
