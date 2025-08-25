package br.edu.iff.ccc.meupetideal.controller.view;

import br.edu.iff.ccc.meupetideal.entities.Pet;
import br.edu.iff.ccc.meupetideal.service.PetService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "MeuPetIdeal")
public class PetControllerView {

    private final PetService petService;

    public PetControllerView(PetService petService) {
        this.petService = petService;
    }

    // Lista de pets
    @GetMapping("/listaPet")
    public String getListaPet(Model model) {
        model.addAttribute("pets", petService.listarPets());
        return "listaDePet"; // listaDePet.html
    }

    // Formulário de cadastro (GET)
    @GetMapping("/cadastroPet")
    public String getCadastro(Model model) {
        model.addAttribute("pet", new Pet());
        return "cadastroPet"; // cadastroPet.html
    }

    // Processar cadastro (POST)
    @PostMapping("/cadastroPet")
    public String postCadastro(@Valid @ModelAttribute("pet") Pet pet,BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()) {
            return "cadastroPet"; // volta para o formulário se houver erro
        }
        petService.cadastrarPet(pet);
        return "redirect:/MeuPetIdeal/listaPet";
    }

    // Informações de um pet
    @GetMapping("/informacoesPet")
    public String getInfoPets(Model model) {
        return "informacoesPet"; // informacoesPet.html
    }
}