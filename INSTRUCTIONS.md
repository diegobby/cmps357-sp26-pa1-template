# Instruction.md

## Overview
This assignment continues the Recipe application beyond the in-class work.

- **Stages 1–2** have already been completed together in class.
- **Stage 3** was completed partially. Updated requiremrents are given below. 
- **Stages 4 through 6** must be completed independently.
- The goal is to move from a working model + collection features to a small, well-structured application.

You are expected to follow the project architecture and specifications provided in the `docs/` directory.

Each Stage (3,4,5, and 6) must be accomponied by a Demo java file following the example in `src/Stage2Demo.java`.

---

## Stage 3: Searching and Sorting

**Goal**  
Allow users to locate, filter, and view recipes efficiently using flexible search criteria and stable presentation-time sorting.

**Focus**
- Read-only operations on recipe collections
- Separation of storage, querying, and presentation logic
- Predictable and stable search and sort behavior

**Deliverables**

### Searching
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

### Sorting
- **Front-end sorting by recipe name**
  - Case-insensitive ordering
  - Supports ascending and descending order
- **Stable and deterministic output**
  - Secondary comparison used when names compare equal ignoring case
  - Sorting does not permanently reorder stored data

**Acceptance Criteria**
- Searching does not mutate stored data. ✓
- Sorting is applied only when presenting results. ✓
- Recipe insertion order remains unchanged internally. ✓
- Case-insensitive name search (partial matches) is supported. ✓
- Ingredient-based and multi-token searches return correct results. 
- Stable secondary sort behavior is implemented. 

**Progress**
- Partially complete  
  - Case-insensitive name search implemented in `RecipeBook`
  - Front-end name-based sorting implemented via `RecipeSorter`
  - Ingredient-based search not yet implemented
  - Multi-token search not yet implemented
  - Secondary sort key for deterministic ordering not yet implemented


---

## Required Stages to Complete (You)

You must complete **Stages 4–6** as defined in `STAGES.md`.

### Stage 4: Shopping Cart Aggregation
Implement a shopping cart feature that:
- Combines ingredients from multiple recipes.
- Aggregates ingredient amounts using normalized ingredient names.
- Does not modify the original recipes.
- Uses the same formatting rules defined in `SPEC.md`.

---

### Stage 5: Persistence (JSON I/O)
Add file support so the application can:
- Save the full recipe list to a JSON file.
- Load the recipe list from a JSON file.
- Preserve recipe and ingredient order.
- Validate data when loading.

All file I/O must be isolated from the UI and domain model.

---

### Stage 6: User Interface Integration
Extend the front end so users can:
- List recipes
- Search recipes (name and ingredient)
- View recipe details
- Build a shopping cart
- Load and save recipes

A console-based UI is sufficient.  
UI code must depend on service classes, not on persistence or internal data structures.

---

## Documentation Requirements

All required documentation files **must be present and completed**:

```
docs/
├─ SPEC.md
├─ ARCHITECTURE.md
├─ DATA_MODEL.md
├─ STAGES.md
└─ TESTING.md
```

Your implementation must be consistent with these documents.
If you change behavior, the documentation must be updated to match.

Incomplete or missing documentation will result in point deductions.

---

## Submission Requirements

### GitHub Classroom
- All work must be **committed and pushed** to your GitHub Classroom repository.
- Commits should be small and meaningful.
- Your repository must compile and run from a clean clone.

### Moodle Upload
- In addition to GitHub submission, you must upload a **ZIP file** of your completed project to Moodle.
- The ZIP must include:
  - all source files
  - all documentation files
  - no compiled artifacts (`out/`, `.class`, etc.)

---

## Deadline
- The GitHub repository must be pushed **before the deadline**.
- The ZIP file must be uploaded to Moodle **before the same deadline**.

Late submissions follow the course late policy.

---

## Expectations and Evaluation
You will be evaluated on:
- Correctness of functionality for Stages 4–6
- Adherence to the documented architecture
- Code clarity and organization
- Completeness and accuracy of documentation
- Successful build and execution

This assignment is intended to reinforce working from a specification, extending existing code, and reasoning about structure beyond a single class.

