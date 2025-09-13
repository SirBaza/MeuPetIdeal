package br.edu.iff.ccc.meupetideal.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import br.edu.iff.ccc.meupetideal.service.OngService;
import br.edu.iff.ccc.meupetideal.entities.Ong;

@Controller
@RequestMapping("MeuPetIdeal")
public class OngControllerView {

    private final OngService ongService;
    private static final String UPLOAD_DIR = "src/main/resources/static/imgs/";

    public OngControllerView(OngService ongService) {
        this.ongService = ongService;
    }

    @GetMapping("/ong")
    public String getListaOng(
            Model model,
            @RequestParam(value = "pesquisa", required = false) String pesquisa,
            @RequestParam(value = "cidade", required = false) String cidade,
            @RequestParam(value = "atuacao", required = false) String atuacao
    ) {
        List<Ong> ongs = ongService.buscarOngsFiltrados(pesquisa, cidade, atuacao);
        model.addAttribute("ongs", ongs);
        return "listaOng";
    }

    @GetMapping("/informacoesOng/{id}")
    public String getInformacoesOng(@PathVariable Long id, Model model) {
        Ong ong = ongService.buscarOngPorId(id);
        if (ong == null) {
            return "redirect:/MeuPetIdeal/ong";
        }
        model.addAttribute("ong", ong);
        return "informacoesOng";
    }

    @GetMapping("/cadastroOng")
    public String formCadastroOng(Model model) {
        model.addAttribute("ong", new Ong());
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

        try {
            if (foto != null && !foto.isEmpty()) {
                String caminhoFoto = salvarArquivoFoto(foto);
                ong.setImagem(caminhoFoto);
            }
            ongService.salvarOng(ong);
            redirect.addFlashAttribute("msg", "ONG cadastrada com sucesso.");
        } catch (IOException e) {
            model.addAttribute("mensagemErro", "Erro ao salvar a foto: " + e.getMessage());
            return "cadastroOng";
        }
        
        return "redirect:/MeuPetIdeal/ong";
    }

    @GetMapping("/editarOng/{id}")
    public String editarOngForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Ong ong = ongService.buscarOngPorId(id);
        if (ong == null) {
            redirect.addFlashAttribute("msg", "ONG não encontrada.");
            return "redirect:/MeuPetIdeal/ong";
        }
        model.addAttribute("ong", ong);
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
        ong.setId(id); // Garante que estamos atualizando a ONG correta
        try {
            if (foto != null && !foto.isEmpty()) {
                String caminhoFoto = salvarArquivoFoto(foto);
                ong.setImagem(caminhoFoto);
            } else {
                // Mantém a imagem antiga se nenhuma nova for enviada
                Ong ongExistente = ongService.buscarOngPorId(id);
                if (ongExistente != null) {
                    ong.setImagem(ongExistente.getImagem());
                }
            }
            ongService.salvarOng(ong);
            redirect.addFlashAttribute("msg", "ONG atualizada com sucesso!");
        } catch (IOException e) {
            redirect.addFlashAttribute("msg", "Erro ao atualizar a ONG: " + e.getMessage());
        }

        return "redirect:/MeuPetIdeal/ong";
    }

    @PostMapping("/excluirOng/{id}")
    public String excluirOng(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            ongService.excluirOng(id);
            redirect.addFlashAttribute("msg", "ONG excluída com sucesso.");
        } catch (Exception e) {
            redirect.addFlashAttribute("msg", "Não foi possível excluir a ONG. Verifique se ela não possui pets associados.");
        }
        return "redirect:/MeuPetIdeal/ong";
    }

    private String salvarArquivoFoto(MultipartFile foto) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = foto.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/imgs/" + fileName;
    }
}