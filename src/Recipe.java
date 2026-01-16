// src/Recipe.java
import java.util.ArrayList;

public class Recipe {
    private final String name;
    private int servings;

    // Parallel lists to keep Day-1 Java simple (no extra classes required).
    private final ArrayList<String> ingredientNames = new ArrayList<>();
    private final ArrayList<Double> ingredientAmounts = new ArrayList<>();

    public Recipe(String name, int servings) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must be non-empty");
        }
        if (servings <= 0) {
            throw new IllegalArgumentException("servings must be positive");
        }
        this.name = name;
        this.servings = servings;
    }

    public void addIngredient(String ingredientName, double amount) {
        // TODO:
        // - Validate ingredientName non-null and non-blank
        // - Validate amount > 0
        // - Add to ingredientNames and ingredientAmounts in matching positions
        throw new UnsupportedOperationException("TODO");
    }

    public int totalIngredientCount() {
        // TODO: return number of ingredient entries
        throw new UnsupportedOperationException("TODO");
    }

    public void scaleToServings(int newServings) {
        // TODO:
        // - If newServings <= 0 throw IllegalArgumentException
        // - Compute factor = (double) newServings / servings
        // - Multiply each ingredient amount by factor
        // - Update servings
        throw new UnsupportedOperationException("TODO");
    }

    public String toString() {
        // TODO:
        // Return:
        // <name> (serves <servings>)
        // - <amount> <ingredient>
        // ...
        //
        // Use formatAmount(double) helper for printing amounts.
        throw new UnsupportedOperationException("TODO");
    }

    private String formatAmount(double x) {
        // Spec:
        // - If x is an integer value, print without decimals.
        // - Else print with up to 2 decimals, trimming trailing zeros.
        //
        // Examples:
        // 200.0 -> "200"
        // 7.5 -> "7.5"
        // 0.625 -> "0.63"
        // 1.333 -> "1.33"
        //
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
