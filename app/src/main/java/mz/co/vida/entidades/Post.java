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
    public String uidUser;
    public String unidadeProxima;

    public Post(String disponibilidade, String email, String estado, String foto, String nome, String provincia, String senha, String sexo, String telefone, String tipoSanguineo, String uidUser, String unidadeProxima) {
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
        this.uidUser = uidUser;
        this.unidadeProxima = unidadeProxima;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipoSanguineo() {
        return tipoSanguineo;
    }

    public void setTipoSanguineo(String tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
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
}
