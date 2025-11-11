package moviebooking.service;

import moviebooking.common.Manageable;
import moviebooking.model.Movie;

import java.util.ArrayList;

public abstract class BaseManager<T, ID> implements Manageable<T, ID> {
     protected ArrayList<T> list = new ArrayList<>();

    @Override
    public  void save(T item){
        list.add(item);
        System.out.println("[INFO] Saving item: " + item);
        System.out.println("[INFO] Item saved successfully. Total items: " + list.size());
    }

    @Override
    public abstract T findById(ID id);

    @Override
    public ArrayList<T> findAll() {
        return list;
    }

    @Override
    public void deleteById(ID id) {
        System.out.println("[INFO] Attempting to delete item with ID: " + id);
        T item = findById(id);

        if (item != null) {
            list.remove(item);
            System.out.println("[INFO] Item deleted successfully. Remaining items: " + list.size());
        } else {
            System.out.println("[WARNING] Item with ID " + id + " not found for deletion");
        }
    }


}
