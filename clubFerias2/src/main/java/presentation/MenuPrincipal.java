package presentation;

import java.util.Scanner;

public class MenuPrincipal {

    public static void menuPricipal(Scanner scanner) {
        System.out.println("Bem vindo ao sistema de gerenciamento do club de ferias da NewGo");
        System.out.println("Escolha uma opção:");
        System.out.println("1 - gerenciar Sócios");
        System.out.println("2 - gerenciar Espaços");
        System.out.println("3 - gerenciar regitros de utilização");
        System.out.println("4 - obter relatórios");
        System.out.println("5 - Sair");
        System.out.println("Digite o número da opção desejada:");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                MenuSocio.menuSocio(scanner);
                break;
            case 2:
                // menuEspaco.menuEspaco(scanner);
                break;
            case 3:
                // menuRegistro.menuRegistro(scanner);
                break;
            case 4:
                //  menuRelatorio.menuRelatorio(scanner);
                break;
            case 5:
                System.out.println("Obrigado por utilizar o sistema de gerenciamento do club de ferias da NewGo");
                break;
            default:
                System.out.println("Opção inválida");
                menuPricipal(scanner);
        }
    }
}
