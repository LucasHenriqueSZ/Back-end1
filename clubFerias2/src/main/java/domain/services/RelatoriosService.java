package domain.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import infrastructure.*;
import infrastructure.adaptersJson.InterfaceAdapter;
import infrastructure.adaptersJson.LocalDateAdapter;
import infrastructure.adaptersJson.LocalTimeAdapter;
import infrastructure.entities.CategoriaEspaco;
import infrastructure.entities.EspacoClub;
import infrastructure.entities.RegistroUtilizacao;
import infrastructure.entities.socio.Socio;
import infrastructure.entities.socio.documentos.Documento;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RelatoriosService {

    private static RelatoriosService instance;

    private Gson gson;

    private RelatoriosService() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .registerTypeAdapter(Documento.class, new InterfaceAdapter())
                .create();
    }

    public static RelatoriosService getInstance() {
        if (instance == null) {
            instance = new RelatoriosService();
        }
        return instance;
    }

    public void gerarRelatorioTempoDeUsoSocioCategoria() {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Tempo_de_Uso_por_socio_e_categoria");

            List<CategoriaEspaco> categoriaEspacos = CategoriaEspacoDao.getInstance().buscarTodos();

            String[] cabecalhos = new String[categoriaEspacos.size() + 1];
            cabecalhos[0] = "Socio";
            for (int i = 0; i < categoriaEspacos.size(); i++) {
                cabecalhos[i + 1] = categoriaEspacos.get(i).getNome();
            }

            escreverCabecalhoNaPlanilha(workbook, sheet, cabecalhos);

            HashMap<String, HashMap<String, String>> dados = getDadosRelatorioTempoDeUsoSocioCategoria(categoriaEspacos);

            escreveDadosNaPlanilha(workbook, sheet, dados, cabecalhos);

            SalvaRelatorios.getInstance().salvarRelatorioTempoDeUsoSocioCategoria(workbook);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    public void gerarRelatorioTotalUso() {
        try (
                XSSFWorkbook workbook = new XSSFWorkbook()) {

            var sheetSocio = workbook.createSheet("Total_de_Uso_por_socio");

            String[] cabecalhosSocios = {"Socio", "Total de Uso"};
            escreverCabecalhoNaPlanilha(workbook, sheetSocio, cabecalhosSocios);

            HashMap<String, String> dadosSocios = getDadosRelatorioTotalUsoSocio();

            escreveDadosNaPlanilha(workbook, sheetSocio, dadosSocios);

            var sheetEspacos = workbook.createSheet("Total_de_Uso_por_Espaço");

            String[] cabecalhosEspacos = {"Espaço", "Total de Uso"};
            escreverCabecalhoNaPlanilha(workbook, sheetEspacos, cabecalhosEspacos);

            HashMap<String, String> dadosEspacos = getDadosRelatorioTotalUsoEspaco();

            escreveDadosNaPlanilha(workbook, sheetEspacos, dadosEspacos);

            var sheetCategorias = workbook.createSheet("Total_de_Uso_por_categoria");

            String[] cabecalhosCategoria = {"Categoria", "Total de Uso"};
            escreverCabecalhoNaPlanilha(workbook, sheetCategorias, cabecalhosCategoria);

            HashMap<String, String> dadosCategoria = getDadosRelatorioTotalUsoCategoria();

            escreveDadosNaPlanilha(workbook, sheetCategorias, dadosCategoria);

            SalvaRelatorios.getInstance().salvarRelatorioTotalUso(workbook);
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao gerar relatório: " + e.getMessage());
        }
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(Short.parseShort("1"));
        return style;
    }

    private CellStyle createDataStyle(XSSFWorkbook workbook, short bgColor) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(bgColor);
        style.setFillPattern(Short.parseShort("1"));
        return style;
    }

    private void escreverCabecalhoNaPlanilha(XSSFWorkbook workbook, XSSFSheet sheet, String[] cabecalhos) {
        CellStyle cabecalhoStyle = createHeaderStyle(workbook);

        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < cabecalhos.length; col++) {
            Cell headerCell = headerRow.createCell(col);
            headerCell.setCellValue(cabecalhos[col]);
            headerCell.setCellStyle(cabecalhoStyle);
        }
    }

    private void escreveDadosNaPlanilha(XSSFWorkbook workbook, XSSFSheet sheet,
                                        HashMap<String, HashMap<String, String>> dados, String[] cabecalho) {
        CellStyle linhaFormat1 = createDataStyle(workbook, IndexedColors.WHITE.getIndex());
        CellStyle linhaFormat2 = createDataStyle(workbook, IndexedColors.LIGHT_GREEN.getIndex());

        int linha = 1;
        for (String socio : dados.keySet()) {
            HashMap<String, String> tempoCategoria = dados.get(socio);
            CellStyle linhaFormat = (linha % 2 == 0) ? linhaFormat1 : linhaFormat2;

            Row dataRow = sheet.createRow(linha);
            Cell cellSocio = dataRow.createCell(0);
            cellSocio.setCellValue(socio);
            cellSocio.setCellStyle(linhaFormat);

            for (int i = 1; i < cabecalho.length; i++) {
                Cell cellTotalUso = dataRow.createCell(i);
                cellTotalUso.setCellValue("-");
                cellTotalUso.setCellStyle(linhaFormat);
            }

            for (String categoria : tempoCategoria.keySet()) {
                int coluna = 0;
                for (String cabecalhoCategoria : cabecalho) {
                    if (cabecalhoCategoria.equalsIgnoreCase(categoria)) {
                        Cell cellTotalUso = dataRow.createCell(coluna);
                        cellTotalUso.setCellValue(tempoCategoria.get(categoria));
                        cellTotalUso.setCellStyle(linhaFormat);
                    }
                    coluna++;
                }
            }
            linha++;
        }
    }

    private void escreveDadosNaPlanilha(XSSFWorkbook workbook, XSSFSheet sheet, HashMap<String, String> dados) {
        CellStyle linhaFormat1 = createDataStyle(workbook, IndexedColors.WHITE.getIndex());
        CellStyle linhaFormat2 = createDataStyle(workbook, IndexedColors.LIGHT_GREEN.getIndex());

        int linha = 1;
        for (String espaco : dados.keySet()) {
            String totalUso = dados.get(espaco);
            CellStyle linhaFormat = (linha % 2 == 0) ? linhaFormat1 : linhaFormat2;

            Row dataRow = sheet.createRow(linha);
            Cell cellSocio = dataRow.createCell(0);
            cellSocio.setCellValue(espaco);
            cellSocio.setCellStyle(linhaFormat);

            Cell cellTotalUso = dataRow.createCell(1);
            cellTotalUso.setCellValue(totalUso);
            cellTotalUso.setCellStyle(linhaFormat);

            linha++;
        }
    }

    private HashMap<String, String> getDadosRelatorioTotalUsoCategoria() {
        List<CategoriaEspaco> categoriaEspacos = CategoriaEspacoDao.getInstance().buscarTodos();
        List<RegistroUtilizacao> registros = RegistroUtilizacaoDao.getInstance().buscarTodos();
        List<EspacoClub> espacoClubs = EspacoClubDao.getInstance().buscarTodos();
        HashMap<String, String> dados = new HashMap<>();

        for (CategoriaEspaco categoriaEspaco : categoriaEspacos) {
            Duration tempoTotal = Duration.ZERO;

            for (RegistroUtilizacao registro : registros) {
                for (EspacoClub espacoClub : espacoClubs) {
                    if (espacoClub.getCodigo().equalsIgnoreCase(registro.getCodigoEspaco()) &&
                            espacoClub.getCategorias().contains(categoriaEspaco.getCodigo())) {
                        tempoTotal = tempoTotal.plus(registro.getTempoDeUso());
                    }
                }
            }

            dados.put(categoriaEspaco.getNome(), conversorDuratioToString(tempoTotal));
        }
        return dados;
    }

    private HashMap<String, String> getDadosRelatorioTotalUsoEspaco() {
        List<EspacoClub> espacoClubs = EspacoClubDao.getInstance().buscarTodos();
        List<RegistroUtilizacao> registros = RegistroUtilizacaoDao.getInstance().buscarTodos();
        HashMap<String, String> dados = new HashMap<>();

        for (EspacoClub espacoClub : espacoClubs) {
            Duration tempoTotal = Duration.ZERO;
            for (RegistroUtilizacao registro : registros) {
                if (espacoClub.getCodigo().equalsIgnoreCase(registro.getCodigoEspaco())) {
                    tempoTotal = tempoTotal.plus(registro.getTempoDeUso());
                }
            }
            dados.put(espacoClub.getNome(), conversorDuratioToString(tempoTotal));
        }

        return dados;
    }

    private static HashMap<String, String> getDadosRelatorioTotalUsoSocio() {
        List<Socio> socios = SocioDao.getInstance().buscarTodos();
        List<RegistroUtilizacao> registros = RegistroUtilizacaoDao.getInstance().buscarTodos();
        HashMap<String, String> dados = new HashMap<>();

        for (Socio socio : socios) {
            Duration tempoTotal = Duration.ZERO;
            for (RegistroUtilizacao registro : registros) {
                if (socio.getCarteirinha().equalsIgnoreCase(registro.getCarteirinhaSocio())) {
                    tempoTotal = tempoTotal.plus(registro.getTempoDeUso());
                }
            }
            dados.put(socio.getNome(), conversorDuratioToString(tempoTotal));
        }

        return dados;
    }


    private HashMap<String, HashMap<String, String>> getDadosRelatorioTempoDeUsoSocioCategoria(List<CategoriaEspaco> categoriaEspacos) {
        List<Socio> socios = SocioDao.getInstance().buscarTodos();
        List<RegistroUtilizacao> registros = RegistroUtilizacaoDao.getInstance().buscarTodos();
        List<EspacoClub> espacoClubs = EspacoClubDao.getInstance().buscarTodos();

        HashMap<String, HashMap<String, Duration>> dados = new HashMap<>();

        for (Socio socio : socios) {
            Duration tempoTotal = Duration.ZERO;
            HashMap<String, Duration> tempoCategoria = new HashMap<>();

            for (RegistroUtilizacao registro : registros) {
                if (registro.getCarteirinhaSocio().equalsIgnoreCase(socio.getCarteirinha())) {
                    EspacoClub espacoClubCorrespondente = null;
                    List<CategoriaEspaco> categoriasCorrespondente = new ArrayList<>();

                    for (EspacoClub espacoClub : espacoClubs) {
                        if (espacoClub.getCodigo().equalsIgnoreCase(registro.getCodigoEspaco())) {
                            espacoClubCorrespondente = espacoClub;
                            break;
                        }
                    }

                    if (espacoClubCorrespondente != null) {
                        for (CategoriaEspaco categoriaEspaco : categoriaEspacos) {
                            for (String codigo : espacoClubCorrespondente.getCategorias()) {
                                if (categoriaEspaco.getCodigo().equalsIgnoreCase(codigo)) {
                                    categoriasCorrespondente.add(categoriaEspaco);
                                    break;
                                }
                            }
                        }
                    }

                    if (categoriasCorrespondente != null && !categoriasCorrespondente.isEmpty()) {
                        for (CategoriaEspaco categoriaEspaco : categoriasCorrespondente) {
                            if (tempoCategoria.containsKey(categoriaEspaco.getNome())) {
                                tempoCategoria.put(categoriaEspaco.getNome(), tempoCategoria.get(categoriaEspaco.getNome()).plus(registro.getTempoDeUso()));
                            } else {
                                tempoCategoria.put(categoriaEspaco.getNome(), registro.getTempoDeUso());
                            }
                        }
                    }
                }
            }
            dados.put(socio.getNome(), tempoCategoria);
        }

        HashMap<String, HashMap<String, String>> dadosString = new HashMap<>();
        for (String socio : dados.keySet()) {
            HashMap<String, String> tempoCategoria = new HashMap<>();
            for (String categoria : dados.get(socio).keySet()) {
                tempoCategoria.put(categoria, conversorDuratioToString(dados.get(socio).get(categoria)));
            }
            dadosString.put(socio, tempoCategoria);
        }
        return dadosString;
    }

    private static String conversorDuratioToString(Duration tempoTotal) {
        long horas = tempoTotal.toHours();
        long minutos = tempoTotal.toMinutes() - (horas * 60);

        return horas + " Horas e " + minutos + " Minutos";
    }
}
