package domain.services.util.ExceptionsMessages;

public enum ExceptionsRegistroUtilizacaoMessages {

    NENHUM_REGISTRO_UTILIZACAO_CADASTRADO("Nenhum registro de utilização cadastrado."),
    REGISTRO_UTILIZACAO_NAO_ENCONTRADO("Registro de utilização não encontrado."),
    CODIGO_REGISTRO_UTILIZACAO_INVALIDO("Código do registro de utilização inválido."),
    REGISTRO_UTILIZACAO_JA_CADASTRADO("Registro de utilização já cadastrado."),
    DATA_UTILIZACAO_INVALIDA("Data de utilização inválida."),
    HORA_ENTRADA_INVALIDA("Hora de entrada inválida."),
    HORA_SAIDA_INVALIDA("Hora de saída inválida."),
    CARTEIRINHA_SOCIO_INVALIDA("Carteirinha do sócio inválida."),
    CODIGO_ESPACO_INVALIDO("Código do espaço inválido."),
    HORA_ENTRADA_MAIOR_HORA_SAIDA("Hora de entrada maior que hora de saída.");

    private final String mensagem;

    ExceptionsRegistroUtilizacaoMessages(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
