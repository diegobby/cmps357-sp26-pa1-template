# TODO: cmps357-sp26-first-example

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

### Stage 3: Searching and Sorting (IN PROGRESS)
- [x] Implemented case-insensitive name search in `RecipeBook`
- [x] Created `RecipeSorter` utility (`src/RecipeSorter.java`) for sorting recipes by name
- [x] Added tests (test/RecipeSorterTest.java)
- [x] Created `Stage2Demo.java` to demonstrate functionality
- [ ] Implement ingredient-based search (find recipes by ingredient name; case-insensitive, partial match)
- [ ] Implement multi-token search (queries like `garlic oil` — all tokens must match somewhere in recipe name or ingredient names)
- [ ] Add stable secondary sort key for deterministic ordering when names compare equal ignoring case
- [ ] Ensure search operations return a new list and never mutate stored recipes

### Documentation Updates
- [x] Updated docs/STAGES.md to reflect progress on Stages 1, 2, and 3
- [x] Updated docs/SPEC.md with current API

## Next Steps (Future Stages)

### Stage 4: Shopping Cart Aggregation
- [ ] Implement ingredient aggregation across multiple recipes
- [ ] Handle ingredient normalization (case-insensitive matching)
- [ ] Sum amounts for matching ingredients
- [ ] Apply formatting rules to aggregated amounts

### Stage 5: Persistence (JSON I/O)
- [ ] Implement JSON writer for recipe lists
- [ ] Implement JSON reader for recipe lists
- [ ] Preserve recipe and ingredient order
- [ ] Validate loaded data
- [ ] Handle errors gracefully

### Stage 6: User Interface Integration
- [ ] Create console-based UI
- [ ] Commands for listing, searching, viewing recipes
- [ ] Shopping cart generation
- [ ] Load/save functionality

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
