package hotel;

import java.time.LocalDate;

import org.json.JSONObject;

public class Booking {
    
    private LocalDate arrival;
    private LocalDate departure;

    public Booking(LocalDate arrival, LocalDate departure) {
        this.arrival = arrival;
        this.departure = departure;
    }

    /**
     * @return a JSONObject of the form {"arrival": arrival, "departure": departure}
     */
    public JSONObject toJSON() {
        JSONObject booking = new JSONObject();
        booking.put("arrival", arrival);
        booking.put("departure", departure);

        return booking;
    }

    /**
     * Checks if start and end dates are between the arrival and departure dates of the existing old booking
     * 
     * @param start
     * @param end
     * @return true if dates are overlapping
     */
    public boolean overlaps(LocalDate start, LocalDate end) {
        return (start.isAfter(arrival) && start.isBefore(departure)) ||
                (start.isEqual(arrival) && end.isEqual(departure)) ||
                (end.isAfter(arrival) && end.isBefore(departure));
    }

}