package hotel;

import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;

public class Hotel {

    private String name;
    private final int NO_OF_ROOMS = 3;
    private Room[] rooms = new Room[NO_OF_ROOMS];
    private int emptyStandardRooms = NO_OF_ROOMS / 3;
    private int emptyEnsuiteRooms = NO_OF_ROOMS / 3;
    private int emptyPenthouseRooms = NO_OF_ROOMS / 3;

    public Hotel(String name) {
        this.name = name;
    }

    public Room[] getRooms() {
        return this.rooms;
    }

    /**
     * Makes a booking in any available room with the given preferences.
     * 
     * @param arrival
     * @param departure
     * @param standard - does the client want a standard room?
     * @param ensuite - does the client want an ensuite room?
     * @param penthouse - does the client want a penthouse room?
     * @return If there were no available rooms for the given preferences, returns false.
     * Returns true if the booking was successful
     */
    public boolean makeBooking(LocalDate arrival, LocalDate departure, boolean standard, boolean ensuite, boolean penthouse) {

        for (int room_idx = 0; room_idx < NO_OF_ROOMS; room_idx++) {
            if (rooms[room_idx] == null) {
                if (standard && emptyStandardRooms > 0) {
                    rooms[room_idx] = new StandardRoom();
                    emptyStandardRooms--;
                } else if (ensuite && emptyEnsuiteRooms > 0) {
                    rooms[room_idx] = new EnsuiteRoom();
                    emptyEnsuiteRooms--;
                } else if (penthouse && emptyPenthouseRooms > 0) {
                    rooms[room_idx] = new PenthouseRoom();
                    emptyPenthouseRooms--;
                }
            } 
            if ((standard && (rooms[room_idx] instanceof StandardRoom)) || (ensuite && (rooms[room_idx] instanceof EnsuiteRoom)) || 
                (penthouse& (rooms[room_idx] instanceof PenthouseRoom))) {
                if (rooms[room_idx].book(arrival, departure) != null) return true;
            }
        }

        return false;

    }

    /**
     * @return A JSON Object of the form:
     * { "name": name, "rooms": [ each room as a JSON object, in order of creation ]}
     */
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        for (Room room : rooms) {
            if (room instanceof StandardRoom) {
                room = (StandardRoom) room;
            } else if (room instanceof EnsuiteRoom) {
                room = (EnsuiteRoom) room;
            } else if (room instanceof PenthouseRoom) {
                room = (PenthouseRoom) room;
            } else { // null
                continue;
            }
            jsonArray.put(room.toJSON());
        } 

        jsonObject.put("name", name);
        jsonObject.put("rooms", jsonArray);

        return jsonObject;
    }

    public static void main(String[] args) {
        
    }   
}