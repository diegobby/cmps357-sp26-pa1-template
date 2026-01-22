# cmps357-sp26-first-example

## Project Requirements
 - Java IDE such as **IntelliJ Community Edition** [Windows](https://download.jetbrains.com/idea/ideaIC-2025.2.6.1.exe?_cl=MTsxOzE7c2tod09qdG1hNXpQbERWcGpJWGlZUUZ6Ym0zUW0yVTdpSGxRWHE3RU85RVJPaGtGd2tQSmNRTW13NGpSb0lXbTs=&_gl=1*1uc1dra*_gcl_au*MTI0NzgwMjk5NS4xNzY4NTgwNjIy*FPAU*MTI0NzgwMjk5NS4xNzY4NTgwNjIy*_ga*MTk5NDUxODMxNy4xNzY4NTgwNjIy*_ga_9J976DJZ68*czE3Njg1ODA2MTkkbzEkZzEkdDE3Njg1ODA2NjkkajEwJGwwJGgw) [Linux](https://download.jetbrains.com/idea/ideaIC-2025.2.6.1-aarch64.tar.gz?_gl=1*njar6m*_gcl_au*MTI0NzgwMjk5NS4xNzY4NTgwNjIy*FPAU*MTI0NzgwMjk5NS4xNzY4NTgwNjIy*_ga*MTk5NDUxODMxNy4xNzY4NTgwNjIy*_ga_9J976DJZ68*czE3Njg1ODA2MTkkbzEkZzEkdDE3Njg1ODA2NjkkajEwJGwwJGgw) [macOS (Intel)](https://download.jetbrains.com/idea/ideaIC-2025.2.6.1.dmg?_gl=1*ue2abp*_gcl_au*MTI0NzgwMjk5NS4xNzY4NTgwNjIy*FPAU*MTI0NzgwMjk5NS4xNzY4NTgwNjIy*_ga*MTk5NDUxODMxNy4xNzY4NTgwNjIy*_ga_9J976DJZ68*czE3Njg1ODA2MTkkbzEkZzEkdDE3Njg1ODA2NjkkajEwJGwwJGgw) [macOS (Apple Silicone)](https://download.jetbrains.com/idea/idea-2025.3.1.1-aarch64.dmg?_gl=1*ue2abp*_gcl_au*MTI0NzgwMjk5NS4xNzY4NTgwNjIy*FPAU*MTI0NzgwMjk5NS4xNzY4NTgwNjIy*_ga*MTk5NDUxODMxNy4xNzY4NTgwNjIy*_ga_9J976DJZ68*czE3Njg1ODA2MTkkbzEkZzEkdDE3Njg1ODA2NjkkajEwJGwwJGgw)
 - Java SDK such as Azul Zulu
    - Available to download from with IntelliJ, or
    - Available [here](https://www.azul.com/downloads/?version=java-17-lts&package=jdk#zulu)
 - Git [Windows](https://github.com/git-for-windows/git/releases/download/v2.52.0.windows.1/Git-2.52.0-64-bit.exe) [Linux](https://git-scm.com/install/linux) [macOS](https://git-scm.com/install/mac)

## Day 1 Spec-to-Implementation (Recipe Class)

Clone the repository (repo), open it in IntelliJ (Get from VCS), run `Main`.

Our job today is to implement the TODOs in `Recipe.java` using an LLM so that the program output matches the spec in `SPEC.md`.

Suggested workflow:
1. Read [SPEC.md](docs/SPEC.md)
2. Run `Main` (it should compile, but output will be wrong until you implement TODOs)
3. Implement one method at a time
4. Make small commits with meaningful messages

**Note**: Students will not be able to Push changes (save to the server) because they are not contributors to this repository. Normally, sample code would first be Forked into a student’s own repository and then cloned locally. A fork is a server-side copy of a repository created under your own GitHub account, allowing you to make changes, Commit them, and Push updates without affecting the original project. We will use that workflow later in the course, though students are welcome to Fork this repo if they with to experiment.

The original files and workflow in this repository (Day 1) were created with the help of a [ChatGPT Session](https://chatgpt.com/share/696a5966-6000-8011-a070-388192adf348).

## Day 2+ From Single Class to Application

Once the `Recipe` class is complete and behaving correctly, we will begin moving beyond a single-class exercise and toward a small but complete application.

At this stage, the focus shifts from:
- implementing isolated methods  
to:
- designing interactions between classes
- managing collections of objects
- separating responsibilities across files

You should expect the project to grow incrementally rather than all at once.

### What changes conceptually
- `Recipe` becomes a *domain object*, not the whole program.
- New classes will manage collections of recipes, searching, sorting, and aggregation.
- Output formatting and user interaction will move out of `Recipe`.

### What stays the same
-  [SPEC.md](docs/SPEC.md) remains the source of truth for required behavior.
- Formatting rules for ingredients do not change.
- Small, testable steps remain the goal.

### Upcoming focus areas
Over the next several days, we will introduce:
- managing multiple recipes at once
- searching and sorting recipes
- combining ingredients across recipes into a shopping list
- reading and writing data to files using JSON
- thinking in terms of architecture instead of individual methods

Each of these additions will be guided by the documents in the `docs/` directory:
-  [ARCHITECTURE.md](docs/ARCHITECTURE.md) for overall structure
-  [DATA_MODEL.md](docs/DATA_MODEL.md) for how data is represented
-  [STAGES.md](docs/STAGES.md) for incremental milestones

By the end of this sequence, you should be comfortable reading a spec, using an LLM to assist with implementation, and reasoning about how small classes fit into a larger design.




## Repository Structure 
```
cmps357-sp26-first-example/
├─ docs/
   ├─ SPEC.md
   ├─ ARCHITECTURE.md
   ├─ DATA_MODEL.md
   ├─ STAGES.md
└─ src/
   ├─ /main/java/...
   ├─ Main.java
   ├─ model/
      ├─ Recipe.java
      └─ Ingredient.java
   ├─ service/
      ├─ RecipeBook.java
      ├─ ShoppingCart.java
      ├─ Search.java
      └─ Sorts.java
   ├─ io/
      └─ RecipeJsonStore.java
   ├─ ui/
      └─ ConsoleUI.java   (or JavaFXUI.java, or WebUI adapter later)
```

The updated files and workflow in this repository (Day 2+) were created with the help of a [ChatGPT Session](https://chatgpt.com/share/6972a438-2e10-8011-9a61-eef40179e2c6).
