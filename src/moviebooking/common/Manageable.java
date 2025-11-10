package moviebooking.common;

import java.util.ArrayList;

public interface Manageable <T, ID> {

    void save(T item);

    T findById(ID id);
    ArrayList<T> findAll();

    void deleteById(ID id);
}
