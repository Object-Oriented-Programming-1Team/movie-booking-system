package moviebooking.service;

import moviebooking.common.Manageable;
import moviebooking.model.Movie;

import java.util.ArrayList;

public class MovieManager extends BaseManager<Movie, String> {

    @Override
    public Movie findById(String id) {
        for (Movie movie : list) {
            if(movie.getMovieTitle().equals(id)) {
                return movie;
            }
        }
        return null;
    }

}
