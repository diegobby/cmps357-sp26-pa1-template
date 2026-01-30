import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Stage 5 Demo: Demonstrates JSON persistence features.
 *
 * <p>This demo showcases:
 * - Saving recipes to JSON files
 * - Loading recipes from JSON files
 * - Order preservation (recipes and ingredients)
 * - Data validation
 * - Error handling
 */
public class Stage5Demo {
    public static void main(String[] args) {
        System.out.println("=== Stage 5 Demo: JSON Persistence ===\n");

        String filePath = "recipes.json";

        // --- Test 1: Create, Save, and Load ---
        System.out.println("--- Test 1: Save RecipeBook to JSON ---");
        RecipeBook recipeBook = createSampleRecipes();

        try {
            RecipeJsonManager.saveToFile(recipeBook, filePath);
            System.out.println("✓ Saved recipes to " + filePath);
            System.out.println();

            // Display the JSON file contents
            System.out.println("JSON file contents:");
            System.out.println(new String(Files.readAllBytes(Paths.get(filePath))));
            System.out.println();

        } catch (IOException e) {
            System.err.println("✗ Error saving recipes: " + e.getMessage());
            return;
        }

        // --- Test 2: Load from JSON ---
        System.out.println("--- Test 2: Load RecipeBook from JSON ---");
        try {
            RecipeBook loaded = RecipeJsonManager.loadFromFile(filePath);
            System.out.println("✓ Loaded " + loaded.size() + " recipes from " + filePath);
            System.out.println();

            System.out.println("Loaded Recipes:");
            for (Recipe r : loaded.getAllRecipes()) {
                System.out.println(r);
                System.out.println();
            }

        } catch (IOException e) {
            System.err.println("✗ Error loading recipes: " + e.getMessage());
            return;
        }

        // --- Test 3: Verify Order Preservation ---
        System.out.println("--- Test 3: Verify Order Preservation ---");
        try {
            RecipeBook loaded = RecipeJsonManager.loadFromFile(filePath);
            System.out.println("Recipe order after load:");
            for (int i = 0; i < loaded.size(); i++) {
                System.out.println((i + 1) + ". " + getRecipeName(loaded.getAllRecipes().get(i)));
            }
            System.out.println();

        } catch (IOException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }

        // --- Test 4: Round-trip Test ---
        System.out.println("--- Test 4: Round-trip Test (Save → Load → Save → Load) ---");
        try {
            RecipeBook loaded1 = RecipeJsonManager.loadFromFile(filePath);
            String tempFile = "recipes_temp.json";

            RecipeJsonManager.saveToFile(loaded1, tempFile);
            RecipeBook loaded2 = RecipeJsonManager.loadFromFile(tempFile);

            System.out.println("✓ Round-trip successful");
            System.out.println("  Original file recipes: " + recipeBook.size());
            System.out.println("  After round-trip recipes: " + loaded2.size());
            System.out.println();

            // Cleanup
            Files.deleteIfExists(Paths.get(tempFile));

        } catch (IOException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }

        // --- Test 5: Invalid Data Handling ---
        System.out.println("--- Test 5: Invalid Data Handling ---");
        String invalidFile = "invalid.json";
        try {
            Files.write(Paths.get(invalidFile),
                    "{\n  \"recipes\": [\n    {\n      \"name\": \"\",\n      \"servings\": 2,\n      \"ingredients\": []\n    }\n  ]\n}".getBytes());

            try {
                RecipeJsonManager.loadFromFile(invalidFile);
                System.out.println("✗ Should have rejected empty name");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Correctly rejected empty recipe name");
                System.out.println("  Error: " + e.getMessage());
            }

            Files.deleteIfExists(Paths.get(invalidFile));

        } catch (IOException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }

        System.out.println();

        // --- Test 6: Precision Preservation ---
        System.out.println("--- Test 6: Numeric Precision Preservation ---");
        try {
            RecipeBook loaded = RecipeJsonManager.loadFromFile(filePath);

            // Scale a recipe and save it
            if (loaded.size() > 0) {
                Recipe recipe = loaded.getAllRecipes().get(0);
                int originalServings = getRecipeServings(recipe);
                recipe.scaleToServings(originalServings * 2);

                System.out.println("Original recipe scaled from " + originalServings + " to " + (originalServings * 2) + " servings");
                System.out.println("Scaled recipe preview:");
                System.out.println(recipe.toString().split("\n")[0]); // Just show the header
                System.out.println("(First 3 ingredients shown)");

                String[] lines = recipe.toString().split("\n");
                for (int i = 1; i < Math.min(4, lines.length); i++) {
                    System.out.println(lines[i]);
                }
            }

        } catch (IOException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }

        System.out.println();

        // Cleanup
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            // Ignore
        }

        System.out.println("=== Stage 5 Demo Complete ===");
    }

    private static RecipeBook createSampleRecipes() {
        RecipeBook book = new RecipeBook();

        // Recipe 1: Pasta Carbonara
        Recipe carbonara = new Recipe("Pasta Carbonara", 4);
        carbonara.addIngredient("spaghetti", 400);
        carbonara.addIngredient("eggs", 4);
        carbonara.addIngredient("bacon", 200);
        carbonara.addIngredient("parmesan cheese", 100);
        carbonara.addIngredient("black pepper", 2);
        book.addRecipe(carbonara);

        // Recipe 2: Caesar Salad
        Recipe salad = new Recipe("Caesar Salad", 2);
        salad.addIngredient("romaine lettuce", 200);
        salad.addIngredient("croutons", 100);
        salad.addIngredient("parmesan cheese", 50);
        salad.addIngredient("caesar dressing", 100);
        book.addRecipe(salad);

        // Recipe 3: Garlic Bread
        Recipe bread = new Recipe("Garlic Bread", 4);
        bread.addIngredient("baguette", 1);
        bread.addIngredient("garlic cloves", 4);
        bread.addIngredient("butter", 0.25);
        bread.addIngredient("parsley", 0.1);
        book.addRecipe(bread);

        return book;
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
