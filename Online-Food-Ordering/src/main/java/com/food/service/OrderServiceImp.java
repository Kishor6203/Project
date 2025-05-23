package com.food.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.Address;
import com.food.Cart;
import com.food.CartItem;
import com.food.Order;
import com.food.OrderItem;
import com.food.Restaurant;
import com.food.User;
import com.food.repository.AddressRepository;
import com.food.repository.OrderItemRepository;
import com.food.repository.OrderRepository;
import com.food.repository.UserRepository;
import com.food.request.OrderRequest;

@Service
public class OrderServiceImp implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private CartService cartService;

	public Order createOrder(OrderRequest order, User user) throws Exception {
	    Address shippingAddress = order.getDeliveryAddress();
	    Address savedAddress = addressRepository.save(shippingAddress);

	    if (!user.getAddresses().contains(savedAddress)) {
	        user.getAddresses().add(savedAddress);
	        userRepository.save(user);
	    }
	    
	    Restaurant restaurant = restaurantService.findRestaurantById(order.getRestaurantId());

	    Order createdOrder = new Order();
	    createdOrder.setCustomer(user);
	    createdOrder.setCreatedAt(new Date());
	    createdOrder.setOrderStatus("PENDING");
	    createdOrder.setDeliveryAddress(savedAddress);
	    createdOrder.setRestaurant(restaurant);

	    Cart cart =cartService.findCartByUserId(user.getId());
		
		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItem cartItem : cart.getItems()) {
		    OrderItem orderItem = new OrderItem();
		    orderItem.setFood(cartItem.getFood());
		    orderItem.setIngredients(cartItem.getIngredients());
		    orderItem.setQuantity(cartItem.getQuantity());
		    orderItem.setTotalPrice(cartItem.getTotalPrice());

		    OrderItem savedOrderItem = orderItemRepository.save(orderItem);
		    orderItems.add(savedOrderItem);
		}
		long totalPrice=cartService.calculateCartTotals(cart);

		createdOrder.setItems(orderItems);
		createdOrder.setTotalPrice(cart.getTotal());
		
		Order saveOrder=orderRepository.save(createdOrder);
		restaurant.getOrders().add(saveOrder);
		return createdOrder;
	}

	@Override
	public Order updateOrder(long orderId, String orderStatus) throws Exception {
		
		Order order = findOrderById(orderId);
		if (orderStatus.equals("OUT_FOR_DELIVERY") ||
			    orderStatus.equals("DELIVERED") ||
			    orderStatus.equals("COMPLETED") ||
			    orderStatus.equals("PENDING")) {
			    order.setOrderStatus(orderStatus);
			    return orderRepository.save(order);
			} else {
			    throw new Exception("Please select a valid order status");
			}
	}

	@Override
	public void cancleOrder(long orderId) throws Exception {
		
		Order order = findOrderById(orderId);
		orderRepository.deleteById(orderId);
		
	}

	@Override
	public List<Order> getUsersOrder(long userId) throws Exception {
		return orderRepository.findByCustomerId(userId);
	}

	@Override
	public List<Order> getRestaurantsOrder(long restaurantId, String orderStatus) throws Exception {
		List<Order> orders= orderRepository.findByRestaurantId(restaurantId);
		if(orderStatus!=null) {
			orders = orders.stream().filter(order -> order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());
		}
		return orders;
	}

	@Override
	public Order findOrderById(long orderId) throws Exception {
		Optional<Order> optionalOrder=orderRepository.findById(orderId);
		if(optionalOrder.isEmpty()) {
			throw new Exception("order not found");
		}
		return optionalOrder.get();
	}
	
	

}
