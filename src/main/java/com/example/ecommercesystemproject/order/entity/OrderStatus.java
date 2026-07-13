package com.example.ecommercesystemproject.order.entity;

public enum OrderStatus {
    PREPARING {
        @Override
        public OrderStatus next() {
            return SHIPPING;
        }
    },
    SHIPPING {
        @Override
        public OrderStatus next() {
            return DELIVERED;
        }
    },
    DELIVERED {
        @Override
        public OrderStatus next() {
            throw new IllegalStateException("이미 배송 완료된 주문입니다.");
        }
    },
    CANCELED {
        @Override
        public OrderStatus next() {
            throw new IllegalStateException("취소된 주문은 상태를 변경할 수 없습니다.");
        }
    };

    public abstract OrderStatus next();
}