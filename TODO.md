# TODO: cmps357-sp26-pa1-template

## Work Completed

### Stage 1: Core Recipe Model (COMPLETE)
- [x] Implemented `totalIngredientCount()` with Javadoc
- [x] Implemented `scaleToServings(int)` with validation, scaling, and Javadoc
- [x] Implemented `toString()` with Javadoc (prints name, servings, and ingredient lines)
- [x] Implemented `formatAmount(double)` to print integers without decimals and otherwise up to 2 decimals
- [x] Verified `Main` runs and output matches `SPEC.md` expected output
- [x] Added minimal tests in test/RecipeTest.java covering count, scaling, and amount formatting
- [x] Added `toPrettyString()` delegating to `toString()`
- [x] Switched debug prints in `addIngredient` from `System.out` to `System.err`
- [x] Expanded tests in test/RecipeTest.java to cover `toPrettyString()`, additional scaling edge-cases
- [x] All tests pass locally via the existing build tasks
- [x] Updated docs/SPEC.md to document `toPrettyString()` method

### Stage 2: Recipe Collection Management (COMPLETE)
- [x] Created `Ingredient` class (src/Ingredient.java)
  - Value object with name and amount fields
  - Validation for non-null/non-blank names and positive amounts
  - Immutable with scale() method for creating scaled copies
- [x] Created `RecipeBook` class (src/RecipeBook.java)
  - Add recipes to collection
  - Remove recipes by name (case-sensitive)
  - Retrieve all recipes
  - Search by name (case-insensitive, partial matches)
- [x] Added comprehensive tests (test/IngredientTest.java, test/RecipeBookTest.java)
- [x] All Stage 2 acceptance criteria met

### Stage 3: Searching and Sorting (COMPLETE)
- [x] Implemented case-insensitive name search in `RecipeBook`
- [x] Created `RecipeSorter` utility (`src/RecipeSorter.java`) for sorting recipes by name
- [x] Added tests (test/RecipeSorterTest.java)
- [x] Created `Stage2Demo.java` to demonstrate functionality
- [x] Implemented ingredient-based search (find recipes by ingredient name; case-insensitive, partial match)
- [x] Implemented multi-token search (queries like `garlic oil` — all tokens must match somewhere in recipe name or ingredient names)
- [x] Added stable secondary sort key for deterministic ordering when names compare equal ignoring case
- [x] Ensured search operations return a new list and never mutate stored recipes
- [x] Created `Stage3Demo.java` and `Stage3Test.java`

### Stage 4: Shopping Cart Aggregation (COMPLETE)
- [x] Implemented `ShoppingCart` class (`src/ShoppingCart.java`)
- [x] Added ingredient aggregation with case-insensitive normalization
- [x] Implemented amount summation for matching ingredient names
- [x] Added proper amount formatting (integers without decimals, others up to 2 decimals)
- [x] Created alphabetical sorting for aggregated ingredients
- [x] Verified non-destructive operation (original recipes unchanged)
- [x] Created `Stage4Demo.java` to demonstrate functionality
- [x] Created comprehensive `Stage4Test.java` with 25 passing tests

### Documentation Updates
- [x] Updated docs/STAGES.md to reflect progress on Stages 1, 2, and 3
- [x] Updated docs/SPEC.md with current API

## Next Steps (Future Stages)

### Stage 5: Persistence (JSON I/O) (COMPLETE)
- [x] Implemented `RecipeJsonManager` class (`src/RecipeJsonManager.java`)
- [x] Implemented JSON writer for serializing RecipeBook to JSON files
- [x] Implemented JSON reader for deserializing JSON files to RecipeBook
- [x] Preserved recipe and ingredient order during save/load
- [x] Stored numeric values with full precision (no formatting)
- [x] Implemented comprehensive data validation on load
- [x] Implemented error handling with clear error messages and location info
- [x] Applied all-or-nothing validation policy (no partial loads)
- [x] Created `Stage5Demo.java` to demonstrate functionality
- [x] Created comprehensive `Stage5Test.java` with 24 passing tests

### Stage 6: User Interface Integration (COMPLETE)
- [x] Created `RecipeManagerUI` class (`src/RecipeManagerUI.java`) with console-based menu
- [x] Implemented all core commands:
  - [x] List all recipes (with sorting)
  - [x] Search recipes (by name, ingredient, multi-token)
  - [x] View recipe details (full recipe display)
  - [x] Build shopping cart (aggregation and display)
  - [x] Load recipes from JSON file
  - [x] Save recipes to JSON file
  - [x] Exit functionality
- [x] Implemented robust error handling and user-friendly messages
- [x] Created `Stage6Demo.java` to demonstrate full application workflow
- [x] Created comprehensive `Stage6Test.java` with 21 passing tests
- [x] All UI components properly integrated with model and persistence layers

### Stage 7: Refinement and Extension
- [ ] Optional: Refactor Recipe to use Ingredient class instead of parallel lists
- [ ] Improved error messages
- [ ] Additional search options
- [ ] Code quality improvements

## Notes
- Recipe class continues to use parallel lists for Day-1 simplicity
- Ingredient class is available for future refactoring if needed
- All existing functionality remains working and tested
- Stage 2 scaffolding complete with RecipeBook, Ingredient, and search/sort utilities

## Verification commands
To compile and run locally:

```bash
javac -d bin src\*.java
java -cp bin Main

# compile and run tests (no external deps)
javac -d bin -cp bin src\*.java test\RecipeTest.java
java -cp bin RecipeTest
```

## File references
- Implementation: [src/Recipe.java](src/Recipe.java)
- Runner: [src/Main.java](src/Main.java)
- Spec: [SPEC.md](SPEC.md)
- README: [README.md](README.md)


---

If you want, I can (pick one):
- add `toPrettyString()` delegating to `toString()` now,
- update `SPEC.md` to match the current implementation,
- add unit tests under a `test/` folder, or
- add Javadoc for `toString()` and other public methods.
