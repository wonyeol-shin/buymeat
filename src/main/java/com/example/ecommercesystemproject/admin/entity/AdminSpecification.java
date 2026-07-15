package com.example.ecommercesystemproject.admin.entity;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;

public class AdminSpecification {

    // name이 null이 아닐경우 where에 추가
    public static Specification<Admin> equalName(String name) {
        return (root, query, criteriaBuilder) ->
                // 조건에 null이나 공백이 들어오면 없는걸로 취급
                (name == null || name.isBlank())
                        ? null
                        : criteriaBuilder.equal(root.get("name"), name);
    }

    // email이 null이 아닐경우 where에 추가
    public static Specification<Admin> equalEmail(String email) {
        return (root, query, criteriaBuilder) ->
                (email == null || email.isBlank())
                        ? null
                        : criteriaBuilder.equal(root.get("email"), email);
    }

    // role이 null이 아닐경우 where에 추가
    public static Specification<Admin> equalRole(Role role) {
        return (root, query, criteriaBuilder) ->
                role == null
                        ? null
                        : criteriaBuilder.equal(root.get("role"), role);
    }

    // status가 null이 아닐경우 where에 추가
    public static Specification<Admin> equalStatus(Status status) {
        return (root, query, criteriaBuilder) ->
                status == null
                        ? null
                        : criteriaBuilder.equal(root.get("status"), status);
    }
}
