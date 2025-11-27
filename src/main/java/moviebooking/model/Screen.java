package main.java.moviebooking.model;

public class Screen {
    private String screenId;
    private String screenName;
    private String screenType; // "IMAX", "3D"

    private int defaultPrice;

    private int rows;
    private int cols;

    // 좌석마다 가격 차등 적용할 경우 변경 필요


    //추가 : 관별 좌석 레이아웃 타입
    private String seatLayoutType = "NORMAL";
    // NORMAL / COUPLE_BACK / MIDDLE_AISLE

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public int getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(int defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public String getSeatLayoutType() {
        return seatLayoutType;
    }

    public void setSeatLayoutType(String seatLayoutType) {
        this.seatLayoutType = seatLayoutType;
    }
}
