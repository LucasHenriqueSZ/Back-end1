package application;

import infrastructure.EspacoClubDao;
import infrastructure.RegistroUtilizacaoDao;
import infrastructure.SocioDao;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import infrastructure.entities.RegistroUtilizacao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuRegistrosUtilizacao {

    private static MenuRegistrosUtilizacao instance;

    private DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");

    private MenuRegistrosUtilizacao() {
    }

    public static MenuRegistrosUtilizacao getInstance() {
        if (instance == null) {
            instance = new MenuRegistrosUtilizacao();
        }
        return instance;
    }

    public void menuRegistroUtilizacao(Scanner scanner) {
        System.out.println("Escolha uma opção:");
        System.out.println("1 - Adicionar novo registro de utilização");
        System.out.println("2 - Editar registro de utilização");
        System.out.println("3 - Remover registro de utilização");
        System.out.println("4 - Listar registros de utilização");
        System.out.println("5 - Consultar registro de utilização");
        System.out.println("6 - Voltar ao menu principal");

        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                adicionarRegistroUtilizacao(scanner);
                break;
            case 2:
                editarRegistroUtilizacao(scanner);
                break;
            case 3:
                removerRegistroUtilizacao(scanner);
                break;
            case 4:
                listarRegistrosUtilizacao(scanner);
                break;
            case 5:
                consultarRegistroUtilizacao(scanner);
                break;
            case 6:
                MenuPrincipal.getInstance().menuPricipal(scanner);
                break;
            default:
                System.out.println("Opção inválida!");
                menuRegistroUtilizacao(scanner);
                break;
        }
    }

    private void adicionarRegistroUtilizacao(Scanner scanner) {
        try {
            System.out.println("Digite a data de utilização no formato dd/mm/aaaa:");
            String dataUtilizacao = scanner.nextLine();

            System.out.println("Digite a hora de entrada no formato hh:mm:ss");
            String horaEntrada = scanner.nextLine();

            System.out.println("Digite a hora de saída no formato hh:mm:ss");
            String horaSaida = scanner.nextLine();

            String carteirinhaSocio = null;
            do {
                System.out.println("Digite o código da carteirinha do sócio:");
                carteirinhaSocio = scanner.nextLine();
            } while (carteirinhaSocio == null || carteirinhaSocio.isEmpty() || carteirinhaSocio.length() != 8);

            String codigoEspaco = null;
            do {
                System.out.println("Digite o código do espaço:");
                codigoEspaco = scanner.nextLine();
            } while (codigoEspaco == null || codigoEspaco.isEmpty() || codigoEspaco.length() != 5);

            RegistroUtilizacao registroUtilizacao = new RegistroUtilizacao(LocalDate.parse(dataUtilizacao, formatterDate),
                    LocalTime.parse(horaEntrada, formatterTime), LocalTime.parse(horaSaida, formatterTime), carteirinhaSocio, codigoEspaco);

            RegistroUtilizacaoDao.getInstance().salvar(registroUtilizacao);
            System.out.println("Registro de utilização cadastrado com sucesso!");
            menuRegistroUtilizacao(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao cadastrar registro de utilização: " + e.getMessage());
            menuRegistroUtilizacao(scanner);
        }
    }

    private void editarRegistroUtilizacao(Scanner scanner) {
        try {
            System.out.println("Digite o código do registro de utilização que deseja editar:");
            String codigoRegistro = scanner.nextLine();

            Optional<RegistroUtilizacao> registro = RegistroUtilizacaoDao.getInstance().buscarRegistroCodigo(codigoRegistro);
            if (!registro.isPresent()) {
                throw new IllegalArgumentException("Registro de utilização não encontrado!");
            }

            System.out.println("Digite a data de utilização no formato dd/mm/aaaa:");
            String dataUtilizacao = scanner.nextLine();

            System.out.println("Digite a hora de entrada no formato hh:mm:ss");
            String horaEntrada = scanner.nextLine();

            System.out.println("Digite a hora de saída no formato hh:mm:ss");
            String horaSaida = scanner.nextLine();

            String carteirinhaSocio = null;
            do {
                System.out.println("Digite o código da carteirinha do sócio:");
                carteirinhaSocio = scanner.nextLine();
            } while (carteirinhaSocio == null || carteirinhaSocio.isEmpty() || carteirinhaSocio.length() != 8);

            String codigoEspaco = null;
            do {
                System.out.println("Digite o código do espaço:");
                codigoEspaco = scanner.nextLine();
            } while (codigoEspaco == null || codigoEspaco.isEmpty() || codigoEspaco.length() != 5);


            RegistroUtilizacao registroUtilizacaoAtualizado = new RegistroUtilizacao(LocalDate.parse(dataUtilizacao, formatterDate),
                    LocalTime.parse(horaEntrada, formatterTime), LocalTime.parse(horaSaida, formatterTime), carteirinhaSocio, codigoEspaco);

            RegistroUtilizacaoDao.getInstance().atualizar(registroUtilizacaoAtualizado, codigoRegistro);
            System.out.println("Registro de utilização editado com sucesso!");
            menuRegistroUtilizacao(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao editar registro de utilização: " + e.getMessage());
            menuRegistroUtilizacao(scanner);
        }

    }

    private void removerRegistroUtilizacao(Scanner scanner) {
        try {
            System.out.println("Digite o código do registro de utilização que deseja remover:");
            String codigoRegistro = scanner.nextLine();

            Optional<RegistroUtilizacao> registros = RegistroUtilizacaoDao.getInstance().buscarRegistroCodigo(codigoRegistro);
            if (!registros.isPresent()) {
                throw new IllegalArgumentException("Registro de utilização não encontrado!");
            }

            RegistroUtilizacaoDao.getInstance().deletar(codigoRegistro);
            System.out.println("Registro de utilização removido com sucesso!");
            menuRegistroUtilizacao(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao remover registro de utilização: " + e.getMessage());
            menuRegistroUtilizacao(scanner);
        }
    }

    private void listarRegistrosUtilizacao(Scanner scanner) {
        try {
            List<RegistroUtilizacao> registros = RegistroUtilizacaoDao.getInstance().listarTodos();
            if (registros.isEmpty()) {
                System.out.println("Nenhum registro de utilização cadastrado!");
            } else {
                System.out.println("Registros de utilização cadastrados:");
                AsciiTable at = new AsciiTable();
                at.addRule();
                at.addRow("Código", "Data", "Hora Entrada", "Hora Saída", "Nome Socio", "Espaço");
                at.addRule();
                for (RegistroUtilizacao registro : registros) {
                    at.addRow(registro.getCodigoRegistro(), registro.getDataUtilizacao(), registro.getHoraEntrada(),
                            registro.getHoraSaida(), getNomeSocio(registro.getCarteirinhaSocio()), getNomeEspaco(registro.getCodigoEspaco())).setTextAlignment(TextAlignment.CENTER);
                    at.addRule();
                }
                System.out.println(at.render());
                System.out.println("Total de registros de utilização: " + registros.size());
            }

            menuRegistroUtilizacao(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao listar registros de utilização: " + e.getMessage());
            menuRegistroUtilizacao(scanner);
        }
    }

    private void consultarRegistroUtilizacao(Scanner scanner) {
        try {
            System.out.println("Digite o codigo do registro que deseja consultar:");
            String codigo = scanner.nextLine();

            if (codigo == null || codigo.isEmpty() || codigo.length() != 5) {
                System.out.println("Código inválido!");
                consultarRegistroUtilizacao(scanner);
            }

            Optional<RegistroUtilizacao> registro = RegistroUtilizacaoDao.getInstance().buscarRegistroCodigo(codigo);
            if (registro.isPresent()) {
                AsciiTable at = new AsciiTable();
                at.addRule();
                at.addRow("Código", "Data", "Hora Entrada", "Hora Saída", "Nome Socio", "Espaço");
                at.addRule();
                at.addRow(registro.get().getCodigoRegistro(), registro.get().getDataUtilizacao(), registro.get().getHoraEntrada(),
                        registro.get().getHoraSaida(), getNomeSocio(registro.get().getCarteirinhaSocio()), getNomeEspaco(registro.get().getCodigoEspaco()));
                at.addRule();
                at.setTextAlignment(TextAlignment.CENTER);
                System.out.println(at.render());
                menuRegistroUtilizacao(scanner);
            } else {
                System.out.println("Registro não encontrado!");
            }
            menuRegistroUtilizacao(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao consultar registro: " + e.getMessage());
            menuRegistroUtilizacao(scanner);
        }
    }

    private String getNomeEspaco(String codigoEspaco) {
        try {
            return EspacoClubDao.getInstance().buscarEspacoCodigo(codigoEspaco).get().getNome();
        } catch (Exception e) {
            System.out.println("Falha ao consultar nome do espaço: " + e.getMessage());
        }
        return "";
    }

    private String getNomeSocio(String carteirinhaSocio) {
        try {
            return SocioDao.getInstance().buscaPorCarteirinha(carteirinhaSocio).get().getNome();
        } catch (Exception e) {
            System.out.println("Falha ao consultar nome do sócio: " + e.getMessage());
        }
        return "";
    }
}
