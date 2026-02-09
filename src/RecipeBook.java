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
     * Searches for recipes that contain an ingredient matching the specified query.
     *
     * <p>The search is case-insensitive and matches partial ingredient names.
     * A recipe matches if any of its ingredients have a name containing the query.
     *
     * @param query the search string
     * @return a list of recipes containing a matching ingredient, in insertion order
     */
    public List<Recipe> searchByIngredient(String query) {
        if (query == null || query.isEmpty()) {
            return new ArrayList<>();
        }
        
        String lowerQuery = query.toLowerCase();
        List<Recipe> results = new ArrayList<>();
        
        for (Recipe r : recipes) {
            if (recipeContainsIngredient(r, lowerQuery)) {
                results.add(r);
            }
        }
        
        return results;
    }

    /**
     * Searches for recipes matching all tokens in the query string.
     *
     * <p>The query is split by whitespace into tokens. A recipe matches if all tokens
     * match somewhere in the recipe (recipe name or ingredient names), using
     * case-insensitive, partial matching.
     *
     * @param query the search string (may contain multiple space-separated tokens)
     * @return a list of recipes matching all tokens, in insertion order
     */
    public List<Recipe> searchMultiToken(String query) {
        if (query == null || query.isEmpty()) {
            return new ArrayList<>();
        }
        
        String[] tokens = query.trim().split("\\s+");
        if (tokens.length == 0) {
            return new ArrayList<>();
        }
        
        List<Recipe> results = new ArrayList<>();
        
        for (Recipe r : recipes) {
            boolean allTokensMatch = true;
            for (String token : tokens) {
                if (!recipeMatchesToken(r, token.toLowerCase())) {
                    allTokensMatch = false;
                    break;
                }
            }
            if (allTokensMatch) {
                results.add(r);
            }
        }
        
        return results;
    }

    /**
     * Helper method to check if a recipe contains an ingredient matching the query.
     */
    private boolean recipeContainsIngredient(Recipe recipe, String lowerQuery) {
        java.util.List<String> ingredientNames = getIngredientNames(recipe);
        for (String ingredientName : ingredientNames) {
            if (ingredientName.toLowerCase().contains(lowerQuery)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to check if a recipe matches a single token.
     * Token can match in recipe name or ingredient names.
     */
    private boolean recipeMatchesToken(Recipe recipe, String lowerToken) {
        // Check recipe name
        try {
            String recipeName = getRecipeName(recipe);
            if (recipeName.toLowerCase().contains(lowerToken)) {
                return true;
            }
        } catch (ReflectiveOperationException e) {
            // Continue to ingredient check
        }
        
        // Check ingredient names
        return recipeContainsIngredient(recipe, lowerToken);
    }

    /**
     * Helper method to get all ingredient names from a recipe using reflection.
     */
    private java.util.List<String> getIngredientNames(Recipe recipe) {
        try {
            java.lang.reflect.Field ingredientNamesField = Recipe.class.getDeclaredField("ingredientNames");
            ingredientNamesField.setAccessible(true);
            return (java.util.List<String>) ingredientNamesField.get(recipe);
        } catch (ReflectiveOperationException e) {
            return new ArrayList<>();
        }
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
