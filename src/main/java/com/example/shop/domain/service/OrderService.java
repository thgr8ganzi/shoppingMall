package com.example.shop.domain.service;

import com.example.shop.domain.entity.*;
import com.example.shop.domain.entity.item.Item;
import com.example.shop.domain.repository.ItemRepository;
import com.example.shop.domain.repository.MemberRepository;
import com.example.shop.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        // 앤티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 주문 상품 생성
        Orderitem orderitem = Orderitem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderitem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    // 취소
    @Transactional
    public void cancelOrder(Long orderId){
        // 주문조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancle();
    }

    // 검색
//    public List<Order> findOrder(OrderSearch orderSearch){
//        return orderRepository.findAll(orderSearch);
//    }
}










