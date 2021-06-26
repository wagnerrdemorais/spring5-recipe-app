package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static java.math.BigDecimal.valueOf;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public DataLoader(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Recipe perfectGuacamole = new Recipe();
        perfectGuacamole.setDescription("Perfect Guacamole");

        perfectGuacamole.getIngredients().add(createIngredient("Ripe avocado", 2L, perfectGuacamole, "Teaspoon"));
        perfectGuacamole.getIngredients().add(createIngredient("Salt", (1L/4L), perfectGuacamole, "Teaspoon"));
        perfectGuacamole.getIngredients().add(createIngredient("Lemon juice", 1L, perfectGuacamole, "Teaspoon"));
        perfectGuacamole.getIngredients().add(createIngredient("Serrano Chiles", 2L, perfectGuacamole, "Teaspoon"));
        perfectGuacamole.getIngredients().add(createIngredient("Cilantro", 2L, perfectGuacamole, "Teaspoon"));
        perfectGuacamole.getIngredients().add(createIngredient("Black pepper", 1L, perfectGuacamole, "Dash"));
        perfectGuacamole.getIngredients().add(createIngredient("Ripe tomato", 1L/2L, perfectGuacamole, "Unit"));
        perfectGuacamole.getIngredients().add(createIngredient("Red radishes, or jicama, to garnish", 1L, perfectGuacamole, "Unit"));
        perfectGuacamole.getIngredients().add(createIngredient("Tortilla", 1L, perfectGuacamole, "Unit"));

        perfectGuacamole.setDifficulty(Difficulty.EASY);

        perfectGuacamole.setCategories(Set.of(categoryRepository.findByDescription("Mexican").get()));

        StringBuilder sb = new StringBuilder();
        sb.append("Cut the avocado: Cut the avocados in half. Remove the pit. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.");
        sb.append("Mash the avocado flesh: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)");
        sb.append("so on and so forth");
        perfectGuacamole.setDirections(sb.toString());

        perfectGuacamole.setCookTime(10);
        perfectGuacamole.setPrepTime(10);
        perfectGuacamole.setServings(2);

        perfectGuacamole.setSource("https://www.simplyrecipes.com/recipes/perfect_guacamole/");

        Notes notes = new Notes();
        notes.setRecipe(perfectGuacamole);
        notes.setRecipeNotes("Be careful handling chilis! If using, it's best to wear food-safe gloves. If no gloves are available, wash your hands thoroughly after handling, and do not touch your eyes or the area near your eyes for several hours afterwards");

        perfectGuacamole.setNotes(notes);

        Recipe savedPercectGuacamole = recipeRepository.save(perfectGuacamole);
        System.out.println("RecipeSaved: " + savedPercectGuacamole.getDescription());

    }

    private Ingredient createIngredient(String description, Long amount, Recipe recipe, String unitOfMeasure){
        return new IngredientBuilder()
                .description(description)
                .amount(valueOf(amount))
                .recipe(recipe)
                .unitOfMeasure(getUnitOfMeasure(unitOfMeasure))
                .build();
    }

    private UnitOfMeasure getUnitOfMeasure(String description){
        return unitOfMeasureRepository.findByDescription(description)
                .orElse(newUnitOfMeasure(description));
    }

    private UnitOfMeasure newUnitOfMeasure(String description){
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription(description);
        return unitOfMeasure;
    }

    private static class IngredientBuilder {

        private final Ingredient ingredient;
        IngredientBuilder() {
            this.ingredient = new Ingredient();
        }

        private IngredientBuilder description(String description) {
            this.ingredient.setDescription(description);
            return this;
        }

        private IngredientBuilder amount(BigDecimal amount){
            this.ingredient.setAmount(amount);
            return this;
        }

        private IngredientBuilder unitOfMeasure(UnitOfMeasure unitOfMeasure){
            this.ingredient.setUnitOfMeasure(unitOfMeasure);
            return this;
        }

        private IngredientBuilder recipe(Recipe recipe){
            this.ingredient.setRecipe(recipe);
            return this;
        }

        private Ingredient build(){
            return this.ingredient;
        }
    }
}
