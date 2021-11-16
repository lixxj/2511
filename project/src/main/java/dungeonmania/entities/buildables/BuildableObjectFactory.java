package dungeonmania.entities.buildables;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.*;
import dungeonmania.entities.collectables.Weapon;
import dungeonmania.entities.moving.player.*;
import dungeonmania.exceptions.InvalidActionException;

public class BuildableObjectFactory {
    
    public  static Entity build(String type, Player player){
        if (!canBuild(type, player.getPlayerInventory())) return null;
        
        switch (type) {
            case DungeonConstants.ENTITY_BOW:
                makeRecipe(getBowRecipes().stream().filter(x -> checkRecipe(x,player.getPlayerInventory())).findFirst().get(), player.getPlayerInventory());
                String bowName = DungeonConstants.ENTITY_BOW + player.getPlayerInventory().countItemsByType(type);
                Entity newBow = new Bow(new EntityInfo(bowName, player.getPosition(), DungeonConstants.ENTITY_BOW, false));
                player.getPlayerInventory().add(newBow);
                player.setWeapon((Weapon)newBow);
                return newBow;
            case DungeonConstants.ENTITY_SHIELD:
                makeRecipe(getShieldRecipes().stream().filter(x -> checkRecipe(x,player.getPlayerInventory())).findFirst().get(), player.getPlayerInventory());
                String shieldName = DungeonConstants.ENTITY_SHIELD + player.getPlayerInventory().countItemsByType(type);
                Entity newShield = new Shield(new EntityInfo(shieldName, player.getPosition(), DungeonConstants.ENTITY_SHIELD, false));
                player.getPlayerInventory().add(newShield);
                player.setProtection((Protection)newShield);
                return newShield;
            case DungeonConstants.ENTITY_MIDNIGHT_ARMOUR:
                makeRecipe(getMidnightArmourRecipes().stream().filter(x -> checkRecipe(x,player.getPlayerInventory())).findFirst().get(), player.getPlayerInventory());
                String armourName = DungeonConstants.ENTITY_MIDNIGHT_ARMOUR + player.getPlayerInventory().countItemsByType(type);
                Entity newMidightArmour = new MidnightArmour(new EntityInfo(armourName, player.getPosition(), DungeonConstants.ENTITY_MIDNIGHT_ARMOUR, false));
                player.getPlayerInventory().add(newMidightArmour);
                player.setProtection((Protection)newMidightArmour);
                player.setWeapon((Weapon)newMidightArmour);
                return newMidightArmour;
            case DungeonConstants.ENTITY_SCEPTRE:
                makeRecipe(getSceptreRecipes().stream().filter(x -> checkRecipe(x,player.getPlayerInventory())).findFirst().get(), player.getPlayerInventory());
                String sceptreName = DungeonConstants.ENTITY_SCEPTRE + player.getPlayerInventory().countItemsByType(type);
                Entity newSceptre = new Sceptre(new EntityInfo(sceptreName, player.getPosition(), DungeonConstants.ENTITY_SCEPTRE, false));
                player.getPlayerInventory().add(newSceptre);
                return newSceptre;
            default:
                return null;
        }
    }


    /**
     * Checks if an item of specified type can be made using the current playerInventory
     * 
     * @param type - type of entity (constant that must be provided in DungeonConstants file)
     * @return true if item can be made, false otherwise
     * @throws IllegalArgumentException - if item type is not a buildable entity (see spec.)
     * @throws InvalidActionException - if player does not have sufficient items to craft the buildable
     */
    private static boolean canBuild(String type, Inventory inventory) throws IllegalArgumentException, InvalidActionException {
        boolean resp = false;

        switch (type) {
            case DungeonConstants.ENTITY_BOW:
                resp = getBowRecipes().stream().anyMatch(x -> checkRecipe(x,inventory));
                if (!resp) throw new InvalidActionException("Player does not have sufficient items to craft a Bow");
                break;
            case DungeonConstants.ENTITY_SHIELD:
                resp = getShieldRecipes().stream().anyMatch(x -> checkRecipe(x,inventory));
                if (!resp) throw new InvalidActionException("Player does not have sufficient items to craft a Shield");
                break;
            case DungeonConstants.ENTITY_MIDNIGHT_ARMOUR:
                resp = getMidnightArmourRecipes().stream().anyMatch(x -> checkRecipe(x,inventory));
                if (!resp) throw new InvalidActionException("Player does not have sufficient items to craft a Midnight armour");
                break;
            case DungeonConstants.ENTITY_SCEPTRE:
            resp = getSceptreRecipes().stream().anyMatch(x -> checkRecipe(x,inventory));
            if (!resp) throw new InvalidActionException("Player does not have sufficient items to craft a sceptre");
            break;
        }

        if (!resp) throw new IllegalArgumentException("Buildable item type is invalid");
        return resp;
    }
    
    /**
     * Checks if the specified recipe can be made using the current inventory
     * 
     * @param recipe - recipe that must be checked
     * @return true if recipe can be made, false otherwise
     */
    private static boolean checkRecipe(Recipe recipe, Inventory inventory) {
        return recipe.checkRecipeSatisfied(inventory);
    }

    /**
     * Makes the specified recipe
     * 
     * @pre recipe can be made (checkRecipe is called before)
     * @param recipe - recipe that must be made
     */
    private static void makeRecipe(Recipe recipe, Inventory inventory) {
        recipe.make(inventory);
    }

    public static List<String> getBuildables(Inventory inventory) {
        List<String> buildables = new ArrayList<>();

        if (getBowRecipes().stream().anyMatch(x-> checkRecipe(x,inventory))) {
            buildables.add(DungeonConstants.ENTITY_BOW);
        }

        if (getShieldRecipes().stream().anyMatch(x -> checkRecipe(x,inventory))) {
            buildables.add(DungeonConstants.ENTITY_SHIELD);
        }

        if (getMidnightArmourRecipes().stream().anyMatch(x -> checkRecipe(x,inventory))) {
            buildables.add(DungeonConstants.ENTITY_MIDNIGHT_ARMOUR);
        }

        if (getSceptreRecipes().stream().anyMatch(x -> checkRecipe(x,inventory))) {
            buildables.add(DungeonConstants.ENTITY_SCEPTRE);
        }

        return buildables;
    }

    private static List<Recipe> getBowRecipes(){
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe1 = new Recipe();
        recipe1.add(DungeonConstants.ENTITY_WOOD, 1);
        recipe1.add(DungeonConstants.ENTITY_ARROW, 3);
        recipes.add(recipe1);
        return recipes;
     
    }

    private static List<Recipe> getShieldRecipes(){
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe1 = new Recipe();
        recipe1.add(DungeonConstants.ENTITY_WOOD, 2);
        recipe1.add(DungeonConstants.ENTITY_TREASURE, 1);
        recipes.add(recipe1); 

        Recipe recipe2 = new Recipe();
        recipe2.add(DungeonConstants.ENTITY_WOOD, 2);
        recipe2.add(DungeonConstants.ENTITY_KEY, 1);
        recipes.add(recipe2);
        return recipes;
    }

    private static List<Recipe> getMidnightArmourRecipes(){
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe1 = new Recipe();
        recipe1.add(DungeonConstants.ENTITY_ARMOUR, 1);
        recipe1.add(DungeonConstants.ENTITY_SUN_STONE, 1);
        recipes.add(recipe1);
        return recipes; 
    }

    private static List<Recipe> getSceptreRecipes(){
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe1 = new Recipe();
        recipe1.add(DungeonConstants.ENTITY_WOOD, 1);
        recipe1.add(DungeonConstants.ENTITY_KEY, 1);
        recipe1.add(DungeonConstants.ENTITY_SUN_STONE, 1);
        recipes.add(recipe1);

        Recipe recipe2 = new Recipe();
        recipe2.add(DungeonConstants.ENTITY_WOOD, 1);
        recipe2.add(DungeonConstants.ENTITY_TREASURE, 1);
        recipe2.add(DungeonConstants.ENTITY_SUN_STONE, 1);
        recipes.add(recipe2);

        Recipe recipe3 = new Recipe();
        recipe3.add(DungeonConstants.ENTITY_ARROW, 2);
        recipe3.add(DungeonConstants.ENTITY_TREASURE, 1);
        recipe3.add(DungeonConstants.ENTITY_SUN_STONE, 1);
        recipes.add(recipe3);

        Recipe recipe4 = new Recipe();
        recipe4.add(DungeonConstants.ENTITY_ARROW, 2);
        recipe4.add(DungeonConstants.ENTITY_KEY, 1);
        recipe4.add(DungeonConstants.ENTITY_SUN_STONE, 1);
        recipes.add(recipe4); 
        return recipes;
    }
}
