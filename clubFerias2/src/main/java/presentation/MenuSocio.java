package presentation;

import data.SocioDao;
import de.vandermeer.asciitable.AsciiTable;
import domain.socio.Socio;
import domain.socio.documentos.Cpf;
import domain.socio.documentos.Rg;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public abstract class MenuSocio {

    public static void menuSocio(Scanner scanner) {
        System.out.println("Escolha uma opção:");
        System.out.println("1 - cadastrar sócio");
        System.out.println("2 - Buscar sócio");
        System.out.println("3 - Atualizar sócio");
        System.out.println("4 - Remover sócio");
        System.out.println("5 - Listar Todos sócios");
        System.out.println("6 - Voltar ao menu principal");

        System.out.println("Digite o número da opção desejada:");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                cadastrarSocio(scanner);
                break;
            case "2":
                buscarSocio(scanner);
                break;
            case "3":
                //atualizarSocio(scanner);
                break;
            case "4":
                //removerSocio(scanner);
                break;
            case "5":
                listarTodosSocios(scanner);
                break;
            case "6":
                MenuPrincipal.menuPricipal(scanner);
                break;
            default:
                System.out.println("Opção inválida");
                menuSocio(scanner);
        }
    }

    private static void listarTodosSocios(Scanner scanner) {
        SocioDao socioDao = new SocioDao();
        List<Socio> socios = socioDao.listarTodos();
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Carteirinha", "Nome", "Data de Associação", "Documento");
        at.addRule();
        for (Socio socio : socios) {
            at.addRow(socio.getCarteirinha(), socio.getNome(), socio.getDataAssociacao().toString(), socio.getDocumento().toString());
            at.addRule();
        }
        System.out.println(at.render());
        System.out.println();
        menuSocio(scanner);
    }

    private static void buscarSocio(Scanner scanner) {
        try {
            scanner.nextLine();
            System.out.println("Digite o nome/Documento do sócio:");
            String nomeOudocumento = scanner.nextLine();

            SocioDao socioDao = new SocioDao();
            Optional<Socio> socio = socioDao.buscarPorDocumentoOuNome(nomeOudocumento);
            if (socio.isEmpty()) {
                throw new Exception("Sócio não encontrado");
            }
            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("Carteirinha", "Nome", "Data de Associação", "Documento");
            at.addRule();
            at.addRow(socio.get().getCarteirinha(), socio.get().getNome(), socio.get().getDataAssociacao().toString(), socio.get().getDocumento().toString());
            at.addRule();
            System.out.println(at.render());
            System.out.println();


            opcoesBuscaSocio(scanner);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            opcoesBuscaSocio(scanner);
        }
    }

    private static void opcoesBuscaSocio(Scanner scanner) {
        String opcao;
        do {
            System.out.println("1 - Voltar ao menu principal");
            System.out.println("2 - Buscar outro sócio");
            System.out.println("Digite o número da opção desejada:");
            opcao = scanner.nextLine();

        } while (!opcao.equals("1") && !opcao.equals("2"));


        if (opcao.equals("1")) {
            MenuPrincipal.menuPricipal(scanner);
        } else if (opcao.equals("2")) {
            buscarSocio(scanner);
        }
    }

    private static void cadastrarSocio(Scanner scanner) {
        try {
            Socio socio = null;

            scanner.nextLine();
            System.out.println("Digite o nome do sócio:");
            String nome = scanner.nextLine();
            System.out.println("Escolha o tipo de documento:");
            String opcao;
            do {
                System.out.println("1 - CPF");
                System.out.println("2 - RG");
                System.out.println("Digite o número da opção desejada:");
                opcao = scanner.nextLine();
            }
            while (!opcao.equals("1") && !opcao.equals("2"));
            scanner.nextLine();


            if (opcao.equals("1")) {
                System.out.println("Digite o número do CPF:");
                String cpf = scanner.nextLine();
                socio = new Socio(nome, new Cpf(cpf));
            } else if (opcao.equals("2")) {
                System.out.println("Digite o número do RG:");
                String rg = scanner.nextLine();
                socio = new Socio(nome, new Rg(rg));
            }


            SocioDao socioDao = new SocioDao();
            socioDao.salvar(socio);

            System.out.println("Sócio cadastrado com sucesso!");
            menuSocio(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            opcoesExceptionCadastrarSocio(scanner);
        }
    }

    private static void opcoesExceptionCadastrarSocio(Scanner scanner) {
        String opcao;
        do {
            System.out.println("1 - Voltar ao menu principal");
            System.out.println("2 - Realizar novo cadastro de socio");
            System.out.println("Digite o número da opção desejada:");
            opcao = scanner.nextLine();

        } while (!opcao.equals("1") && !opcao.equals("2"));

        if (opcao.equals("1")) {
            MenuPrincipal.menuPricipal(scanner);
        } else if (opcao.equals("2")) {
            cadastrarSocio(scanner);
        }
    }
}

