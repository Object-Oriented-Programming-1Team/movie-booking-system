package moviebooking.service;

import moviebooking.common.Manageable;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

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

    // 자식 클래스들이 구현해야하는 것
    protected abstract T readItem(Scanner scan);

    public void loadData(String filename) {
        Scanner filein = openFile(filename);
        if (filein == null) return;

        while (filein.hasNextLine()) {
            T item = readItem(filein);
            if (item != null) {
                list.add(item);
            }
        }
        filein.close();
    }

    private Scanner openFile(String filename) {
        Scanner filein = null;
        try {
            filein = new Scanner(new File("resources/" + filename));
        } catch (Exception e) {
            System.out.println(filename + ": 파일 열기 실패 - " + e.getMessage());
        }
        return filein;
    }


}
