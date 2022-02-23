package hotel;

import org.json.JSONObject;

public class PenthouseRoom extends Room {

    /**
     * @return A JSON object of the form:
     * {
     *  "bookings": [ each booking as a JSON object, in order of creation ],
     *  "type": the type of the room (standard, ensuite, penthouse)
     * }
     */
    public JSONObject toJSON() {
        JSONObject jsonObject = super.toJSON();
        jsonObject.put("type", "penthouse");
        return jsonObject;
    }
    
    public void printWelcomeMessage() {
        System.out.println("Welcome to your penthouse apartment, complete with ensuite, lounge, kitchen and master bedroom.");
    }
    
}