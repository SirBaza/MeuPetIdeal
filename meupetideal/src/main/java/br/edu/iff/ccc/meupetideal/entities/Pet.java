package br.edu.iff.ccc.meupetideal.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import br.edu.iff.ccc.meupetideal.entities.Ong;

@Entity
public class Pet implements Serializable {


    private static final long serialVersionUID = 1L;

    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    
    @NotEmpty(message = "O nome é obrigatório!")
    @Size(min = 2, max = 50)
    private String nome;

    
    @NotNull(message = "A raça é obrigatória!")
    @ManyToOne
    @JoinColumn(name = "raca_id")
    private Raca raca;

    @NotEmpty(message = "A descrição é obrigatória!")
    private String descricao;

    @Min(value = 0, message = "A idade não pode ser negativa!")
    private int idade; // Idade em anos
    
    @ManyToOne
    @NotNull(message = "A ONG é obrigatória!")
    @JoinColumn(name = "ong_id")
    private Ong ong;

    @NotNull(message = "O tipo é obrigatório!")
    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private Tipo tipo;

    @NotEmpty(message = "O sexo é obrigatório!")
    private String sexo;
    
    //Aqui vai ser armazenado o caminho de onde a foto será salva.
    private String foto;

    public Pet() {
        // Default constructor
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Raca getRaca() { return raca; }
    public void setRaca(Raca raca) { this.raca = raca; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    public Ong getOng() { return ong; }
    public void setOng(Ong ong) { this.ong = ong; }

    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pet)) return false;
        Pet pet = (Pet) o;
        return Objects.equals(id, pet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}