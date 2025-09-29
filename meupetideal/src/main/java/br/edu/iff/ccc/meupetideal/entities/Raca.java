package br.edu.iff.ccc.meupetideal.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Raca implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nome;

    //uma raça, ou conjunto de raças pertencem a 1 tipo.
    @JsonBackReference
    @ManyToOne
    @NotNull
    @JoinColumn(name = "tipo_Id", nullable = false)
    private Tipo tipo;

    public Raca() {}

    public Raca(String nome, Tipo tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Tipo getTipo(){return tipo;}
    public void setTipo(Tipo tipo){this.tipo = tipo;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Raca raca = (Raca) o;
        return Objects.equals(id, raca.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
