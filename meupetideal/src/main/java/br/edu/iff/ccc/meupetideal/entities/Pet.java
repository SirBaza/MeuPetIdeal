package br.edu.iff.ccc.meupetideal.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Pet implements Serializable {


    private static final long serialVersionUID = 1L;

    
    @NotNull(message = "O id é obrigatório")
    @NotEmpty(message = "O id é obrigatório")
    @Id
    private Long id;


    @NotNull(message = "O nome é obrigatório")
    @NotEmpty(message = "O nome é obrigatório")
    @Size(min = 2, max = 50)
    private String nome;

    @NotNull
    @NotEmpty
    @Size(min = 2, max = 50)
    private String raca;

    private String descricao;
    

    public Pet() {
        // Default constructor
    }

    public Pet(String nome, String raca) {
        this.nome = nome;
        this.raca = raca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
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
        if (!(o instanceof Pet)) return false;
        Pet pet = (Pet) o;
        return Objects.equals(nome, pet.nome) && Objects.equals(raca, pet.raca);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, raca);
    }
}