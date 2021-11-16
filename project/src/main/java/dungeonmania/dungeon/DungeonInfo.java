package dungeonmania.dungeon;

import dungeonmania.goals.Goal;

public class DungeonInfo {
    private final int height;
    private final int width;
    private final String id;
    private final String name;
    private final String mode;
    private final Goal goals;

    public DungeonInfo(int height, int width, String id, String name, String mode, Goal goals) {
        this.height = height;
        this.width = width;
        this.id = id;
        this.name = name;
        this.mode = mode;
        this.goals = goals;
    }

    
    public Goal getGoals() {
        return goals;
    }

    public String getMode() {
        return mode;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getHeight() {
        return height;
    }
    
    public int getWidth() {
        return width;
    }

}
