package application;

import infrastructure.CategoriaEspacoDao;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import infrastructure.entities.CategoriaEspaco;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuCategoria {

    private static MenuCategoria instance;

    private MenuCategoria() {
    }

    public static MenuCategoria getInstance() {
        if (instance == null) {
            instance = new MenuCategoria();
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
            case "1":
                cadastrarCategoria(scanner);
                break;
            case "2":
                editarCategoria(scanner);
                break;
            case "3":
                removerCategoria(scanner);
                break;
            case "4":
                listarCategorias(scanner);
                break;
            case "5":
                buscarCategoria(scanner);
                break;
            case "6":
                MenuEspaco.getInstance().menuEspaco(scanner);
                break;
            case "7":
                MenuPrincipal.getInstance().menuPricipal(scanner);
                break;
            default:
                System.out.println("Opção inválida");
                menuCategoria(scanner);
        }
    }

    private void editarCategoria(Scanner scanner) {
        try {
            System.out.println("Digite o nome da categoria:");
            String nomeCategoria = scanner.nextLine();

            System.out.println("Digite o novo nome da categoria:");
            String novoNomeCategoria = scanner.nextLine();

            CategoriaEspaco categoriaEspaco = new CategoriaEspaco(novoNomeCategoria);

            CategoriaEspacoDao.getInstance().atualizar(categoriaEspaco, nomeCategoria);
            System.out.println("Categoria editada com sucesso");
            menuCategoria(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao editar categoria: " + e.getMessage());
            menuCategoria(scanner);
        }
    }

    private void removerCategoria(Scanner scanner) {
        try {
            System.out.println("Digite o nome da categoria:");
            String nomeCategoria = scanner.nextLine();

            Optional<CategoriaEspaco> categoria = CategoriaEspacoDao.getInstance().buscarCategoria(nomeCategoria);
            if (!categoria.isPresent()) {
                throw new IllegalArgumentException("Categoria não encontrada");
            }

            CategoriaEspacoDao.getInstance().deletar(categoria.get().getNome());
            System.out.println("Categoria removida com sucesso");
            menuCategoria(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao remover categoria: " + e.getMessage());
            menuCategoria(scanner);
        }
    }

    private void buscarCategoria(Scanner scanner) {
        try {
            System.out.println("Digite o nome da categoria:");
            String nomeCategoria = scanner.nextLine();

            Optional<CategoriaEspaco> categoria = CategoriaEspacoDao.getInstance().buscarCategoria(nomeCategoria);
            if (!categoria.isPresent()) {
                throw new IllegalArgumentException("Categoria não encontrada");
            }

            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("Código Categoria", "Nome Categoria");
            at.addRule();
            at.addRow(categoria.get().getCodigo(), categoria.get().getNome()).setTextAlignment(TextAlignment.CENTER);
            at.addRule();
            System.out.println(at.render());
            menuCategoria(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao buscar categoria: " + e.getMessage());
            menuCategoria(scanner);
        }
    }

    private void listarCategorias(Scanner scanner) {
        try {

            List<CategoriaEspaco> categoriaEspacos = CategoriaEspacoDao.getInstance().listarTodos();
            if (categoriaEspacos.isEmpty()) {
                System.out.println("Nenhuma categoria cadastrada");
            } else {
                System.out.println("categorias cadastradas:");
                AsciiTable at = new AsciiTable();
                at.addRule();
                at.addRow("Código Categoria", "Nome Categoria");
                at.addRule();
                for (CategoriaEspaco categoriaEspaco : categoriaEspacos) {
                    at.addRow(categoriaEspaco.getCodigo(), categoriaEspaco.getNome()).setTextAlignment(TextAlignment.CENTER);
                    at.addRule();
                }
                System.out.println(at.render());
                System.out.println("Total de categorias: " + categoriaEspacos.size());
            }

            menuCategoria(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao listar categorias: " + e.getMessage());
            menuCategoria(scanner);
        }
    }

    private void cadastrarCategoria(Scanner scanner) {
        try {
            System.out.println("Digite o nome da categoria:");
            String nomeCategoria = scanner.nextLine();

            CategoriaEspaco categoriaEspaco = new CategoriaEspaco(nomeCategoria);

            CategoriaEspacoDao.getInstance().salvar(categoriaEspaco);
            System.out.println("Categoria cadastrada com sucesso");

            menuCategoria(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao cadastrar categoria: " + e.getMessage());
            menuCategoria(scanner);
        }
    }
}
