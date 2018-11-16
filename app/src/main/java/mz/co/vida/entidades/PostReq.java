package mz.co.vida.entidades;

public class PostReq {

    public String estado;
    public String nome;
    public String provincia;
    public String telefone;
    public String uidUser;
    public String unidadeProxima;
    private int quantSanguinea;
    private String dataDoacao;
    private String comentario;
    private String localizacao;
    private String id;
    private String tipo_sangue;


    public PostReq() {

    }

    public PostReq(String estado, String nome,  String telefone, String uidUser, String unidadeProxima, int quantSanguinea, String dataDoacao, String comentario, String localizacao, String id, String tipo_sangue) {
        this.estado = estado;
        this.nome = nome;
        this.telefone = telefone;
        this.uidUser = uidUser;
        this.unidadeProxima = unidadeProxima;
        this.quantSanguinea = quantSanguinea;
        this.dataDoacao = dataDoacao;
        this.comentario = comentario;
        this.localizacao = localizacao;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
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
