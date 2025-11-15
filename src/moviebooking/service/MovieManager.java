package moviebooking.service;

import moviebooking.common.Manageable;
import moviebooking.model.Movie;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MovieManager extends BaseManager<Movie, String> {

    @Override
    public Movie findById(String id) {
        for (Movie movie : list) {
            if(movie.getMovieId().equals(id)) {
                return movie;
            }
        }
        return null;
    }

    @Override
    protected Movie readItem(Scanner scan) {
        try{
            Movie movie=new Movie();
            movie.setMovieId(scan.next());
            String titleWithUnderBar=scan.next();
            movie.setRuntime(scan.nextInt());
            movie.setRating(scan.next());
            movie.setGenre(scan.next());
            movie.setStatus(scan.next());
            movie.setPosterUrl(scan.next());

            movie.setMovieTitle(titleWithUnderBar.replace("_", " "));
            return movie;

        } catch (InputMismatchException e){
            System.out.println("[ERROR] MovieManager: InputMismatch " + e.getMessage());
            scan.nextLine();
            return null;
        } catch (NoSuchElementException e) {
            //파일 끝에 도달했거나 형식이 맞지 않음
            return null;
        }
    }

}
