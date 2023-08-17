package domain.services.util.ExceptionsMessages;

public enum ExceptionsSocioMessages {

    NOME_OU_DOCUMENTO_NULO_OU_VAZIO("Nome ou documento não pode ser nulo ou vazio"),
    NOME_OU_DOCUMENTO_MENOR_3_CARACTERES("Nome ou documento deve conter pelo menos 3 caracteres"),
    NOME_OU_DOCUMENTO_MAIOR_50_CARACTERES("Nome ou documento deve conter no máximo 50 caracteres"),
    NOME_OU_DOCUMENTO_DEVE_CONTER_APENAS_LETRAS_E_NUMEROS("Nome ou documento deve conter apenas letras e números"),
    SOCIO_NAO_ENCONTRADO("Sócio não encontrado"),
    SOCIO_NULO("Sócio não pode ser nulo"),
    CARTEIRINHA_NULA("Carteirinha não pode ser nula"),
    DATA_ASSOCIACAO_NULA("Data de associação não pode ser nula"),
    NOME_NULO_OU_VAZIO("Nome não pode ser nulo ou vazio"),
    NOME_MENOR_3_CARACTERES("Nome deve conter pelo menos 3 caracteres"),
    NOME_MAIOR_50_CARACTERES("Nome deve conter no máximo 50 caracteres"),
    NOME_DEVE_CONTER_APENAS_LETRAS("Nome deve conter apenas letras"),
    DOCUMENTO_NULO("Documento não pode ser nulo"),
    DOCUMENTO_INVALIDO("Documento inválido"),
    SOCIO_JA_CADASTRADO("Sócio já cadastrado"),
    DOCUMENTO_DUPLICADO("Documento duplicado"),
    NENHUM_SOCIO_CADASTRADO("Nenhum sócio cadastrado"),
    SOCIO_EM_REGISTRO_UTILIZACAO("Sócio está cadastrado em um registro de utilização");

    private final String mensagem;

    ExceptionsSocioMessages(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
