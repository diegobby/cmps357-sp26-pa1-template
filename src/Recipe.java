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
    /**
     * Adds an ingredient and its amount to the recipe.
     *
     * <p>The ingredient is only added if both inputs are valid:
     * <ul>
     *   <li>{@code ingredientName} must be non-null and not blank</li>
     *   <li>{@code amount} must be greater than 0</li>
     * </ul>
     *
     * <p>If either input is invalid, this method leaves the recipe unchanged
     * and prints a debug message to standard output.
     *
     * @param ingredientName the name of the ingredient
     * @param amount the amount of the ingredient
     */
    public void addIngredient(String ingredientName, double amount) {
        if (ingredientName == null || ingredientName.trim().isEmpty()) {
            System.out.println("[DEBUG] Invalid ingredient name. Ingredient not added.");
            return;
        }

        if (amount <= 0) {
            System.out.println("[DEBUG] Invalid ingredient amount. Ingredient not added.");
            return;
        }

        ingredientNames.add(ingredientName);
        ingredientAmounts.add(amount);
    }


    /**
     * Returns the number of ingredient entries in this recipe.
     *
     * <p>This recipe stores ingredient data in parallel lists; the number of
     * entries is the size of the ingredient name list.
     *
     * @return the number of ingredients added to the recipe
     */
    public int totalIngredientCount() {
        // Return the number of ingredient entries (parallel lists length)
        return ingredientNames.size();
    }

    public void scaleToServings(int newServings) {
        /**
         * Scales all ingredient amounts proportionally to match {@code newServings}.
         *
         * @param newServings the target number of servings; must be positive
         * @throws IllegalArgumentException if {@code newServings} is not positive
         */
        if (newServings <= 0) {
            throw new IllegalArgumentException("newServings must be positive");
        }

        double factor = (double) newServings / this.servings;

        for (int i = 0; i < ingredientAmounts.size(); i++) {
            ingredientAmounts.set(i, ingredientAmounts.get(i) * factor);
        }

        this.servings = newServings;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (serves ").append(servings).append(")\n");

        for (int i = 0; i < ingredientNames.size(); i++) {
            String iname = ingredientNames.get(i);
            double amt = ingredientAmounts.get(i);
            sb.append("- ").append(formatAmount(amt)).append(" ").append(iname).append("\n");
        }

        return sb.toString();
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
        // Treat values extremely close to an integer as integers
        double rounded = Math.rint(x);
        if (Math.abs(x - rounded) < 1e-9) {
            return String.valueOf((long) rounded);
        }

        // Format with two decimals, then trim trailing zeros
        String s = String.format("%.2f", x);
        // remove trailing zeros and possible trailing decimal point
        if (s.indexOf('.') >= 0) {
            s = s.replaceAll("0+$", "");
            s = s.replaceAll("\\.$", "");
        }
        return s;
    }
}
