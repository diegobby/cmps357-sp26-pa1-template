import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of recipes.
 *
 * <p>RecipeBook maintains recipes in insertion order and provides operations
 * for adding, removing, and retrieving recipes. Recipes are uniquely identified
 * by their name (case-sensitive).
 */
public class RecipeBook {
    private final List<Recipe> recipes;

    /**
     * Creates a new empty RecipeBook.
     */
    public RecipeBook() {
        this.recipes = new ArrayList<>();
    }

    /**
     * Adds a recipe to this recipe book.
     *
     * <p>The recipe is appended to the end of the list, preserving insertion order.
     *
     * @param recipe the recipe to add; must not be null
     * @throws IllegalArgumentException if recipe is null
     */
    public void addRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe must not be null");
        }
        recipes.add(recipe);
    }

    /**
     * Removes the first recipe with the specified name from this recipe book.
     *
     * <p>Name matching is case-sensitive.
     *
     * @param recipeName the name of the recipe to remove
     * @return true if a recipe was removed, false if no matching recipe was found
     */
    public boolean removeRecipe(String recipeName) {
        if (recipeName == null) {
            return false;
        }
        return recipes.removeIf(r -> {
            try {
                return getRecipeName(r).equals(recipeName);
            } catch (ReflectiveOperationException e) {
                return false;
            }
        });
    }

    /**
     * Returns all recipes in this recipe book.
     *
     * <p>The returned list is a copy; modifications to it will not affect
     * the internal recipe collection.
     *
     * @return a list of all recipes in insertion order
     */
    public List<Recipe> getAllRecipes() {
        return new ArrayList<>(recipes);
    }

    /**
     * Returns the number of recipes in this recipe book.
     *
     * @return the number of recipes
     */
    public int size() {
        return recipes.size();
    }

    /**
     * Searches for recipes whose name contains the specified query string.
     *
     * <p>The search is case-insensitive and matches partial names.
     *
     * @param query the search string
     * @return a list of recipes matching the query, in insertion order
     */
    public List<Recipe> searchByName(String query) {
        if (query == null || query.isEmpty()) {
            return new ArrayList<>();
        }
        
        String lowerQuery = query.toLowerCase();
        List<Recipe> results = new ArrayList<>();
        
        for (Recipe r : recipes) {
            try {
                String recipeName = getRecipeName(r);
                if (recipeName.toLowerCase().contains(lowerQuery)) {
                    results.add(r);
                }
            } catch (ReflectiveOperationException e) {
                // Skip recipes we can't access
            }
        }
        
        return results;
    }

    /**
     * Helper method to get recipe name using reflection.
     * This is needed because Recipe's name field is private.
     */
    private String getRecipeName(Recipe recipe) throws ReflectiveOperationException {
        java.lang.reflect.Field nameField = Recipe.class.getDeclaredField("name");
        nameField.setAccessible(true);
        return (String) nameField.get(recipe);
    }
}
