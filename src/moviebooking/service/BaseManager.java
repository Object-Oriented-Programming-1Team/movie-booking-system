package moviebooking.service;

import moviebooking.common.Manageable;

import java.io.File;
import java.net.URL;
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

        // 'ClassLoader'를 통해 'resources' 폴더 내부의 파일에 접근해야 한다.
        try {
            URL url = getClass().getClassLoader().getResource(filename);
            if (url == null){
                System.out.println(filename + " : 파일 찾을 수 없습니다");
                return null;
            }
            filein = new Scanner(new File(url.toURI()));
        } catch (Exception e) {
            System.out.println(filename + ": 파일 열기 실패 - " + e.getMessage());
        }
        return filein;
    }


}
