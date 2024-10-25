package Dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> getById(Long id);
    List<T> getAll();
    Optional<T> update(Long id, T obj);
    T create(T obj);
    int deleteByid(Long id);
}
