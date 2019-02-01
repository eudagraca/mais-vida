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


    public Requisitante() {

    }

    public Requisitante(String estado, String nome, String telefone, String unidadeProxima, int quantSanguinea, String data, String comentario, String localizacao, String user_id, String tipo_sangue) {
        this.estado = estado;
        this.nome = nome;
        this.contacto = telefone;
        this.unidadeProxima = unidadeProxima;
        this.quantSanguinea = quantSanguinea;
        this.data = data;
        this.descricao = comentario;
        this.provincia = localizacao;
        this.user_id = user_id;
        this.tipo_sangue = tipo_sangue;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public String getUnidadeProxima() {
        return unidadeProxima;
    }

    public void setUnidadeProxima(String unidadeProxima) {
        this.unidadeProxima = unidadeProxima;
    }

    public int getQuantSanguinea() {
        return quantSanguinea;
    }

    public void setQuantSanguinea(int quantSanguinea) {
        this.quantSanguinea = quantSanguinea;
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

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public void setTipo_sangue(String tipo_sangue) {
        this.tipo_sangue = tipo_sangue;
    }
}
