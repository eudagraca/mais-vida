package mz.co.vida.entities;

public class Requisitante {

    private String user_id;
    private String nome;
    private String provincia;
    private String tipo_sangue;
    private String estado;
    private String contacto;
    private String unidadeProxima;
    private int quantSanguinea;
    private String data;
    private String descricao;
    private String unidade;



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

    public String getUnidadeProxima() {
        return unidadeProxima;
    }

    public int getQuantSanguinea() {
        return quantSanguinea;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getId() {
        return user_id;
    }

    public void setId(String id) {
        this.user_id = id;
    }

    public String getTipo_sangue() {
        return tipo_sangue;
    }

}
