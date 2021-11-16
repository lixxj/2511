package dungeonmania.entities.moving.enemies;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.entities.EntityInfo;
import dungeonmania.dungeon.Dungeon;

public class Assassin extends Mercenary {
    
    /**
     * Class Constructor. Creates a new instance of a assassin.
     * 
     * @param dungeon - dungeon in which the assassin spawns and moves around
     * @param entityInfo - entityInfo object
     */
    public Assassin(Dungeon dungeon, EntityInfo entityInfo) {
        super(dungeon, entityInfo, DungeonConstants.ASSASSIN_ATTACK_DAMAGE);
    }

    public Assassin(Dungeon dungeon, EntityInfo entityInfo, boolean ally) {
        super(dungeon, entityInfo, DungeonConstants.ASSASSIN_ATTACK_DAMAGE, ally);
    }
    
}
