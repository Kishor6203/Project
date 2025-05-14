package com.food;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	
	@JsonIgnore
	@ManyToOne
	private Restaurant restaurant;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<IngredientsItem> ingredients = new ArrayList<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public List<IngredientsItem> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<IngredientsItem> ingredients) {
		this.ingredients = ingredients;
	}

	@Override
	public String toString() {
		return "IngredientCategory [id=" + id + ", name=" + name + ", restaurant=" + restaurant + ", ingredients="
				+ ingredients + "]";
	}

	
	
	

}
