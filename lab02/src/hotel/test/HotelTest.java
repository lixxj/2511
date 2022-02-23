package hotel.test;

import java.time.LocalDate;

import hotel.Booking;
import hotel.Hotel;
import hotel.Room;

public class HotelTest {
    public static void main(String[] args) {
        Hotel myHotel = new Hotel("Hotel California");
        
        LocalDate date1 = LocalDate.of(2021, 9, 18);
        LocalDate date2 = LocalDate.of(2021, 9, 25);
        LocalDate date3 = LocalDate.of(2021, 9, 30);
        LocalDate date4 = LocalDate.of(2021, 10, 6);


        myHotel.makeBooking(date1, date2, true, false, false);
        myHotel.makeBooking(date1, date2, false, true, false);
        myHotel.makeBooking(date1, date2, false, false, true);

        System.out.println("==========Output 1: Booking a Standard Room, Ensuite Room and Penthouse Room==========\n" + myHotel.toJSON() + "\n");

        myHotel.makeBooking(date1, date2, true, false, false);

        System.out.println("==========Output 2: Repeat Booking -- Must be Identical to Output 1==========\n" + myHotel.toJSON() + "\n");

        myHotel.makeBooking(date2, date3, true, false, false);
        
        System.out.println("==========Output 3: Added New Booking==========\n" + myHotel.toJSON() + "\n");

        Room[] rooms = myHotel.getRooms();

        rooms[0].removeBooking(new Booking(date1, date2));

        System.out.println("==========Output 4: Removed a Booking from Room[0]==========\n" + rooms[0].toJSON() + "\n");

        rooms[0].changeBooking(new Booking(date2, date3), date3, date4);

        System.out.println("==========Output 5: Changed a Booking in Room[0]==========\n" + rooms[0].toJSON() + "\n");

    }
}
