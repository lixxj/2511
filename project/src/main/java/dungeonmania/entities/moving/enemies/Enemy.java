package dungeonmania.entities.moving.enemies;

import dungeonmania.entities.moving.Character;
import dungeonmania.entities.moving.*;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.dungeon.Dungeon;

import java.util.Objects;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.entities.EntityInfo;
import dungeonmania.util.Position;

public abstract class Enemy extends Character {
    
    private MovementBehaviour defaultMovementBehaviour;
    private MovementBehaviour currentMovementBehaviour;
    private Dungeon dungeon;


    /**
     * Constructor used in creation of enemy
     * 
     * @param dungeon - current dungeon that enemy is in
     * @param entityInfo - entityInfo object
     * @param attackDamage - attack damage of enemy
     * @param defaultMovementStrategy - default movement strategy of enemy 
     *                                  (see MovementStrategy interface)
     */
    public Enemy(Dungeon dungeon, EntityInfo entityInfo, int attackDamage, MovementBehaviour defaultMovementStrategy) {
        super(entityInfo, DungeonConstants.DEFAULT_HEALTH, attackDamage);
        this.defaultMovementBehaviour = defaultMovementStrategy;
        this.currentMovementBehaviour = defaultMovementStrategy;
        this.dungeon = dungeon;
    }

    /**
     * Simulates movement for enemy in 1 tick
     */
    public void move() {
        Position newPosition = currentMovementBehaviour.move();

        // Don't move allies on to the same cell as the player
        if (this instanceof Mercenary && ((Mercenary)this).isAlly()) {
            if (Objects.equals(newPosition, dungeon.getPlayerPosition())) return;
        }
        
        this.setPosition(newPosition);
    }
    
    /**
     * Edits the current movement strategy of the instantiated enemy
     * @param newBehaviour - new movement strategy
     */
    public void setMovableBehaviour(MovementBehaviour newBehaviour) {
        this.currentMovementBehaviour = newBehaviour;
    }

    /**
     * Resets the movement strategy of the enemy to the default strategy
     */
    public void initialiseMovementBehaviour() {
        this.currentMovementBehaviour = this.defaultMovementBehaviour;
        this.currentMovementBehaviour.setCurrentPosition(this.getPosition());
    }

    public void update(Player player) {
        if (player.isInvincible()) {
            setMovableBehaviour(new MoveAway(dungeon, this.getPosition()));
        } else if (player.isInvisible() && this instanceof Mercenary) {
            setMovableBehaviour(new MoveRandom(dungeon, this.getPosition()));
        }else {
            initialiseMovementBehaviour();
        }
    }
    /**
     * Gets the position of the player in the same dungeon as the enemy.
     * @return player position
     */
    public Position getPlayerPosition() {
        return this.dungeon.getPlayerPosition();
    }


    //decrease enemy health
    public void decreaseEnemyHealth(Character character, int totalAttackDamage) {
        setHealth(getHealth() - ((character.getHealth() * totalAttackDamage) / 5));
    }

    public void attackPlayer(){
        this.dungeon.getPlayer().decreasePlayerHealth(this, this.getAttackDamage());
    }

    // GETTER - current dungeon
    public Dungeon getDungeon() {
        return this.dungeon;
    }

}