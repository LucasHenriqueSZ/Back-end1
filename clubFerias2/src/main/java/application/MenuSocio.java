package application;

import application.dto.socioDto.SocioDto;
import application.dto.socioDto.documentosDto.CpfDto;
import application.dto.socioDto.documentosDto.RgDto;
import de.vandermeer.asciitable.AsciiTable;
import domain.services.SocioService;

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
            case "1" -> cadastrarSocio(scanner);
            case "2" -> buscarSocio(scanner);
            case "3" -> atualizarSocio(scanner);
            case "4" -> removerSocio(scanner);
            case "5" -> listarTodosSocios(scanner);
            case "6" -> MenuPrincipal.getInstance().menuPricipal(scanner);
            default -> {
                System.out.println("Opção inválida");
                menuSocio(scanner);
            }
        }
    }

    private void listarTodosSocios(Scanner scanner) {
        try {
            List<SocioDto> socioDtoList = SocioService.getInstance().listarTodos();

            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("Carteirinha", "Nome", "Data de Associação", "Documento");
            at.addRule();
            for (SocioDto socio : socioDtoList) {
                at.addRow(socio.getCarteirinha(), socio.getNome(), socio.getDataAssociacao().toString(), socio.getDocumento().toString());
                at.addRule();
            }
            System.out.println(at.render());
            System.out.println("Total de sócios: " + socioDtoList.size());

            menuSocio(scanner);

        } catch (Exception e) {
            System.out.println("Falha ao listar socios: " + e.getMessage());
            menuSocio(scanner);
        }
    }

    private void removerSocio(Scanner scanner) {
        try {
            System.out.println("Digite a nome ou documento do sócio que deseja remover:");
            String nomeOudocumento = scanner.nextLine();

            SocioService.getInstance().remover(nomeOudocumento);

            System.out.println("Sócio removido com sucesso!");

            menuSocio(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao remover espaço: " + e.getMessage());
            menuSocio(scanner);
        }
    }

    private void atualizarSocio(Scanner scanner) {
        try {
            System.out.println("Digite o nome/documento do sócio que deseja atualizar:");
            String nomeOuDocumento = scanner.nextLine();

            SocioDto socioDto = SocioService.getInstance().buscar(nomeOuDocumento).get();
            SocioDto socio = new SocioDto();

            System.out.println("Digite o novo nome do sócio - nome atual: " + socioDto.getNome());
            String nome = scanner.nextLine();
            socio.setNome(nome);

            System.out.println("Escolha o novo tipo de documento - documento atual: " + socioDto.getDocumento().toString());

            String opcao;
            do {
                System.out.println("1 - CPF");
                System.out.println("2 - RG");
                System.out.println("Digite o número da opção desejada: - documento atual: " + socioDto.getDocumento().toString());
                opcao = scanner.nextLine();
            }
            while (!opcao.equals("1") && !opcao.equals("2"));

            if (opcao.equals("1")) {
                System.out.println("Digite o número do CPF:");
                String cpf = scanner.nextLine();
                socio.setDocumento(new CpfDto(cpf));
            } else if (opcao.equals("2")) {
                System.out.println("Digite o número do RG:");
                String rg = scanner.nextLine();
                socio.setDocumento(new RgDto(rg));
            }

            SocioService.getInstance().atualizar(socio, nomeOuDocumento);

            System.out.println("Sócio atualizado com sucesso!");
            System.out.println();
            menuSocio(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            atualizarSocio(scanner);
        }
    }

    private void buscarSocio(Scanner scanner) {
        try {
            System.out.println("Digite o nome/Documento do sócio:");
            String nomeOudocumento = scanner.nextLine();

            Optional<SocioDto> socio = SocioService.getInstance().buscar(nomeOudocumento);

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

    private void cadastrarSocio(Scanner scanner) {
        try {
            SocioDto socioDto = null;

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

            if (opcao.equals("1")) {
                System.out.println("Digite o número do CPF:");
                String cpf = scanner.nextLine();
                socioDto = new SocioDto(nome, new CpfDto(cpf), null, null);
            } else if (opcao.equals("2")) {
                System.out.println("Digite o número do RG:");
                String rg = scanner.nextLine();
                socioDto = new SocioDto(nome, new RgDto(rg), null, null);
            }

            SocioService.getInstance().cadastrarSocio(socioDto);

            System.out.println("Sócio cadastrado com sucesso!");
            menuSocio(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            opcoesExceptionCadastrarSocio(scanner);
        }
    }

    private void opcoesExceptionCadastrarSocio(Scanner scanner) {
        String opcao;
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

    private void opcoesBuscaSocio(Scanner scanner) {
        String opcao;
        do {
            System.out.println("1 - Voltar ao menu principal");
            System.out.println("2 - Buscar outro sócio");
            System.out.println("Digite o número da opção desejada:");
            opcao = scanner.nextLine();

        } while (!opcao.equals("1") && !opcao.equals("2"));

        if (opcao.equals("1")) {
            MenuPrincipal.getInstance().menuPricipal(scanner);
        } else {
            buscarSocio(scanner);
        }
    }
}

