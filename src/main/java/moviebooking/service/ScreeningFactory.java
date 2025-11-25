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
        switch (screen.getSeatLayoutType()) {
            case "COUPLE_BACK":
                seats = createCoupleBackLayout(screen);
                break;

            case "MIDDLE_AISLE":
                seats = createMiddleAisleLayout(screen);
                break;

            default:
                seats = createNormalLayout(screen);
        }

        screening.setScreeningId(screeningId);
        screening.setMovie(movie);
        screening.setScreen(screen);
        screening.setStartTime(startTime);
        screening.setSeats(seats);
        screening.setEndTime(startTime.plusMinutes(movie.getRuntime()));

        return screening; // '완제품' 반환
    }

    // ================================
    // 1) 일반관 — 전체 좌석 동일
    // ================================
    private Seat[][] createNormalLayout(Screen screen) {
        int rows = screen.getRows();
        int cols = screen.getCols();
        int price = screen.getDefaultPrice();

        Seat[][] seats = new Seat[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String seatNum = (char) ('A' + i) + "" + (j + 1);
                seats[i][j] = new Seat(seatNum, false, price);
            }
        }
        return seats;
    }

    // =======================================
    // 2) COUPLE_BACK 레이아웃
    // → 마지막 2줄은 짝지어서 붙은 좌석으로 표시
    // =======================================
    private Seat[][] createCoupleBackLayout(Screen screen) {

        int rows = screen.getRows();
        int cols = screen.getCols();
        int price = screen.getDefaultPrice();

        Seat[][] seats = new Seat[rows][cols];

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {

                String seatNum = (char) ('A' + i) + "" + (j + 1);

                Seat seat = new Seat(seatNum, false, price);

                // 마지막 두 줄이 커플석
                if (i >= rows - 2) {
                    seat.setCoupleSeat(true);
                }

                seats[i][j] = seat;
            }
        }

        return seats;
    }


    // =======================================
    // 3) MIDDLE_AISLE 레이아웃
    // → 가운데 통로가 있어서 중앙 몇 칸은 null로 처리
    // =======================================
    private Seat[][] createMiddleAisleLayout(Screen screen) {

        int rows = screen.getRows();
        int cols = screen.getCols();
        int price = screen.getDefaultPrice();

        Seat[][] seats = new Seat[rows][cols];

        int aisleCol = cols / 2 - 1;  // 가운데 구역 2칸 통로
        int aisleCol2 = aisleCol + 1;

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {

                // 가운데 2칸은 통로
                if (j == aisleCol || j == aisleCol2) {
                    seats[i][j] = null; // 통로
                    continue;
                }

                String seatNum = (char) ('A' + i) + "" + (j + 1);
                seats[i][j] = new Seat(seatNum, false, price);
            }
        }

        return seats;
    }
}