package moviebooking.service;

import moviebooking.common.Manageable;
import moviebooking.model.Screen;

import java.util.ArrayList;

public class ScreenManager implements Manageable<Screen, String> {
    private ArrayList<Screen> screens = new ArrayList<>();

    @Override
    public void save(Screen item) {
        screens.add(item);
        System.out.println("영화관 저장됨" + item.getScreenId());
    }

    @Override
    public Screen findById(String s) {
        for (Screen screen : screens) {
            if (screen.getScreenId().equals(s)) {
                return screen;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Screen> findAll() {
        return screens;
    }

    @Override
    public void deleteById(String s) {
        screens.remove(findById(s));
    }
}
