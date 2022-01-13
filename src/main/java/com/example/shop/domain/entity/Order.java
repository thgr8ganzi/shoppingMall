package com.example.shop.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Orderitem> orderitems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus staus; // 주문상태(ORDER, CANCLE)

    // 연관관계 메소드
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(Orderitem orderitem){
        orderitems.add(orderitem);
        orderitem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    // 생성 메소드
    public static Order createOrder(Member member, Delivery delivery, Orderitem... orderitems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(Orderitem orderitem: orderitems){
            order.addOrderItem(orderitem);
        }
        order.setStaus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    // 주문 취소
    // 비즈니스 로직
    public void cancle(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능 합니다.");
        }
        this.setStaus(OrderStatus.CANCEL);
        for(Orderitem orderitem : orderitems){
            orderitem.cancel();
        }
    }
    // 조회로직
    // 전체 주문 가격
    public int getTotalPrice(){
        int totalPrice = 0;
        for(Orderitem orderitem: orderitems){
            totalPrice += orderitem.getTotalPrice();
        }
        return totalPrice;
    }
}










