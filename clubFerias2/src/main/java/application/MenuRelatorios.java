//package application;
//
//import domain.services.RelatoriosService;
//
//import java.util.Scanner;
//
//public class MenuRelatorios {
//
//    private static MenuRelatorios instance;
//
//    private MenuRelatorios() {
//    }
//
//    public static MenuRelatorios getInstance() {
//        if (instance == null) {
//            instance = new MenuRelatorios();
//        }
//        return instance;
//    }
//
//    public void menuRelatorios(Scanner scanner) {
//
//        System.out.println("Escolha uma opção:");
//        System.out.println("1 - Gerar relatório Tempo total de uso");
//        System.out.println("2 - Gerar relatório Tempo total de uso por socio e categoria");
//        System.out.println("3 - Voltar ao menu principal");
//
//        System.out.println("Digite o número da opção desejada:");
//        String opcao = scanner.nextLine();
//
//        switch (opcao) {
//            case "1":
//                tempoTotalUso(scanner);
//                break;
//            case "2":
//                tempoTotalUsoSocioCategoria(scanner);
//                break;
//            case "3":
//                MenuPrincipal.getInstance().menuPricipal(scanner);
//                break;
//            default:
//                System.out.println("Opção inválida");
//                menuRelatorios(scanner);
//        }
//    }
//
//    private void tempoTotalUsoSocioCategoria(Scanner scanner) {
//        try {
//            RelatoriosService.getInstance().gerarRelatorioTempoDeUsoSocioCategoria();
//            System.out.println("Relatório gerado com sucesso!");
//            menuRelatorios(scanner);
//        } catch (Exception e) {
//            System.out.println("Erro: " + e.getMessage());
//            menuRelatorios(scanner);
//        }
//    }
//
//    private void tempoTotalUso(Scanner scanner) {
//        try {
//            RelatoriosService.getInstance().gerarRelatorioTotalUso();
//            System.out.println("Relatório gerado com sucesso!");
//            menuRelatorios(scanner);
//        } catch (Exception e) {
//            System.out.println("Erro: " + e.getMessage());
//            menuRelatorios(scanner);
//        }
//    }
//}
