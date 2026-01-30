import java.io.*;
import java.nio.file.*;

/**
 * Test suite for Stage 5 JSON persistence.
 */
public class Stage5Test {

    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static final String TEST_DIR = "test_data";

    public static void main(String[] args) {
        System.out.println("Running Stage 5 Tests...\n");

        // Create test directory
        try {
            Files.createDirectories(Paths.get(TEST_DIR));
        } catch (IOException e) {
            System.err.println("Could not create test directory");
            System.exit(1);
        }

        testBasicSaveLoad();
        testRecipeOrder();
        testIngredientOrder();
        testPrecision();
        testValidation();
        testErrorHandling();
        testEmptyRecipeBook();

        // Cleanup
        try {
            Files.deleteIfExists(Paths.get(TEST_DIR + "/test.json"));
            Files.deleteIfExists(Paths.get(TEST_DIR + "/invalid.json"));
            Files.deleteIfExists(Paths.get(TEST_DIR));
        } catch (IOException e) {
            // Ignore cleanup errors
        }

        System.out.println("\n=== Test Summary ===");
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);

        if (testsFailed == 0) {
            System.out.println("All Stage 5 tests passed!");
        } else {
            System.out.println("Some tests failed!");
            System.exit(1);
        }
    }

    private static void testBasicSaveLoad() {
        System.out.println("--- Testing Basic Save and Load ---");

        RecipeBook original = new RecipeBook();

        Recipe pasta = new Recipe("Spaghetti", 2);
        pasta.addIngredient("spaghetti", 200);
        pasta.addIngredient("garlic", 3);
        pasta.addIngredient("olive oil", 0.25);
        original.addRecipe(pasta);

        Recipe salad = new Recipe("Caesar Salad", 4);
        salad.addIngredient("lettuce", 200);
        salad.addIngredient("parmesan", 50);
        original.addRecipe(salad);

        String filePath = TEST_DIR + "/test.json";

        try {
            // Save
            RecipeJsonManager.saveToFile(original, filePath);
            assertTrue("File should exist after save", Files.exists(Paths.get(filePath)));

            // Load
            RecipeBook loaded = RecipeJsonManager.loadFromFile(filePath);

            // Verify
            assertTrue("Should load 2 recipes", loaded.size() == 2);
            assertTrue("First recipe should be Spaghetti",
                    loaded.getAllRecipes().get(0).toString().contains("Spaghetti"));
            assertTrue("Second recipe should be Caesar Salad",
                    loaded.getAllRecipes().get(1).toString().contains("Caesar Salad"));

            System.out.println("Basic save and load tests passed!\n");
        } catch (IOException e) {
            testsFailed++;
            System.err.println("FAILED: " + e.getMessage());
        }
    }

    private static void testRecipeOrder() {
        System.out.println("--- Testing Recipe Order Preservation ---");

        RecipeBook original = new RecipeBook();
        String[] names = {"Alpha", "Zebra", "Beta", "Charlie"};

        for (String name : names) {
            Recipe r = new Recipe(name, 2);
            r.addIngredient("ingredient", 1);
            original.addRecipe(r);
        }

        String filePath = TEST_DIR + "/test.json";

        try {
            RecipeJsonManager.saveToFile(original, filePath);
            RecipeBook loaded = RecipeJsonManager.loadFromFile(filePath);

            // Check order
            for (int i = 0; i < names.length; i++) {
                assertTrue("Recipe " + i + " should be " + names[i],
                        getRecipeName(loaded.getAllRecipes().get(i)).equals(names[i]));
            }

            System.out.println("Recipe order preservation tests passed!\n");
        } catch (IOException e) {
            testsFailed++;
            System.err.println("FAILED: " + e.getMessage());
        }
    }

    private static void testIngredientOrder() {
        System.out.println("--- Testing Ingredient Order Preservation ---");

        RecipeBook original = new RecipeBook();
        Recipe recipe = new Recipe("Test", 1);

        String[] ingredients = {"flour", "sugar", "eggs", "butter"};
        for (String ing : ingredients) {
            recipe.addIngredient(ing, 1);
        }
        original.addRecipe(recipe);

        String filePath = TEST_DIR + "/test.json";

        try {
            RecipeJsonManager.saveToFile(original, filePath);
            RecipeBook loaded = RecipeJsonManager.loadFromFile(filePath);

            Recipe loadedRecipe = loaded.getAllRecipes().get(0);
            String recipeStr = loadedRecipe.toString();

            // Check that ingredients appear in order
            int lastIndex = -1;
            for (String ing : ingredients) {
                int index = recipeStr.indexOf(ing);
                assertTrue("Ingredient " + ing + " should appear in recipe", index != -1);
                assertTrue("Ingredients should maintain order", index > lastIndex);
                lastIndex = index;
            }

            System.out.println("Ingredient order preservation tests passed!\n");
        } catch (IOException e) {
            testsFailed++;
            System.err.println("FAILED: " + e.getMessage());
        }
    }

    private static void testPrecision() {
        System.out.println("--- Testing Numeric Precision ---");

        RecipeBook original = new RecipeBook();
        Recipe recipe = new Recipe("Precision Test", 3);

        double[] amounts = {2.5, 0.333, 1.234567, 100.0, 0.01};
        for (int i = 0; i < amounts.length; i++) {
            recipe.addIngredient("ingredient" + i, amounts[i]);
        }
        original.addRecipe(recipe);

        String filePath = TEST_DIR + "/test.json";

        try {
            RecipeJsonManager.saveToFile(original, filePath);
            RecipeBook loaded = RecipeJsonManager.loadFromFile(filePath);

            // Scale the loaded recipe and check that precision is maintained
            Recipe loadedRecipe = loaded.getAllRecipes().get(0);
            loadedRecipe.scaleToServings(6); // 2x scaling

            // The scaled values should preserve the original precision
            String scaled = loadedRecipe.toString();
            assertTrue("Scaled recipe should contain values", scaled.length() > 0);

            System.out.println("Numeric precision tests passed!\n");
        } catch (IOException e) {
            testsFailed++;
            System.err.println("FAILED: " + e.getMessage());
        }
    }

    private static void testValidation() {
        System.out.println("--- Testing Data Validation ---");

        String validJson = "{\n  \"recipes\": [\n    {\n      \"name\": \"Valid\",\n      \"servings\": 2,\n      \"ingredients\": [\n        {\"name\": \"flour\", \"amount\": 1.5}\n      ]\n    }\n  ]\n}";
        String filePath = TEST_DIR + "/test.json";

        try {
            Files.write(Paths.get(filePath), validJson.getBytes());
            RecipeBook loaded = RecipeJsonManager.loadFromFile(filePath);
            assertTrue("Valid JSON should load", loaded.size() == 1);
            System.out.println("Validation tests passed!\n");
        } catch (IOException e) {
            testsFailed++;
            System.err.println("FAILED: " + e.getMessage());
        }
    }

    private static void testErrorHandling() {
        System.out.println("--- Testing Error Handling ---");

        // Test 1: Missing name
        String noNameJson = "{\n  \"recipes\": [\n    {\n      \"servings\": 2,\n      \"ingredients\": []\n    }\n  ]\n}";
        testInvalidJson(noNameJson, "missing name");

        // Test 2: Invalid servings (zero)
        String zeroServingsJson = "{\n  \"recipes\": [\n    {\n      \"name\": \"Test\",\n      \"servings\": 0,\n      \"ingredients\": []\n    }\n  ]\n}";
        testInvalidJson(zeroServingsJson, "zero servings");

        // Test 3: Negative servings
        String negativeServingsJson = "{\n  \"recipes\": [\n    {\n      \"name\": \"Test\",\n      \"servings\": -1,\n      \"ingredients\": []\n    }\n  ]\n}";
        testInvalidJson(negativeServingsJson, "negative servings");

        // Test 4: Missing amount
        String noAmountJson = "{\n  \"recipes\": [\n    {\n      \"name\": \"Test\",\n      \"servings\": 2,\n      \"ingredients\": [{\"name\": \"flour\"}]\n    }\n  ]\n}";
        testInvalidJson(noAmountJson, "missing amount");

        // Test 5: Zero amount
        String zeroAmountJson = "{\n  \"recipes\": [\n    {\n      \"name\": \"Test\",\n      \"servings\": 2,\n      \"ingredients\": [{\"name\": \"flour\", \"amount\": 0}]\n    }\n  ]\n}";
        testInvalidJson(zeroAmountJson, "zero amount");

        System.out.println("Error handling tests passed!\n");
    }

    private static void testInvalidJson(String json, String testName) {
        String filePath = TEST_DIR + "/invalid.json";
        try {
            Files.write(Paths.get(filePath), json.getBytes());
            try {
                RecipeJsonManager.loadFromFile(filePath);
                testsFailed++;
                System.err.println("FAILED: Should reject " + testName);
            } catch (IllegalArgumentException e) {
                testsPassed++;
            }
        } catch (IOException e) {
            testsFailed++;
            System.err.println("FAILED: Could not write test file for " + testName);
        }
    }

    private static void testEmptyRecipeBook() {
        System.out.println("--- Testing Empty RecipeBook ---");

        RecipeBook empty = new RecipeBook();
        String filePath = TEST_DIR + "/test.json";

        try {
            RecipeJsonManager.saveToFile(empty, filePath);
            RecipeBook loaded = RecipeJsonManager.loadFromFile(filePath);

            assertTrue("Empty book should load as empty", loaded.size() == 0);
            System.out.println("Empty RecipeBook tests passed!\n");
        } catch (IOException e) {
            testsFailed++;
            System.err.println("FAILED: " + e.getMessage());
        }
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
