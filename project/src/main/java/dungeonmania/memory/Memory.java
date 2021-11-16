package dungeonmania.memory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dungeonmania.util.FileLoader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Memory {

    /**
     * Save the current game
     * @param dungeonJson
     * @param id
     */
    public static void saveGame(String dungeonJson, String id) {
        try {
            String path = "savedGames/" + id + ".json";
            FileWriter file = new FileWriter(path);
            file.write(dungeonJson);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Read in a new dungeon file
     * @param name
     * @return JsonObject
     */
    public static JsonObject readNewDungeon(String name) {
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + name + ".json");
            return JsonParser.parseString(file).getAsJsonObject();
        } catch (Exception e1) {
            try {
                return JsonParser.parseReader(new FileReader("src/test/resources/dungeons/" + name + ".json")).getAsJsonObject();
            } catch (Exception e2) {
                try {
                    return JsonParser.parseReader(new FileReader("src/main/resources/dungeons/" + name + ".json")).getAsJsonObject();
                } catch (Exception e3) {
                    throw new IllegalArgumentException("Invalid dungeon name");
                }
            }
        }
    }

    /**
     * Read in saved dungeon file from saved games
     * @param name
     * @return JsonObject
     */
    public static JsonObject readSavedDungeon(String name) {
        try {
            return JsonParser.parseReader(new FileReader("savedGames/" + name + ".json")).getAsJsonObject();
        } catch (Exception e1) {
            throw new IllegalArgumentException("Invalid dungeon name");
        }
    }

}
