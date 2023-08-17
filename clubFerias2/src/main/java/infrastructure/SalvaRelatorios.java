package infrastructure;

import com.google.gson.Gson;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class SalvaRelatorios {

    private static SalvaRelatorios instance;

    private final String PATH = "relatorios/";

    Gson gson = new Gson();

    private SalvaRelatorios() {
    }

    public static SalvaRelatorios getInstance() {
        if (instance == null) {
            instance = new SalvaRelatorios();
        }
        return instance;
    }

    public void salvarRelatorioTotalUso(XSSFWorkbook workbook) {
        try {
            String nomeArquivo = "relatorios tempo Total de Uso.xlsx";
            FileOutputStream fileOut = new FileOutputStream(PATH + nomeArquivo);

            salvaArquivo(workbook, fileOut);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void salvarRelatorioTempoDeUsoSocioCategoria(XSSFWorkbook workbook) {
        try {
            String nomeArquivo = "Tempo de Uso por socio e categoria.xlsx";
            FileOutputStream fileOut = new FileOutputStream(PATH + nomeArquivo);

            salvaArquivo(workbook, fileOut);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void salvaArquivo(XSSFWorkbook workbook, FileOutputStream fileOut) throws IOException {
        workbook.write(fileOut);
        workbook.close();
        fileOut.close();
    }
}
