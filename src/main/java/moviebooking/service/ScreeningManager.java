package main.java.moviebooking.service;

import main.java.moviebooking.model.Movie;
import main.java.moviebooking.model.Screen;
import main.java.moviebooking.model.Screening;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ScreeningManager extends BaseManager<Screening, String> {
    MovieManager movieManager;
    ScreenManager screenManager;
    ScreeningFactory screeningFactory;

    public ScreeningManager(ScreeningFactory screeningFactory,MovieManager movieManager,ScreenManager screenManager){
        this.movieManager= movieManager;
        this.screenManager= screenManager;
        this.screeningFactory= screeningFactory;
    }

    @Override
    public Screening findById(String id) {
        for (Screening screening : list) {
            if(screening.getScreeningId().equals(id))
                return screening;
        }
        return null;
    }

    @Override
    protected Screening readItem(Scanner scan) {
        try{
            Screening screening;
            
            String screeningId = scan.next();
            String movieId = scan.next();
            String screenId = scan.next();
            String startStr = scan.next();
            
            Movie movie = movieManager.findById(movieId);
            Screen screen = screenManager.findById(screenId);
            LocalDateTime start = LocalDateTime.parse(startStr);

            if(movie==null){
                System.out.println("[ERROR] ScreeningManager: 존재하지 않는 영화를 입력했습니다.");
                return null;
            }
            
            if(screen==null){
                System.out.println("[ERROR] ScreeningManager: 존재하지 않는 상영관을 입력했습니다.");
                return null;
            }

            screening = screeningFactory.create(screeningId, movie, screen, start);

            return screening;
        } catch (InputMismatchException e){
            System.out.println("[ERROR] ScreeningManager: InputMismatch " + e.getMessage());
            scan.nextLine();
            return null;
        } catch (NoSuchElementException e) {
            //파일 끝에 도달했거나 형식이 맞지 않음
            return null;
        } catch (Exception e){
            System.out.println("[ERROR] ScreeningManager: 기타 오류: " + e.getMessage());
            return null;
        }
    }

    public List<Movie> findMoviesByDate(LocalDate date){
        // Set을 사용해 중복된 영화를 자동으로 제거
        java.util.Set<Movie> moviesOnDate = new java.util.HashSet<>();

        for (Screening screening : list) {
            // Screening의 startTime(LocalDateTime)을 LocalDate로 변환하여 비교
            if (screening.getStartTime().toLocalDate().isEqual(date)) {
                moviesOnDate.add(screening.getMovie());
            }
        }
        // Set을 다시 List로 변환하여 반환
        return new ArrayList<>(moviesOnDate);
    }

    public Screening findScreeningsByMovieId(String movieId){
        for (Screening screening : list) {
            if(screening.getMovie().getMovieId().equals(movieId))
                return screening;
        }
        return null;
    }
}
