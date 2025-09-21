package br.edu.iff.ccc.meupetideal.exception;

public class OngNotFoundException extends RuntimeException {

    private final Long ongId; //Quando procuramos uma ONG pelo id dela, mas essa ONG não existe ou foi apagada.

    public OngNotFoundException(Long ongId) {
        super("ONG com ID " + ongId + " não foi encontrada no sistema.");
        this.ongId = ongId;
    }

    public OngNotFoundException(String message) {
        super(message);
        this.ongId = null;
    }

    public Long getOngId() {
        return ongId;
    }
}