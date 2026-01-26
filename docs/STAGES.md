# STAGES

## Purpose
This document outlines a staged implementation plan for the Recipe application.  
Each stage introduces a small, coherent set of concepts and responsibilities, allowing the project to grow incrementally while remaining testable and understandable at every step.

Stages are ordered intentionally and should be completed sequentially.

---

## Stage 1: Core Recipe Model

**Goal**  
Implement the foundational data model and required behavior for a single recipe.

**Focus**
- Class design
- Validation
- Numeric scaling and formatting rules

**Deliverables**
- `Ingredient` class
- `Recipe` class with:
  - `addIngredient(String ingredientName, double amount)`
  - `scaleToServings(int newServings)`
  - `totalIngredientCount()`
  - `toString()`

**Acceptance Criteria**
- Ingredients are stored in insertion order.
- Amount formatting rules are applied correctly in output.
- Scaling updates ingredient amounts proportionally without rounding stored values.
- Invalid inputs do not corrupt internal state.

**Progress**
- Complete (Stage 1 fully implemented and tested)

---

## Stage 2: Recipe Collection Management

**Goal**  
Manage multiple recipes as a collection.

**Focus**
- Aggregation of domain objects
- Basic collection operations

**Deliverables**
- `RecipeBook` (or equivalent) class
- Ability to:
  - Add recipes
  - Remove recipes by name
  - Retrieve all recipes

**Acceptance Criteria**
- Recipe list is never null. ✓
- Removal logic correctly identifies recipes by name. ✓
- No implicit sorting is enforced at the data level. ✓

**Progress**
- Complete (RecipeBook class created with all required operations)

---

## Stage 3: Searching and Sorting

**Goal**  
Allow users to locate, filter, and view recipes efficiently using flexible search criteria and stable presentation-time sorting.

**Focus**
- Read-only operations on recipe collections
- Separation of storage, querying, and presentation logic
- Predictable and stable search and sort behavior

### Deliverables

#### Searching
- **Case-insensitive recipe name search**
  - Partial (substring) matches
  - Leading and trailing whitespace in queries is ignored
- **Ingredient-based search**
  - Find recipes containing at least one ingredient whose name matches the query
  - Case-insensitive, partial matching
- **Multi-token search**
  - Queries containing multiple tokens (e.g., `garlic oil`)
  - A recipe matches if *all tokens* match somewhere in the recipe
    (recipe name or ingredient names)
- Search operations return a new list and never modify stored recipes.

#### Sorting
- **Front-end sorting by recipe name**
  - Case-insensitive ordering
  - Supports ascending and descending order
- **Stable and deterministic output**
  - Secondary comparison used when names compare equal ignoring case
  - Sorting does not permanently reorder stored data

### Acceptance Criteria
- Searching does not mutate stored data. ✓
- Sorting is applied only when presenting results. ✓
- Recipe insertion order remains unchanged internally. ✓
- Case-insensitive name search (partial matches) is supported. ✓
- Ingredient-based and multi-token searches return correct results. ◻︎
- Stable secondary sort behavior is implemented. ◻︎

### Progress
- Partially complete  
  - Case-insensitive name search implemented in `RecipeBook`
  - Front-end name-based sorting implemented via `RecipeSorter`
  - Ingredient-based search not yet implemented
  - Multi-token search not yet implemented
  - Secondary sort key for deterministic ordering not yet implemented

---

## Stage 4: Shopping Cart Aggregation

**Goal**  
Generate a combined ingredient list from multiple recipes.

**Focus**
- Derived data views
- Normalization and aggregation rules

### Deliverables

#### Shopping Cart Logic
- **Aggregate ingredients from multiple recipes**
  - Accept a collection of recipes as input
  - Produce a single combined ingredient list
- **Name normalization**
  - Case-insensitive matching of ingredient names
  - Ingredients with matching normalized names are combined
- **Amount summation**
  - Add quantities for ingredients with the same normalized name
  - Preserve ingredient units (if applicable)
- **Non-destructive operation**
  - Original recipes remain unchanged
  - Returns a new data structure representing the aggregated list

#### Output Formatting
- **Consistent amount display**
  - Follow the same formatting rules as Recipe output
  - Handle trailing zeros appropriately
- **Sorted presentation**
  - Ingredients displayed in a consistent order (e.g., alphabetical by name)

### Acceptance Criteria
- Ingredients with the same normalized name are summed.
- Aggregation does not modify original recipes.
- Output formatting follows the same amount rules as recipes.
- Shopping cart ingredients are displayed in a consistent order.

### Progress
- Not started

---

## Stage 5: Persistence (JSON I/O)

**Goal**  
Persist and restore application state using files.

**Focus**
- Serialization
- Validation on load
- Isolation of I/O logic

### Deliverables

#### JSON Writer
- **Serialize RecipeBook to JSON**
  - Convert recipe collection to valid JSON format
  - Preserve all recipe and ingredient data
- **Maintain structure**
  - Recipe order is preserved
  - Ingredient order within each recipe is preserved
- **Numeric precision**
  - Store ingredient amounts without formatting
  - Preserve full numeric precision for accurate restoration
- **File output**
  - Write JSON to specified file path
  - Handle I/O errors gracefully

#### JSON Reader
- **Deserialize JSON to RecipeBook**
  - Parse valid JSON files into recipe objects
  - Reconstruct recipe collection with original ordering
- **Validation**
  - Validate JSON structure before loading
  - Check for required fields (recipe name, servings, ingredients)
  - Validate data types and value ranges
- **Error handling**
  - Detect and report malformed JSON
  - Handle missing or invalid fields according to documented policy (see Validation Policy in DATA_MODEL.md)
  - Provide meaningful error messages for debugging

### Acceptance Criteria
- Recipe and ingredient order is preserved.
- Stored numeric values remain unformatted.
- Loaded data is validated before use.
- Invalid data is handled according to documented policy (see Validation Policy in DATA_MODEL.md).

### Progress
- Not started

---

## Stage 6: User Interface Integration

**Goal**  
Provide a usable front-end for interacting with the system.

**Focus**
- Coordinating existing components
- Clear separation between UI and model logic

### Deliverables

#### User Interface
- **Console-based interaction**
  - Command-line menu or prompt-based interface
  - Clear display of available commands
  - User-friendly input handling

#### Core Commands
- **List all recipes**
  - Display all recipes in the collection
  - Apply sorting (using RecipeSorter)
  - Show summary information (name, servings)
- **Search recipes**
  - Search by recipe name (case-insensitive, partial match)
  - Search by ingredient
  - Multi-token search support
  - Display search results with count
- **View recipe details**
  - Select a recipe by name or index
  - Display full recipe with all ingredients and amounts
  - Show formatted output using Recipe.toString()
- **Build shopping cart**
  - Select multiple recipes for aggregation
  - Generate combined ingredient list
  - Display aggregated results

#### Data Management Commands
- **Load recipes from file**
  - Specify file path
  - Load and validate JSON data
  - Report success or errors
- **Save recipes to file**
  - Specify output file path
  - Serialize current recipe collection
  - Confirm successful save

#### Input Handling
- **Robust parsing**
  - Handle invalid commands gracefully
  - Provide helpful error messages
  - Allow users to retry or exit

### Example CLI Output

```
=== Recipe Manager ===
1. List all recipes
2. Search recipes
3. View recipe details
4. Build shopping cart
5. Load recipes from file
6. Save recipes to file
7. Exit

Enter command: 1

All Recipes (3):
1. Pancakes (4 servings)
2. Garlic Bread (2 servings)
3. Tomato Soup (6 servings)

Enter command: 2
Enter search query: garlic

Search Results (1):
1. Garlic Bread (2 servings)

Enter command: 3
Enter recipe name: Garlic Bread

=== Garlic Bread ===
Servings: 2

Ingredients:
  - Bread: 1
  - Garlic: 4
  - Butter: 0.5
  - Parsley: 0.25

Enter command: 4
Enter recipe names (comma-separated): Pancakes, Garlic Bread

=== Shopping Cart ===
Aggregated Ingredients:
  - Bread: 1
  - Butter: 1.5
  - Eggs: 2
  - Flour: 2
  - Garlic: 4
  - Milk: 1.5
  - Parsley: 0.25

Enter command: 5
Enter file path: recipes.json
Successfully loaded 3 recipes from recipes.json

Enter command: 6
Enter file path: my_recipes.json
Successfully saved 3 recipes to my_recipes.json

Enter command: 7
Goodbye!
```

### Acceptance Criteria
- UI depends on service and model layers, not persistence internals.
- All previously implemented features are accessible through the UI.
- Sorting and formatting rules are consistently applied.
 - Search commands return correct results consistent with Stage 3.
 - Shopping cart aggregation returns correct results consistent with Stage 4.
 - Load and save commands report success and errors per the Validation Policy.
 - Invalid input does not crash the program; users receive helpful messages and can retry.

### Progress
- Not started

---

## Stage 7: Refinement and Extension

**Goal**  
Stabilize and improve the system.

**Focus**
- Code quality
- Edge cases
- Documentation

**Possible Additions**
- Improved error messages
- Additional search options
- Ingredient-level search
- Unit tests for aggregation and persistence
- Optional identifiers for recipes
- Refactor classes into package `cmps357.sp26` to align namespaces

**Acceptance Criteria**
- Existing behavior remains unchanged unless explicitly extended.
- Documentation remains consistent with implementation.

**Progress**
- Not started

---

## Guiding Principle
At the end of every stage, the application should:
- Compile
- Run
- Demonstrate clear, correct behavior for all implemented features

No stage should rely on partially implemented functionality from a later stage.

