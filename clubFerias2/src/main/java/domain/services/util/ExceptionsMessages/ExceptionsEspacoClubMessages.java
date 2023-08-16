package domain.services.util.ExceptionsMessages;

public enum ExceptionsEspacoClubMessages {

    ESPACO_NULO("Espaço nulo"),
    NOME_INVALIDO("Nome inválido"),
    NOME_MINIMO("Nome deve ter no mínimo 3 caracteres"),
    NOME_MAXIMO("Nome deve ter no máximo 50 caracteres"),
    DESCRICAO_INVALIDA("Descrição inválida"),
    DESCRICAO_MINIMA("Descrição deve ter no mínimo 3 caracteres"),
    DESCRICAO_MAXIMA("Descrição deve ter no máximo 50 caracteres"),
    LOTACAO_INVALIDA("Lotação inválida"),
    CATEGORIA_INVALIDA("Categoria inválida"),
    ESPACO_JA_CADASTRADO("Espaço já cadastrado"),
    ESPACO_NAO_ENCONTRADO("Espaço não encontrado"),
    NENHUM_ESPACO_CADASTRADO("Nenhum espaço cadastrado");

    private final String mensagem;

    ExceptionsEspacoClubMessages(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
