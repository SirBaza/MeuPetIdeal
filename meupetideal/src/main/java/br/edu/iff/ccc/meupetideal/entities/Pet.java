package br.edu.iff.ccc.meupetideal.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Pet implements Serializable {


    private static final long serialVersionUID = 1L;

    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    
    @NotEmpty(message = "O nome é obrigatório")
    @Size(min = 2, max = 50)
    private String nome;

    
    @NotEmpty(message = "A raça é obrigatória!")
    @Size(min = 2, max = 50)
    private String raca;

    @NotEmpty(message = "A descrição é obrigatória!")
    private String descricao;

    @Min(value = 0, message = "A idade não pode ser negativa")
    private int idade; // Idade em anos
    
    @NotEmpty(message = "A descrição é obrigatória!")
    private String ong;
    

    public Pet() {
        // Default constructor
    }

    public Pet(String nome, String raca, String descricao, int idade, String ong) {
        this.nome = nome;
        this.raca = raca;
        this.descricao = descricao;
        this.idade = idade;
        this.ong = ong;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }



    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getOng() {
        return ong;
    }

    public void setOng(String ong) {
        this.ong = ong;
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