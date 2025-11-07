package moviebooking.model;

import java.time.LocalDateTime;

public class Screening {
    private int screeningId;
    private Movie movie;
    private Screen screen;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Seat[][] seats;

}
