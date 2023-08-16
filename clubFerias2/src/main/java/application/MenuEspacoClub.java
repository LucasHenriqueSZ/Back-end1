package application;

import application.dto.CategoriaEspacoDto;
import application.dto.EspacoClubDto;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import domain.services.EspacoClubService;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MenuEspacoClub {

    private static MenuEspacoClub instance;

    private MenuEspacoClub() {
    }

    public static MenuEspacoClub getInstance() {
        if (instance == null) {
            instance = new MenuEspacoClub();
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
                cadastrarEspaco(scanner);
                break;
            case "2":
                editarEspaco(scanner);
                break;
            case "3":
                removerEspaco(scanner);
                break;
            case "4":
                listarEspacos(scanner);
                break;
            case "5":
                buscarEspaco(scanner);
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

    private void buscarEspaco(Scanner scanner) {
        try {
            System.out.println("Digite o nome do espaço que deseja buscar:");
            String nome = scanner.nextLine();

            EspacoClubDto espaco = EspacoClubService.getInstance().buscarEspaco(nome).get();

            System.out.println("Espaço encontrado:");
            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("Código", "Nome", "Descrição", "Capacidade", "Categorias");
            at.addRule();
            at.addRow(espaco.getCodigo(), espaco.getNome(), espaco.getDescricao(), espaco.getLotacaoMaxima(),
                    getCategoriasNomes(espaco.getCategorias()));
            at.addRule();
            at.setTextAlignment(TextAlignment.CENTER);
            System.out.println(at.render());

            menuEspaco(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao buscar espaço: " + e.getMessage());
            menuEspaco(scanner);
        }
    }

    private void listarEspacos(Scanner scanner) {
        try {
            List<EspacoClubDto> espacoClubs = EspacoClubService.getInstance().listarEspacos();

            System.out.println("Espaços cadastrados:");
            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("Código", "Nome", "Descrição", "Capacidade", "Categorias");
            at.addRule();
            for (EspacoClubDto espacoClub : espacoClubs) {
                at.addRow(espacoClub.getCodigo(), espacoClub.getNome(), espacoClub.getDescricao(), espacoClub.getLotacaoMaxima(),
                        getCategoriasNomes(espacoClub.getCategorias())).setTextAlignment(TextAlignment.CENTER);
                at.addRule();
            }
            System.out.println(at.render());
            System.out.println("Total de espaços: " + espacoClubs.size());

            menuEspaco(scanner);
        } catch (
                Exception e) {
            System.out.println("Falha ao listar espaços: " + e.getMessage());
            menuEspaco(scanner);
        }
    }

    private void removerEspaco(Scanner scanner) {
        try {
            System.out.println("Digite o nome do espaço que deseja remover:");
            String nome = scanner.nextLine();

            EspacoClubService.getInstance().removerEspaco(nome);

            System.out.println("Espaço removido com sucesso!");
            menuEspaco(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            menuEspaco(scanner);
        }
    }

    private void editarEspaco(Scanner scanner) {
        try {
            System.out.println("Digite o nome do espaço que deseja editar:");
            String nome = scanner.nextLine();

            EspacoClubDto espacoClubDto = EspacoClubService.getInstance().buscarEspaco(nome).get();

            System.out.println("Digite o novo nome do espaço:");
            String novoNome = scanner.nextLine();
            System.out.println("Digite a nova descrição do espaço:");
            String novaDescricao = scanner.nextLine();
            System.out.println("Digite a nova capacidade do espaço:");
            int novaCapacidade = Integer.parseInt(scanner.nextLine());
            while (novaCapacidade < 0) {
                System.out.println("Capacidade inválida! Digite novamente:");
                novaCapacidade = Integer.parseInt(scanner.nextLine());
            }

            System.out.println("categorias anteriormente cadastradas:");
            AsciiTable tabela = new AsciiTable();
            tabela.addRule();
            tabela.addRow("Nome da categoria");
            tabela.addRule();
            for (CategoriaEspacoDto categoria : espacoClubDto.getCategorias()) {
                tabela.addRow(categoria.getNome());
                tabela.addRule();
            }
            System.out.println(tabela.render());

            System.out.println("A categoria anteriormente cadastrada nao será mantida, se desejar mantê-la, cadastre-a novamente.");
            Set<CategoriaEspacoDto> novasCategorias = new HashSet<>();
            while (true) {
                System.out.println("Digite o nome da nova categoria que deseja adicionar ao espaço:");
                String nomeCategoria = scanner.nextLine();

                novasCategorias.add(new CategoriaEspacoDto(nomeCategoria, null));
                System.out.println("Deseja adicionar outra categoria? (S/N)");
                String opcao = scanner.nextLine();
                if (opcao.equalsIgnoreCase("N")) {
                    break;
                }
            }

            EspacoClubDto espacoClub = new EspacoClubDto(novoNome, novaDescricao, novaCapacidade, novasCategorias, null);

            EspacoClubService.getInstance().editarEspaco(nome, espacoClub);

            menuEspaco(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            menuEspaco(scanner);
        }
    }

    private void cadastrarEspaco(Scanner scanner) {
        try {
            System.out.println("Digite o nome do espaço:");
            String nome = scanner.nextLine();
            System.out.println("Digite a capacidade do espaço:");
            int capacidade = Integer.parseInt(scanner.nextLine());

            System.out.println("Digite a descrição do espaço:");
            String descricao = scanner.nextLine();
            Set<CategoriaEspacoDto> categorias = new HashSet<>();
            while (true) {
                System.out.println("Digite o nome da categoria que deseja adicionar ao espaço:");
                String nomeCategoria = scanner.nextLine();

                categorias.add(new CategoriaEspacoDto(nomeCategoria, null));
                System.out.println("Deseja adicionar outra categoria? (S/N)");
                String opcao = scanner.nextLine();
                if (opcao.equalsIgnoreCase("N")) {
                    break;
                }
            }

            EspacoClubDto espacoClub = new EspacoClubDto(nome, descricao, capacidade, categorias, null);

            EspacoClubService.getInstance().cadastrarEspaco(espacoClub);
            System.out.println("Espaço cadastrado com sucesso!");
            menuEspaco(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            menuEspaco(scanner);
        }
    }

    private String getCategoriasNomes(Set<CategoriaEspacoDto> categorias) {
        String categoriasNomes = "";
        for (CategoriaEspacoDto categoria : categorias) {
            categoriasNomes += categoria.getNome() + ", ";
        }
        return categoriasNomes;
    }
}
