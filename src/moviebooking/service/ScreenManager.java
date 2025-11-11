package moviebooking.service;

import moviebooking.common.Manageable;
import moviebooking.model.Screen;

import java.util.ArrayList;

public class ScreenManager extends BaseManager<Screen, String> {

    @Override
    public Screen findById(String s) {
        for (Screen screen : list) {
            if (screen.getScreenId().equals(s)) {
                return screen;
            }
        }
        return null;
    }
}
