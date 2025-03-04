package com.oxyl.technight_api_first.controller;

import com.oxyl.technight_api_first.domain.MealService;
import com.oxyl.technight_api_first.server.api.MealsApi;
import com.oxyl.technight_api_first.server.model.MealListDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MealController implements MealsApi {
    private static final Logger logger = LoggerFactory.getLogger(MealController.class);
    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @Override
    public ResponseEntity<MealListDto> getMeals(String ingredient, String category, String area) {
        logger.info("[getMeals] {} {} {}", ingredient, category, area);
        if (ingredient == null && category == null && area == null) {
            return ResponseEntity.ok(new MealListDto());
        }
        return ResponseEntity.ok(mealService.getMeals(ingredient, category, area));
    }
}
