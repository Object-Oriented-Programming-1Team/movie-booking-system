package moviebooking.service;

import moviebooking.common.Manageable;
import moviebooking.model.Movie;

import java.util.ArrayList;

public class MovieManager implements Manageable<Movie, String> {

    private ArrayList<Movie> movies = new ArrayList<>();

    @Override
    public void save(Movie item) {
        movies.add(item);
        System.out.println("영화 저장됨: " + item.getMovieTitle());
    }


    @Override
    public Movie findById(String id) {
        for (Movie movie : movies) {
            if(movie.getMovieTitle().equals(id)) {
                return movie;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Movie> findAll() {
        return movies;
    }

    @Override
    public void deleteById(String id) {
        movies.remove(findById(id));
    }
}
