package moviebooking.service;

import moviebooking.common.Manageable;
import moviebooking.model.Screening;

import java.util.ArrayList;

public class ScreeningManager extends BaseManager<Screening, String> {


    @Override
    public Screening findById(String id) {
        for (Screening screening : list) {
            if(screening.getScreeningId().equals(id))
                return screening;
        }
        return null;
    }

}
