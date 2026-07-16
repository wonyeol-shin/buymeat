package com.example.ecommercesystemproject.order.entity;

import com.example.ecommercesystemproject.common.ServiceException;
import org.springframework.http.HttpStatus;

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
            throw new ServiceException("이미 배송 완료된 주문입니다.", HttpStatus.BAD_REQUEST);
        }
    },
    CANCELED {
        @Override
        public OrderStatus next() {
            throw new ServiceException("취소된 주문은 상태를 변경할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    };

    public abstract OrderStatus next();
}