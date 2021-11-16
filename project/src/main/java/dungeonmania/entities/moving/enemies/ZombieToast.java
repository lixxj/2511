package dungeonmania.entities.moving.enemies;

import dungeonmania.entities.*;
import dungeonmania.entities.buildables.Protection;
import dungeonmania.entities.moving.Character;
import dungeonmania.entities.moving.MoveRandom;
import dungeonmania.dungeon.*;

public class ZombieToast extends Enemy {
    private Protection protection;
    /**
     * Creates a new instance of a zombie toast
     * 
     * @param dungeon - dungeon in which the zombie toast spawns and moves around
     * @param entityInfo - entityInfo object
     */
    public ZombieToast(Dungeon dungeon, EntityInfo entityInfo) {
        super(dungeon, entityInfo, DungeonConstants.ZOMBIE_ATTACK_DAMAGE, 
                new MoveRandom(dungeon, entityInfo.getPosition()));
    }

    public void setProtection(Protection protection) {
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
