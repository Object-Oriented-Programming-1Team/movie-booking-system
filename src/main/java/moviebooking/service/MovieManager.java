package main.java.moviebooking.service;

import main.java.moviebooking.model.Movie;

import java.util.*;

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

    // 검색 기능
    // 소문자 변환 기능은 추후 영어 제목을 위해 (한글에 영향 없음)
    public ArrayList<Movie> searchMoviesByTitle(String keyword) {
        ArrayList<Movie> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase().trim(); // 소문자로 변환 및 공백 제거

        for (Movie movie : list) {
            String movieTitle = movie.getMovieTitle().toLowerCase(); // 영화 제목도 소문자로

            if (movieTitle.contains(lowerKeyword)) {
                results.add(movie);
            }
        }
        return results;
    }

    public List<Movie> searchMoviesByTitleOrGenre(String keyword) {
        ArrayList<Movie> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase().trim();

        for (Movie movie : list){
            String movieTitle = movie.getMovieTitle().toLowerCase();
            String genre = movie.getGenre().toLowerCase();

            if (movieTitle.contains(lowerKeyword) || genre.contains(lowerKeyword)) {
                results.add(movie);
            }

        }

        return results;
    }

}
