# Instruction.md

## Overview
This assignment continues the Recipe application beyond the in-class work.

- **Stages 1–2** have already been completed together in class.
- **Stage 3** was completed partially. Updated requiremrents are given below. 
- **Stages 4 through 6** must be completed independently.
- The goal is to move from a working model + collection features to a small, well-structured application.

You are expected to follow the project architecture and specifications provided in the `docs/` directory.

Each Stage (3, 4, 5, and 6) must be accomponied by a Demo java file following the example in `src/Stage2Demo.java`.

---

## Stage 3: Advanced Searching and Sorting

Stage 3 was partially completed in class and is included here as a reference for what your codebase should already support.

At the end of the in-class work, the project includes:
- A working **case-insensitive recipe name search** using partial (substring) matching.
- A clear separation between **stored recipe order** and **presentation-time sorting**.
- A dedicated sorting utility used by the front end to display recipes alphabetically by name.

These features establish the baseline for searching and sorting and ensure that collection operations are read-only and non-destructive.

The items listed below are **required extensions** to this baseline.  
They build directly on the in-class implementation and must be completed as part of this stage, even if the initial in-class version does not yet support them.

### Search capabilities (advanced)
Your project must support the following search behaviors:

- **Case-insensitive name search**
  - Partial matches (contains)
  - Leading/trailing whitespace in the query is ignored

- **Ingredient-based search**
  - Find recipes that contain an ingredient whose name matches the query (case-insensitive, partial match)
  - This search must not modify the recipe or ingredient lists

- **Multiple-token queries**
  - A query like `garlic oil` should be treated as multiple tokens
  - Recipes match if all tokens match somewhere in the recipe (name or ingredient list), using case-insensitive partial matching

- **Search result stability**
  - Searching is read-only
  - Stored recipe order remains unchanged
  - Results are returned as a new list

### Sorting capabilities (advanced)
Your project should already support sorting at presentation time, including:

- **Sort by recipe name (A–Z / Z–A)**
  - Case-insensitive ordering
  - Stable sorting preferred

- **Secondary sort keys**
  - If recipe names compare equal ignoring case, use a secondary key (such as original name, or insertion order) to ensure consistent output

- **No storage mutation**
  - Sorting must not permanently reorder the underlying stored list unless you explicitly document that behavior (default expectation: do not mutate)


---

## Additional Stages Required to Complete

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

