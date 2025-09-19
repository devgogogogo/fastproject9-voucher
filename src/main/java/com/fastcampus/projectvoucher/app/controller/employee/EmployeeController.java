package com.fastcampus.projectvoucher.app.controller.employee;


import com.fastcampus.projectvoucher.app.controller.employee.request.EmployeeCreateRequest;
import com.fastcampus.projectvoucher.app.controller.employee.response.EmployeeResponse;
import com.fastcampus.projectvoucher.domain.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    //사원 생성
    @PostMapping("/api/v1/employee/")
    public Long create(@RequestBody EmployeeCreateRequest request) {
        return employeeService.create(request.name(),request.position(),request.department());
    }

    //사원 조회
    @GetMapping("/api/v1/employee/{no}")
    public EmployeeResponse employee(@PathVariable Long no) {
        return employeeService.get(no);
    }


}

