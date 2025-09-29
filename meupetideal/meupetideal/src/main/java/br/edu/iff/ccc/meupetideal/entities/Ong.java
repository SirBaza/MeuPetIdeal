package br.edu.iff.ccc.meupetideal.entities;

import java.io.Serializable;
import java.util.List;

import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;




@Entity
public class Ong implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotEmpty (message = "O nome é obrigatório!")
    @Size(min = 2, max = 50)
    private String nome;

    @NotNull @NotEmpty @Size(min = 2, max = 100)
    private String endereco;
    
    @JsonIgnore
    @OneToMany(mappedBy = "ong", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets;

    private String imagem;

    // campos adicionais do formulário
    @NotNull @NumberFormat
    private String fundacao;

    @NotNull
    private String atuacao;

    @NotNull @NumberFormat
    private String telefone;

    @NotNull @Email
    private String email;

    //Campos não obrigatórios!
    private String site;
    private String instagram;
    private String descricao;

    public Ong() {}


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }

    public String getFundacao() { return fundacao; }
    public void setFundacao(String fundacao) { this.fundacao = fundacao; }

    public String getAtuacao() { return atuacao; }
    public void setAtuacao(String atuacao) { this.atuacao = atuacao; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }

    public String getInstagram() { return instagram; }
    public void setInstagram(String instagram) { this.instagram = instagram; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

   public List<Pet> getPets()
   {
    return pets;
   }

   public void setPets(List<Pet> pets)
   {
    this.pets = pets;
   }

    // @Override
    // public boolean equals(Object o) {
    //     if (this == o) return true;
    //     if (!(o instanceof Ong)) return false;
    //     Ong ong = (Ong) o;
    //     return Objects.equals(id, ong.id);
    // }

    // @Override
    // public int hashCode() {
    //     return Objects.hash(id);
    // }
}