import java.util.List;

/**
 * Test suite for Stage 6 UI and application integration.
 */
public class Stage6Test {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("Running Stage 6 Tests...\n");

        testRecipeListingAndSorting();
        testSearchIntegration();
        testRecipeDetails();
        testShoppingCartIntegration();
        testLoadSaveIntegration();
        testCompleteWorkflow();

        System.out.println("\n=== Test Summary ===");
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);

        if (testsFailed == 0) {
            System.out.println("All Stage 6 tests passed!");
        } else {
            System.out.println("Some tests failed!");
            System.exit(1);
        }
    }

    private static void testRecipeListingAndSorting() {
        System.out.println("--- Testing Recipe Listing and Sorting ---");

        RecipeBook book = new RecipeBook();

        Recipe z = new Recipe("Zebra Stew", 2);
        z.addIngredient("meat", 1);
        book.addRecipe(z);

        Recipe a = new Recipe("Apple Pie", 4);
        a.addIngredient("apples", 2);
        book.addRecipe(a);

        Recipe m = new Recipe("Meatballs", 6);
        m.addIngredient("beef", 1);
        book.addRecipe(m);

        // Test insertion order
        List<Recipe> all = book.getAllRecipes();
        assertTrue("Should maintain insertion order", getRecipeName(all.get(0)).equals("Zebra Stew"));
        assertTrue("Should maintain insertion order", getRecipeName(all.get(1)).equals("Apple Pie"));

        // Test sorted listing
        List<Recipe> sorted = RecipeSorter.sortByName(all);
        assertTrue("Should sort by name (case-insensitive)", getRecipeName(sorted.get(0)).equals("Apple Pie"));
        assertTrue("Should sort by name", getRecipeName(sorted.get(1)).equals("Meatballs"));
        assertTrue("Should sort by name", getRecipeName(sorted.get(2)).equals("Zebra Stew"));

        System.out.println("Recipe listing and sorting tests passed!\n");
    }

    private static void testSearchIntegration() {
        System.out.println("--- Testing Search Integration ---");

        RecipeBook book = new RecipeBook();

        Recipe carbonara = new Recipe("Pasta Carbonara", 4);
        carbonara.addIngredient("spaghetti", 400);
        carbonara.addIngredient("eggs", 4);
        carbonara.addIngredient("bacon", 200);
        book.addRecipe(carbonara);

        Recipe garlicBread = new Recipe("Garlic Bread", 4);
        garlicBread.addIngredient("bread", 1);
        garlicBread.addIngredient("garlic cloves", 4);
        book.addRecipe(garlicBread);

        Recipe soup = new Recipe("Garlic Soup", 6);
        soup.addIngredient("garlic cloves", 8);
        soup.addIngredient("broth", 1000);
        book.addRecipe(soup);

        // Test name search
        List<Recipe> nameSearch = book.searchByName("garlic");
        assertTrue("Name search should find 2 recipes", nameSearch.size() == 2);

        // Test ingredient search
        List<Recipe> ingredientSearch = book.searchByIngredient("garlic");
        assertTrue("Ingredient search should find 2 recipes", ingredientSearch.size() == 2);

        // Test multi-token search
        List<Recipe> multiSearch = book.searchMultiToken("garlic bread");
        assertTrue("Multi-token should find Garlic Bread", multiSearch.size() == 1);
        assertTrue("Should find Garlic Bread recipe", 
            getRecipeName(multiSearch.get(0)).equals("Garlic Bread"));

        System.out.println("Search integration tests passed!\n");
    }

    private static void testRecipeDetails() {
        System.out.println("--- Testing Recipe Details Display ---");

        Recipe recipe = new Recipe("Test Recipe", 3);
        recipe.addIngredient("flour", 2.5);
        recipe.addIngredient("eggs", 3);

        String details = recipe.toString();

        assertTrue("Should contain recipe name", details.contains("Test Recipe"));
        assertTrue("Should contain servings", details.contains("3"));
        assertTrue("Should contain ingredients", details.contains("flour"));
        assertTrue("Should contain amounts", details.contains("2.5"));

        System.out.println("Recipe details display tests passed!\n");
    }

    private static void testShoppingCartIntegration() {
        System.out.println("--- Testing Shopping Cart Integration ---");

        RecipeBook book = new RecipeBook();

        Recipe r1 = new Recipe("Dish 1", 2);
        r1.addIngredient("garlic", 2);
        r1.addIngredient("butter", 0.5);
        book.addRecipe(r1);

        Recipe r2 = new Recipe("Dish 2", 2);
        r2.addIngredient("garlic", 1);
        r2.addIngredient("onion", 1);
        book.addRecipe(r2);

        // Get both recipes and create cart
        List<Recipe> all = book.getAllRecipes();
        ShoppingCart cart = new ShoppingCart(all);

        assertTrue("Cart should have 3 unique ingredients", cart.size() == 3);
        assertTrue("Garlic should be aggregated to 3", cart.getAmount("garlic") == 3);
        assertTrue("Butter should be 0.5", cart.getAmount("butter") == 0.5);

        System.out.println("Shopping cart integration tests passed!\n");
    }

    private static void testLoadSaveIntegration() {
        System.out.println("--- Testing Load/Save Integration ---");

        RecipeBook original = new RecipeBook();

        Recipe r = new Recipe("Integration Test", 5);
        r.addIngredient("ingredient", 1.5);
        original.addRecipe(r);

        String testFile = "stage6_test.json";

        try {
            // Save
            RecipeJsonManager.saveToFile(original, testFile);

            // Load
            RecipeBook loaded = RecipeJsonManager.loadFromFile(testFile);

            assertTrue("Should load 1 recipe", loaded.size() == 1);
            assertTrue("Should have correct servings", 
                getRecipeServings(loaded.getAllRecipes().get(0)) == 5);

            // Cleanup
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(testFile));

            System.out.println("Load/save integration tests passed!\n");

        } catch (Exception e) {
            testsFailed++;
            System.err.println("FAILED: " + e.getMessage());
        }
    }

    private static void testCompleteWorkflow() {
        System.out.println("--- Testing Complete Workflow ---");

        // Create a recipe book
        RecipeBook book = new RecipeBook();

        Recipe pasta = new Recipe("Spaghetti", 2);
        pasta.addIngredient("spaghetti", 200);
        pasta.addIngredient("tomato sauce", 300);
        pasta.addIngredient("garlic", 3);
        book.addRecipe(pasta);

        Recipe bread = new Recipe("Garlic Bread", 4);
        bread.addIngredient("bread", 1);
        bread.addIngredient("garlic", 4);
        bread.addIngredient("butter", 0.25);
        book.addRecipe(bread);

        // Workflow: Search → Get details → Build cart
        List<Recipe> garlicRecipes = book.searchByIngredient("garlic");
        assertTrue("Should find 2 garlic recipes", garlicRecipes.size() == 2);

        ShoppingCart cart = new ShoppingCart(garlicRecipes);
        assertTrue("Cart should aggregate garlic", cart.getAmount("garlic") == 7);

        // Sort and display
        List<Recipe> sorted = RecipeSorter.sortByName(book.getAllRecipes());
        assertTrue("Should have 2 recipes sorted", sorted.size() == 2);

        System.out.println("Complete workflow tests passed!\n");
    }

    private static void assertTrue(String message, boolean condition) {
        if (condition) {
            testsPassed++;
        } else {
            testsFailed++;
            System.err.println("FAILED: " + message);
        }
    }

    private static String getRecipeName(Recipe recipe) {
        try {
            java.lang.reflect.Field nameField = Recipe.class.getDeclaredField("name");
            nameField.setAccessible(true);
            return (String) nameField.get(recipe);
        } catch (ReflectiveOperationException e) {
            return null;
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
