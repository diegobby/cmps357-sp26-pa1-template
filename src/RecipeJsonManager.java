import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * JSON persistence layer for saving and loading recipe collections.
 *
 * <p>Provides methods to serialize RecipeBook to JSON format and deserialize
 * from JSON files. Handles validation and error reporting according to the
 * Validation Policy defined in docs/DATA_MODEL.md.
 */
public class RecipeJsonManager {

    /**
     * Saves a RecipeBook to a JSON file.
     *
     * <p>The JSON structure follows the format defined in DATA_MODEL.md.
     * Preserves recipe and ingredient order from the RecipeBook.
     * Stores ingredient amounts with full precision (no formatting).
     *
     * @param recipeBook the RecipeBook to save
     * @param filePath   the path to write the JSON file to
     * @throws IOException if an I/O error occurs while writing
     */
    public static void saveToFile(RecipeBook recipeBook, String filePath) throws IOException {
        if (recipeBook == null) {
            throw new IllegalArgumentException("RecipeBook must not be null");
        }
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path must not be null or empty");
        }

        StringBuilder json = buildJsonString(recipeBook);
        Files.write(Paths.get(filePath), json.toString().getBytes());
    }

    /**
     * Loads a RecipeBook from a JSON file.
     *
     * <p>Validates all data according to the Validation Policy:
     * - Recipes must have non-blank names and positive servings
     * - Ingredients must have non-blank names and positive amounts
     * - Invalid data causes the entire load to fail (all-or-nothing)
     *
     * @param filePath the path to the JSON file to load
     * @return a new RecipeBook populated from the JSON file
     * @throws IOException            if an I/O error occurs while reading
     * @throws IllegalArgumentException if the JSON is invalid or validation fails
     */
    public static RecipeBook loadFromFile(String filePath) throws IOException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path must not be null or empty");
        }

        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        return parseJsonString(json);
    }

    /**
     * Builds a JSON string from a RecipeBook.
     */
    private static StringBuilder buildJsonString(RecipeBook recipeBook) {
        StringBuilder json = new StringBuilder();
        json.append("{\n  \"recipes\": [\n");

        List<Recipe> recipes = recipeBook.getAllRecipes();
        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);
            json.append("    {\n");
            json.append("      \"name\": ").append(escapeJsonString(getRecipeName(recipe))).append(",\n");
            json.append("      \"servings\": ").append(getRecipeServings(recipe)).append(",\n");
            json.append("      \"ingredients\": [\n");

            List<String> ingredientNames = getIngredientNames(recipe);
            List<Double> ingredientAmounts = getIngredientAmounts(recipe);

            for (int j = 0; j < ingredientNames.size(); j++) {
                String name = ingredientNames.get(j);
                Double amount = ingredientAmounts.get(j);
                json.append("        {\n");
                json.append("          \"name\": ").append(escapeJsonString(name)).append(",\n");
                json.append("          \"amount\": ").append(amount).append("\n");
                json.append("        }");
                if (j < ingredientNames.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }

            json.append("      ]\n");
            json.append("    }");
            if (i < recipes.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ]\n");
        json.append("}");

        return json;
    }

    /**
     * Parses a JSON string into a RecipeBook.
     */
    private static RecipeBook parseJsonString(String json) {
        RecipeBook recipeBook = new RecipeBook();

        try {
            // Simple JSON parsing without external libraries
            json = json.trim();
            if (!json.startsWith("{") || !json.endsWith("}")) {
                throw new IllegalArgumentException("Invalid JSON: root must be an object");
            }

            // Extract recipes array
            int recipesStart = json.indexOf("\"recipes\"");
            if (recipesStart == -1) {
                throw new IllegalArgumentException("Invalid JSON: missing 'recipes' field");
            }

            int arrayStart = json.indexOf("[", recipesStart);
            int arrayEnd = findMatchingBracket(json, arrayStart);

            if (arrayStart == -1 || arrayEnd == -1) {
                throw new IllegalArgumentException("Invalid JSON: malformed recipes array");
            }

            String recipesArrayJson = json.substring(arrayStart + 1, arrayEnd);
            List<Recipe> recipes = parseRecipesArray(recipesArrayJson);

            // All recipes validated, add them to the book
            for (Recipe recipe : recipes) {
                recipeBook.addRecipe(recipe);
            }

            return recipeBook;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON format: " + e.getMessage());
        }
    }

    /**
     * Parses the recipes array from JSON.
     */
    private static List<Recipe> parseRecipesArray(String arrayContent) {
        List<Recipe> recipes = new ArrayList<>();
        List<String> recipeJsons = splitJsonObjects(arrayContent);

        for (int i = 0; i < recipeJsons.size(); i++) {
            String recipeJson = recipeJsons.get(i);
            try {
                Recipe recipe = parseRecipeObject(recipeJson);
                recipes.add(recipe);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Recipe [" + i + "]: " + e.getMessage());
            }
        }

        return recipes;
    }

    /**
     * Parses a single recipe object from JSON.
     */
    private static Recipe parseRecipeObject(String recipeJson) {
        String name = extractJsonStringValue(recipeJson, "name");
        String servingsStr = extractJsonValue(recipeJson, "servings");

        // Validate recipe name
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("missing or invalid 'name' field");
        }

        // Validate servings
        int servings;
        try {
            servings = Integer.parseInt(servingsStr.trim());
            if (servings <= 0) {
                throw new IllegalArgumentException("'servings' must be greater than 0");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'servings' must be a valid integer");
        }

        Recipe recipe = new Recipe(name, servings);

        // Parse ingredients
        int ingredientsStart = recipeJson.indexOf("\"ingredients\"");
        if (ingredientsStart != -1) {
            int arrayStart = recipeJson.indexOf("[", ingredientsStart);
            int arrayEnd = findMatchingBracket(recipeJson, arrayStart);

            if (arrayStart != -1 && arrayEnd != -1) {
                String ingredientsJson = recipeJson.substring(arrayStart + 1, arrayEnd);
                List<String> ingredientJsons = splitJsonObjects(ingredientsJson);

                for (int i = 0; i < ingredientJsons.size(); i++) {
                    String ingredientJson = ingredientJsons.get(i);
                    try {
                        String ingredientName = extractJsonStringValue(ingredientJson, "name");
                        String amountStr = extractJsonValue(ingredientJson, "amount");

                        // Validate ingredient name
                        if (ingredientName == null || ingredientName.trim().isEmpty()) {
                            throw new IllegalArgumentException("ingredient [" + i + "]: missing or invalid 'name'");
                        }

                        // Validate amount
                        double amount;
                        try {
                            amount = Double.parseDouble(amountStr.trim());
                            if (amount <= 0) {
                                throw new IllegalArgumentException("ingredient [" + i + "]: 'amount' must be greater than 0");
                            }
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("ingredient [" + i + "]: 'amount' must be a valid number");
                        }

                        recipe.addIngredient(ingredientName, amount);
                    } catch (IllegalArgumentException e) {
                        throw e;
                    }
                }
            }
        }

        return recipe;
    }

    /**
     * Extracts a string value from JSON (removes quotes).
     */
    private static String extractJsonStringValue(String json, String key) {
        String value = extractJsonValue(json, key);
        if (value == null) {
            return null;
        }

        value = value.trim();
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
            // Unescape common JSON escapes
            value = value.replace("\\\"", "\"").replace("\\\\", "\\");
        }
        return value;
    }

    /**
     * Extracts a raw value from JSON for a given key.
     */
    private static String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) {
            return null;
        }

        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1) {
            return null;
        }

        int startIndex = colonIndex + 1;
        while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
            startIndex++;
        }

        // Find the end of the value
        int endIndex = startIndex;
        boolean inString = false;
        int braceDepth = 0;
        int bracketDepth = 0;

        while (endIndex < json.length()) {
            char c = json.charAt(endIndex);
            if (c == '"' && (endIndex == 0 || json.charAt(endIndex - 1) != '\\')) {
                inString = !inString;
                endIndex++;
            } else if (!inString) {
                if (c == '{') {
                    braceDepth++;
                    endIndex++;
                } else if (c == '}') {
                    if (braceDepth == 0) {
                        break;
                    }
                    braceDepth--;
                    endIndex++;
                } else if (c == '[') {
                    bracketDepth++;
                    endIndex++;
                } else if (c == ']') {
                    if (bracketDepth == 0) {
                        break;
                    }
                    bracketDepth--;
                    endIndex++;
                } else if ((c == ',' || c == '}' || c == ']') && braceDepth == 0 && bracketDepth == 0) {
                    break;
                } else {
                    endIndex++;
                }
            } else {
                endIndex++;
            }
        }

        String result = json.substring(startIndex, endIndex).trim();
        return result;
    }

    /**
     * Splits JSON objects or arrays by comma (at the top level).
     */
    private static List<String> splitJsonObjects(String json) {
        List<String> objects = new ArrayList<>();
        int startIndex = 0;
        int braceCount = 0;
        int bracketCount = 0;
        boolean inString = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
            } else if (!inString) {
                if (c == '{') {
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;
                } else if (c == '[') {
                    bracketCount++;
                } else if (c == ']') {
                    bracketCount--;
                } else if (c == ',' && braceCount == 0 && bracketCount == 0) {
                    String obj = json.substring(startIndex, i).trim();
                    if (!obj.isEmpty()) {
                        objects.add(obj);
                    }
                    startIndex = i + 1;
                }
            }
        }

        String lastObj = json.substring(startIndex).trim();
        if (!lastObj.isEmpty()) {
            objects.add(lastObj);
        }

        return objects;
    }

    /**
     * Finds the matching closing bracket for an opening bracket.
     */
    private static int findMatchingBracket(String json, int openIndex) {
        if (openIndex < 0 || json.charAt(openIndex) != '[') {
            return -1;
        }

        int depth = 1;
        boolean inString = false;

        for (int i = openIndex + 1; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
            } else if (!inString) {
                if (c == '[') {
                    depth++;
                } else if (c == ']') {
                    depth--;
                    if (depth == 0) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    /**
     * Escapes a string for JSON output.
     */
    private static String escapeJsonString(String str) {
        if (str == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder("\"");
        for (char c : str.toCharArray()) {
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    // Helper methods using reflection to access Recipe fields

    private static String getRecipeName(Recipe recipe) {
        try {
            java.lang.reflect.Field nameField = Recipe.class.getDeclaredField("name");
            nameField.setAccessible(true);
            return (String) nameField.get(recipe);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not access recipe name", e);
        }
    }

    private static int getRecipeServings(Recipe recipe) {
        try {
            java.lang.reflect.Field servingsField = Recipe.class.getDeclaredField("servings");
            servingsField.setAccessible(true);
            return servingsField.getInt(recipe);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not access recipe servings", e);
        }
    }

    private static List<String> getIngredientNames(Recipe recipe) {
        try {
            java.lang.reflect.Field ingredientNamesField = Recipe.class.getDeclaredField("ingredientNames");
            ingredientNamesField.setAccessible(true);
            return (List<String>) ingredientNamesField.get(recipe);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not access ingredient names", e);
        }
    }

    private static List<Double> getIngredientAmounts(Recipe recipe) {
        try {
            java.lang.reflect.Field ingredientAmountsField = Recipe.class.getDeclaredField("ingredientAmounts");
            ingredientAmountsField.setAccessible(true);
            return (List<Double>) ingredientAmountsField.get(recipe);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not access ingredient amounts", e);
        }
    }
}
