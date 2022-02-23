package hotel;

import org.json.JSONObject;

public class EnsuiteRoom extends Room {

    /**
     * @return A JSON object of the form:
     * {
     *  "bookings": [ each booking as a JSON object, in order of creation ],
     *  "type": the type of the room (standard, ensuite, penthouse)
     * }
     */
    public JSONObject toJSON() {
        JSONObject jsonObject = super.toJSON();
        jsonObject.put("type", "ensuite");
        return jsonObject;
    }

    public void printWelcomeMessage() {
        System.out.println("Welcome to your beautiful ensuite room which overlooks the Sydney harbour. Enjoy your stay");
    }
    
}