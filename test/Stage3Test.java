import java.util.List;

/**
 * Test suite for Stage 3 advanced search and sort features.
 */
public class Stage3Test {
    
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("Running Stage 3 Tests...\n");

        // Test ingredient-based search
        testSearchByIngredient();
        
        // Test multi-token search
        testSearchMultiToken();
        
        // Test stable sorting
        testStableSorting();

        System.out.println("\n=== Test Summary ===");
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);
        
        if (testsFailed == 0) {
            System.out.println("All Stage 3 tests passed!");
        } else {
            System.out.println("Some tests failed!");
            System.exit(1);
        }
    }

    private static void testSearchByIngredient() {
        System.out.println("--- Testing searchByIngredient ---");

        RecipeBook book = new RecipeBook();

        Recipe r1 = new Recipe("Salad", 4);
        r1.addIngredient("lettuce", 2);
        r1.addIngredient("tomato", 1);
        book.addRecipe(r1);

        Recipe r2 = new Recipe("Soup", 4);
        r2.addIngredient("tomato", 3);
        r2.addIngredient("garlic", 2);
        book.addRecipe(r2);

        Recipe r3 = new Recipe("Pasta", 2);
        r3.addIngredient("pasta", 1);
        r3.addIngredient("garlic", 1);
        book.addRecipe(r3);

        // Test 1: Search for "tomato"
        List<Recipe> tomatoRecipes = book.searchByIngredient("tomato");
        assertTrue("Should find 2 recipes with tomato", tomatoRecipes.size() == 2);
        assertTrue("Should find Salad", tomatoRecipes.contains(r1));
        assertTrue("Should find Soup", tomatoRecipes.contains(r2));

        // Test 2: Search for "garlic"
        List<Recipe> garlicRecipes = book.searchByIngredient("garlic");
        assertTrue("Should find 2 recipes with garlic", garlicRecipes.size() == 2);
        assertTrue("Should find Soup", garlicRecipes.contains(r2));
        assertTrue("Should find Pasta", garlicRecipes.contains(r3));

        // Test 3: Case-insensitive search
        List<Recipe> caseInsensitive = book.searchByIngredient("TOMATO");
        assertTrue("Should be case-insensitive", caseInsensitive.size() == 2);

        // Test 4: Partial match
        List<Recipe> partial = book.searchByIngredient("tom");
        assertTrue("Should match partial ingredient name", partial.size() == 2);

        // Test 5: Non-existent ingredient
        List<Recipe> noMatch = book.searchByIngredient("xyz");
        assertTrue("Should find 0 recipes for non-existent ingredient", noMatch.size() == 0);

        // Test 6: Empty query
        List<Recipe> emptySearch = book.searchByIngredient("");
        assertTrue("Should return empty list for empty query", emptySearch.size() == 0);

        System.out.println("Ingredient search tests passed!\n");
    }

    private static void testSearchMultiToken() {
        System.out.println("--- Testing searchMultiToken ---");

        RecipeBook book = new RecipeBook();

        Recipe r1 = new Recipe("Garlic Bread", 4);
        r1.addIngredient("garlic", 3);
        r1.addIngredient("bread", 1);
        book.addRecipe(r1);

        Recipe r2 = new Recipe("Garlic Oil", 2);
        r2.addIngredient("garlic", 4);
        r2.addIngredient("olive oil", 1);
        book.addRecipe(r2);

        Recipe r3 = new Recipe("Pasta Carbonara", 4);
        r3.addIngredient("pasta", 1);
        r3.addIngredient("eggs", 4);
        r3.addIngredient("garlic", 1);
        book.addRecipe(r3);

        // Test 1: Multi-token query "garlic bread" - matches recipe name
        List<Recipe> result1 = book.searchMultiToken("garlic bread");
        assertTrue("Should find Garlic Bread by recipe name", result1.size() == 1);
        assertTrue("Should find Garlic Bread recipe", result1.contains(r1));

        // Test 2: Multi-token query "garlic oil" - matches recipe name
        List<Recipe> result2 = book.searchMultiToken("garlic oil");
        assertTrue("Should find Garlic Oil by recipe name", result2.size() == 1);
        assertTrue("Should find Garlic Oil recipe", result2.contains(r2));

        // Test 3: Multi-token query "pasta garlic" - matches recipe with both in name+ingredients
        List<Recipe> result3 = book.searchMultiToken("pasta garlic");
        assertTrue("Should find Pasta Carbonara (has pasta in name, garlic in ingredient)", result3.size() == 1);
        assertTrue("Should find Pasta Carbonara", result3.contains(r3));

        // Test 4: Multi-token query "bread oil" - no recipe has both
        List<Recipe> result4 = book.searchMultiToken("bread oil");
        assertTrue("Should find 0 recipes (no recipe has both bread and oil)", result4.size() == 0);

        // Test 5: Case-insensitive multi-token search
        List<Recipe> result5 = book.searchMultiToken("GARLIC BREAD");
        assertTrue("Should be case-insensitive", result5.size() == 1);

        // Test 6: Partial match in multi-token
        List<Recipe> result6 = book.searchMultiToken("gar bre");
        assertTrue("Should match partial tokens", result6.size() == 1);

        // Test 7: Single token in multi-token search
        List<Recipe> result7 = book.searchMultiToken("garlic");
        assertTrue("Should find all recipes with garlic", result7.size() == 3);

        // Test 8: Empty query
        List<Recipe> result8 = book.searchMultiToken("");
        assertTrue("Should return empty list for empty query", result8.size() == 0);

        System.out.println("Multi-token search tests passed!\n");
    }

    private static void testStableSorting() {
        System.out.println("--- Testing Stable Sorting with Secondary Key ---");

        RecipeBook book = new RecipeBook();

        // Create recipes with names that differ only in case
        Recipe apple = new Recipe("apple", 2);
        book.addRecipe(apple);

        Recipe Apple = new Recipe("Apple", 2);
        book.addRecipe(Apple);

        Recipe APPLE = new Recipe("APPLE", 2);
        book.addRecipe(APPLE);

        Recipe banana = new Recipe("banana", 2);
        book.addRecipe(banana);

        List<Recipe> all = book.getAllRecipes();
        List<Recipe> sorted = RecipeSorter.sortByName(all);

        // All three "apple" variants should be together (primary sort)
        // When ignoring case, they're equal, so secondary (case-sensitive) sort applies

        // Find the indices of the apple recipes
        int indexApple = -1, indexAPPLE = -1, indexSmallApple = -1;
        for (int i = 0; i < sorted.size(); i++) {
            String name = getRecipeName(sorted.get(i));
            if (name.equals("apple")) indexSmallApple = i;
            if (name.equals("Apple")) indexApple = i;
            if (name.equals("APPLE")) indexAPPLE = i;
        }

        // Verify they're grouped together
        assertTrue("APPLE variants should be together", 
            Math.abs(indexApple - indexSmallApple) <= 2 && 
            Math.abs(indexAPPLE - indexSmallApple) <= 2);

        // Verify secondary sort (case-sensitive): APPLE < Apple < apple
        assertTrue("Should use case-sensitive secondary sort (APPLE < Apple < apple)",
            indexAPPLE < indexApple && indexApple < indexSmallApple);

        // Verify banana comes after apple group
        int indexBanana = -1;
        for (int i = 0; i < sorted.size(); i++) {
            if (getRecipeName(sorted.get(i)).equals("banana")) {
                indexBanana = i;
            }
        }
        assertTrue("Banana should come after apple group", indexBanana > indexSmallApple);

        System.out.println("Stable sorting tests passed!\n");
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
}
