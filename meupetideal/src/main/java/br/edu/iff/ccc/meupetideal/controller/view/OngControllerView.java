package br.edu.iff.ccc.meupetideal.controller.view;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class OngControllerView
{
        @GetMapping("/ong")
        public String getListaOng(Model model)
        {
            return "listaDeOng.html";

        }

        @GetMapping("/informacoesOng")
        public String getInformacoesOng(Model model)
        {
            return "informacoesOng.html";

        }

}

