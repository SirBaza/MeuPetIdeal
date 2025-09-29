package br.edu.iff.ccc.meupetideal.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação/atualização de ONG")
public class OngRequestDTO {

    @Schema(description = "Nome da ONG", example = "Abrigo dos Animais", required = true)
    @NotEmpty(message = "O nome é obrigatório!")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Schema(description = "Endereço da ONG", example = "Rua das Flores, 123 - Centro")
    @NotEmpty(message = "O endereço é obrigatório!")
    private String endereco;

    @Schema(description = "Ano de fundação", example = "2010")
    private String fundacao;

    @Schema(description = "Área de atuação", example = "Resgate e adoção de animais abandonados")
    private String atuacao;

    @Schema(description = "Telefone para contato", example = "(11) 99999-9999")
    private String telefone;

    @Schema(description = "Email da ONG", example = "contato@abrigo.org.br")
    @Email(message = "Email deve ter um formato válido")
    private String email;

    @Schema(description = "Site da ONG", example = "https://www.abrigo.org.br")
    private String site;

    @Schema(description = "Instagram da ONG", example = "@abrigodosanimais")
    private String instagram;

    @Schema(description = "Descrição da ONG", example = "ONG dedicada ao resgate e cuidado de animais abandonados")
    @NotEmpty(message = "A descrição é obrigatória!")
    private String descricao;

    // Construtores
    public OngRequestDTO() {
    }

    public OngRequestDTO(String nome, String endereco, String fundacao, String atuacao,
            String telefone, String email, String site, String instagram, String descricao) {
        this.nome = nome;
        this.endereco = endereco;
        this.fundacao = fundacao;
        this.atuacao = atuacao;
        this.telefone = telefone;
        this.email = email;
        this.site = site;
        this.instagram = instagram;
        this.descricao = descricao;
    }

    // Getters e Setters
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

    public String getFundacao() {
        return fundacao;
    }

    public void setFundacao(String fundacao) {
        this.fundacao = fundacao;
    }

    public String getAtuacao() {
        return atuacao;
    }

    public void setAtuacao(String atuacao) {
        this.atuacao = atuacao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "OngRequestDTO{" +
                "nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", fundacao='" + fundacao + '\'' +
                ", atuacao='" + atuacao + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", site='" + site + '\'' +
                ", instagram='" + instagram + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}