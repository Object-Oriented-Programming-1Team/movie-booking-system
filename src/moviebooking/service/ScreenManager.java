package moviebooking.service;

import moviebooking.common.Manageable;
import moviebooking.model.Movie;
import moviebooking.model.Screen;
import moviebooking.model.Screening;

import java.util.ArrayList;
import java.util.Scanner;

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

    //TODO 3 Screen readItem method
    @Override
    protected Screen readItem(Scanner scan) {
        return null;
    }
}
