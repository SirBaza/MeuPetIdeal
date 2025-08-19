package br.edu.iff.ccc.meupetideal.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class Ong implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @NotEmpty
    @Id
    private Long id;

    @NotNull
    @NotEmpty
    @Size(min = 2, max = 50)
    private String nome;

    @NotNull
    @NotEmpty
    @Size(min = 2, max = 50)
    private String endereco;

    private ArrayList<Pet> pets;

    public Ong() {
        // Default constructor
    }

    public Ong(String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ong)) return false;
        Ong ong = (Ong) o;
        return Objects.equals(id, ong.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}