package main.java.moviebooking.service;

import main.java.moviebooking.model.Booking;
import main.java.moviebooking.model.Seat;
import main.java.moviebooking.model.User;

import java.util.ArrayList;
import java.util.Scanner;

public class BookingManager extends BaseManager<Booking, String> {

    // 메모리 상에 저장된 예약 목록에서 ID로 찾기
    @Override
    public Booking findById(String bookingId) {
        // 사용자가 입력한 ID와 저장된 ID 비교 (대소문자 무시 등은 선택사항)
        for (Booking b : list) {
            if (b.getBookingId().equals(bookingId)) {
                return b;
            }
            if (b.getBookingId().startsWith(bookingId)) {
                return b;
            }
        }
        return null;
    }

    @Override
    protected Booking readItem(Scanner scan) {
        // 파일에서 읽어오는 로직이 필요하다면 구현 (지금은 메모리 저장 위주라면 null 리턴)
        return null;
    }

    public ArrayList<Booking> findByPhoneNumber(String phoneNumber) {
        ArrayList<Booking> result = new ArrayList<>();

        for (Booking booking : list) {
            User user = booking.getUser();

            if (user != null &&
                    user.getPhoneNumber() != null &&
                    user.getPhoneNumber().equals(phoneNumber)) {

                result.add(booking);
            }
        }
        return result;
    }

    @Override
    public void save(Booking booking) {
        // 부모 클래스(BaseManager)의 save를 호출하여 리스트에 추가
        super.save(booking);

        // 핵심 로직: 예매된 좌석들을 '사용 불가(Booked)' 상태로 변경
        if (booking.getSeats() != null) {
            for (Seat seat : booking.getSeats()) {
                seat.setBooked(true);
            }
        }
        System.out.println("[INFO] 예약 저장 및 좌석 점유 완료: " + booking.getBookingId());
    }
    // 2. 예매 취소 메서드 추가
    public boolean cancelBooking(String bookingId) {
        Booking booking = findById(bookingId);

        if (booking == null) {
            System.out.println("[ERROR] 취소할 예약을 찾을 수 없습니다.");
            return false;
        }

        // 예매된 좌석들을 다시 '사용 가능(Unbooked)' 상태로 복구
        if (booking.getSeats() != null) {
            for (Seat seat : booking.getSeats()) {
                seat.setBooked(false);
            }
        }

        // 리스트에서 예약 정보 삭제
        list.remove(booking);
        System.out.println("[INFO] 예약 취소 완료: " + bookingId);
        return true;
    }
}