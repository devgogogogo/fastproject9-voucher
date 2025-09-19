package com.fastcampus.projectvoucher.domain.employee;

import com.fastcampus.projectvoucher.app.controller.request.EmployeeCreateRequest;
import com.fastcampus.projectvoucher.app.controller.response.EmployeeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final Map<Long, EmployeeResponse> employeeResponseMap = new HashMap<>();

//    //사원 생성
    public Long create(EmployeeCreateRequest request) {
        Long no = employeeResponseMap.size() + 1L;
        employeeResponseMap.put(no,new EmployeeResponse(no, request.name(), request.position(), request.department()));
        return no;
    }

    //사원 조회
    public EmployeeResponse get(Long no) {
        return employeeResponseMap.get(no);
    }


}
