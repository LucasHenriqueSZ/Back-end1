package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.Categoria;
import domain.Espaco;
import domain.RegistroUtilizacao;
import domain.socio.Socio;
import domain.socio.documentos.Documento;
import gson.InterfaceAdapter;
import gson.LocalDateAdapter;
import gson.LocalTimeAdapter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RelatoriosDao {

    private static RelatoriosDao instance;

    private final String PATH = "relatorios/";

    private final String NOME_ARQUIVO_TOTAL_USO = "relatorios tempo Total de Uso.xlsx";

    private final String NOME_ARQUIVO_USO_POR_SOCIO_E_CATEGORIA = "Tempo de Uso por socio e categoria.xlsx";

    private Gson gson;

    private RelatoriosDao() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .registerTypeAdapter(Documento.class, new InterfaceAdapter())
                .create();
    }

    public static RelatoriosDao getInstance() {
        if (instance == null) {
            instance = new RelatoriosDao();
        }
        return instance;
    }

    public void gerarRelatorioTempoDeUsoSocioCategoria() {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Tempo_de_Uso_por_socio_e_categoria");

            List<Categoria> categorias = CategoriaDao.getInstance().listarTodos();

            String[] cabecalhos = new String[categorias.size() + 1];
            cabecalhos[0] = "Socio";
            for (int i = 0; i < categorias.size(); i++) {
                cabecalhos[i + 1] = categorias.get(i).getNome();
            }

            escreverCabecalhoNaPlanilha(workbook, sheet, cabecalhos);

            HashMap<String, HashMap<String, String>> dados = getDadosRelatorioTempoDeUsoSocioCategoria(categorias);

            escreveDadosNaPlanilha(workbook, sheet, dados, cabecalhos);

            FileOutputStream fileOut = new FileOutputStream(PATH + NOME_ARQUIVO_USO_POR_SOCIO_E_CATEGORIA);

            workbook.write(fileOut);
            workbook.close();
            fileOut.close();
        } catch (IOException e) {
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

            FileOutputStream fileOut = new FileOutputStream(PATH + NOME_ARQUIVO_TOTAL_USO);

            workbook.write(fileOut);
            workbook.close();
            fileOut.close();
        } catch (IOException e) {
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
        style.setFillPattern(Short.valueOf("1"));
        return style;
    }

    private CellStyle createDataStyle(XSSFWorkbook workbook, short bgColor) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(bgColor);
        style.setFillPattern(Short.valueOf("1"));
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
        List<Categoria> categorias = CategoriaDao.getInstance().listarTodos();
        List<RegistroUtilizacao> registros = RegistroUtilizacaoDao.getInstance().listarTodos();
        List<Espaco> espacos = EspacoDao.getInstance().listarTodos();
        HashMap<String, String> dados = new HashMap<>();

        for (Categoria categoria : categorias) {
            Duration tempoTotal = Duration.ZERO;

            for (RegistroUtilizacao registro : registros) {
                for (Espaco espaco : espacos) {
                    if (espaco.getCodigo().equalsIgnoreCase(registro.getCodigoEspaco()) &&
                            espaco.getCategorias().contains(categoria.getCodigo())) {
                        tempoTotal = tempoTotal.plus(registro.getTempoDeUso());
                    }
                }
            }

            dados.put(categoria.getNome(), conversorDuratioToString(tempoTotal));
        }
        return dados;
    }

    private HashMap<String, String> getDadosRelatorioTotalUsoEspaco() {
        List<Espaco> espacos = EspacoDao.getInstance().listarTodos();
        List<RegistroUtilizacao> registros = RegistroUtilizacaoDao.getInstance().listarTodos();
        HashMap<String, String> dados = new HashMap<>();

        for (Espaco espaco : espacos) {
            Duration tempoTotal = Duration.ZERO;
            for (RegistroUtilizacao registro : registros) {
                if (espaco.getCodigo().equalsIgnoreCase(registro.getCodigoEspaco())) {
                    tempoTotal = tempoTotal.plus(registro.getTempoDeUso());
                }
            }
            dados.put(espaco.getNome(), conversorDuratioToString(tempoTotal));
        }

        return dados;
    }

    private static HashMap<String, String> getDadosRelatorioTotalUsoSocio() {
        List<Socio> socios = SocioDao.getInstance().listarTodos();
        List<RegistroUtilizacao> registros = RegistroUtilizacaoDao.getInstance().listarTodos();
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


    private HashMap<String, HashMap<String, String>> getDadosRelatorioTempoDeUsoSocioCategoria(List<Categoria> categorias) {
        List<Socio> socios = SocioDao.getInstance().listarTodos();
        List<RegistroUtilizacao> registros = RegistroUtilizacaoDao.getInstance().listarTodos();
        List<Espaco> espacos = EspacoDao.getInstance().listarTodos();

        HashMap<String, HashMap<String, Duration>> dados = new HashMap<>();

        for (Socio socio : socios) {
            Duration tempoTotal = Duration.ZERO;
            HashMap<String, Duration> tempoCategoria = new HashMap<>();

            for (RegistroUtilizacao registro : registros) {
                if (registro.getCarteirinhaSocio().equalsIgnoreCase(socio.getCarteirinha())) {
                    Espaco espacoCorrespondente = null;
                    List<Categoria> categoriasCorrespondente = new ArrayList<>();

                    for (Espaco espaco : espacos) {
                        if (espaco.getCodigo().equalsIgnoreCase(registro.getCodigoEspaco())) {
                            espacoCorrespondente = espaco;
                            break;
                        }
                    }

                    if (espacoCorrespondente != null) {
                        for (Categoria categoria : categorias) {
                            for (String codigo : espacoCorrespondente.getCategorias()) {
                                if (categoria.getCodigo().equalsIgnoreCase(codigo)) {
                                    categoriasCorrespondente.add(categoria);
                                    break;
                                }
                            }
                        }
                    }

                    if (categoriasCorrespondente != null && !categoriasCorrespondente.isEmpty()) {
                        for (Categoria categoria : categoriasCorrespondente) {
                            if (tempoCategoria.containsKey(categoria.getNome())) {
                                tempoCategoria.put(categoria.getNome(), tempoCategoria.get(categoria.getNome()).plus(registro.getTempoDeUso()));
                            } else {
                                tempoCategoria.put(categoria.getNome(), registro.getTempoDeUso());
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
