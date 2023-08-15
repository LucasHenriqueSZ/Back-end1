package application;

import infrastructure.CategoriaEspacoDao;
import infrastructure.EspacoClubDao;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import infrastructure.entities.CategoriaEspaco;
import infrastructure.entities.EspacoClub;

import java.util.*;

public class MenuEspaco {

    private static MenuEspaco instance;

    private MenuEspaco() {
    }

    public static MenuEspaco getInstance() {
        if (instance == null) {
            instance = new MenuEspaco();
        }
        return instance;
    }

    public void menuEspaco(Scanner scanner) {
        System.out.println("Escolha uma opção:");
        System.out.println("1 - Cadastrar Espaço");
        System.out.println("2 - Editar Espaço");
        System.out.println("3 - Remover Espaço");
        System.out.println("4 - Listar Espaços");
        System.out.println("5 - Buscar Espaço");
        System.out.println("6 - Gerenciar Categorias");
        System.out.println("7 - Voltar ao menu principal");
        System.out.println("Digite o número da opção desejada:");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
               // cadastrarEspaco(scanner);
                break;
            case "2":
               // editarEspaco(scanner);
                break;
            case "3":
                //removerEspaco(scanner);
                break;
            case "4":
                //listarEspacos(scanner);
                break;
            case "5":
                //buscarEspaco(scanner);
                break;
            case "6":
                MenuCategoriaEspaco.getInstance().menuCategoria(scanner);
                break;
            case "7":
                MenuPrincipal.getInstance().menuPricipal(scanner);
                break;
            default:
                System.out.println("Opção inválida");
                menuEspaco(scanner);
        }
    }
//
//    private void cadastrarEspaco(Scanner scanner) {
//        try {
//            System.out.println("Digite o nome do espaço:");
//            String nome = scanner.nextLine();
//            System.out.println("Digite a capacidade do espaço:");
//            int capacidade = Integer.parseInt(scanner.nextLine());
//
//            System.out.println("Digite a descrição do espaço:");
//            String descricao = scanner.nextLine();
//            Set<String> categorias = new HashSet<>();
//            while (true) {
//                System.out.println("Digite o nome da categoria que deseja adicionar ao espaço:");
//                String nomeCategoria = scanner.nextLine();
//                Optional<CategoriaEspaco> categoria = CategoriaEspacoDao.getInstance().buscarCategoria(nomeCategoria);
//                if (categoria.isPresent()) {
//                    categorias.add(categoria.get().getCodigo());
//                } else {
//                    System.out.println("Categoria não encontrada!");
//                }
//                System.out.println("Deseja adicionar outra categoria? (S/N)");
//                String opcao = scanner.nextLine();
//                if (opcao.equalsIgnoreCase("N")) {
//                    break;
//                }
//            }
//
//            EspacoClub espacoClub = new EspacoClub(nome, descricao, capacidade, categorias);
//
//            EspacoClubDao.getInstance().salvar(espacoClub);
//            System.out.println("Espaço cadastrado com sucesso!");
//            menuEspaco(scanner);
//        } catch (Exception e) {
//            System.out.println("Falha ao cadastrar espaço: " + e.getMessage());
//            menuEspaco(scanner);
//        }
//    }
//
//    private void editarEspaco(Scanner scanner) {
//        try {
//            System.out.println("Digite o nome do espaço que deseja editar:");
//            String nome = scanner.nextLine();
//            Optional<EspacoClub> espaco = EspacoClubDao.getInstance().buscarEspaco(nome);
//            if (espaco.isPresent()) {
//                System.out.println("Digite o novo nome do espaço:");
//                String novoNome = scanner.nextLine();
//                System.out.println("Digite a nova descrição do espaço:");
//                String novaDescricao = scanner.nextLine();
//                System.out.println("Digite a nova capacidade do espaço:");
//                int novaCapacidade = Integer.parseInt(scanner.nextLine());
//                while (novaCapacidade < 0) {
//                    System.out.println("Capacidade inválida! Digite novamente:");
//                    novaCapacidade = Integer.parseInt(scanner.nextLine());
//                }
//
//                Set<String> categorias = new HashSet<>();
//                while (true) {
//                    System.out.println("Digite o nome da categoria que deseja adicionar ao espaço:");
//                    String nomeCategoria = scanner.nextLine();
//                    Optional<CategoriaEspaco> categoria = CategoriaEspacoDao.getInstance().buscarCategoria(nomeCategoria);
//                    if (categoria.isPresent()) {
//                        categorias.add(categoria.get().getCodigo());
//                    } else {
//                        System.out.println("Categoria não encontrada!");
//                    }
//                    System.out.println("Deseja adicionar outra categoria? (S/N)");
//                    String opcao = scanner.nextLine();
//                    if (opcao.equalsIgnoreCase("N")) {
//                        break;
//                    }
//                }
//                EspacoClubDao.getInstance().atualizar(new EspacoClub(novoNome, novaDescricao, novaCapacidade, categorias), nome);
//            } else {
//                System.out.println("Espaço não encontrado!");
//            }
//            menuEspaco(scanner);
//        } catch (Exception e) {
//            System.out.println("Falha ao editar espaço: " + e.getMessage());
//            menuEspaco(scanner);
//        }
//    }
//
//    private void removerEspaco(Scanner scanner) {
//        try {
//            System.out.println("Digite o nome do espaço que deseja remover:");
//            String nome = scanner.nextLine();
//            Optional<EspacoClub> espaco = EspacoClubDao.getInstance().buscarEspaco(nome);
//            if (espaco.isPresent()) {
//                EspacoClubDao.getInstance().deletar(espaco.get().getNome());
//                System.out.println("Espaço removido com sucesso!");
//            } else {
//                System.out.println("Espaço não encontrado!");
//            }
//            menuEspaco(scanner);
//        } catch (Exception e) {
//            System.out.println("Falha ao remover espaço: " + e.getMessage());
//            menuEspaco(scanner);
//        }
//    }
//
//    private void listarEspacos(Scanner scanner) {
//        try {
//            List<EspacoClub> espacoClubs = EspacoClubDao.getInstance().listarTodos();
//            if (espacoClubs.isEmpty()) {
//                System.out.println("Não há espaços cadastrados!");
//            } else {
//                System.out.println("Espaços cadastrados:");
//                AsciiTable at = new AsciiTable();
//                at.addRule();
//                at.addRow("Código", "Nome", "Descrição", "Capacidade", "Categorias");
//                at.addRule();
//                for (EspacoClub espacoClub : espacoClubs) {
//                    at.addRow(espacoClub.getCodigo(), espacoClub.getNome(), espacoClub.getDescricao(), espacoClub.getLotacaoMaxima(),
//                            getCategoriasNomes(espacoClub.getCategorias())).setTextAlignment(TextAlignment.CENTER);
//                    at.addRule();
//                }
//                System.out.println(at.render());
//                System.out.println("Total de espaços: " + espacoClubs.size());
//            }
//
//            menuEspaco(scanner);
//        } catch (Exception e) {
//            System.out.println("Falha ao listar espaços: " + e.getMessage());
//            menuEspaco(scanner);
//        }
//    }
//
//    private void buscarEspaco(Scanner scanner) {
//        try {
//            System.out.println("Digite o nome do espaço que deseja buscar:");
//            String nome = scanner.nextLine();
//            Optional<EspacoClub> espaco = EspacoClubDao.getInstance().buscarEspaco(nome);
//            if (espaco.isPresent()) {
//                System.out.println("Espaço encontrado:");
//                AsciiTable at = new AsciiTable();
//                at.addRule();
//                at.addRow("Código", "Nome", "Descrição", "Capacidade", "Categorias");
//                at.addRule();
//                at.addRow(espaco.get().getCodigo(), espaco.get().getNome(), espaco.get().getDescricao(), espaco.get().getLotacaoMaxima(), getCategoriasNomes(espaco.get().getCategorias()));
//                at.addRule();
//                at.setTextAlignment(TextAlignment.CENTER);
//                System.out.println(at.render());
//            } else {
//                System.out.println("Espaço não encontrado!");
//            }
//            menuEspaco(scanner);
//        } catch (Exception e) {
//            System.out.println("Falha ao buscar espaço: " + e.getMessage());
//            menuEspaco(scanner);
//        }
//    }
//
//    private String getCategoriasNomes(Set<String> categorias) {
//        String categoriasNomes = "";
//        for (String codigoCategoria : categorias) {
//            Optional<CategoriaEspaco> categoria = CategoriaEspacoDao.getInstance().buscarCategoriaCodigo(codigoCategoria);
//            if (categoria.isPresent()) {
//                categoriasNomes += categoria.get().getNome() + " ";
//            }
//        }
//        return categoriasNomes;
//    }

}
