package br.edu.iff.ccc.meupetideal.service;

import br.edu.iff.ccc.meupetideal.entities.Ong;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class OngService {

    private final List<Ong> storage = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public OngService() {
        Ong o1 = new Ong();
        o1.setNome("ONG Exemplo");
        o1.setEndereco("Rua Exemplo, 123");
        o1.setId(idGenerator.getAndIncrement());
        o1.setImagem("/imgs/ongExemplo.png");
        o1.setFundacao("2010");
        o1.setAtuacao("Resgate e Adoção");
        o1.setTelefone("(22) 99999-0001");
        o1.setEmail("contato@exemplo.org");
        o1.setDescricao("ONG dedicada ao resgate e adoção.");
        storage.add(o1);

        Ong o2 = new Ong();
        o2.setNome("Abrigo Amigo");
        o2.setEndereco("Rua Exemplo, 123");
        o2.setId(idGenerator.getAndIncrement());
        o2.setImagem("/imgs/abrigoamigo.png"); // coloque esse arquivo em static/imgs
        o2.setFundacao("2010");
        o2.setAtuacao("Resgate e Adoção");
        o2.setTelefone("(22) 99999-9999");
        o2.setEmail("contato@abrigoamigo.org");
        o2.setSite("https://abrigoamigo.org");
        o2.setInstagram("https://instagram.com/abrigoamigo");
        o2.setDescricao("Abrigo Amigo cuida de animais e promove adoções.");
        storage.add(o2);
    }

    // cria ou atualiza (se id presente)
    public void salvarOng(Ong ong) {
        if (ong == null) return;
        if (ong.getId() == null) {
            ong.setId(idGenerator.getAndIncrement());
            storage.add(ong);
            System.out.println("Criou ONG: " + ong.getNome() + " (id=" + ong.getId() + ")");
            return;
        }
        for (int i = 0; i < storage.size(); i++) {
            Ong s = storage.get(i);
            if (ong.getId().equals(s.getId())) {
                storage.set(i, ong);
                System.out.println("Atualizou ONG: " + ong.getNome() + " (id=" + ong.getId() + ")");
                return;
            }
        }
        storage.add(ong);
        System.out.println("Salvou ONG (fallback): " + ong.getNome() + " (id=" + ong.getId() + ")");
    }

    // remove ONG por id
    public boolean excluirOng(Long id) {
        if (id == null) return false;
        return storage.removeIf(o -> id.equals(o.getId()));
    }

    public Ong buscarOngPorId(Long id) {
        if (id == null) return null;
        return storage.stream().filter(o -> id.equals(o.getId())).findFirst().orElse(null);
    }

    public ArrayList<Ong> listarOngs() {
        return new ArrayList<>(storage);
    }

    public ArrayList<Ong> buscarOngsFiltrados(String termo, String cidade, String atuacao) {
        ArrayList<Ong> resultado = new ArrayList<>();
        String buscaLower = (termo != null) ? termo.toLowerCase() : null;

        for (Ong ong : storage) {
            boolean match = false;

            // busca por termo (nome, endereco, atuacao)
            if (buscaLower != null && !buscaLower.isEmpty()) {
                if ((ong.getNome() != null && ong.getNome().toLowerCase().contains(buscaLower))
                        || (ong.getEndereco() != null && ong.getEndereco().toLowerCase().contains(buscaLower))
                        || (ong.getAtuacao() != null && ong.getAtuacao().toLowerCase().contains(buscaLower))) {
                    match = true;
                }
            } else {
                match = true; // sem termo => todos passam esta etapa
            }

            // filtro por cidade (se informado)
            if (cidade != null && !cidade.isEmpty()) {
                if (ong.getEndereco() == null || !ong.getEndereco().toLowerCase().contains(cidade.toLowerCase())) {
                    match = false;
                }
            }

            // filtro por atuação (se informado)
            if (atuacao != null && !atuacao.isEmpty()) {
                if (ong.getAtuacao() == null || !ong.getAtuacao().toLowerCase().contains(atuacao.toLowerCase())) {
                    match = false;
                }
            }

            if (match) resultado.add(ong);
        }
        return resultado;
    }

    public ArrayList<Ong> filtrarPorCidade(String cidade) {
        ArrayList<Ong> filtrados = new ArrayList<>();
        if (cidade == null || cidade.isEmpty()) return filtrados;
        for (Ong ong : storage) {
            if (ong.getEndereco() != null && ong.getEndereco().toLowerCase().contains(cidade.toLowerCase())) {
                filtrados.add(ong);
            }
        }
        return filtrados;
    }
}