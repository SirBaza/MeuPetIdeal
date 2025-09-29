package br.edu.iff.ccc.meupetideal.controller.view;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.iff.ccc.meupetideal.entities.Ong;
import br.edu.iff.ccc.meupetideal.exception.OngNotFoundException;
import br.edu.iff.ccc.meupetideal.exception.OngValidationException;
import br.edu.iff.ccc.meupetideal.service.OngService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("MeuPetIdeal")
public class OngControllerView {

    private final OngService ongService;

    public OngControllerView(OngService ongService) {
        this.ongService = ongService;
    }

    // MÉTODO PARA LISTAR/BUSCAR ONGs COM FILTROS OPCIONAIS - Exibe página de lista de ONGs
    @GetMapping("/ong")
    public String getListaOng(
            Model model,
            @RequestParam(value = "pesquisa", required = false) String pesquisa,
            @RequestParam(value = "cidade", required = false) String cidade,
            @RequestParam(value = "atuacao", required = false) String atuacao) {
        List<Ong> ongs = ongService.buscarOngsFiltrados(pesquisa, cidade, atuacao);
        model.addAttribute("ongs", ongs);
        return "listaOng";
    }

    // MÉTODO PARA EXIBIR DETALHES DE UMA ONG ESPECÍFICA - Exibe página de informações da ONG
    @GetMapping("/informacoesOng/{id}")
    public String getInformacoesOng(@PathVariable Long id, Model model) {
        Ong ong = ongService.buscarOngPorId(id); // Agora lança exception se não encontrar
        model.addAttribute("ong", ong);
        return "informacoesOng";
    }

    // MÉTODO PARA EXIBIR FORMULÁRIO DE CADASTRO DE ONG - Exibe página de cadastro
    @GetMapping("/cadastroOng")
    public String formCadastroOng(Model model) {
        model.addAttribute("ong", new Ong());
        return "cadastroOng";
    }

    // MÉTODO PARA PROCESSAR CADASTRO DE NOVA ONG - Processa formulário de cadastro com upload
    @PostMapping("/cadastroOng")
    public String postCadastroOng(
            @Valid @ModelAttribute("ong") Ong ong,
            BindingResult binding,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            RedirectAttributes redirect,
            Model model) {
        if (binding.hasErrors()) {
            return "cadastroOng";
        }

        try {
            // Usar o método do service que já gera URLs corretas
            ongService.salvarOngComUpload(ong, foto);
            redirect.addFlashAttribute("msg", "ONG cadastrada com sucesso.");
        } catch (OngValidationException e) {
            model.addAttribute("mensagemErro", "Erro de validação: " + e.getMessage());
            return "cadastroOng";
        } catch (IOException e) {
            model.addAttribute("mensagemErro", "Erro ao salvar a foto: " + e.getMessage());
            return "cadastroOng";
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao cadastrar ONG: " + e.getMessage());
            return "cadastroOng";
        }

        return "redirect:/MeuPetIdeal/ong";
    }

    // MÉTODO PARA EXIBIR FORMULÁRIO DE EDIÇÃO DE ONG - Exibe página de edição preenchida
    @GetMapping("/editarOng/{id}")
    public String editarOngForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Ong ong = ongService.buscarOngPorIdOpcional(id); // Usa o método que não lança exception
        if (ong == null) {
            redirect.addFlashAttribute("msg", "ONG não encontrada.");
            return "redirect:/MeuPetIdeal/ong";
        }
        model.addAttribute("ong", ong);
        return "editarOng";
    }

    // MÉTODO PARA PROCESSAR ATUALIZAÇÃO DE ONG - Processa formulário de edição com upload opcional
    @PostMapping("/editarOng/{id}")
    public String editarOng(
            @PathVariable Long id,
            @Valid @ModelAttribute("ong") Ong ong,
            BindingResult bindingResult,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            RedirectAttributes redirect) {
        if (bindingResult.hasErrors()) {
            return "editarOng";
        }
        ong.setId(id); // Garante que estamos atualizando a ONG correta
        try {
            // Se não enviou nova foto, manter a existente
            if (foto == null || foto.isEmpty()) {
                Ong ongExistente = ongService.buscarOngPorIdOpcional(id);
                if (ongExistente != null) {
                    ong.setImagem(ongExistente.getImagem());
                }
            }

            // Usar o método do service que já gera URLs corretas
            ongService.salvarOngComUpload(ong, foto);
            redirect.addFlashAttribute("msg", "ONG atualizada com sucesso!");
        } catch (OngValidationException e) {
            redirect.addFlashAttribute("msg", "Erro de validação: " + e.getMessage());
        } catch (IOException e) {
            redirect.addFlashAttribute("msg", "Erro ao atualizar a ONG: " + e.getMessage());
        } catch (Exception e) {
            redirect.addFlashAttribute("msg", "Erro ao atualizar ONG: " + e.getMessage());
        }

        return "redirect:/MeuPetIdeal/ong";
    }

    // MÉTODO PARA PROCESSAR EXCLUSÃO DE ONG - Exclui ONG e redireciona para lista
    @PostMapping("/excluirOng/{id}")
    public String excluirOng(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            System.out.println("🎯 Controller: Solicitação de exclusão da ONG ID: " + id);
            ongService.excluirOng(id);
            redirect.addFlashAttribute("msg", "ONG excluída com sucesso!");
            System.out.println("✅ Controller: ONG excluída com sucesso");
        } catch (OngNotFoundException e) {
            System.err.println("❌ Controller: ONG não encontrada - " + e.getMessage());
            redirect.addFlashAttribute("msg", "Erro: ONG não encontrada.");
        } catch (Exception e) {
            System.err.println("❌ Controller: Erro ao excluir ONG - " + e.getMessage());
            e.printStackTrace();
            redirect.addFlashAttribute("msg", 
                    "Erro interno ao excluir ONG. Verifique os logs para mais detalhes.");
        }
        return "redirect:/MeuPetIdeal/ong";
    }

}