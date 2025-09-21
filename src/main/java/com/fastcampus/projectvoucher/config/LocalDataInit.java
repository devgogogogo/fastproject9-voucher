package com.fastcampus.projectvoucher.config;

import com.fastcampus.projectvoucher.storage.voucher.ContractEntity;
import com.fastcampus.projectvoucher.storage.voucher.ContractRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class LocalDataInit {

    private final ContractRepository contractRepository;


    @PostConstruct
    public void init() {
        //계약 데이터 초기화
        contractRepository.save(new ContractEntity("CT0001", LocalDate.now().minusDays(7), LocalDate.now().plusDays(7), 366 * 5));
        contractRepository.save(new ContractEntity("CT0010", LocalDate.now().minusDays(30), LocalDate.now().minusDays(7), 366 * 5));
        /**
         * 애플리케이션이 실행될때 @Configuration 이 클래스가 실행된다.
         * 서비스 테스트할때 유용하게 쓸수 있을것 같다.
         */
    }
}
