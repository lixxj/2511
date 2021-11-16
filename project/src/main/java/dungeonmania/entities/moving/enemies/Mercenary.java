
package dungeonmania.entities.moving.enemies;

import dungeonmania.entities.*;
import dungeonmania.entities.buildables.Protection;
import dungeonmania.entities.moving.Character;
import dungeonmania.entities.moving.MoveTowards;
import dungeonmania.dungeon.*;
import dungeonmania.util.Position;
import org.json.JSONObject;

public class Mercenary extends Enemy {

    private boolean ally;
    private int enchanted_expiry;
    private Protection protection;

    /**
     * Alternate constructor. Creates a new instance of a mercenary with type.
     *
     * @param dungeon - dungeon in which the mercenary spawns and moves around
     * @param entityInfo - entityInfo object
     */
    public Mercenary(Dungeon dungeon, EntityInfo entityInfo, int attackDamage, boolean ally) {
        super(dungeon, entityInfo, attackDamage, new MoveTowards(dungeon, entityInfo.getPosition()));
        this.ally = ally;
    }

    /**
     * Alternate constructor. Creates a new instance of a mercenary with type.
     * 
     * @param dungeon - dungeon in which the mercenary spawns and moves around
     * @param entityInfo - entityInfo object
     */
    public Mercenary(Dungeon dungeon, EntityInfo entityInfo, int attackDamage) {
        super(dungeon, entityInfo, attackDamage, new MoveTowards(dungeon, entityInfo.getPosition()));
        this.ally = false;
        this.enchanted_expiry = -1;
    }

    /**
     * Creates a new instance of a mercenary. Gives mercenary default attack damage.
     * 
     * @param dungeon - dungeon in which the mercenary spawns and moves around
     * @param entityInfo - entityInfo object
     */
    public Mercenary(Dungeon dungeon, EntityInfo entityInfo) {
        this(dungeon, entityInfo, DungeonConstants.MERCENARY_ATTACK_DAMAGE);
    }

    /**
     * Checks if the mercenary is in the battle radius of the player in the same dungeon.
     * @return true if in battle radius, false otherwise.
     */
    public boolean inBattleRadius() {
        Position currentPosition = this.getPosition();
        Position playerPosition = super.getPlayerPosition();
        double distanceFromPlayer = Math.sqrt(Math.pow((currentPosition.getX() - playerPosition.getX()), 2) + 
                                    Math.pow((currentPosition.getY() - playerPosition.getY()), 2));
        return distanceFromPlayer < DungeonConstants.MERCENARY_BATTLE_RADIUS;
    }

    /**
     * Bribes a mercenary forever. Dungeon ticks are assumed to be within integer max bounds.
     */
    public void bribe() {
        bribe(Integer.MAX_VALUE);
    }

    public void bribe(int time) {
        this.enchanted_expiry = (time == Integer.MAX_VALUE) ? time : getDungeon().getCurrentTick() + time;
        this.ally = true;
    }

    public boolean isAlly() {
        if (getDungeon().getCurrentTick() > this.enchanted_expiry) {
            this.ally = false;
        }
        return this.ally;
    }

    public int getBribeAmount() {
        return DungeonConstants.MERCENARY_BRIBE_AMOUNT;
    }
    
    public void attackEnemyAsAlly(Enemy enemy){
        enemy.decreaseEnemyHealth(this, getAttackDamage());
    }

    @Override
    public JSONObject getEntityStateAsJSON() {
        JSONObject entityObject = super.getEntityStateAsJSON();
        entityObject.put("ally", ally);
        return entityObject;
    }

    public void setProtection(Protection protection){
        this.protection = protection;
    }

    public boolean hasProtection(){
        return protection!= null;
    }

    @Override
    public void decreaseEnemyHealth(Character character, int totalAttackDamage) {
        if (!hasProtection()) {
            //int totalAttackDamage = player.getAttackDamage() + attackDamage;
            this.setHealth(getHealth() - ((character.getHealth() * totalAttackDamage) / 10));
        } else {
            this.setHealth(getHealth() - (character.getHealth() * totalAttackDamage) / (int)(10 * this.protection.getProtectionFactor()));
            protection.decreaseDurability();
            if(protection.getDurability() <= 0){
                this.protection = null;
            }
        }
    }
}
