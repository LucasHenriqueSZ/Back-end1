package presentation;

import data.CategoriaDao;
import data.EspacoDao;
import domain.Categoria;
import domain.Espaco;

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
        System.out.println("5 - Gerenciar Categorias");
        System.out.println("6 - Voltar ao menu principal");
        System.out.println("Digite o número da opção desejada:");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                cadastrarEspaco(scanner);
                break;
            case "2":
                // editarEspaco(scanner);
                break;
            case "3":
                // removerEspaco(scanner);
                break;
            case "4":
                // listarEspacos(scanner);
                break;
            case "5":
                MenuCategoria.getInstance().menuCategoria(scanner);
                break;
            case "6":
                MenuPrincipal.getInstance().menuPricipal(scanner);
                break;
            default:
                System.out.println("Opção inválida");
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
            Set<String> categorias = new HashSet<>();
            while (true) {
                System.out.println("Digite o nome da categoria que deseja adicionar ao espaço:");
                String nomeCategoria = scanner.nextLine();
                Optional<Categoria> categoria = CategoriaDao.getInstance().buscarCategoria(nomeCategoria);
                if (categoria.isPresent()) {
                    categorias.add(categoria.get().getCodigo());
                } else {
                    System.out.println("Categoria não encontrada!");
                }
                System.out.println("Deseja adicionar outra categoria? (S/N)");
                String opcao = scanner.nextLine();
                if (opcao.equalsIgnoreCase("N")) {
                    break;
                }
            }

            Espaco espaco = new Espaco(nome, descricao, capacidade, categorias);

            EspacoDao.getInstance().salvar(espaco);
            System.out.println("Espaço cadastrado com sucesso!");
            menuEspaco(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao cadastrar espaço: " + e.getMessage());
            menuEspaco(scanner);
        }
    }
}
