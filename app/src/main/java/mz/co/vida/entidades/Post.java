package mz.co.vida.entidades;

public class Post {

    private String name;
    private String tipodesangue;
    private String provincia;
    private String data;
    private String disponibilidade;
    private String estado;
    private String quantidade;
    private String description;



    public Post(){

    }

    public Post(String name, String tipodesangue,
                String provincia, String data,
                String disponibilidade, String estado,
                String description) {
        this.name = name;
        this.tipodesangue = tipodesangue;
        this.provincia = provincia;
        this.data = data;
        this.disponibilidade = disponibilidade;
        this.estado = estado;
        this.description = description;

    }

    public String getComment() {
        return description;
    }

    public void setComment(String comment) {
        this.description = comment;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTipodesangue() {
        return tipodesangue;
    }

    public void setTipodesangue(String tipodesangue) {
        this.tipodesangue = tipodesangue;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    public void setQuantidade(String quantidade){this.quantidade=quantidade;}

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}
