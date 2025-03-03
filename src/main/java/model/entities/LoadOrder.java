package model.entities;

import model.exceptions.NoSuchElement;

import java.util.List;

public class LoadOrder {
    private String orderCharger;
    private List<Order> orders;

    public LoadOrder(List<Order> listOrders) {
        if(!listOrders.isEmpty()) {
            this.orders = listOrders;
        }else {
            throw new IllegalArgumentException("The list cannot are empty!");
        }

    }

    public LoadOrder(List<Order> listproducts, String orderCharger) throws NoSuchElement {
        if(!listproducts.isEmpty()) {
            this.orders = listproducts;
            this.orderCharger = orderCharger;
        }else {
            throw new NoSuchElement();
        }

    }

    public LoadOrder() {}


    public String getOrderCharger() {
        return orderCharger;
    }

    public void setOrderCharger(String orderCharger) {
        this.orderCharger = orderCharger;
    }

    public void setOrder(Order order) {
        this.orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public boolean remeveOrder(Order order) {
        return orders.remove(order);
    }
}
