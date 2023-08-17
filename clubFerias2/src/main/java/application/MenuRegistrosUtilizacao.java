package application;

import application.dto.EspacoClubDto;
import application.dto.RegistroUtilizacaoDto;
import application.dto.socioDto.SocioDto;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import domain.services.RegistroUtilizacaoService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuRegistrosUtilizacao {

    private static MenuRegistrosUtilizacao instance;

    private final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");

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
        System.out.println("Digite o número da opção desejada:");

        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1 -> adicionarRegistroUtilizacao(scanner);
            case 2 -> editarRegistroUtilizacao(scanner);
            case 3 -> removerRegistroUtilizacao(scanner);
            case 4 -> listarRegistrosUtilizacao(scanner);
            case 5 -> consultarRegistroUtilizacao(scanner);
            case 6 -> MenuPrincipal.getInstance().menuPricipal(scanner);
            default -> {
                System.out.println("Opção inválida!");
                menuRegistroUtilizacao(scanner);
            }
        }
    }

    private void editarRegistroUtilizacao(Scanner scanner) {
        try {
            System.out.println("Digite o código do registro de utilização que deseja editar:");
            String codigoRegistro = scanner.nextLine();

            RegistroUtilizacaoDto registro = RegistroUtilizacaoService.getInstance().consultarRegistroUtilizacao(codigoRegistro).get();

            System.out.println("Digite a data de utilização no formato dd/mm/aaaa: - data atual: " + registro.getDataUtilizacao().format(formatterDate));
            String dataUtilizacao = scanner.nextLine();

            System.out.println("Digite a hora de entrada no formato hh:mm:ss - hora atual: " + registro.getHoraEntrada().format(formatterTime));
            String horaEntrada = scanner.nextLine();

            System.out.println("Digite a hora de saída no formato hh:mm:ss - hora atual: " + registro.getHoraSaida().format(formatterTime));
            String horaSaida = scanner.nextLine();

            System.out.println("Digite o nome do sócio - nome atual: " + registro.getSocio().getNome());
            String nomeSocio = scanner.nextLine();

            System.out.println("Digite o nome do espaço utilizado - nome atual: " + registro.getEspacoClub().getNome());
            String nomeEspaco = scanner.nextLine();

            RegistroUtilizacaoDto registroUtilizacaoAtualizado = new RegistroUtilizacaoDto(LocalDate.parse(dataUtilizacao, formatterDate),
                    LocalTime.parse(horaEntrada, formatterTime), LocalTime.parse(horaSaida, formatterTime), new SocioDto(nomeSocio), new EspacoClubDto(nomeEspaco), null);

            RegistroUtilizacaoService.getInstance().editarRegistroUtilizacao(codigoRegistro, registroUtilizacaoAtualizado);
            System.out.println("Registro de utilização editado com sucesso!");
            menuRegistroUtilizacao(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao editar registro de utilização: " + e.getMessage());
            menuRegistroUtilizacao(scanner);
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

            System.out.println("Digite o nome do sócio:");
            String nomeSocio = scanner.nextLine();

            System.out.println("Digite o nome do espaço utilizado:");
            String nomeEspaco = scanner.nextLine();

            RegistroUtilizacaoDto registroUtilizacaoDto = new RegistroUtilizacaoDto(LocalDate.parse(dataUtilizacao, formatterDate),
                    LocalTime.parse(horaEntrada, formatterTime), LocalTime.parse(horaSaida, formatterTime), new SocioDto(nomeSocio), new EspacoClubDto(nomeEspaco), null);

            RegistroUtilizacaoService.getInstance().adicionarRegistroUtilizacao(registroUtilizacaoDto);
            System.out.println("Registro de utilização cadastrado com sucesso!");
            menuRegistroUtilizacao(scanner);
        } catch (Exception e) {
            System.out.println("Falha ao cadastrar registro de utilização: " + e.getMessage());
            menuRegistroUtilizacao(scanner);
        }
    }

    private void removerRegistroUtilizacao(Scanner scanner) {
        try {
            System.out.println("Digite o código do registro de utilização que deseja remover:");
            String codigoRegistro = scanner.nextLine();

            RegistroUtilizacaoService.getInstance().removerRegistroUtilizacao(codigoRegistro);

            System.out.println("Registro de utilização removido com sucesso!");
            menuRegistroUtilizacao(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            menuRegistroUtilizacao(scanner);
        }
    }

    private void listarRegistrosUtilizacao(Scanner scanner) {
        try {
            List<RegistroUtilizacaoDto> registros = RegistroUtilizacaoService.getInstance().listarRegistrosUtilizacao();

            System.out.println("Registros de utilização cadastrados:");
            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("Código", "Data", "Hora Entrada", "Hora Saída", "Nome Socio", "Espaço");
            at.addRule();
            for (RegistroUtilizacaoDto registro : registros) {
                at.addRow(registro.getCodigoRegistro(), registro.getDataUtilizacao(), registro.getHoraEntrada(),
                        registro.getHoraSaida(), registro.getSocio().getNome(), registro.getEspacoClub().getNome()).setTextAlignment(TextAlignment.CENTER);
                at.addRule();
            }
            System.out.println(at.render());
            System.out.println("Total de registros de utilização: " + registros.size());

            menuRegistroUtilizacao(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            menuRegistroUtilizacao(scanner);
        }
    }

    private void consultarRegistroUtilizacao(Scanner scanner) {
        try {
            System.out.println("Digite o codigo do registro que deseja consultar:");
            String codigo = scanner.nextLine();

            RegistroUtilizacaoDto registro = RegistroUtilizacaoService.getInstance().consultarRegistroUtilizacao(codigo).get();

            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("Código", "Data", "Hora Entrada", "Hora Saída", "Nome Socio", "Espaço");
            at.addRule();
            at.addRow(registro.getCodigoRegistro(), registro.getDataUtilizacao(), registro.getHoraEntrada(),
                    registro.getHoraSaida(), registro.getSocio().getNome(), registro.getEspacoClub().getNome());
            at.addRule();
            at.setTextAlignment(TextAlignment.CENTER);
            System.out.println(at.render());
            menuRegistroUtilizacao(scanner);

            menuRegistroUtilizacao(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            menuRegistroUtilizacao(scanner);
        }
    }
}
