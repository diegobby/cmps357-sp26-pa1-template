import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class for sorting recipes.
 *
 * <p>Sorting is applied only for presentation purposes and does not modify
 * the original recipe collection.
 */
public class RecipeSorter {

    /**
     * Returns a new list of recipes sorted by name (case-insensitive).
     *
     * <p>The original list is not modified. Sorting is case-insensitive
     * using natural alphabetical order.
     *
     * @param recipes the list of recipes to sort
     * @return a new sorted list
     */
    public static List<Recipe> sortByName(List<Recipe> recipes) {
        if (recipes == null) {
            return new ArrayList<>();
        }
        
        List<Recipe> sorted = new ArrayList<>(recipes);
        Collections.sort(sorted, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe r1, Recipe r2) {
                try {
                    String name1 = getRecipeName(r1);
                    String name2 = getRecipeName(r2);
                    return name1.compareToIgnoreCase(name2);
                } catch (ReflectiveOperationException e) {
                    return 0;
                }
            }
        });
        
        return sorted;
    }

    /**
     * Helper method to get recipe name using reflection.
     * This is needed because Recipe's name field is private.
     */
    private static String getRecipeName(Recipe recipe) throws ReflectiveOperationException {
        java.lang.reflect.Field nameField = Recipe.class.getDeclaredField("name");
        nameField.setAccessible(true);
        return (String) nameField.get(recipe);
    }
}
