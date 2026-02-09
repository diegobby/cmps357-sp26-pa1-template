import java.util.ArrayList;
import java.util.List;

/**
 * Stage 4 Demo: Demonstrates shopping cart aggregation features.
 *
 * <p>This demo showcases:
 * - Aggregating ingredients from multiple recipes
 * - Case-insensitive ingredient name normalization
 * - Amount summation with proper formatting
 * - Sorted presentation of aggregated ingredients
 */
public class Stage4Demo {
    public static void main(String[] args) {
        System.out.println("=== Stage 4 Demo: Shopping Cart Aggregation ===\n");

        // Create a recipe book with sample recipes
        RecipeBook recipeBook = new RecipeBook();

        // Recipe 1: Pasta Aglio e Olio
        Recipe pasta = new Recipe("Pasta Aglio e Olio", 2);
        pasta.addIngredient("spaghetti", 200);
        pasta.addIngredient("garlic", 3);
        pasta.addIngredient("olive oil", 0.25);
        pasta.addIngredient("red pepper flakes", 0.5);
        recipeBook.addRecipe(pasta);

        // Recipe 2: Garlic Bread
        Recipe garlicBread = new Recipe("Garlic Bread", 4);
        garlicBread.addIngredient("bread", 1);
        garlicBread.addIngredient("garlic", 2);
        garlicBread.addIngredient("butter", 0.5);
        recipeBook.addRecipe(garlicBread);

        // Recipe 3: Garlic Soup (with capitalized ingredient names)
        Recipe garlicSoup = new Recipe("Garlic Soup", 4);
        garlicSoup.addIngredient("Garlic", 4);  // Note: capitalized
        garlicSoup.addIngredient("Chicken Broth", 4);
        garlicSoup.addIngredient("cream", 0.5);
        garlicSoup.addIngredient("Olive Oil", 0.125);  // Note: different case
        recipeBook.addRecipe(garlicSoup);

        System.out.println("Recipes in cookbook:");
        for (Recipe r : recipeBook.getAllRecipes()) {
            System.out.println(r);
            System.out.println();
        }

        // --- Test 1: Single Recipe Shopping Cart ---
        System.out.println("--- Test 1: Shopping Cart from Single Recipe ---");
        List<Recipe> singleRecipe = new ArrayList<>();
        singleRecipe.add(pasta);
        
        ShoppingCart singleCart = new ShoppingCart(singleRecipe);
        System.out.println("Shopping cart for '" + pasta + "':");
        System.out.println(singleCart);

        // --- Test 2: Two Recipes Shopping Cart ---
        System.out.println("--- Test 2: Shopping Cart from Two Recipes ---");
        List<Recipe> twoRecipes = new ArrayList<>();
        twoRecipes.add(pasta);
        twoRecipes.add(garlicBread);
        
        ShoppingCart twoCart = new ShoppingCart(twoRecipes);
        System.out.println("Shopping cart for '" + pasta + "' + '" + garlicBread + "':");
        System.out.println("Total unique ingredients: " + twoCart.size());
        System.out.println(twoCart);

        // --- Test 3: All Three Recipes with Case-Insensitive Aggregation ---
        System.out.println("--- Test 3: Shopping Cart from All Recipes (Case-Insensitive) ---");
        List<Recipe> allRecipes = recipeBook.getAllRecipes();
        
        ShoppingCart allCart = new ShoppingCart(allRecipes);
        System.out.println("Shopping cart for all recipes:");
        System.out.println("Total unique ingredients: " + allCart.size());
        System.out.println("Ingredients (sorted alphabetically):");
        System.out.println(allCart);

        // --- Test 4: Demonstrate Case-Insensitive Lookup ---
        System.out.println("--- Test 4: Case-Insensitive Ingredient Lookup ---");
        System.out.println("Amount of 'garlic' (lowercase): " + allCart.getAmount("garlic"));
        System.out.println("Amount of 'GARLIC' (uppercase): " + allCart.getAmount("GARLIC"));
        System.out.println("Amount of 'Garlic' (mixed case): " + allCart.getAmount("Garlic"));
        System.out.println();

        // --- Test 5: Verify Non-Destructiveness ---
        System.out.println("--- Test 5: Verify Recipes Remain Unchanged ---");
        System.out.println("Original Pasta recipe after cart creation:");
        System.out.println(pasta);
        System.out.println();

        System.out.println("=== Stage 4 Demo Complete ===");
    }
}
