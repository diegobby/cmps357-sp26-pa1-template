import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Test suite for Stage 4 shopping cart aggregation.
 */
public class Stage4Test {
    
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("Running Stage 4 Tests...\n");

        testBasicAggregation();
        testCaseInsensitiveNormalization();
        testAmountFormatting();
        testMultipleRecipes();
        testEmptyRecipes();
        testEdgeCases();

        System.out.println("\n=== Test Summary ===");
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);
        
        if (testsFailed == 0) {
            System.out.println("All Stage 4 tests passed!");
        } else {
            System.out.println("Some tests failed!");
            System.exit(1);
        }
    }

    private static void testBasicAggregation() {
        System.out.println("--- Testing Basic Aggregation ---");

        Recipe r1 = new Recipe("Pasta", 2);
        r1.addIngredient("garlic", 2);
        r1.addIngredient("olive oil", 1);

        Recipe r2 = new Recipe("Salad", 4);
        r2.addIngredient("garlic", 1);
        r2.addIngredient("tomato", 2);

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(r1);
        recipes.add(r2);

        ShoppingCart cart = new ShoppingCart(recipes);

        assertTrue("Cart should have 3 unique ingredients", cart.size() == 3);
        assertTrue("Garlic should be aggregated to 3", cart.getAmount("garlic") == 3);
        assertTrue("Olive oil should be 1", cart.getAmount("olive oil") == 1);
        assertTrue("Tomato should be 2", cart.getAmount("tomato") == 2);

        System.out.println("Basic aggregation tests passed!\n");
    }

    private static void testCaseInsensitiveNormalization() {
        System.out.println("--- Testing Case-Insensitive Normalization ---");

        Recipe r1 = new Recipe("Dish1", 2);
        r1.addIngredient("Garlic", 2);

        Recipe r2 = new Recipe("Dish2", 2);
        r2.addIngredient("GARLIC", 1);

        Recipe r3 = new Recipe("Dish3", 2);
        r3.addIngredient("garlic", 1.5);

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(r1);
        recipes.add(r2);
        recipes.add(r3);

        ShoppingCart cart = new ShoppingCart(recipes);

        assertTrue("Should normalize 'Garlic', 'GARLIC', 'garlic' to same ingredient", cart.size() == 1);
        assertTrue("Total garlic should be 4.5", cart.getAmount("garlic") == 4.5);
        assertTrue("Should match 'GARLIC'", cart.getAmount("GARLIC") == 4.5);
        assertTrue("Should match 'Garlic'", cart.getAmount("Garlic") == 4.5);

        System.out.println("Case-insensitive normalization tests passed!\n");
    }

    private static void testAmountFormatting() {
        System.out.println("--- Testing Amount Formatting ---");

        Recipe r1 = new Recipe("Mix", 2);
        r1.addIngredient("ingredient1", 2.0);      // Integer
        r1.addIngredient("ingredient2", 1.5);      // Decimal
        r1.addIngredient("ingredient3", 0.25);     // Decimal
        r1.addIngredient("ingredient4", 1.333);    // Needs rounding to 1.33

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(r1);

        ShoppingCart cart = new ShoppingCart(recipes);
        String output = cart.toString();

        assertTrue("Should format 2.0 as '2'", output.contains("- 2 ingredient1"));
        assertTrue("Should format 1.5 as '1.5'", output.contains("- 1.5 ingredient2"));
        assertTrue("Should format 0.25 as '0.25'", output.contains("- 0.25 ingredient3"));
        assertTrue("Should format 1.333 as '1.33'", output.contains("- 1.33 ingredient4"));

        System.out.println("Amount formatting tests passed!\n");
    }

    private static void testMultipleRecipes() {
        System.out.println("--- Testing Multiple Recipes Aggregation ---");

        // Create 3 recipes with overlapping ingredients
        Recipe pasta = new Recipe("Pasta", 2);
        pasta.addIngredient("garlic", 2);
        pasta.addIngredient("pasta", 200);
        pasta.addIngredient("olive oil", 0.25);

        Recipe salad = new Recipe("Salad", 4);
        salad.addIngredient("garlic", 1);
        salad.addIngredient("lettuce", 200);
        salad.addIngredient("olive oil", 0.5);

        Recipe sauce = new Recipe("Sauce", 1);
        sauce.addIngredient("garlic", 4);
        sauce.addIngredient("tomato", 500);
        sauce.addIngredient("olive oil", 0.1);

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(pasta);
        recipes.add(salad);
        recipes.add(sauce);

        ShoppingCart cart = new ShoppingCart(recipes);

        assertTrue("Should have 5 unique ingredients", cart.size() == 5);
        assertTrue("Garlic: 2 + 1 + 4 = 7", cart.getAmount("garlic") == 7);
        assertTrue("Olive oil: 0.25 + 0.5 + 0.1 = 0.85", 
            Math.abs(cart.getAmount("olive oil") - 0.85) < 0.0001);
        assertTrue("Pasta should be 200", cart.getAmount("pasta") == 200);
        assertTrue("Lettuce should be 200", cart.getAmount("lettuce") == 200);
        assertTrue("Tomato should be 500", cart.getAmount("tomato") == 500);

        System.out.println("Multiple recipes aggregation tests passed!\n");
    }

    private static void testEmptyRecipes() {
        System.out.println("--- Testing Empty Recipes ---");

        Recipe empty = new Recipe("Empty", 2);
        // No ingredients added

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(empty);

        ShoppingCart cart = new ShoppingCart(recipes);

        assertTrue("Empty recipe should result in empty cart", cart.size() == 0);
        assertTrue("Empty cart toString should be empty", cart.toString().isEmpty());

        System.out.println("Empty recipes tests passed!\n");
    }

    private static void testEdgeCases() {
        System.out.println("--- Testing Edge Cases ---");

        // Test with null list throws exception
        try {
            new ShoppingCart(null);
            testsFailed++;
            System.err.println("FAILED: Should throw exception for null recipes");
        } catch (IllegalArgumentException e) {
            testsPassed++;
        }

        // Test with empty list
        ShoppingCart emptyCart = new ShoppingCart(new ArrayList<>());
        assertTrue("Empty list should create empty cart", emptyCart.size() == 0);

        // Test with whitespace normalization
        Recipe r1 = new Recipe("Test", 2);
        r1.addIngredient("  garlic  ", 2);

        Recipe r2 = new Recipe("Test2", 2);
        r2.addIngredient("garlic", 1);

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(r1);
        recipes.add(r2);

        ShoppingCart cart = new ShoppingCart(recipes);
        assertTrue("Should normalize whitespace in ingredient names", cart.size() == 1);
        assertTrue("Should sum whitespace-trimmed ingredients", cart.getAmount("garlic") == 3);

        // Test ingredient order preservation and sorting
        Recipe r3 = new Recipe("Mix", 1);
        r3.addIngredient("zebra", 1);
        r3.addIngredient("apple", 1);
        r3.addIngredient("banana", 1);

        List<Recipe> recipes2 = new ArrayList<>();
        recipes2.add(r3);

        ShoppingCart cart2 = new ShoppingCart(recipes2);
        String output = cart2.toString();
        
        int appleIdx = output.indexOf("apple");
        int bananaIdx = output.indexOf("banana");
        int zebraIdx = output.indexOf("zebra");
        
        assertTrue("Ingredients should be sorted alphabetically", 
            appleIdx < bananaIdx && bananaIdx < zebraIdx);

        System.out.println("Edge cases tests passed!\n");
    }

    private static void assertTrue(String message, boolean condition) {
        if (condition) {
            testsPassed++;
        } else {
            testsFailed++;
            System.err.println("FAILED: " + message);
        }
    }
}
