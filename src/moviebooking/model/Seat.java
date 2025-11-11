package moviebooking.model;

public class Seat {
    private String seatNumber;
    private boolean isBooked;
    //가격 차등 적용할 경우
    //private String seatType;
    private int price;

    public Seat(String seatNumber, boolean isBooked, int defaultPrice) {
        this.seatNumber = seatNumber;
        this.isBooked = isBooked;
        this.price = defaultPrice;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
