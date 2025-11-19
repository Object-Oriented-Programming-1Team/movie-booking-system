package main.java.moviebooking.service;

import main.java.moviebooking.model.Movie;
import main.java.moviebooking.model.Screen;
import main.java.moviebooking.model.Screening;
import main.java.moviebooking.model.Seat;

import java.time.LocalDateTime;

public class ScreeningFactory {

    /**
     * 새로운 상영시간표(Screening) 객체를 '조립'하여 반환합니다.
     *
     * @param screeningId 새 상영 ID (예: 1, 2, 3...)
     * @param movie       상영할 영화 객체
     * @param screen      상영할 상영관 객체 (틀)
     * @param startTime   상영 시작 시간
     * @return 정보가 모두 채워진 '완제품' Screening 객체
     */

    public Screening create(String screeningId, Movie movie, Screen screen, LocalDateTime startTime) {

        Screening screening = new Screening();

        int rows = screen.getRows();
        int cols = screen.getCols();
        int defaultPrice = screen.getDefaultPrice();

        Seat[][] seats = new Seat[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // [0][0] -> "A1", [1][2] -> "B3"
                String seatNum = (char)('A' + i) + "" + (j + 1);
                seats[i][j] = new Seat(seatNum, false, defaultPrice);
            }
        }

        screening.setScreeningId(screeningId);
        screening.setMovie(movie);
        screening.setScreen(screen);
        screening.setStartTime(startTime);
        screening.setSeats(seats);
        screening.setEndTime(startTime.plusMinutes(movie.getRuntime()));

        return screening; // '완제품' 반환
    }
}