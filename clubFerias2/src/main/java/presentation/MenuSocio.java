package presentation;

import data.SocioDao;
import de.vandermeer.asciitable.AsciiTable;
import domain.socio.Socio;
import domain.socio.documentos.Cpf;
import domain.socio.documentos.Rg;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuSocio {

    private static MenuSocio instance;

    private MenuSocio() {
    }

    public static MenuSocio getInstance() {
        if (instance == null) {
            instance = new MenuSocio();
        }
        return instance;
    }

    public void menuSocio(Scanner scanner) {

        System.out.println("Escolha uma opção:");
        System.out.println("1 - cadastrar sócio");
        System.out.println("2 - Buscar sócio");
        System.out.println("3 - Atualizar sócio");
        System.out.println("4 - Remover sócio");
        System.out.println("5 - Listar Todos sócios");
        System.out.println("6 - Voltar ao menu principal");

        System.out.println("Digite o número da opção desejada:");
        java.lang.String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                cadastrarSocio(scanner);
                break;
            case "2":
                buscarSocio(scanner);
                break;
            case "3":
                atualizarSocio(scanner);
                break;
            case "4":
                removerSocio(scanner);
                break;
            case "5":
                listarTodosSocios(scanner);
                break;
            case "6":
                MenuPrincipal.getInstance().menuPricipal(scanner);
                break;
            default:
                System.out.println("Opção inválida");
                menuSocio(scanner);
        }
    }

    private void atualizarSocio(Scanner scanner) {
        try {
            scanner.nextLine();
            System.out.println("Digite o nome/documento do sócio que deseja atualizar:");
            java.lang.String nomeOuDocumento = scanner.nextLine();

            Optional<Socio> socio = SocioDao.getInstance().buscarPorDocumentoOuNome(nomeOuDocumento);
            if (socio.isEmpty()) {
                throw new Exception("Sócio não encontrado");
            }
            System.out.println("Digite o novo nome do sócio:");
            java.lang.String nome = scanner.nextLine();
            socio.get().setNome(nome);
            do {
                System.out.println("Digite o novo documento(RG/CPF) do sócio:");
                java.lang.String documento = scanner.nextLine();
                if (documento.length() == 11) {
                    socio.get().setDocumento(new Cpf(documento));
                } else if (documento.length() == 9) {
                    socio.get().setDocumento(new Rg(documento));
                } else {
                    System.out.println("Documento inválido");
                }
            } while (socio.get().getDocumento() == null);

            SocioDao.getInstance().atualizar(socio.get(), socio.get().getCarteirinha());
            System.out.println("Sócio atualizado com sucesso!");
            System.out.println();
            menuSocio(scanner);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            atualizarSocio(scanner);
        }
    }

    private void removerSocio(Scanner scanner) {
        try {
            System.out.println("Digite a nome ou documento do sócio que deseja remover:");
            String nomeOudocumento = scanner.nextLine();
            Optional<Socio> socio = SocioDao.getInstance().buscarPorDocumentoOuNome(nomeOudocumento);
            if (socio.isPresent()) {
                SocioDao.getInstance().deletar(socio.get().getCarteirinha());
                System.out.println("Sócio removido com sucesso!");
            } else {
                System.out.println("Sócio não encontrado!");
            }
            menuSocio(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao remover espaço: " + e.getMessage());
            menuSocio(scanner);
        }
    }

    private void listarTodosSocios(Scanner scanner) {
        try {
            List<Socio> socios = SocioDao.getInstance().listarTodos();
            if (socios.isEmpty()) {
                System.out.println("Não há sócios cadastrados");
            } else {
                AsciiTable at = new AsciiTable();
                at.addRule();
                at.addRow("Carteirinha", "Nome", "Data de Associação", "Documento");
                at.addRule();
                for (Socio socio : socios) {
                    at.addRow(socio.getCarteirinha(), socio.getNome(), socio.getDataAssociacao().toString(), socio.getDocumento().toString());
                    at.addRule();
                }
                System.out.println(at.render());
                System.out.println("Total de sócios: " + socios.size());
            }
            menuSocio(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao listar socios: " + e.getMessage());
            menuSocio(scanner);
        }
    }

    private void buscarSocio(Scanner scanner) {
        try {
            scanner.nextLine();
            System.out.println("Digite o nome/Documento do sócio:");
            java.lang.String nomeOudocumento = scanner.nextLine();

            Optional<Socio> socio = SocioDao.getInstance().buscarPorDocumentoOuNome(nomeOudocumento);
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

    private void opcoesBuscaSocio(Scanner scanner) {
        java.lang.String opcao;
        do {
            System.out.println("1 - Voltar ao menu principal");
            System.out.println("2 - Buscar outro sócio");
            System.out.println("Digite o número da opção desejada:");
            opcao = scanner.nextLine();

        } while (!opcao.equals("1") && !opcao.equals("2"));


        if (opcao.equals("1")) {
            MenuPrincipal.getInstance().menuPricipal(scanner);
        } else if (opcao.equals("2")) {
            buscarSocio(scanner);
        }
    }

    private void cadastrarSocio(Scanner scanner) {
        try {
            Socio socio = null;

            scanner.nextLine();
            System.out.println("Digite o nome do sócio:");
            java.lang.String nome = scanner.nextLine();
            System.out.println("Escolha o tipo de documento:");
            java.lang.String opcao;
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
                java.lang.String cpf = scanner.nextLine();
                socio = new Socio(nome, new Cpf(cpf));
            } else if (opcao.equals("2")) {
                System.out.println("Digite o número do RG:");
                java.lang.String rg = scanner.nextLine();
                socio = new Socio(nome, new Rg(rg));
            }

            SocioDao.getInstance().salvar(socio);

            System.out.println("Sócio cadastrado com sucesso!");
            menuSocio(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            opcoesExceptionCadastrarSocio(scanner);
        }
    }

    private void opcoesExceptionCadastrarSocio(Scanner scanner) {
        java.lang.String opcao;
        do {
            System.out.println("1 - Voltar ao menu principal");
            System.out.println("2 - Realizar novo cadastro de socio");
            System.out.println("Digite o número da opção desejada:");
            opcao = scanner.nextLine();

        } while (!opcao.equals("1") && !opcao.equals("2"));

        if (opcao.equals("1")) {
            MenuPrincipal.getInstance().menuPricipal(scanner);
        } else if (opcao.equals("2")) {
            cadastrarSocio(scanner);
        }
    }
}

