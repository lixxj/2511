package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

public class StandardRoom extends Room {

    private List<Booking> bookings = new ArrayList<Booking>();

    @Override
    public Booking book(LocalDate arrival, LocalDate departure) {
        for (Booking booking : bookings) {
            if (booking.overlaps(arrival, departure)) {
                return null;
            }
        }

        Booking booking = new Booking(arrival, departure);
        bookings.add(booking);
        return booking;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject standard = new JSONObject();
        JSONArray bookingsj = new JSONArray();
        
        standard.put("type", "standard");
        
        for (Booking booking : bookings) {
            JSONObject bookingj = booking.toJSON();
            bookingsj.put(bookingj);
        }
        standard.put("bookings", bookingsj);

        return standard;
    }
    
    @Override
    public void printWelcomeMessage() {
        System.out.println("Welcome to your standard room. Enjoy your stay :)");
    }
    
}