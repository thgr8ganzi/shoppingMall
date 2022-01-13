package com.example.shop.service;

import com.example.shop.domain.entity.Address;
import com.example.shop.domain.entity.Member;
import com.example.shop.domain.entity.Order;
import com.example.shop.domain.entity.OrderStatus;
import com.example.shop.domain.entity.item.Book;
import com.example.shop.domain.entity.item.Item;
import com.example.shop.domain.exception.NotEnoughStockException;
import com.example.shop.domain.repository.OrderRepository;
import com.example.shop.domain.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{

        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("책이름1");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품주문시 상태 order", OrderStatus.ORDER, getOrder.getStaus());
        assertEquals("주문한 상품 종류수가 정확해야 한다.", 1, getOrder.getOrderitems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어든다.", 8, book.getStockQuantity());

    }


    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{

        Member member = createMember();
        Item item = createBook("책이름1", 10000, 10);
        int orderCount = 11;

        orderService.order(member.getId(), item.getId(), orderCount);

        fail("재고 수량 부족 예외");

    }

    @Test
    public void 주문취소() {
      Member member = createMember();
      Book item = createBook("책1", 10000, 10);

      int orderCount = 2;
      Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

      orderService.cancelOrder(orderId);

      Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품주문시 상태 cancel", OrderStatus.CANCEL, getOrder.getStaus());
        assertEquals("주문 취소된 상품은 재고 증가", 10, item.getStockQuantity());

    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }
}
