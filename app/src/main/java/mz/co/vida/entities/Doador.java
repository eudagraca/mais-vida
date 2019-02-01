package mz.co.vida.entities;

public class Doador {

    public Doador(){}

    private String user_id;
    private String nome;
    private String provincia;
    private String tipo_sangue;
    private String disponibilidade;



    public String getUser_id() {
        return user_id;
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

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}
