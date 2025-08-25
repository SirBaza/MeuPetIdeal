package br.edu.iff.ccc.meupetideal.controller.view;
//package br.edu.iff.ccc.meupetideal.controller.view;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.util.*;
//um exemplo da classe principal



@Controller
@RequestMapping(path = "MeuPetIdeal")
public class HomeViewController {

    @GetMapping("/home")
    public String getHome(Model model) //get por que estaremos mostrando esses animais na tela.
    {
        return "home.html"; // home.html
    }
}
