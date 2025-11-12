package moviebooking.service;

import moviebooking.common.Manageable;
import moviebooking.model.Movie;
import moviebooking.model.Screen;
import moviebooking.model.Screening;
import moviebooking.model.Seat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ScreeningManager extends BaseManager<Screening, String> {
    MovieManager movieManager;
    ScreenManager screenManager;
    ScreeningFactory screeningFactory;

    public ScreeningManager(ScreeningFactory screeningFactory,MovieManager movieManager,ScreenManager screenManager){
        this.movieManager=new MovieManager();
        this.screenManager=new ScreenManager();
        this.screeningFactory=new ScreeningFactory();
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
}
