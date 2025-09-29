package br.edu.iff.ccc.meupetideal.exception;

public class PetNotFoundException extends RuntimeException {

    private final Long petId; //Quando procuramos um pet pelo id dele, mas esse pet não existe ou foi apagado.

    public PetNotFoundException(Long petId) {
        super("Pet com ID " + petId + " não foi encontrado no sistema.");
        this.petId = petId;
    }

    public PetNotFoundException(String message) {
        super(message);
        this.petId = null;
    }

    public Long getPetId() {
        return petId;
    }
}