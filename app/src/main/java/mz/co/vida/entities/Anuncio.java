package mz.co.vida.entities;

public class Anuncio extends  Usuario{
    private int quantSanguinea;
    private String dataDoacao;
    private String descricao;
    private String id;


    public Anuncio() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}