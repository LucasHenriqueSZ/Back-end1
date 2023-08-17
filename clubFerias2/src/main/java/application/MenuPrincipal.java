package application;

import java.util.Scanner;

public class MenuPrincipal {

    private static MenuPrincipal instance;

    private MenuPrincipal() {
    }

    public static MenuPrincipal getInstance() {
        if (instance == null) {
            instance = new MenuPrincipal();
        }
        return instance;
    }

    public void menuPricipal(Scanner scanner) {
        System.out.println("Bem vindo ao sistema de gerenciamento do club de ferias da NewGo");
        System.out.println("Escolha uma opção:");
        System.out.println("1 - gerenciar Sócios");
        System.out.println("2 - gerenciar Espaços");
        System.out.println("3 - gerenciar registros de utilização");
        System.out.println("4 - obter relatórios");
        System.out.println("5 - Sair");
        System.out.println("Digite o número da opção desejada:");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                MenuSocio.getInstance().menuSocio(scanner);
                break;
            case "2":
                MenuEspacoClub.getInstance().menuEspaco(scanner);
                break;
            case "3":
                MenuRegistrosUtilizacao.getInstance().menuRegistroUtilizacao(scanner);
                break;
            case "4":
                MenuRelatorios.getInstance().menuRelatorios(scanner);
                break;
            case "5":
                System.out.println("Obrigado por utilizar o sistema de gerenciamento do club de ferias da NewGo");
                break;
            default:
                System.out.println("Opção inválida");
                menuPricipal(scanner);
        }
    }
}
