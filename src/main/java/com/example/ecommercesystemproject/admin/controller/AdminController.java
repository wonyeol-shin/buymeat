package com.example.ecommercesystemproject.admin.controller;

import com.example.ecommercesystemproject.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;


}
