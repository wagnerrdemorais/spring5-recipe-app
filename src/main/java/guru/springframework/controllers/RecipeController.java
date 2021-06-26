package guru.springframework.controllers;

import guru.springframework.repositories.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("/recipes")
public class RecipeController {


    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @RequestMapping
    public String listRecipes(Model model){
        model.addAttribute("recipes", recipeRepository.findByDescription("Perfect Guacamole"));
        return "recipes";
    }
}
