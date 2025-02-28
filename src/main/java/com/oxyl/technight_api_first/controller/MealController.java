package com.oxyl.technight_api_first.controller;

import com.oxyl.technight_api_first.domain.MealService;
import com.oxyl.technight_api_first.server.api.MealsApi;
import com.oxyl.technight_api_first.server.model.MealListDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MealController implements MealsApi {
    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @Override
    public ResponseEntity<MealListDto> getMeals(String ingredient, String category, String area) {
        return ResponseEntity.ok(mealService.getMeals(ingredient, category, area));
    }
}
