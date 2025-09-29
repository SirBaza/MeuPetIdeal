package br.edu.iff.ccc.meupetideal.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação/atualização de Pet")
public class PetRequestDTO {

    @Schema(description = "Nome do pet", example = "Chico", required = true)
    @NotEmpty(message = "O nome é obrigatório!")
    @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres")
    private String nome;

    @Schema(description = "ID da raça", example = "1", required = true)
    @JsonProperty("raca_id")
    @NotNull(message = "A raça é obrigatória!")
    private Long racaId;

    @Schema(description = "ID do tipo", example = "1", required = true)
    @JsonProperty("tipo_id")
    @NotNull(message = "O tipo é obrigatório!")
    private Long tipoId;

    @Schema(description = "ID da ONG", example = "1", required = true)
    @JsonProperty("ong_id")
    @NotNull(message = "A ONG é obrigatória!")
    private Long ongId;

    @Schema(description = "Descrição do pet", example = "Pet carinhoso e brincalhão")
    @NotEmpty(message = "A descrição é obrigatória!")
    private String descricao;

    @Schema(description = "Idade do pet", example = "3")
    @Min(value = 0, message = "A idade não pode ser negativa!")
    private Integer idade;

    @Schema(description = "Sexo do pet", example = "Macho", allowableValues = { "Macho", "Fêmea" })
    @NotEmpty(message = "O sexo é obrigatório!")
    private String sexo;

    // Construtores
    public PetRequestDTO() {
    }

    public PetRequestDTO(String nome, Long racaId, Long tipoId, Long ongId, String descricao, Integer idade,
            String sexo) {
        this.nome = nome;
        this.racaId = racaId;
        this.tipoId = tipoId;
        this.ongId = ongId;
        this.descricao = descricao;
        this.idade = idade;
        this.sexo = sexo;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getRacaId() {
        return racaId;
    }

    public void setRacaId(Long racaId) {
        this.racaId = racaId;
    }

    public Long getTipoId() {
        return tipoId;
    }

    public void setTipoId(Long tipoId) {
        this.tipoId = tipoId;
    }

    public Long getOngId() {
        return ongId;
    }

    public void setOngId(Long ongId) {
        this.ongId = ongId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @Override
    public String toString() {
        return "PetRequestDTO{" +
                "nome='" + nome + '\'' +
                ", racaId=" + racaId +
                ", tipoId=" + tipoId +
                ", ongId=" + ongId +
                ", descricao='" + descricao + '\'' +
                ", idade=" + idade +
                ", sexo='" + sexo + '\'' +
                '}';
    }
}