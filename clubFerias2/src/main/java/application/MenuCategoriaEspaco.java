package application;

import application.dto.CategoriaEspacoDto;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import domain.services.CategoriaEspacoService;

import java.util.List;
import java.util.Scanner;

public class MenuCategoriaEspaco {

    private static MenuCategoriaEspaco instance;

    private MenuCategoriaEspaco() {
    }

    public static MenuCategoriaEspaco getInstance() {
        if (instance == null) {
            instance = new MenuCategoriaEspaco();
        }
        return instance;
    }

    public void menuCategoria(Scanner scanner) {
        System.out.println("Escolha uma opção:");
        System.out.println("1 - Cadastrar Categoria");
        System.out.println("2 - Editar Categoria");
        System.out.println("3 - Remover Categoria");
        System.out.println("4 - Listar Categorias");
        System.out.println("5 - Buscar Categoria");
        System.out.println("6 - Voltar ao menu de Espaços");
        System.out.println("7 - Voltar ao menu principal");
        System.out.println("Digite o número da opção desejada:");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1" -> cadastrarCategoria(scanner);
            case "2" -> editarCategoria(scanner);
            case "3" -> removerCategoria(scanner);
            case "4" -> listarCategorias(scanner);
            case "5" -> buscarCategoria(scanner);
            case "6" -> MenuEspacoClub.getInstance().menuEspaco(scanner);
            case "7" -> MenuPrincipal.getInstance().menuPricipal(scanner);
            default -> {
                System.out.println("Opção inválida");
                menuCategoria(scanner);
            }
        }
    }

    private void editarCategoria(Scanner scanner) {
        try {
            System.out.println("Digite o nome da categoria que deseja atualizar:");
            String nomeCategoria = scanner.nextLine();

            CategoriaEspacoDto categoria = CategoriaEspacoService.getInstance().buscarCategoria(nomeCategoria);

            System.out.println("Digite o novo nome da categoria - nome atual: " + categoria.getNome());
            String novoNomeCategoria = scanner.nextLine();

            CategoriaEspacoDto categoriaAtualizada = new CategoriaEspacoDto(novoNomeCategoria, null);

            CategoriaEspacoService.getInstance().editarCategoria(nomeCategoria, categoriaAtualizada);
            System.out.println("Categoria editada com sucesso");
            menuCategoria(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao editar categoria: " + e.getMessage());
            menuCategoria(scanner);
        }
    }

    private void removerCategoria(Scanner scanner) {
        try {
            System.out.println("Digite o nome da categoria que deseja remover:");
            String nomeCategoria = scanner.nextLine();

            CategoriaEspacoService.getInstance().removerCategoria(nomeCategoria);
            System.out.println("Categoria removida com sucesso");
            menuCategoria(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            menuCategoria(scanner);
        }
    }

    private void buscarCategoria(Scanner scanner) {
        try {
            System.out.println("Digite o nome da categoria:");
            String nomeCategoria = scanner.nextLine();

            CategoriaEspacoDto categoria = CategoriaEspacoService.getInstance().buscarCategoria(nomeCategoria);

            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("Código Categoria", "Nome Categoria");
            at.addRule();
            at.addRow(categoria.getCodigo(), categoria.getNome()).setTextAlignment(TextAlignment.CENTER);
            at.addRule();
            System.out.println(at.render());
            menuCategoria(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            menuCategoria(scanner);
        }
    }

    private void listarCategorias(Scanner scanner) {
        try {
            List<CategoriaEspacoDto> categoriaEspacos = CategoriaEspacoService.getInstance().listarCategorias();

            System.out.println("categorias cadastradas:");
            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("Código Categoria", "Nome Categoria");
            at.addRule();
            for (CategoriaEspacoDto categoriaEspaco : categoriaEspacos) {
                at.addRow(categoriaEspaco.getCodigo(), categoriaEspaco.getNome()).setTextAlignment(TextAlignment.CENTER);
                at.addRule();
            }
            System.out.println(at.render());
            System.out.println("Total de categorias: " + categoriaEspacos.size());

            menuCategoria(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            menuCategoria(scanner);
        }
    }

    private void cadastrarCategoria(Scanner scanner) {
        try {
            System.out.println("Digite o nome da categoria:");
            String nomeCategoria = scanner.nextLine();

            CategoriaEspacoDto categoriaEspaco = new CategoriaEspacoDto(nomeCategoria, null);

            CategoriaEspacoService.getInstance().cadastrarCategoria(categoriaEspaco);
            System.out.println("Categoria cadastrada com sucesso");

            menuCategoria(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            menuCategoria(scanner);
        }
    }
}
