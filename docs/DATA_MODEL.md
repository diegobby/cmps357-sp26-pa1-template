# DATA MODEL

## Overview
This document defines the core data structures used by the Recipe application, their responsibilities, invariants, and how they relate to one another. It is intentionally focused on data and rules, not UI behavior or control flow.

---

## Core Entities

### Ingredient
Represents a single ingredient entry within a recipe.

**Fields**
- `name : String`
- `amount : double`

**Invariants**
- `name` must be non-null and non-blank after trimming.
- `amount` must be greater than 0.
- `amount` is stored as a raw `double` with no rounding applied.

**Behavioral Rules**
- Ingredients are stored in the order they are added to a recipe.
- Formatting rules for `amount` are applied only at display time:
  - If the value is mathematically an integer (e.g. `2.0`), display as `2`.
  - Otherwise, display with up to two decimal places, trimming trailing zeros.
- Ingredient names are stored exactly as provided (after trimming).
  Normalization for comparison or aggregation is handled outside this class.

---

### Recipe
Represents a single recipe consisting of a name, a serving count, and a list of ingredients.

**Fields**
- `name : String`
- `servings : int`
- `ingredients : List<Ingredient>`

**Invariants**
- `name` must be non-null and non-blank after trimming.
- `servings` must be greater than 0.
- `ingredients` is never null.
- Ingredient order is preserved.

**Behavioral Rules**
- Ingredients are appended, not reordered or merged.
- Scaling a recipe:
  - Multiplies each ingredient’s `amount` by  
    `newServings / oldServings`.
  - Updates `servings` to `newServings`.
  - Does not round stored values.
- `totalIngredientCount()` returns the number of ingredient entries, not unique names.
- `toString()`:
  - Uses ingredient formatting rules.
  - Does not mutate internal state.

---

## Collection-Level Entities

### RecipeBook
Represents the application’s collection of recipes.

**Fields**
- `recipes : List<Recipe>`

**Invariants**
- `recipes` is never null.
- Recipe order reflects insertion order unless explicitly sorted for display.

**Rules**
- Recipes are uniquely identified by name.
- Removal operations are based on recipe name.
- Searching is case-insensitive and based on partial name matching.
- Sorting by name is applied at presentation time, not enforced on storage.

---

### Shopping Cart Aggregation
The shopping cart is a derived view, not a persisted entity.

**Conceptual Model**
- Aggregated ingredients are represented as:
  - `Map<String, Double>` or equivalent structure
  - Key: normalized ingredient name
  - Value: summed ingredient amount

**Normalization Rules**
- Trim leading and trailing whitespace.
- Comparison is case-insensitive.
- Internal spacing is preserved unless explicitly normalized later.

**Aggregation Rules**
- Ingredients from multiple recipes with the same normalized name are summed.
- Formatting rules are applied only when displaying the cart.

---

## Persistence Model (JSON)

### Top-Level Structure
```json
{
  "recipes": [ ... ]
}
```

### Recipe Object
```json
{
  "name": "Recipe Name",
  "servings": 4,
  "ingredients": [
    { "name": "Flour", "amount": 2.5 },
    { "name": "Sugar", "amount": 1.0 }
  ]
}
```

**Persistence Rules**
  - Validation rules for names and amounts are re-applied.
  - Invalid data handling follows the Validation Policy.


## Validation Policy

This policy defines how JSON input is validated during load and how invalid data is handled.

### Required Fields and Constraints
- Recipe
  - `name`: non-null, non-blank after trimming
  - `servings`: integer > 0
  - `ingredients`: array (may be empty)
- Ingredient
  - `name`: non-null, non-blank after trimming
  - `amount`: numeric > 0

### Unknown or Extra Fields
- Extra fields are ignored during load and do not cause failure.

### Failure Handling
- If any required field is missing or any constraint is violated in the input:
  - The load operation fails and returns an error.
  - No partial data is applied to the current in-memory state (all-or-nothing).

### Error Reporting
- Errors should include a clear reason (e.g., missing `name`, invalid `amount`) and, when feasible, the location within the input (e.g., recipe index).

- Formatting is a presentation concern and must never affect stored values.
- Ingredient identity within a recipe is positional, not semantic.
- Aggregation and normalization logic are intentionally separated from core entities.
- Future extensions (IDs, tags, categories) should not break the existing JSON structure.

