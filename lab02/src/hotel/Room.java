package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Room {
    
    protected List<Booking> bookings = new ArrayList<Booking>();

    /**
     * Checks if the room is not booked out during the given time.
     * If so, creates a booking for the room at that time.
     * @param arrival
     * @param departure
     * @return The booking object if the booking succeeded, null if failed
     */
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

    
    /**
     * Removes the given booking from the list of bookings.
     * If the booking isn't in the list of bookings, does nothing.
     * @param booking
     */
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    /**
     * Changes the given booking to be from the updated arrival date to the updated departure date,
     * if and only if the room is available between the updated times.
     * Note that a room being unavailable does NOT include the time of the original booking.
     * 
     * If the room is not available between the updated times, the change fails
     * and the room's booking list remains as it was before the function was called unchanged.
     * @param booking
     * @param arrival
     * @param departure
     */
    public Booking changeBooking(Booking booking, LocalDate arrival, LocalDate departure) {
        Booking newBooking = book(arrival, departure);
        if (newBooking != null) { // New booking successfully placed
            removeBooking(booking); // Remove old booking
            return newBooking;
        }
        return booking;
    }

    /**
     * @return A JSON object of the form:
     * {
     *  "bookings": [ each booking as a JSON object, in order of creation ]
     * } 
     */
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        for (Booking booking : bookings) {
            jsonArray.put(booking.toJSON());
        } 

        jsonObject.put("bookings", jsonArray);

        return jsonObject;
    }

    /**
     * Prints a welcome message to the guest staying in the room.
     */
    public abstract void printWelcomeMessage();

}