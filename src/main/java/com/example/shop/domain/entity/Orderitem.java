package com.example.shop.domain.entity;

import com.example.shop.domain.entity.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orderitem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    // 생성 메소드
    public static Orderitem createOrderItem(Item item, int orderPrice, int count){
        Orderitem orderitem = new Orderitem();
        orderitem.setItem(item);
        orderitem.setOrderPrice(orderPrice);
        orderitem.setCount(count);

        item.removeStock(count);
        return orderitem;
    }

    // 비즈니스 로직
    public void cancel() {
        getItem().addStock(count);
    }

    // 조회로직
    // 주문상품 전체 가격 조회회
   public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
