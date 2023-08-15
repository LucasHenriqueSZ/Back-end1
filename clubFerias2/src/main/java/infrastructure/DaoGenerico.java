package infrastructure;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DaoGenerico<T> {

    void salvar(T entity) throws IOException;
    void atualizar(T entity) throws IOException;
    void deletar(T entity) throws IOException;
    Optional<T> buscarPorCodigo(String codigo) throws IOException;

    List<T> buscarTodos() throws IOException;
}
