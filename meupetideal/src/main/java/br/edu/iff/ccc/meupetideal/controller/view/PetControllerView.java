package br.edu.iff.ccc.meupetideal.controller.view;
//package br.edu.iff.ccc.meupetideal.controller.view;
//package br.edu.iff.ccc.meupetideal.controller.view;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
@Controller
public class PetControllerView
{
    @GetMapping("/listaPet")
    public String getListaPet(Model model)
    {
        return "listaDePet.html";

    }

    @GetMapping("/cadastroPet")
    public String getCadastro(Model model)
    {
        return "cadastroPets.html";

    }

    @GetMapping("/informacoesPet")
    public String getInfoPets(Model model)
    {
        return "informacoesPet.html";

    }
}
