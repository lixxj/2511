package dungeonmania.entities.moving;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityInfo;

public abstract class Character extends Entity {
    
    private int health;
    private int attackDamage;

    /**
     * Constructor for a character object
     * 
     * @pre startingHealth <= 100
     * @pre defaultAttackDamage <= 100
     * @param entityInfo - entityInfo object
     * @param startingHealth - starting health of the character
     * @param defaultAttackDamage - default attack damage of the character
     */
    public Character(EntityInfo entityInfo, int startingHealth, int defaultAttackDamage) {
        super(entityInfo);
        this.health = startingHealth;
        this.attackDamage = defaultAttackDamage;
    }

    /**
     * Additional constructor for a character object. Sets health to default and attack damage to 0.
     * Caller is recommended to use the setter method to change the attack damage as required.
     * 
     * @param entityInfo - entityInfo object
     */
    public Character(EntityInfo entityInfo) {
        this(entityInfo, DungeonConstants.DEFAULT_HEALTH, 0);
    }

    /**
     * @return current health of the character
     */
    public int getHealth() {
        return this.health;
    }
    
    /**
     * Sets the health of the character to the specified value
     * @param health - health that the character is 
     */
    public void setHealth(int health) {
        this.health = health;
    }

    // public void damage(int enemyHealth, int enemyAttackDamage) {
    //     this.health -= (enemyHealth * enemyAttackDamage) / 10;
    // }

    /**
     * @return current attack damage of the character
     */
    public int getAttackDamage() {
        return this.attackDamage;
    }

    /**
     * Sets the attack damage of the character to the specified value
     * @param attackDamage
     */
    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Checks if the character is alive.
     * @return true if health > 0, false otherwise
     */
    public boolean isAlive() {
        return this.health > 0;
    }
}
