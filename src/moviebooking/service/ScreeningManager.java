package moviebooking.service;

import moviebooking.common.Manageable;
import moviebooking.model.Movie;
import moviebooking.model.Screening;

import java.util.ArrayList;
import java.util.Scanner;

public class ScreeningManager extends BaseManager<Screening, String> {


    @Override
    public Screening findById(String id) {
        for (Screening screening : list) {
            if(screening.getScreeningId().equals(id))
                return screening;
        }
        return null;
    }

    //TODO 2 Screening readItem method
    @Override
    protected Screening readItem(Scanner scan) {
        return null;
    }
}
