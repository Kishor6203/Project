package com.food.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.IngredientCategory;
import com.food.IngredientsItem;
import com.food.Restaurant;
import com.food.repository.IngredientCategoryRepository;
import com.food.repository.IngredientItemRepository;

@Service
public class IngredientServiceImp implements IngredientService{
	
	@Autowired
	private IngredientItemRepository ingredientItemRepository;
	
	@Autowired
	private IngredientCategoryRepository ingredientCategoryRepository;
	
	private RestaurantService restaurantService;

	@Override
	public IngredientCategory createIngredientCategory(String name, long restaurantId) throws Exception {
		Restaurant restaurant=restaurantService.findRestaurantById(restaurantId);
		IngredientCategory category=new IngredientCategory();
		category.setRestaurant(restaurant);
		category.setName(name);
		return ingredientCategoryRepository.save(category);
	}

	@Override
	public IngredientCategory findIngredientCategoryById(long id) throws Exception {
		Optional<IngredientCategory> opt=ingredientCategoryRepository.findById(id);
		
		if(opt.isEmpty()) {
			throw new Exception("ingredient category not found");
		}
		return opt.get();
	}

	@Override
	public List<IngredientCategory> findIngredientCategoryByRestaurantId(long id) throws Exception {
	    restaurantService.findRestaurantById(id);
		return ingredientCategoryRepository.findByRestaurantId(id);
	}

	@Override
	public IngredientsItem createIngredientItem(long restaurantId, String ingredientName, long categoryId)
			throws Exception {
		Restaurant restaurant=restaurantService.findRestaurantById(restaurantId);
		IngredientCategory category=findIngredientCategoryById(categoryId);
		IngredientsItem item=new IngredientsItem();
		item.setName(ingredientName);
		item.setRestaurant(restaurant);
		item.setCategory(category);
		
		IngredientsItem ingredient=ingredientItemRepository.save(item);
		category.getIngredients().add(ingredient);
		return ingredient;
	}

	@Override
	public List<IngredientsItem> findRestaurantsIngredients(long restaurantId) {
		
		return ingredientItemRepository.findByRestaurantId(restaurantId);
	}

	@Override
	public IngredientsItem updateStock(long id) throws Exception {
		Optional<IngredientsItem> optionalIngredientsItem=ingredientItemRepository.findById(id);
		if(optionalIngredientsItem.isEmpty()) {
			throw new Exception("ingredient not found");
		}
		IngredientsItem ingredientsItem=optionalIngredientsItem.get();
		ingredientsItem.setInStoke(!ingredientsItem.isInStoke());
		return ingredientItemRepository.save(ingredientsItem);
	}

}
