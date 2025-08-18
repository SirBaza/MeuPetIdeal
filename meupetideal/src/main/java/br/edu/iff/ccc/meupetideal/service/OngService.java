package br.edu.iff.ccc.meupetideal.service;

import br.edu.iff.ccc.meupetideal.entities.Ong;

import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class OngService {
    
    public void salvarOng(Ong ong) {
        // Implement the logic to save the ONG
        // This could involve saving to a database or any other storage
        System.out.println("ID:" + ong.getId() + "Nome: " + ong.getNome() + ", Endereço: " + ong.getEndereco());
    }
    //mostrar as ongs cadastradas
    public Ong buscarOngPorId(Long id) {
        // Implement the logic to find an ONG by its ID
        // This could involve querying a database or any other storage
        Ong ong = new Ong();

        if(id == null) {
            return null; // or throw an exception
        }
        // Simulating a found ONG for demonstration purposes
        ong.setId(id);
        ong.setNome("ONG Exemplo");
        ong.setEndereco("Rua Exemplo, 123");
        return ong;

    }

    public ArrayList<Ong> listarOngs() {
        // Implement the logic to list all ONGs
        // This could involve querying a database or any other storage
        ArrayList<Ong> ongs = new ArrayList<>();
        
        // Simulating some ONGs for demonstration purposes
        Ong ong1 = new Ong("ONG A", "Endereço A");
        ong1.setId(1L);
        ongs.add(ong1);
        
        Ong ong2 = new Ong("ONG B", "Endereço B");
        ong2.setId(2L);
        ongs.add(ong2);
        
        return ongs;
    }
}