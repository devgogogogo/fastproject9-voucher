package com.fastcampus.projectvoucher.app.controller;


import com.fastcampus.projectvoucher.app.controller.request.EmployeeCreateRequest;
import com.fastcampus.projectvoucher.app.controller.response.EmployeeResponse;
import com.fastcampus.projectvoucher.domain.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    //사원 생성
    @PostMapping("/api/v1/employee/")
    public Long create(@RequestBody EmployeeCreateRequest request) {
        Long no = employeeService.create(request);
        return no;
    }

    //사원 조회
    @GetMapping("/api/v1/employee/{no}")
    public EmployeeResponse employee(@PathVariable Long no) {
        return employeeService.get(no);
    }


}

