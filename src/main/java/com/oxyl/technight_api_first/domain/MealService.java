package com.oxyl.technight_api_first.domain;

import com.oxyl.technight_api_first.server.model.IngredientDto;
import com.oxyl.technight_api_first.server.model.MealDetailedDto;
import com.oxyl.technight_api_first.server.model.MealDto;
import com.oxyl.technight_api_first.server.model.MealListDto;
import com.oxyl.technight_api_first.themealdb.api.FilterApi;
import com.oxyl.technight_api_first.themealdb.api.LookupApi;
import com.oxyl.technight_api_first.themealdb.model.Meal;
import com.oxyl.technight_api_first.themealdb.model.MealDetailed;
import com.oxyl.technight_api_first.themealdb.model.MealList;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MealService {

    private final FilterApi filterApi;
    private final LookupApi lookupApi;

    public MealService(FilterApi filterApi, LookupApi lookupApi) {
        this.filterApi = filterApi;
        this.lookupApi = lookupApi;
    }

    public MealListDto getMeals(String ingredient, String category, String area) {
        return toMealListDto(filterApi.filter(ingredient, category, area));
    }

    public MealDetailedDto getMealById(String id) {
        return toMealDetailedDto(lookupApi.lookupById(Integer.valueOf(id)));
    }

    private MealListDto toMealListDto(MealList mealList) {
        if (mealList == null || mealList.getMeals() == null) return new MealListDto();
        return new MealListDto().items(mealList.getMeals().stream()
                .map(this::toMealDto)
                .toList());
    }

    private MealDto toMealDto(Meal meal) {
        if (meal.getIdMeal() != null) {
            return new MealDto().id(Long.valueOf(meal.getIdMeal())).name(meal.getStrMeal()).thumbnail(meal.getStrMealThumb());
        }
        return new MealDto();
    }

    private MealDetailedDto toMealDetailedDto(List<MealDetailed> mealDetailedList) {
        MealDetailed mealDetailed = mealDetailedList.getFirst();
        if (mealDetailed == null || mealDetailed.getIdMeal() == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No meal found");
        return new MealDetailedDto()
                .id(Long.valueOf(mealDetailed.getIdMeal()))
                .name(mealDetailed.getStrMeal())
                .area(mealDetailed.getStrArea())
                .category(mealDetailed.getStrCategory())
                .thumbnail(mealDetailed.getStrMealThumb())
                .instructions(mealDetailed.getStrInstructions())
                .addIngredientsItem(
                        new IngredientDto().name(mealDetailed.getStrIngredient1()).measure(mealDetailed.getStrMeasure1())
                );
    }
}
