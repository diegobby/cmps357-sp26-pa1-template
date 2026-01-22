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
- Recipe list is never null.
- Removal logic correctly identifies recipes by name.
- No implicit sorting is enforced at the data level.

---

## Stage 3: Searching and Sorting

**Goal**  
Allow users to locate and view recipes efficiently.

**Focus**
- Read-only operations on collections
- Separation of data storage from presentation logic

**Deliverables**
- Case-insensitive name search (partial matches)
- Front-end sorting of recipes by name

**Acceptance Criteria**
- Searching does not mutate stored data.
- Sorting is applied only when presenting results.
- Recipe insertion order remains unchanged internally.

---

## Stage 4: Shopping Cart Aggregation

**Goal**  
Generate a combined ingredient list from multiple recipes.

**Focus**
- Derived data views
- Normalization and aggregation rules

**Deliverables**
- Shopping cart aggregation logic
- Ingredient amount summation across recipes

**Acceptance Criteria**
- Ingredients with the same normalized name are summed.
- Aggregation does not modify original recipes.
- Output formatting follows the same amount rules as recipes.

---

## Stage 5: Persistence (JSON I/O)

**Goal**  
Persist and restore application state using files.

**Focus**
- Serialization
- Validation on load
- Isolation of I/O logic

**Deliverables**
- JSON writer for recipe lists
- JSON reader for recipe lists

**Acceptance Criteria**
- Recipe and ingredient order is preserved.
- Stored numeric values remain unformatted.
- Loaded data is validated before use.
- Invalid data is handled according to documented policy.

---

## Stage 6: User Interface Integration

**Goal**  
Provide a usable front-end for interacting with the system.

**Focus**
- Coordinating existing components
- Clear separation between UI and model logic

**Deliverables**
- Console-based UI or equivalent
- Commands for:
  - Listing recipes
  - Searching
  - Viewing recipe details
  - Building a shopping cart
  - Loading and saving data

**Acceptance Criteria**
- UI depends on service and model layers, not persistence internals.
- All previously implemented features are accessible through the UI.
- Sorting and formatting rules are consistently applied.

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

**Acceptance Criteria**
- Existing behavior remains unchanged unless explicitly extended.
- Documentation remains consistent with implementation.

---

## Guiding Principle
At the end of every stage, the application should:
- Compile
- Run
- Demonstrate clear, correct behavior for all implemented features

No stage should rely on partially implemented functionality from a later stage.

