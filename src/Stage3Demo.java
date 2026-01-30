import java.util.List;

/**
 * Stage 3 Demo: Demonstrates advanced searching and sorting features.
 *
 * <p>This demo showcases:
 * - Case-insensitive name search (partial matches)
 * - Ingredient-based search
 * - Multi-token search (all tokens must match)
 * - Stable sorting with secondary sort keys
 */
public class Stage3Demo {
    public static void main(String[] args) {
        System.out.println("=== Stage 3 Demo: Advanced Searching and Sorting ===\n");

        // Create a recipe book with sample recipes
        RecipeBook recipeBook = new RecipeBook();

        // Recipe 1: Garlic Bread
        Recipe garlicBread = new Recipe("Garlic Bread", 4);
        garlicBread.addIngredient("garlic", 3);
        garlicBread.addIngredient("butter", 0.5);
        garlicBread.addIngredient("bread", 1);
        recipeBook.addRecipe(garlicBread);

        // Recipe 2: garlic oil (different case)
        Recipe garlicOil = new Recipe("garlic oil", 2);
        garlicOil.addIngredient("garlic", 4);
        garlicOil.addIngredient("olive oil", 1);
        recipeBook.addRecipe(garlicOil);

        // Recipe 3: Pasta Carbonara
        Recipe pastaCarbonara = new Recipe("Pasta Carbonara", 4);
        pastaCarbonara.addIngredient("pasta", 1);
        pastaCarbonara.addIngredient("eggs", 4);
        pastaCarbonara.addIngredient("bacon", 0.5);
        pastaCarbonara.addIngredient("garlic", 1);
        recipeBook.addRecipe(pastaCarbonara);

        // Recipe 4: Olive Tapenade
        Recipe oliveTapenade = new Recipe("Olive Tapenade", 6);
        oliveTapenade.addIngredient("olives", 2);
        oliveTapenade.addIngredient("garlic", 2);
        oliveTapenade.addIngredient("olive oil", 0.5);
        recipeBook.addRecipe(oliveTapenade);

        System.out.println("All Recipes:");
        for (Recipe r : recipeBook.getAllRecipes()) {
            System.out.println("  - " + r);
        }
        System.out.println();

        // --- Test 1: Case-Insensitive Name Search ---
        System.out.println("--- Test 1: Case-Insensitive Name Search ---");
        List<Recipe> garlic = recipeBook.searchByName("garlic");
        System.out.println("Search for 'garlic': Found " + garlic.size() + " recipes");
        for (Recipe r : garlic) {
            System.out.println("  - " + r);
        }
        System.out.println();

        // --- Test 2: Ingredient-Based Search ---
        System.out.println("--- Test 2: Ingredient-Based Search ---");
        List<Recipe> garlicRecipes = recipeBook.searchByIngredient("garlic");
        System.out.println("Recipes with 'garlic' ingredient: Found " + garlicRecipes.size() + " recipes");
        for (Recipe r : garlicRecipes) {
            System.out.println("  - " + r);
        }
        System.out.println();

        // --- Test 3: Multi-Token Search ---
        System.out.println("--- Test 3: Multi-Token Search ---");
        List<Recipe> garlicOilRecipes = recipeBook.searchMultiToken("garlic oil");
        System.out.println("Multi-token search for 'garlic oil': Found " + garlicOilRecipes.size() + " recipes");
        for (Recipe r : garlicOilRecipes) {
            System.out.println("  - " + r);
        }
        System.out.println();

        // --- Test 4: Multi-Token Search with Recipe Name + Ingredient ---
        System.out.println("--- Test 4: Multi-Token Search (name + ingredient) ---");
        List<Recipe> pastaGarlic = recipeBook.searchMultiToken("pasta garlic");
        System.out.println("Multi-token search for 'pasta garlic': Found " + pastaGarlic.size() + " recipes");
        for (Recipe r : pastaGarlic) {
            System.out.println("  - " + r);
        }
        System.out.println();

        // --- Test 5: Sorting with Stable Secondary Key ---
        System.out.println("--- Test 5: Sorting with Stable Secondary Key ---");
        List<Recipe> all = recipeBook.getAllRecipes();
        List<Recipe> sorted = RecipeSorter.sortByName(all);
        System.out.println("Sorted recipes (case-insensitive, with secondary case-sensitive tiebreaker):");
        for (Recipe r : sorted) {
            System.out.println("  - " + r);
        }
        System.out.println();

        // --- Test 6: Edge Cases ---
        System.out.println("--- Test 6: Edge Cases ---");
        List<Recipe> emptySearch = recipeBook.searchByName("");
        System.out.println("Search for empty string: Found " + emptySearch.size() + " recipes");

        List<Recipe> noMatch = recipeBook.searchByIngredient("xyz");
        System.out.println("Search for non-existent ingredient 'xyz': Found " + noMatch.size() + " recipes");

        List<Recipe> multiTokenNoMatch = recipeBook.searchMultiToken("xyz abc");
        System.out.println("Multi-token search for 'xyz abc': Found " + multiTokenNoMatch.size() + " recipes");
        System.out.println();

        System.out.println("=== Stage 3 Demo Complete ===");
    }
}
