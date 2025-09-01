package br.edu.iff.ccc.meupetideal.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import org.springframework.util.StringUtils;
import jakarta.validation.Valid;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.util.ArrayList;

import br.edu.iff.ccc.meupetideal.service.OngService;
import br.edu.iff.ccc.meupetideal.entities.Ong;

@Controller
@RequestMapping("MeuPetIdeal")
public class OngControllerView {

    @Autowired
    private OngService ongService;

    @GetMapping("/ong")
    public String getListaOng(
            Model model,
            @RequestParam(value = "pesquisa", required = false) String pesquisa,
            @RequestParam(value = "cidade", required = false) String cidade,
            @RequestParam(value = "atuacao", required = false) String atuacao
    ) {
        ArrayList<Ong> ongs = ongService.buscarOngsFiltrados(pesquisa, cidade, atuacao);
        model.addAttribute("ongs", ongs);
        model.addAttribute("pesquisa", pesquisa);
        model.addAttribute("cidade", cidade);
        model.addAttribute("atuacao", atuacao);
        return "listaOng";
    }

    @GetMapping("/informacoesOng/{id}")
    public String getInformacoesOng(@PathVariable Long id, Model model) {
        Ong found = ongService.buscarOngPorId(id);
        if (found == null) {
            found = new Ong();
            found.setImagem("ong-exemplo.jpg");
        }
        model.addAttribute("ong", found);
        return "informacoesOng";
    }

    @GetMapping("/cadastroOng")
    public String formCadastroOng(Model model) {
        if (!model.containsAttribute("ong")) model.addAttribute("ong", new Ong());
        return "cadastroOng";
    }

    @PostMapping("/cadastroOng")
    public String postCadastroOng(
            @Valid @ModelAttribute("ong") Ong ong,
            BindingResult binding,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            RedirectAttributes redirect,
            Model model) 
    {
        if (binding.hasErrors()) {
            return "cadastroOng";
        }

        if (foto != null && !foto.isEmpty()) {
            String uploadDir = "src/main/resources/static/imgs/";
            String fileName = StringUtils.cleanPath(foto.getOriginalFilename());
            Path filePath = Paths.get(uploadDir + fileName);

            try {
                // Salva a imagem no diretório especificado
                Files.createDirectories(Paths.get(uploadDir)); // Cria o diretório, se não existir
                Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Define o caminho relativo da imagem na entidade ONG
                ong.setImagem("/imgs/" + fileName);
            } catch (IOException e) {
                model.addAttribute("mensagemErro", "Erro ao salvar a foto: " + e.getMessage());
                return "cadastroOng";
            }
        }

        ongService.salvarOng(ong);
        redirect.addFlashAttribute("msg", "ONG cadastrada com sucesso.");
        return "redirect:/MeuPetIdeal/ong";
    }

    // formulário de edição (reusa cadastroOng)
    @GetMapping("/editarOng/{id}")
    public String editarOngForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Ong found = ongService.buscarOngPorId(id);
        if (found == null) {
            redirect.addFlashAttribute("msg", "ONG não encontrada.");
            return "redirect:/MeuPetIdeal/ong";
        }
        model.addAttribute("ong", found);
        return "editarOng";
    }

    @PostMapping("/editarOng/{id}")
    public String editarOng(
            @PathVariable Long id,
            @Valid @ModelAttribute("ong") Ong ong,
            BindingResult bindingResult,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            RedirectAttributes redirect
    ) {
        if (bindingResult.hasErrors()) {
            return "editarOng";
        }

        try {
            if (foto != null && !foto.isEmpty()) {
                String uploadDir = "src/main/resources/static/imgs/";
                String fileName = StringUtils.cleanPath(foto.getOriginalFilename());
                Path filePath = Paths.get(uploadDir + fileName);

                // Salva a nova imagem no diretório especificado
                Files.createDirectories(Paths.get(uploadDir)); // Cria o diretório, se não existir
                Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Define o caminho relativo da imagem na entidade ONG
                ong.setImagem("/imgs/" + fileName);
            }

            // Atualiza a ONG no serviço
            ongService.salvarOng(ong);
            redirect.addFlashAttribute("msg", "ONG atualizada com sucesso!");
        } catch (IOException e) {
            redirect.addFlashAttribute("msg", "Erro ao atualizar a ONG: " + e.getMessage());
        }

        return "redirect:/MeuPetIdeal/ong";
    }

    // exclusão via POST
    @PostMapping("/excluirOng/{id}")
    public String excluirOng(@PathVariable Long id, RedirectAttributes redirect) {
        boolean removed = ongService.excluirOng(id);
        if (removed) {
            redirect.addFlashAttribute("msg", "ONG excluída com sucesso.");
        } else {
            redirect.addFlashAttribute("msg", "Não foi possível excluir a ONG.");
        }
        return "redirect:/MeuPetIdeal/ong";

    }
    
}