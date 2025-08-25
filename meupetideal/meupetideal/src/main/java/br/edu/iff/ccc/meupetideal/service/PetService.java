package br.edu.iff.ccc.meupetideal.service;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import br.edu.iff.ccc.meupetideal.entities.Pet;

@Service
public class PetService {

    private final ArrayList<Pet> pets = new ArrayList<>(); 
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public void salvarPet(Pet pet) {
        // Implement the logic to save the Pet
        // This could involve saving to a database or any other storage
        System.out.println("ID: " + pet.getId() + ", Nome: " + pet.getNome() + ", Raça: " + pet.getRaca());
    }

    public Pet buscarPetPorId(Long id) {
        // Implement the logic to find a Pet by its ID
        // This could involve querying a database or any other storage
        //Esse objeto Pet vai guardar 
        Pet pet = new Pet();

        if(id == null) {
            return null; // or throw an exception
        }
        // Simulating a found Pet for demonstration purposes
        pet.setId(id);
        pet.setNome("Pet Exemplo");
        pet.setRaca("Raça Exemplo");
        return pet;
    }

    public ArrayList<Pet> listarPets() {
        return new ArrayList<>(pets); 
    }

    public void atualizarPet(Pet pet) {
        // Implement the logic to update an existing Pet
        // This could involve updating a database record or any other storage
        System.out.println("Atualizando Pet: ID: " + pet.getId() + ", Nome: " + pet.getNome() + ", Raça: " + pet.getRaca());
    }

    public Pet cadastrarPet(Pet pet) {
        pet.setId(idGenerator.getAndIncrement());
        pets.add(pet);
        return pet;
    }

    public void excluirPet(Long id) {
        // Implement the logic to delete a Pet by its ID
        // This could involve removing a record from a database or any other storage
        System.out.println("Excluindo Pet com ID: " + id);
    }

    public ArrayList<Pet> mostrarTodosPets() {
        // Implement the logic to show all Pets
        // This could involve retrieving all records from a database or any other storage
        ArrayList<Pet> pets = listarPets();
        for (Pet pet : pets) {
            System.out.println("ID: " + pet.getId() + ", Nome: " + pet.getNome() + ", Raça: " + pet.getRaca());
        }
        return pets;
    }
    
    public ArrayList<Pet> mostrarTodosPetsPorOng(Long ongId) {
        // Implement the logic to show all Pets for a specific ONG
        // This could involve filtering Pets by ONG ID
        ArrayList<Pet> pets = listarPets();
        for (Pet pet : pets) {
            if (pet.getId().equals(ongId)) {
                System.out.println("ID: " + pet.getId() + ", Nome: " + pet.getNome() + ", Raça: " + pet.getRaca());
            }
        }
        return pets;
    }

    public ArrayList<Pet> mostrarTodosPetsPorRaca(String raca) {
        // Implement the logic to show all Pets by breed
        // This could involve filtering Pets by breed
        ArrayList<Pet> pets = listarPets();
        for (Pet pet : pets) {
            if (pet.getRaca().equalsIgnoreCase(raca)) {
                System.out.println("ID: " + pet.getId() + ", Nome: " + pet.getNome() + ", Raça: " + pet.getRaca());
            }
        }
        return pets;
    } 

}