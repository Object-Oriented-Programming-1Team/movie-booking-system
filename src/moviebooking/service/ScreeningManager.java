package moviebooking.service;

import moviebooking.common.Manageable;
import moviebooking.model.Screening;

import java.util.ArrayList;

public class ScreeningManager implements Manageable<Screening, String> {

    private ArrayList<Screening> screenings = new ArrayList<>();

    @Override
    public void save(Screening item) {
            screenings.add(item);
            System.out.println("상영정보 저장됨 " + item.getScreeningId());
    }

    @Override
    public Screening findById(String id) {
        for (Screening screening : screenings) {
            if(screening.getScreeningId().equals(id))
                return screening;
        }
        return null;
    }

    @Override
    public ArrayList<Screening> findAll() {
        return screenings;
    }

    @Override
    public void deleteById(String id) {
        screenings.remove(findById(id));
    }
}
