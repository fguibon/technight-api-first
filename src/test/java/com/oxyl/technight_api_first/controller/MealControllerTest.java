package com.oxyl.technight_api_first.controller;

import com.oxyl.technight_api_first.themealdb.api.LookupApi;
import com.oxyl.technight_api_first.themealdb.model.MealDetailed;
import com.oxyl.technight_api_first.themealdb.model.MealDetailedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
class MealControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private LookupApi lookupApi;

    @BeforeEach
    void setup() {
        lookupApi = mock(LookupApi.class);
    }

    @Test
    void getMealByIdNotFound() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/meals/123"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No meal found"))
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
    }

    @Test
    void getMealById() throws Exception {
        when(lookupApi.lookupById(123)).thenReturn(new MealDetailedList().addMealsItem(new MealDetailed().idMeal("123").strMeal("pizza")));
        MvcResult mvcResult = this.mockMvc.perform(get("/meals/123"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello World!!!"))
                .andReturn();

        assertEquals("application/json;charset=UTF-8", mvcResult.getResponse().getContentType());
    }
}