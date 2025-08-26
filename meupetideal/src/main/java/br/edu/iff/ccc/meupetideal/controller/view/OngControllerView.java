package br.edu.iff.ccc.meupetideal.controller.view;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping(path = "MeuPetIdeal")
public class OngControllerView
{
        @GetMapping("/ong")
        public String getListaOng(Model model)
        {
            return "listaDeOngBaza.html";

        }

        @GetMapping("/informacoesOng")
        public String getInformacoesOng(Model model)
        {
            return "informacoesOng.html";

        }

}

