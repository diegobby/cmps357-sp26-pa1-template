import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Console-based user interface for the Recipe Manager application.
 *
 * <p>Provides an interactive menu-driven interface for users to:
 * - List all recipes
 * - Search recipes (by name, ingredient, or multi-token)
 * - View recipe details
 * - Build shopping carts
 * - Load and save recipes to JSON files
 */
public class RecipeManagerUI {

    private RecipeBook recipeBook;
    private BufferedReader reader;
    private boolean running;
    private String currentFile;

    /**
     * Creates a new RecipeManagerUI with an empty recipe book.
     */
    public RecipeManagerUI() {
        this.recipeBook = new RecipeBook();
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.running = true;
        this.currentFile = null;
    }

    /**
     * Starts the main UI loop.
     */
    public void start() {
        System.out.println("=== Recipe Manager ===\n");

        while (running) {
            displayMenu();
            handleCommand();
        }

        System.out.println("\nGoodbye!");
    }

    /**
     * Displays the main menu.
     */
    private void displayMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. List all recipes");
        System.out.println("2. Search recipes");
        System.out.println("3. View recipe details");
        System.out.println("4. Build shopping cart");
        System.out.println("5. Load recipes from file");
        System.out.println("6. Save recipes to file");
        System.out.println("7. Exit");
        System.out.print("\nEnter command (1-7): ");
    }

    /**
     * Handles user commands.
     */
    private void handleCommand() {
        try {
            String input = reader.readLine();
            if (input == null) {
                running = false;
                return;
            }

            String command = input.trim();

            switch (command) {
                case "1":
                    listAllRecipes();
                    break;
                case "2":
                    searchRecipes();
                    break;
                case "3":
                    viewRecipeDetails();
                    break;
                case "4":
                    buildShoppingCart();
                    break;
                case "5":
                    loadRecipesFromFile();
                    break;
                case "6":
                    saveRecipesToFile();
                    break;
                case "7":
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid command. Please enter a number 1-7.");
            }
        } catch (IOException e) {
            System.err.println("\nError reading input: " + e.getMessage());
        }
    }

    /**
     * Lists all recipes in sorted order.
     */
    private void listAllRecipes() {
        List<Recipe> allRecipes = recipeBook.getAllRecipes();

        if (allRecipes.isEmpty()) {
            System.out.println("\nNo recipes in the collection.");
            return;
        }

        // Sort by name for display
        List<Recipe> sorted = RecipeSorter.sortByName(allRecipes);

        System.out.println("\n--- All Recipes (" + sorted.size() + ") ---");
        for (int i = 0; i < sorted.size(); i++) {
            String name = getRecipeName(sorted.get(i));
            int servings = getRecipeServings(sorted.get(i));
            System.out.printf("%d. %s (%d servings)\n", i + 1, name, servings);
        }
    }

    /**
     * Handles recipe search (by name, ingredient, or multi-token).
     */
    private void searchRecipes() {
        System.out.println("\n--- Search Recipes ---");
        System.out.println("1. Search by name");
        System.out.println("2. Search by ingredient");
        System.out.println("3. Multi-token search");
        System.out.print("Enter search type (1-3): ");

        try {
            String searchType = reader.readLine().trim();
            System.out.print("Enter search query: ");
            String query = reader.readLine();

            if (query == null || query.isEmpty()) {
                System.out.println("Search query cannot be empty.");
                return;
            }

            List<Recipe> results = new ArrayList<>();

            switch (searchType) {
                case "1":
                    results = recipeBook.searchByName(query);
                    break;
                case "2":
                    results = recipeBook.searchByIngredient(query);
                    break;
                case "3":
                    results = recipeBook.searchMultiToken(query);
                    break;
                default:
                    System.out.println("Invalid search type.");
                    return;
            }

            displaySearchResults(results, query);

        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    /**
     * Displays search results.
     */
    private void displaySearchResults(List<Recipe> results, String query) {
        if (results.isEmpty()) {
            System.out.printf("\nNo recipes found matching '%s'.\n", query);
            return;
        }

        System.out.printf("\n--- Search Results (%d) ---\n", results.size());
        for (int i = 0; i < results.size(); i++) {
            String name = getRecipeName(results.get(i));
            int servings = getRecipeServings(results.get(i));
            System.out.printf("%d. %s (%d servings)\n", i + 1, name, servings);
        }
    }

    /**
     * Displays full recipe details.
     */
    private void viewRecipeDetails() {
        System.out.print("\nEnter recipe name: ");

        try {
            String query = reader.readLine();
            if (query == null || query.isEmpty()) {
                System.out.println("Recipe name cannot be empty.");
                return;
            }

            List<Recipe> results = recipeBook.searchByName(query);

            if (results.isEmpty()) {
                System.out.printf("No recipe found matching '%s'.\n", query);
                return;
            }

            if (results.size() > 1) {
                System.out.println("\nMultiple recipes found. Showing first match:");
            }

            Recipe recipe = results.get(0);
            System.out.println("\n=== " + getRecipeName(recipe) + " ===");
            System.out.println(recipe.toString());

        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    /**
     * Handles shopping cart creation.
     */
    private void buildShoppingCart() {
        System.out.println("\n--- Build Shopping Cart ---");
        System.out.print("Enter recipe names (comma-separated): ");

        try {
            String input = reader.readLine();
            if (input == null || input.isEmpty()) {
                System.out.println("No recipes specified.");
                return;
            }

            String[] names = input.split(",");
            List<Recipe> selectedRecipes = new ArrayList<>();

            for (String name : names) {
                String trimmedName = name.trim();
                List<Recipe> results = recipeBook.searchByName(trimmedName);

                if (results.isEmpty()) {
                    System.out.printf("Warning: Recipe '%s' not found.\n", trimmedName);
                } else {
                    selectedRecipes.add(results.get(0));
                }
            }

            if (selectedRecipes.isEmpty()) {
                System.out.println("No valid recipes selected for shopping cart.");
                return;
            }

            ShoppingCart cart = new ShoppingCart(selectedRecipes);

            System.out.println("\n=== Shopping Cart ===");
            System.out.println("Selected recipes: " + selectedRecipes.size());
            System.out.println("Total ingredients: " + cart.size());
            System.out.println("\nAggregated Ingredients:");
            System.out.println(cart.toString());

        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    /**
     * Handles loading recipes from a JSON file.
     */
    private void loadRecipesFromFile() {
        System.out.print("\nEnter file path: ");

        try {
            String filePath = reader.readLine();
            if (filePath == null || filePath.isEmpty()) {
                System.out.println("File path cannot be empty.");
                return;
            }

            RecipeBook loaded = RecipeJsonManager.loadFromFile(filePath);
            this.recipeBook = loaded;
            this.currentFile = filePath;

            System.out.printf("✓ Successfully loaded %d recipes from '%s'\n", recipeBook.size(), filePath);

        } catch (IllegalArgumentException e) {
            System.out.printf("✗ Validation error: %s\n", e.getMessage());
        } catch (IOException e) {
            System.out.printf("✗ Error reading file: %s\n", e.getMessage());
        }
    }

    /**
     * Handles saving recipes to a JSON file.
     */
    private void saveRecipesToFile() {
        System.out.print("\nEnter file path: ");

        try {
            String filePath = reader.readLine();
            if (filePath == null || filePath.isEmpty()) {
                System.out.println("File path cannot be empty.");
                return;
            }

            RecipeJsonManager.saveToFile(recipeBook, filePath);
            this.currentFile = filePath;

            System.out.printf("✓ Successfully saved %d recipes to '%s'\n", recipeBook.size(), filePath);

        } catch (IOException e) {
            System.out.printf("✗ Error writing file: %s\n", e.getMessage());
        }
    }

    /**
     * Helper method to get recipe name using reflection.
     */
    private String getRecipeName(Recipe recipe) {
        try {
            java.lang.reflect.Field nameField = Recipe.class.getDeclaredField("name");
            nameField.setAccessible(true);
            return (String) nameField.get(recipe);
        } catch (ReflectiveOperationException e) {
            return "Unknown";
        }
    }

    /**
     * Helper method to get recipe servings using reflection.
     */
    private int getRecipeServings(Recipe recipe) {
        try {
            java.lang.reflect.Field servingsField = Recipe.class.getDeclaredField("servings");
            servingsField.setAccessible(true);
            return servingsField.getInt(recipe);
        } catch (ReflectiveOperationException e) {
            return 0;
        }
    }

    /**
     * Main entry point for the application.
     */
    public static void main(String[] args) {
        RecipeManagerUI ui = new RecipeManagerUI();
        ui.start();
    }
}
