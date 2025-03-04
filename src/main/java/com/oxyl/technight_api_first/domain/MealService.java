package com.oxyl.technight_api_first.domain;

import com.oxyl.technight_api_first.server.model.MealDto;
import com.oxyl.technight_api_first.server.model.MealListDto;
import com.oxyl.technight_api_first.themealdb.api.FilterApi;
import com.oxyl.technight_api_first.themealdb.model.Meal;
import com.oxyl.technight_api_first.themealdb.model.MealList;
import org.springframework.stereotype.Service;

@Service
public class MealService {

    private final FilterApi filterApi;

    public MealService(FilterApi filterApi) {
        this.filterApi = filterApi;
    }

    public MealListDto getMeals(String ingredient, String category, String area) {
        return toMealListDto(filterApi.filter(ingredient, category, area));
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
}
