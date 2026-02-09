import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Stage 6 Demo: Demonstrates the complete Recipe Manager application.
 *
 * <p>This demo showcases:
 * - Creating and managing recipes
 * - Searching recipes (by name, ingredient, multi-token)
 * - Viewing recipe details
 * - Building shopping carts
 * - Saving and loading recipes from JSON files
 * - Complete workflow examples
 */
public class Stage6Demo {
    public static void main(String[] args) {
        System.out.println("=== Stage 6 Demo: Recipe Manager Application ===\n");

        String demoFile = "demo_recipes.json";

        // --- Test 1: Create a recipe collection ---
        System.out.println("--- Test 1: Creating Recipe Collection ---");
        RecipeBook book = createSampleRecipes();
        System.out.println("✓ Created " + book.size() + " recipes\n");

        // --- Test 2: List all recipes ---
        System.out.println("--- Test 2: List All Recipes (Sorted) ---");
        listAllRecipes(book);

        // --- Test 3: Search by name ---
        System.out.println("--- Test 3: Search by Recipe Name ---");
        demonstrateNameSearch(book, "garlic");

        // --- Test 4: Search by ingredient ---
        System.out.println("--- Test 4: Search by Ingredient ---");
        demonstrateIngredientSearch(book, "garlic");

        // --- Test 5: Multi-token search ---
        System.out.println("--- Test 5: Multi-Token Search ---");
        demonstrateMultiTokenSearch(book, "garlic bread");

        // --- Test 6: View recipe details ---
        System.out.println("--- Test 6: View Recipe Details ---");
        demonstrateViewDetails(book, "Carbonara");

        // --- Test 7: Build shopping cart ---
        System.out.println("--- Test 7: Build Shopping Cart ---");
        demonstrateShoppingCart(book);

        // --- Test 8: Save recipes ---
        System.out.println("--- Test 8: Save Recipes to File ---");
        try {
            RecipeJsonManager.saveToFile(book, demoFile);
            System.out.println("✓ Successfully saved " + book.size() + " recipes to '" + demoFile + "'\n");
        } catch (IOException e) {
            System.err.println("✗ Error: " + e.getMessage());
            return;
        }

        // --- Test 9: Load recipes ---
        System.out.println("--- Test 9: Load Recipes from File ---");
        try {
            RecipeBook loaded = RecipeJsonManager.loadFromFile(demoFile);
            System.out.println("✓ Successfully loaded " + loaded.size() + " recipes from '" + demoFile + "'\n");

            // Verify loaded data
            System.out.println("Loaded recipes:");
            listAllRecipes(loaded);

        } catch (IOException e) {
            System.err.println("✗ Error: " + e.getMessage());
            return;
        }

        // --- Test 10: Complete workflow ---
        System.out.println("--- Test 10: Complete Workflow Example ---");
        demonstrateCompleteWorkflow(book);

        // Cleanup
        try {
            Files.deleteIfExists(Paths.get(demoFile));
        } catch (IOException e) {
            // Ignore
        }

        System.out.println("=== Stage 6 Demo Complete ===\n");
    }

    private static RecipeBook createSampleRecipes() {
        RecipeBook book = new RecipeBook();

        // Recipe 1: Pasta Carbonara
        Recipe carbonara = new Recipe("Pasta Carbonara", 4);
        carbonara.addIngredient("spaghetti", 400);
        carbonara.addIngredient("eggs", 4);
        carbonara.addIngredient("bacon", 200);
        carbonara.addIngredient("parmesan", 100);
        book.addRecipe(carbonara);

        // Recipe 2: Garlic Bread
        Recipe garlicBread = new Recipe("Garlic Bread", 4);
        garlicBread.addIngredient("bread", 1);
        garlicBread.addIngredient("garlic cloves", 4);
        garlicBread.addIngredient("butter", 0.25);
        garlicBread.addIngredient("parsley", 0.1);
        book.addRecipe(garlicBread);

        // Recipe 3: Caesar Salad
        Recipe salad = new Recipe("Caesar Salad", 2);
        salad.addIngredient("lettuce", 200);
        salad.addIngredient("croutons", 100);
        salad.addIngredient("parmesan", 50);
        salad.addIngredient("caesar dressing", 100);
        book.addRecipe(salad);

        // Recipe 4: Garlic Soup
        Recipe soup = new Recipe("Garlic Soup", 6);
        soup.addIngredient("garlic cloves", 8);
        soup.addIngredient("vegetable broth", 1000);
        soup.addIngredient("cream", 200);
        soup.addIngredient("olive oil", 0.25);
        book.addRecipe(soup);

        return book;
    }

    private static void listAllRecipes(RecipeBook book) {
        List<Recipe> all = book.getAllRecipes();
        List<Recipe> sorted = RecipeSorter.sortByName(all);

        System.out.println("All Recipes (" + sorted.size() + "):");
        for (int i = 0; i < sorted.size(); i++) {
            String name = getRecipeName(sorted.get(i));
            int servings = getRecipeServings(sorted.get(i));
            System.out.printf("  %d. %s (%d servings)\n", i + 1, name, servings);
        }
        System.out.println();
    }

    private static void demonstrateNameSearch(RecipeBook book, String query) {
        List<Recipe> results = book.searchByName(query);
        System.out.printf("Search by name '%s': Found %d recipe(s)\n", query, results.size());
        for (Recipe r : results) {
            System.out.println("  - " + getRecipeName(r));
        }
        System.out.println();
    }

    private static void demonstrateIngredientSearch(RecipeBook book, String query) {
        List<Recipe> results = book.searchByIngredient(query);
        System.out.printf("Search by ingredient '%s': Found %d recipe(s)\n", query, results.size());
        for (Recipe r : results) {
            System.out.println("  - " + getRecipeName(r));
        }
        System.out.println();
    }

    private static void demonstrateMultiTokenSearch(RecipeBook book, String query) {
        List<Recipe> results = book.searchMultiToken(query);
        System.out.printf("Multi-token search '%s': Found %d recipe(s)\n", query, results.size());
        for (Recipe r : results) {
            System.out.println("  - " + getRecipeName(r));
        }
        System.out.println();
    }

    private static void demonstrateViewDetails(RecipeBook book, String query) {
        List<Recipe> results = book.searchByName(query);
        if (results.isEmpty()) {
            System.out.println("No recipe found.\n");
            return;
        }

        Recipe recipe = results.get(0);
        System.out.println("Recipe Details:");
        System.out.println(recipe.toString());
    }

    private static void demonstrateShoppingCart(RecipeBook book) {
        System.out.println("Building cart from: Garlic Bread, Caesar Salad");

        List<Recipe> selected = new ArrayList<>();
        for (Recipe r : book.getAllRecipes()) {
            String name = getRecipeName(r);
            if (name.equals("Garlic Bread") || name.equals("Caesar Salad")) {
                selected.add(r);
            }
        }

        if (selected.isEmpty()) {
            System.out.println("No recipes selected.\n");
            return;
        }

        ShoppingCart cart = new ShoppingCart(selected);
        System.out.println("Shopping Cart (" + cart.size() + " unique ingredients):");
        System.out.println(cart.toString());
    }

    private static void demonstrateCompleteWorkflow(RecipeBook book) {
        System.out.println("Workflow: User searches for recipes with 'garlic', views details, builds cart\n");

        // Step 1: Search
        System.out.println("Step 1: Search for recipes with garlic");
        List<Recipe> garlic = book.searchByIngredient("garlic");
        System.out.printf("  Found %d recipes with garlic\n", garlic.size());

        // Step 2: View details
        if (!garlic.isEmpty()) {
            System.out.println("\nStep 2: View details of first result");
            System.out.println("  " + getRecipeName(garlic.get(0)) + ":");
            String details = garlic.get(0).toString();
            for (String line : details.split("\n")) {
                System.out.println("    " + line);
            }
        }

        // Step 3: Build cart
        System.out.println("\nStep 3: Build shopping cart from garlic recipes");
        ShoppingCart cart = new ShoppingCart(garlic);
        System.out.printf("  Total ingredients needed: %d\n", cart.size());
        System.out.printf("  Garlic (aggregated): %.0f cloves\n", cart.getAmount("garlic cloves"));

        System.out.println();
    }

    private static String getRecipeName(Recipe recipe) {
        try {
            java.lang.reflect.Field nameField = Recipe.class.getDeclaredField("name");
            nameField.setAccessible(true);
            return (String) nameField.get(recipe);
        } catch (ReflectiveOperationException e) {
            return "Unknown";
        }
    }

    private static int getRecipeServings(Recipe recipe) {
        try {
            java.lang.reflect.Field servingsField = Recipe.class.getDeclaredField("servings");
            servingsField.setAccessible(true);
            return servingsField.getInt(recipe);
        } catch (ReflectiveOperationException e) {
            return 0;
        }
    }
}
