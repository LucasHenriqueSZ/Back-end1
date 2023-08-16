package domain.services.util.ExceptionsMessages;

public enum ExceptionsCategoriaEspacoMessages {

    CATEGORIA_NULA("Categoria não pode ser nula"),
    NOME_CATEGORIA_NULO("Nome da categoria não pode ser nulo"),
    NOME_CATEGORIA_MENOR_3("Nome da categoria deve conter no mínimo 3 caracteres"),
    NOME_CATEGORIA_MAIOR_50("Nome da categoria deve conter no máximo 50 caracteres"),
    CATEGORIA_JA_CADASTRADA("Categoria já cadastrada"),
    NOME_DEVE_CONTER_APENAS_LETRAS_OU_NUMEROS("Nome da categoria deve conter apenas letras ou números"),
    CATEGORIA_NAO_ENCONTRADA("Categoria não encontrada"),
    CATEGORIA_EM_USO("Categoria em uso"),
    NENHUMA_CATEGORIA_CADASTRADA("Nenhuma categoria cadastrada");

    private final String mensagem;

    ExceptionsCategoriaEspacoMessages(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
