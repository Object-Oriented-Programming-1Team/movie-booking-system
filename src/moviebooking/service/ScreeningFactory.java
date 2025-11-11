package moviebooking.service;

import moviebooking.model.Screening;

public class ScreeningFactory<Screening> implements Factory<Screening> {
    public Screening create(){
        return new Screening();
    }
}
