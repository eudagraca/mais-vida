package mz.co.vida.entities;

public class Requisitante {

    public String id;
    public String nome;
    public String provincia;
    public String tipo_sangue;
    public String estado;
    public String contacto;
    public String unidadeProxima;
    private int quantSanguinea;
    private String dataDoacao;
    private String descricao;


    public Requisitante() {

    }

    public Requisitante(String estado, String nome, String telefone, String unidadeProxima, int quantSanguinea, String dataDoacao, String comentario, String localizacao, String id, String tipo_sangue) {
        this.estado = estado;
        this.nome = nome;
        this.contacto = telefone;
        this.unidadeProxima = unidadeProxima;
        this.quantSanguinea = quantSanguinea;
        this.dataDoacao = dataDoacao;
        this.descricao = comentario;
        this.provincia = localizacao;
        this.id = id;
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

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo_sangue() {
        return tipo_sangue;
    }

    public void setTipo_sangue(String tipo_sangue) {
        this.tipo_sangue = tipo_sangue;
    }
}
