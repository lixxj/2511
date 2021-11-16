package dungeonmania.entities.moving.enemies;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.entities.DungeonConstants;
import dungeonmania.entities.moving.MoveRandom;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.collectables.Anduril;
import dungeonmania.entities.moving.Character;

public class Hydra extends Enemy {
    /**
     * Creates a new instance of a zombie toast
     * 
     * @param dungeon - dungeon in which the zombie toast spawns and moves around
     * @param hydraId - unique entity id
     * @param position - spawning position of zombie toast
     */
    public Hydra(Dungeon dungeon, EntityInfo entityInfo) {
        super(dungeon, entityInfo, DungeonConstants.HYDRA_ATTACK_DAMAGE, 
                new MoveRandom(dungeon, entityInfo.getPosition()));
    }

    @Override
    public void decreaseEnemyHealth(Character character, int totalAttackDamage) {
        if(character instanceof Player && ((Player)character).getWeapon() instanceof Anduril){
            setHealth(getHealth() - ((character.getHealth() * totalAttackDamage) / 5));
            return;
        }
        if(willHydraHealthIncrease()){
            setHealth(getHealth() + ((character.getHealth() * totalAttackDamage) / 5));
        }else{
            setHealth(getHealth() - ((character.getHealth() * totalAttackDamage) / 5));
        }
    }


    private boolean willHydraHealthIncrease(){
        if(Math.random() < 0.5) {
            return true;
        }
        return false;
    }

}
