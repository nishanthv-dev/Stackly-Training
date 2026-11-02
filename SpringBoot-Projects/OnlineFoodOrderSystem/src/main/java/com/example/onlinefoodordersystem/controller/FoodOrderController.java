package com.example.onlinefoodordersystem.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.onlinefoodordersystem.model.FoodOrder;
import com.example.onlinefoodordersystem.service.FoodOrderService;

@RestController
@RequestMapping("/api/orders")
public class FoodOrderController {

    @Autowired
    private FoodOrderService foodOrderService;

    // POST - Add Order
    @PostMapping
    public ResponseEntity<FoodOrder> addOrder(
            @Valid @RequestBody FoodOrder order) {

        FoodOrder savedOrder = foodOrderService.addOrder(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    // GET - Get All Orders
    @GetMapping
    public List<FoodOrder> getAllOrders() {
        return foodOrderService.getAllOrders();
    }

    // GET - Get Order By ID
    @GetMapping("/{id}")
    public ResponseEntity<FoodOrder> getOrderById(@PathVariable int id) {

        FoodOrder order = foodOrderService.getOrderById(id);

        if (order != null) {
            return ResponseEntity.ok(order);
        }

        return ResponseEntity.notFound().build();
    }

    // PUT - Update Order
    @PutMapping("/{id}")
    public ResponseEntity<FoodOrder> updateOrder(
            @PathVariable int id,
            @Valid @RequestBody FoodOrder order) {

        FoodOrder updatedOrder = foodOrderService.updateOrder(id, order);

        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        }

        return ResponseEntity.notFound().build();
    }

    // DELETE - Delete Order
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable int id) {

        boolean deleted = foodOrderService.deleteOrder(id);

        if (deleted) {
            return ResponseEntity.ok("Order deleted successfully");
        }

        return ResponseEntity.notFound().build();
    }
    
    
 // PATCH - Partial Update
    @PatchMapping("/{id}")
    public ResponseEntity<FoodOrder> patchOrder(
            @PathVariable int id,
            @RequestBody FoodOrder order) {

        FoodOrder updatedOrder = foodOrderService.patchOrder(id, order);

        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        }

        return ResponseEntity.notFound().build();
    }
}