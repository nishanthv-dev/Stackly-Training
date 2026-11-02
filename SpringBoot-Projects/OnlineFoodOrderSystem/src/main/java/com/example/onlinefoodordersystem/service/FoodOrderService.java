package com.example.onlinefoodordersystem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.onlinefoodordersystem.model.FoodOrder;

@Service
public class FoodOrderService {

    private List<FoodOrder> orders = new ArrayList<>();

    private int nextOrderId = 1;

    // Add Order
    public FoodOrder addOrder(FoodOrder order) {
        order.setOrderId(nextOrderId++);
        orders.add(order);
        return order;
    }

    // Get All Orders
    public List<FoodOrder> getAllOrders() {
        return orders;
    }

    // Get Order By ID
    public FoodOrder getOrderById(int orderId) {
        for (FoodOrder order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        return null;
    }

    // Update Order
    public FoodOrder updateOrder(int orderId, FoodOrder updatedOrder) {
        FoodOrder existingOrder = getOrderById(orderId);

        if (existingOrder != null) {
            existingOrder.setCustomerName(updatedOrder.getCustomerName());
            existingOrder.setFoodItem(updatedOrder.getFoodItem());
            existingOrder.setQuantity(updatedOrder.getQuantity());
            existingOrder.setPrice(updatedOrder.getPrice());

            return existingOrder;
        }

        return null;
    }

    // Delete Order
    public boolean deleteOrder(int orderId) {
        FoodOrder order = getOrderById(orderId);

        if (order != null) {
            orders.remove(order);
            return true;
        }

        return false;
    }
    
    
 // PATCH - Partial Update
    public FoodOrder patchOrder(int orderId, FoodOrder updatedOrder) {

        FoodOrder existingOrder = getOrderById(orderId);

        if (existingOrder != null) {

            if (updatedOrder.getCustomerName() != null) {
                existingOrder.setCustomerName(updatedOrder.getCustomerName());
            }

            if (updatedOrder.getFoodItem() != null) {
                existingOrder.setFoodItem(updatedOrder.getFoodItem());
            }

            if (updatedOrder.getQuantity() > 0) {
                existingOrder.setQuantity(updatedOrder.getQuantity());
            }

            if (updatedOrder.getPrice() != null) {
                existingOrder.setPrice(updatedOrder.getPrice());
            }

            return existingOrder;
        }

        return null;
    }
}