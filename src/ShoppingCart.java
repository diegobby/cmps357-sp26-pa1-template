import java.util.*;

/**
 * Represents a shopping cart that aggregates ingredients from multiple recipes.
 *
 * <p>A shopping cart combines ingredients from multiple recipes using case-insensitive
 * name normalization. Ingredients with the same normalized name are summed, and the
 * result is stored with no formatting applied (preserving full precision).
 *
 * <p>This class is non-destructive: it does not modify the original recipes.
 * The aggregation is a derived view computed at creation time.
 */
public class ShoppingCart {
    
    /**
     * Map of normalized ingredient names to their total amounts.
     * Key: lowercase ingredient name (trimmed)
     * Value: total amount (unformatted double)
     */
    private final Map<String, Double> aggregatedIngredients;
    
    /**
     * List to preserve insertion order of unique normalized ingredient names.
     * This ensures consistent ordering in output.
     */
    private final List<String> ingredientOrder;

    /**
     * Creates a new shopping cart from a collection of recipes.
     *
     * <p>Aggregates all ingredients from the provided recipes, combining those
     * with the same normalized name (case-insensitive, trimmed).
     *
     * @param recipes the list of recipes to aggregate; must not be null
     * @throws IllegalArgumentException if recipes is null
     */
    public ShoppingCart(List<Recipe> recipes) {
        if (recipes == null) {
            throw new IllegalArgumentException("Recipes list must not be null");
        }
        
        this.aggregatedIngredients = new LinkedHashMap<>();
        this.ingredientOrder = new ArrayList<>();
        
        // Aggregate ingredients from all recipes
        for (Recipe recipe : recipes) {
            aggregateRecipe(recipe);
        }
    }

    /**
     * Adds all ingredients from a recipe to the cart, aggregating by normalized name.
     *
     * @param recipe the recipe to add ingredients from
     */
    private void aggregateRecipe(Recipe recipe) {
        if (recipe == null) {
            return;
        }
        
        List<String> ingredientNames = getIngredientNames(recipe);
        List<Double> ingredientAmounts = getIngredientAmounts(recipe);
        
        if (ingredientNames == null || ingredientAmounts == null) {
            return;
        }
        
        for (int i = 0; i < ingredientNames.size() && i < ingredientAmounts.size(); i++) {
            String name = ingredientNames.get(i);
            Double amount = ingredientAmounts.get(i);
            
            if (name != null && amount != null && amount > 0) {
                addIngredient(name, amount);
            }
        }
    }

    /**
     * Adds or aggregates an ingredient into the cart.
     *
     * @param ingredientName the ingredient name
     * @param amount the ingredient amount
     */
    private void addIngredient(String ingredientName, double amount) {
        String normalized = normalizeIngredientName(ingredientName);
        
        if (!aggregatedIngredients.containsKey(normalized)) {
            ingredientOrder.add(normalized);
            aggregatedIngredients.put(normalized, 0.0);
        }
        
        aggregatedIngredients.put(normalized, aggregatedIngredients.get(normalized) + amount);
    }

    /**
     * Normalizes an ingredient name for comparison and aggregation.
     *
     * <p>Normalization includes:
     * - Trimming leading and trailing whitespace
     * - Converting to lowercase
     *
     * @param name the ingredient name to normalize
     * @return the normalized name
     */
    private String normalizeIngredientName(String name) {
        if (name == null) {
            return "";
        }
        return name.trim().toLowerCase();
    }

    /**
     * Returns the total number of unique aggregated ingredients.
     *
     * @return the number of unique ingredients in the cart
     */
    public int size() {
        return aggregatedIngredients.size();
    }

    /**
     * Returns a map of all aggregated ingredients (normalized name -> total amount).
     *
     * <p>The returned map is a copy; modifications to it will not affect
     * the internal cart state.
     *
     * @return a map of normalized ingredient names to their total amounts
     */
    public Map<String, Double> getAggregatedIngredients() {
        return new LinkedHashMap<>(aggregatedIngredients);
    }

    /**
     * Returns the total amount for a specific ingredient (by normalized name).
     *
     * @param ingredientName the ingredient name to look up (case-insensitive)
     * @return the total amount, or 0 if not found
     */
    public double getAmount(String ingredientName) {
        String normalized = normalizeIngredientName(ingredientName);
        return aggregatedIngredients.getOrDefault(normalized, 0.0);
    }

    /**
     * Returns a sorted list of aggregated ingredients for display.
     *
     * <p>Ingredients are sorted alphabetically by normalized name.
     *
     * @return a list of ShoppingCartItem entries, sorted by name
     */
    public List<ShoppingCartItem> getSortedIngredients() {
        List<ShoppingCartItem> items = new ArrayList<>();
        
        for (String normalizedName : ingredientOrder) {
            double amount = aggregatedIngredients.get(normalizedName);
            items.add(new ShoppingCartItem(normalizedName, amount));
        }
        
        // Sort alphabetically
        items.sort((a, b) -> a.name.compareTo(b.name));
        
        return items;
    }

    /**
     * Returns a formatted string representation of the shopping cart.
     *
     * <p>Ingredients are sorted alphabetically by name and formatted according
     * to the amount formatting rules (integers without decimals, others up to 2 decimals).
     *
     * @return a formatted string of all aggregated ingredients
     */
    @Override
    public String toString() {
        List<ShoppingCartItem> items = getSortedIngredients();
        StringBuilder sb = new StringBuilder();
        
        for (ShoppingCartItem item : items) {
            sb.append("- ").append(formatAmount(item.amount)).append(" ").append(item.name).append("\n");
        }
        
        return sb.toString();
    }

    /**
     * Formats an amount according to Recipe specifications.
     *
     * <p>Rules:
     * - If the amount is an integer (e.g., 2.0), print without decimals: "2"
     * - Otherwise, print with up to 2 decimals, trimming trailing zeros
     *
     * @param amount the amount to format
     * @return the formatted amount as a string
     */
    private String formatAmount(double amount) {
        // Check if it's an integer value
        if (amount == Math.floor(amount) && !Double.isInfinite(amount)) {
            return String.format("%.0f", amount);
        }
        
        // Format with 2 decimals and trim trailing zeros
        String formatted = String.format("%.2f", amount);
        // Remove trailing zeros
        formatted = formatted.replaceAll("0*$", "");
        // Remove trailing decimal point if no decimals remain
        formatted = formatted.replaceAll("\\.$", "");
        
        return formatted;
    }

    /**
     * Helper method to get ingredient names from a recipe using reflection.
     */
    private List<String> getIngredientNames(Recipe recipe) {
        try {
            java.lang.reflect.Field ingredientNamesField = Recipe.class.getDeclaredField("ingredientNames");
            ingredientNamesField.setAccessible(true);
            return (List<String>) ingredientNamesField.get(recipe);
        } catch (ReflectiveOperationException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Helper method to get ingredient amounts from a recipe using reflection.
     */
    private List<Double> getIngredientAmounts(Recipe recipe) {
        try {
            java.lang.reflect.Field ingredientAmountsField = Recipe.class.getDeclaredField("ingredientAmounts");
            ingredientAmountsField.setAccessible(true);
            return (List<Double>) ingredientAmountsField.get(recipe);
        } catch (ReflectiveOperationException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Inner class representing a single item in the shopping cart.
     */
    public static class ShoppingCartItem {
        public final String name;
        public final double amount;

        /**
         * Creates a new shopping cart item.
         *
         * @param name the ingredient name
         * @param amount the total amount
         */
        public ShoppingCartItem(String name, double amount) {
            this.name = name;
            this.amount = amount;
        }
    }
}
