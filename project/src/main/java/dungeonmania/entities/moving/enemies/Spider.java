package dungeonmania.entities.moving.enemies;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.moving.MoveCircle;
import dungeonmania.dungeon.*;

public class Spider extends Enemy {
    
    /**
     * Creates a new instance of a spider
     * 
     * @param dungeon - dungeon in which the spider spawns and moves around
     * @param entityInfo - entityInfo object
     */
    public Spider(Dungeon dungeon, EntityInfo entityInfo) {
        super(dungeon, entityInfo, DungeonConstants.SPIDER_ATTACK_DAMAGE, 
                new MoveCircle(dungeon, entityInfo.getPosition(), true));
    }

}
