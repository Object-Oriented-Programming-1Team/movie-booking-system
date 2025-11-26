package main.java.moviebooking.model;

public class Movie {
    private String movieId;
    private String movieTitle;
    private int runtime;
    private String rating;
    private String genre;
    private String status;
    private String posterUrl;
    private String plot; //줄거리

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    //추가 : 줄거리
    public String getPlot() { return plot; }
    public void setPlot(String plot) { this.plot = plot; }


}