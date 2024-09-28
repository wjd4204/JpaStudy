package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.Orderstatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import jpabook.jpashop.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }

        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
//        List<OrderDto> collect = orders.stream()
//                .map(o -> new OrderDto(o))
//                .toList();

        return orders.stream()
                .map(OrderDto::new)
                .toList();
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithItem(); // 이 부분만 수정하여 알맞은 데이터를 가지고 오게끔 할 수 있다.

        for (Order order : orders) {
            System.out.println("order ref=" + order + " id=" + order.getId());
        }
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .toList();

        return result;
    }

    /*
    batchsize 설정 시 실행되는 쿼리들
    1. fetch join으로 엮인 엔티티까지 한 번에 조회되는 Order 엔티티
    2. BatchSize에 맞게 가져와지는 OrderItem 엔티티
    3. 한 번에 모든 id를 가져오는 Item 엔티티(기존에는 Item엔티티를 일일이 가져왔다.)
    원래 6번이었던 쿼리의 개수가 3개로 줄어든다!
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit); // 'ToOne'으로만 매핑되는 엔티티를 페치 조인한 엔티티 목록

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .toList();

        return result;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();
    }

    @Getter
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private Orderstatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        // 완전히 엔티티와의 관계를 끊어야 함!
        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .toList();
        }
    }

    @Getter
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
