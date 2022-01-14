package com.example.shop.domain;

import com.example.shop.domain.entity.*;
import com.example.shop.domain.entity.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;

        public void dbInit1(){
            Member member = new Member();
            member.setName("userA");
            member.setAddress(new Address("서울", "1", "1111"));
            em.persist(member);

            Book book = new Book();
            book.setName("JPA1");
            book.setPrice(10000);
            book.setStockQuantity(100);
            em.persist(book);

            Book book2 = new Book();
            book2.setName("JPA1");
            book2.setPrice(10000);
            book2.setStockQuantity(100);
            em.persist(book2);

            Orderitem orderitem1 = Orderitem.createOrderItem(book, 10000, 1);
            Orderitem orderitem2 = Orderitem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderitem1, orderitem2);
            em.persist(order);
        }

        public void dbInit2(){
            Member member = new Member();
            member.setName("userB");
            member.setAddress(new Address("북한", "22", "2222"));
            em.persist(member);

            Book book = new Book();
            book.setName("JPA1");
            book.setPrice(10000);
            book.setStockQuantity(100);
            em.persist(book);

            Book book2 = new Book();
            book2.setName("JPA1");
            book2.setPrice(10000);
            book2.setStockQuantity(100);
            em.persist(book2);

            Orderitem orderitem1 = Orderitem.createOrderItem(book, 10000, 1);
            Orderitem orderitem2 = Orderitem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderitem1, orderitem2);
            em.persist(order);
        }
    }



}
