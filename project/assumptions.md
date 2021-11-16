# Assumptions
- Dungeons are assumed to have coordinates spanning from x in {0, width - 1} and y in {0, height - 1}
- If dungeon JSON object does not specify a height and width, the default height and width is 50 x 50
- Minimum dungeon size is 3 x 3 and entry position is always (1, 1)

### Player
- Default starting health = 100 
- Default attack damage = 3
- Default starting health (Hard) = 80 
- Default attack damage (Hard) = 2
- If player holds armour, then the player equips armour
- The moment a craftable object is crafted, it is added to the player's inventory

### Battles
- In peaceful mode, the character will still attack enemies, but they will not attack as stated in the spec.
- Player attacks Enemy first in a battle
- If multiple enemies are on the same spot with the player, only one battle occurs on the given box.
- In a battle, Player is able to attack with no weapons(fists), but that deals significantly less damage to enemy.

### Enemies
- Default starting health = 100 
- If multiple enemies are on the same spot with a player, only one battle occurs with an enemy selected at random.

### Zombie Toast and Spawner
- Default attack damage = 5
- 30% chance that zombie toasts are spawned with armour.

### Hydra
- Default attack damage = 15
- Can only spawn if the entry location (or any one of its adjacent positions) is empty 
- Cannot have protection (cannot have armour)
- Health can go over 100 as there is a 50% chance that it spawns with two heads when attacked.

### Spider
- Default attack damage = 2
- Once a spider enters a portal, the other portal's spawn point becomes its new spawn point and the spider starts circling the spawn point.
- Spiders are able to spawn every where, even on static objects (walls, doors, boulders, etc...).

### Mercenary
- Default attack damage = 6
- Moves randomly if player consumes invisibility potion
- Spawns every 21 ticks
- Can only spawn if the entry location (or any one of its adjacent positions) is empty 
- If mercenary is an ally, it does not move on to the player's cell. However, the player can move onto the ally's cell.
- Ally mercenaries are not accounted for when checking for completion in the enemies goal.
- 20% chance that mercenaries are spawned with armour.

### Other Entities

#### Doors and Keys
- Once a door is opened, it remains open for the rest of the game
- If a key is melted, its corresponding door can never be opened (may mean that the game cannot be completed and user has to restart level)

#### Swamp Tile
- Enemies stay on the swamp tile for (movement_factor - 1) ticks

#### Boulders
- Boulders cannot spawn on floor switches

#### Portals
- There can only be two portals in a game of the same colour
- All adjacent squares near a portal are empty

#### Potions
- Invincibility potions last for 5 ticks. Invisibility lasts for the entire game.
- Invisibility potion is prioritised over invincibility potion (if both are consumed).

#### Bomb
- When a bomb explodes, it destroys any entity that is not an enemy.
- The blast radius for the bomb is 2 square, so every adjacent square around the adjacent squares to the square in which the bomb was placed.
- Bomb explodes instantly if placed next to a pre-triggered switch.
- Once placed, it cannot be picked up again.
- Bombs can trigger other bombs to explode. i.e. if a bomb explodes and there is a bomb adjacent to it, the other bomb also explodes.

#### Weapons
- The durability of weapons/craftables is a constant upon creation 
    - Armour and Sword have a durability of X uses
    - Midnight Armour has a durability of X uses
    - Shield has a durability of X uses
    - Bow has a durability of X uses
    - Anduril has a durability of X uses
- The attack damage of weapons/craftables is a constant upon creation. Note that this attack damage is added to the existing attack damage of a player.
    - Armour and Sword and Anduril have an attack damage of X hits per use
    - Midnight Armour has an attack damage of X hits per use
    - Shield has an attack damage of X hits per use
    - Bow has an attack damage of X hits per use
- Anduril has the same durability as a regular sword but triple the attack damage ONLY for bosses.

#### Buildable 
- The moment a crafteable object is crafted, it is added to the player’s inventory.
- If an equipable object is crafted (i.e. weapon or protection), it is instantly equipped.

#### One Ring
- If the player does not hold a one ring and dies, it does not matter if they manage to kill the enemy and win a one ring. They're dead regardless.

### Sceptre
- Just like bribing, using the sceptre to control minds can only be done two tiles away

### Midnight Armour
- Equiping MN armour will replace existing weapon and protection. However, if a weapon is picked up after, the midnight armour is only used for its protective powers.

### SunStone
- When building an item that requires the sunStone, the sunstone is consumed in the building.
