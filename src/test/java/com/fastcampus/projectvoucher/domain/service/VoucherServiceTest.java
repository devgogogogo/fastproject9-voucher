package com.fastcampus.projectvoucher.domain.service;

import com.fastcampus.projectvoucher.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.storage.voucher.VoucherEntity;
import com.fastcampus.projectvoucher.storage.voucher.VoucherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VoucherServiceTest {

    @Autowired
    VoucherService voucherService;

    @Autowired
    VoucherRepository voucherRepository;

    @DisplayName("발행된 상품권은 code로 조회할 수 있어야 된다.")
    @Test
    void test1() {
        //Given
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = LocalDate.now().plusDays(30);
        Long amount = 10000L;
        String code = voucherService.publish(validFrom, validTo, amount);

        //When
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        //Then

        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(validFrom);
        assertThat(voucherEntity.getValidTo()).isEqualTo(validTo);
        assertThat(voucherEntity.getAmount()).isEqualTo(amount);
    }

    @DisplayName("발행된 상품권은 사용 불가 처리 할 수 있다.")
    @Test
    void cancel() {
        //Given
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = LocalDate.now().plusDays(30);
        Long amount = 10000L;
        String code = voucherService.publish(validFrom, validTo, amount);

        //When
        voucherService.disable(code);
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        //Then
        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(validFrom);
        assertThat(voucherEntity.getValidTo()).isEqualTo(validTo);
        assertThat(voucherEntity.getAmount()).isEqualTo(amount);
        assertThat(voucherEntity.getCreatedAt()).isNotEqualTo(voucherEntity.getUpdatedAt());
    }
}