package infrastructure;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DaoGenerico<T> {

    void salvar(T entity);
    void atualizar(T entity);
    void deletar(T entity);
    Optional<T> buscarPorCodigo(String codigo);

    List<T> buscarTodos();
}
