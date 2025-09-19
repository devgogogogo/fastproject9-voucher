package com.fastcampus.projectvoucher;

import com.fastcampus.projectvoucher.app.controller.response.EmployeeResponse;
import com.fastcampus.projectvoucher.domain.employee.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @DisplayName("회원 생성후 조회가 가능하다.")
    @Test
    void test() {
        //Given
        String name = "홍길동";
        String position = "사원 ";
        String department = "개발팀";

        //When
        Long no = employeeService.create(name, position, department);
        EmployeeResponse response = employeeService.get(no);

        //Then
        assertThat(response).isNotNull();
        assertThat(response.no()).isEqualTo(no);
        assertThat(response.name()).isEqualTo(name);
        assertThat(response.position()).isEqualTo(position);
        assertThat(response.department()).isEqualTo(department);

        System.out.println(response);
    }
}
