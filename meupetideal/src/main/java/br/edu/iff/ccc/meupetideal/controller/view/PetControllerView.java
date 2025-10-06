package br.edu.iff.ccc.meupetideal.controller.view;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.iff.ccc.meupetideal.entities.Pet;
import br.edu.iff.ccc.meupetideal.entities.Raca;
import br.edu.iff.ccc.meupetideal.exception.PetNotFoundException;
import br.edu.iff.ccc.meupetideal.exception.PetValidationException;
import br.edu.iff.ccc.meupetideal.service.OngService;
import br.edu.iff.ccc.meupetideal.service.PetService;
import br.edu.iff.ccc.meupetideal.service.RacaService;
import br.edu.iff.ccc.meupetideal.service.TipoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping(path = "MeuPetIdeal")
public class PetControllerView {

    private final PetService petService;
    private final OngService ongService;
    private final TipoService tipoService;
    private final RacaService racaService;

    public PetControllerView(PetService petService, OngService ongService, TipoService tipoService,
            RacaService racaService) {
        this.petService = petService;
        this.ongService = ongService;
        this.tipoService = tipoService;
        this.racaService = racaService;
    }

    @GetMapping("/listaPet")
    public String getListaPet(@RequestParam(required = false) String busca, Model model) {
        List<Pet> pets = petService.buscarPetsFiltrados(busca);
        model.addAttribute("pets", pets);
        if (pets.isEmpty()) {
            model.addAttribute("mensagemErro", "Nenhum pet encontrado.");
        }
        return "listaDePet";
    }

    @GetMapping("/listaPet/tipo/{tipo}")
    public String filtrarPorTipo(@PathVariable String tipo, Model model) {
        List<Pet> petsFiltrados = petService.filtrarPorTipo(tipo);
        model.addAttribute("pets", petsFiltrados);
        if (petsFiltrados.isEmpty()) {
            model.addAttribute("mensagemErro", "Nenhum pet do tipo " + tipo + " encontrado.");
        }
        return "listaDePet";
    }

    @GetMapping("/cadastroPet")
    public String getCadastro(Model model) {
        model.addAttribute("pet", new Pet());
        model.addAttribute("ongs", ongService.listarOngs());
        model.addAttribute("tipos", tipoService.listarTipos());
        model.addAttribute("racas", Collections.emptyList());
        return "cadastroPet";
    }

    @PostMapping("/cadastroPet")
    public String postCadastro(@Valid @ModelAttribute("pet") Pet pet,
            BindingResult bindingResult,
            @RequestParam("fotoArquivo") MultipartFile fotoArquivo,
            Model model,
            RedirectAttributes redirect) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("ongs", ongService.listarOngs());
            model.addAttribute("tipos", tipoService.listarTipos());
            model.addAttribute("racas", Collections.emptyList());
            return "cadastroPet";
        }

        try {
            petService.salvarPet(pet, fotoArquivo);
        } catch (PetValidationException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("ongs", ongService.listarOngs());
            model.addAttribute("tipos", tipoService.listarTipos());
            model.addAttribute("racas", Collections.emptyList());
            return "cadastroPet";
        } catch (IOException e) {
            model.addAttribute("mensagemErro", "Ocorreu um erro no servidor ao salvar a foto.");
            model.addAttribute("ongs", ongService.listarOngs());
            model.addAttribute("tipos", tipoService.listarTipos());
            model.addAttribute("racas", Collections.emptyList());
            return "cadastroPet";
        }

        return "redirect:/MeuPetIdeal/resultadoCadastro";
    }

    /**
     * MÉTODO ADICIONADO
     * Este é o endpoint que o JavaScript (fetch) da sua página de cadastro chama.
     * 
     * @param tipoId O ID do tipo de pet, capturado da URL.
     * @return Uma lista de Raças em formato JSON.
     */
    @GetMapping("/racasPorTipo/{tipoId}")
    @ResponseBody // Essencial: diz ao Spring para retornar os dados, não uma página.
    public List<Raca> getRacasPorTipo(@PathVariable Long tipoId) {
        return racaService.listarPorTipo(tipoId);
    }

    @PostMapping("/excluirPet/{id}")
    public String excluirPet(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            petService.excluirPet(id);
            redirect.addFlashAttribute("msg", "Pet excluído com sucesso.");
        } catch (Exception e) {
            redirect.addFlashAttribute("msg", "Não foi possível excluir o Pet: " + e.getMessage());
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
        model.addAttribute("tipos", tipoService.listarTipos());
        model.addAttribute("racas", Collections.emptyList());
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
            model.addAttribute("tipos", tipoService.listarTipos());
            model.addAttribute("racas", Collections.emptyList());
            return "editarPet";
        }

        try {
            petService.atualizarPet(id, pet, foto);
            redirect.addFlashAttribute("msg", "Pet atualizado com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("msg", "Erro ao atualizar o Pet: " + e.getMessage());
        }

        return "redirect:/MeuPetIdeal/listaPet";
    }

    // NOVO MÉTODO PARA A PÁGINA DE INFORMAÇÕES
    @GetMapping("/informacoesPet/{id}")
    public String getInfoPet(@PathVariable Long id, Model model) {
        Pet pet = petService.buscarPetPorId(id);
        if (pet == null) {
            throw new PetNotFoundException(id);
        }
        model.addAttribute("pet", pet);
        return "informacoesPet";
    }

    // ENDPOINT SIMPLIFICADO PARA DETALHES DO PET
    @GetMapping("/pet/{id}")
    public String getDetalhePet(@PathVariable Long id, Model model) {
        Pet pet = petService.buscarPetPorId(id);
        if (pet == null) {
            throw new PetNotFoundException(id);
        }
        model.addAttribute("pet", pet);
        return "informacoesPet"; // Reutiliza o mesmo template
    }

    @GetMapping("/resultadoCadastro")
    public String getResultadoCadastro() {
        return "resultadoCadastro";
    }
}