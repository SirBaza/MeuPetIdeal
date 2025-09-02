package br.edu.iff.ccc.meupetideal.controller.view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.iff.ccc.meupetideal.entities.Ong;
import br.edu.iff.ccc.meupetideal.entities.Pet;
import br.edu.iff.ccc.meupetideal.service.OngService;
import br.edu.iff.ccc.meupetideal.service.PetService;
import jakarta.validation.Valid;

@Controller
@RequestMapping(path = "MeuPetIdeal")
public class PetControllerView {
    //aqui estamos injetando o petService, para que o controller use seus metodos.
    private final PetService petService;
    private final OngService ongService;

    public PetControllerView(PetService petService, OngService ongService) {
        this.petService = petService;
        this.ongService = ongService;
    }

    //obtém a página Lista de pets
    @GetMapping("/listaPet")
    public String getListaPet(Model model) {
        model.addAttribute("pets", petService.listarPets());
        return "listaDePet"; // listaDePet.html
    }

    //busca filtrada
    @PostMapping("/listaPet")
    public String postBusca(@RequestParam(required = false) String busca, Model model) {
    ArrayList<Pet> petsFiltrados = petService.buscarPetsFiltrados(busca, busca, null);

    if (petsFiltrados.isEmpty()) {
        model.addAttribute("mensagemErro", "Nenhum pet encontrado para o filtro aplicado.");
    }

    model.addAttribute("pets", petsFiltrados);
    return "listaDePet";
    }

    //botoes que filtram o tipo
    @GetMapping("/listaPet/tipo/{tipo}")
    public String filtrarPorTipo(@PathVariable String tipo, Model model) {
    ArrayList<Pet> petsFiltrados = petService.filtrarPorTipo(tipo);

    if (petsFiltrados.isEmpty()) {
        model.addAttribute("mensagemErro", "Nenhum pet do tipo " + tipo + " encontrado.");
    }

    model.addAttribute("pets", petsFiltrados);
    return "listaDePet";
    }

    // Formulário de cadastro (GET)
    //get receber uma requisição, buscar alguma coisa.
    @GetMapping("/cadastroPet")
    public String getCadastro(Model model) {
        model.addAttribute("pet", new Pet());
        model.addAttribute("ongs", ongService.listarOngs());
        return "cadastroPet"; // cadastroPet.html
    }

    //Formulário de cadastro 
    @PostMapping("/cadastroPet")
    public String postCadastro(@Valid @ModelAttribute("pet") Pet pet,
                           BindingResult bindingResult,
                           @RequestParam("foto") MultipartFile foto,
                           Model model) {
    if (bindingResult.hasErrors()) {
        model.addAttribute("ongs", ongService.listarOngs());
        return "cadastroPet";
    }

    if (pet.getOng() != null && pet.getOng().getId() != null) {
        Ong ong = ongService.buscarOngPorId(pet.getOng().getId());
        pet.setOng(ong);
    }

    try {
        petService.cadastrarPetComFoto(pet, foto);
    } catch (IOException e) {
        model.addAttribute("ongs", ongService.listarOngs());
        model.addAttribute("mensagemErro", "Erro ao salvar a foto: " + e.getMessage());
        return "cadastroPet";
    }

    return "redirect:/MeuPetIdeal/resultadoCadastro";
}


    //Exclusão de pet
    // exclusão via POST
    @PostMapping("excluirPet/{id}")
    public String excluirPet(@PathVariable Long id, RedirectAttributes redirect) {
        boolean removed = petService.excluirPet(id);
        if (removed) {
            redirect.addFlashAttribute("msg", "Pet excluído com sucesso.");
            return "redirect:/MeuPetIdeal/listaPet";
        } else {
            redirect.addFlashAttribute("msg", "Não foi possível excluir o Pet.");
        }
        return "redirect:/MeuPetIdeal/listaPet";
    }
    
     @GetMapping("/editarPet/{id}")
    public String editarPetForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Pet pet = petService.buscarPetPorId(id);
        if (pet == null) {
            redirect.addFlashAttribute("msg", "Pet não encontrado.");
            return "redirect:/MeuPetIdeal/listaPet";
        }
        model.addAttribute("pet", pet);
        model.addAttribute("ongs", ongService.listarOngs());
        return "editarPet";
    }

    @PostMapping("/editarPet/{id}")
    public String editarPet(@PathVariable Long id,
                            @Valid @ModelAttribute("pet") Pet pet,
                            BindingResult bindingResult,
                            @RequestParam(value = "foto", required = false) MultipartFile foto,
                            Model model,
                            RedirectAttributes redirect) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("ongs", ongService.listarOngs());
            return "editarPet";
        }

        if (pet.getOng() != null && pet.getOng().getId() != null) {
            Ong ong = ongService.buscarOngPorId(pet.getOng().getId());
            pet.setOng(ong);
        }

        try {
            petService.atualizarPet(id, pet, foto);
            redirect.addFlashAttribute("msg", "Pet atualizado com sucesso!");
        } catch (IOException e) {
            redirect.addFlashAttribute("msg", "Erro ao atualizar o Pet!: " + e.getMessage());
        }

        return "redirect:/MeuPetIdeal/listaPet";
    }

    
    // Informações de um pet
    @GetMapping("/informacoesPet")
    public String getInfoPets(Model model) {
        return "informacoesPet"; // informacoesPet.html
    }

    // Página de resultado do cadastro
    @GetMapping("/resultadoCadastro")
    public String getResultadoCadastro() {
    return "resultadoCadastro"; // resultadoCadastro.html
    }
}