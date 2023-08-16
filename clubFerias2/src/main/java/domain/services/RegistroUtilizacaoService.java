package domain.services;

public class RegistroUtilizacaoService {
    private static RegistroUtilizacaoService instance;

    private RegistroUtilizacaoService() {
    }

    public static RegistroUtilizacaoService getInstance() {
        if (instance == null) {
            instance = new RegistroUtilizacaoService();
        }
        return instance;
    }

}
