package com.fastcampus.projectvoucher.domain.employee;

import com.fastcampus.projectvoucher.app.controller.employee.response.EmployeeResponse;
import com.fastcampus.projectvoucher.storage.employee.EmployeeEntity;
import com.fastcampus.projectvoucher.storage.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

//    //사원 생성
    public Long create(String name, String position, String department) {

        EmployeeEntity employeeEntity = employeeRepository.save(new EmployeeEntity(name, position, department));
        return employeeEntity.getId();
    }

    //사원 조회
    public EmployeeResponse get(Long no) {
        EmployeeEntity employeeEntity = employeeRepository.findById(no).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
       return new EmployeeResponse(employeeEntity.getId(),employeeEntity.getName(),employeeEntity.getPosition(),employeeEntity.getDepartment(),employeeEntity.getCreatedAt(),employeeEntity.getUpdatedAt());
    }


}
