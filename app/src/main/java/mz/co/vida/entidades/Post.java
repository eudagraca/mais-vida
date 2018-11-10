package mz.co.vida.entidades;

import java.util.HashMap;
import java.util.Map;

public class Post {

    public Post(){}

    public String disponibilidade;
    public String email;
    public String estado;
    public String foto;
    public String nome;
    public String provincia;
    public String senha;
    public String sexo;
    public String telefone;
    public String tipoSanguineo;
    public String uidUSer;
    public String unidadeProxima;

    public Post(String disponibilidade, String email, String estado, String foto, String nome, String provincia, String senha, String sexo, String telefone, String tipoSanguineo, String uidUSer, String unidadeProxima) {
        this.disponibilidade = disponibilidade;
        this.email = email;
        this.estado = estado;
        this.foto = foto;
        this.nome = nome;
        this.provincia = provincia;
        this.senha = senha;
        this.sexo = sexo;
        this.telefone = telefone;
        this.tipoSanguineo = tipoSanguineo;
        this.uidUSer = uidUSer;
        this.unidadeProxima = unidadeProxima;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setTipoSanguineo(String tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
    }

    public void setUidUSer(String uidUSer) {
        this.uidUSer = uidUSer;
    }

    public void setUnidadeProxima(String unidadeProxima) {
        this.unidadeProxima = unidadeProxima;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public String getEmail() {
        return email;
    }

    public String getEstado() {
        return estado;
    }

    public String getFoto() {
        return foto;
    }

    public String getNome() {
        return nome;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getSenha() {
        return senha;
    }

    public String getSexo() {
        return sexo;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getTipoSanguineo() {
        return tipoSanguineo;
    }

    public String getUidUSer() {
        return uidUSer;
    }

    public String getUnidadeProxima() {
        return unidadeProxima;
    }
}
