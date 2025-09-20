package com.fastcampus.projectvoucher.domain.service;

import com.fastcampus.projectvoucher.common.dto.RequestContext;
import com.fastcampus.projectvoucher.common.type.RequesterType;
import com.fastcampus.projectvoucher.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.storage.voucher.VoucherEntity;
import com.fastcampus.projectvoucher.storage.voucher.VoucherHistoryEntity;
import com.fastcampus.projectvoucher.storage.voucher.VoucherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VoucherServiceV2Test {

    @Autowired
    VoucherService voucherService;

    @Autowired
    VoucherRepository voucherRepository;

    @DisplayName("발행된 상품권은 code로 조회할 수 있어야 된다.")
    @Test
    void test1() {
        //Given
        RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = LocalDate.now().plusDays(30);
        VoucherAmountType amount = VoucherAmountType.KRW_30000;

        //When
        String code = voucherService.publishV2(requestContext, validFrom, validTo, amount);
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        //Then
        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(validFrom);
        assertThat(voucherEntity.getValidTo()).isEqualTo(validTo);
        assertThat(voucherEntity.getAmount()).isEqualTo(amount);

        //history
        VoucherHistoryEntity voucherHistoryEntity = voucherEntity.getHistories().get(0);
        assertThat(voucherHistoryEntity.getOrderId()).isNotNull();
        assertThat(voucherHistoryEntity.getRequesterType()).isEqualTo(requestContext.requesterType());
        assertThat(voucherHistoryEntity.getRequesterId()).isEqualTo(requestContext.requesterId());
        assertThat(voucherHistoryEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherHistoryEntity.getDescription()).isEqualTo("테스트 발행");
    }

    @DisplayName("발행된 상품권은 사용 불가 처리 할 수 있다.")
    @Test
    void cancel() {
        //Given
        RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = LocalDate.now().plusDays(30);
        VoucherAmountType amount = VoucherAmountType.KRW_30000;
        String code = voucherService.publishV2(requestContext, validFrom, validTo, amount);

        RequestContext disableRequestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        //When
        voucherService.disableV2(disableRequestContext, code);
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        //Then
        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(validFrom);
        assertThat(voucherEntity.getValidTo()).isEqualTo(validTo);
        assertThat(voucherEntity.getAmount()).isEqualTo(amount);
        assertThat(voucherEntity.getCreatedAt()).isNotEqualTo(voucherEntity.getUpdatedAt());

        System.out.println("### voucherEntity.createAt() = " + voucherEntity.getCreatedAt());
        System.out.println("### voucherEntity.updatedAt() = " + voucherEntity.getUpdatedAt());

        //history
        VoucherHistoryEntity voucherHistoryEntity = voucherEntity.getHistories().get(voucherEntity.getHistories().size() - 1);
        assertThat(voucherHistoryEntity.getOrderId()).isNotNull();
        assertThat(voucherHistoryEntity.getRequesterType()).isEqualTo(disableRequestContext.requesterType());
        assertThat(voucherHistoryEntity.getRequesterId()).isEqualTo(disableRequestContext.requesterId());
        assertThat(voucherHistoryEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);
        assertThat(voucherHistoryEntity.getDescription()).isEqualTo("테스트 사용 불가");
    }

    @DisplayName("발행된 상품권은 사용할 수 있다.")
    @Test
    void ableVoucher() {
        //Given
        RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = LocalDate.now().plusDays(30);
        VoucherAmountType amount = VoucherAmountType.KRW_30000;
        String code = voucherService.publishV2(requestContext, validFrom, validTo, amount);

        RequestContext useRequestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        //When
        voucherService.useV2(useRequestContext, code);
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        //Then
        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.USE);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(validFrom);
        assertThat(voucherEntity.getValidTo()).isEqualTo(validTo);
        assertThat(voucherEntity.getAmount()).isEqualTo(amount);
        assertThat(voucherEntity.getCreatedAt()).isNotEqualTo(voucherEntity.getUpdatedAt());

        //history
        VoucherHistoryEntity voucherHistoryEntity = voucherEntity.getHistories().get(voucherEntity.getHistories().size() - 1);
        assertThat(voucherHistoryEntity.getOrderId()).isNotNull();
        assertThat(voucherHistoryEntity.getRequesterType()).isEqualTo(useRequestContext.requesterType());
        assertThat(voucherHistoryEntity.getRequesterId()).isEqualTo(useRequestContext.requesterId());
        assertThat(voucherHistoryEntity.getStatus()).isEqualTo(VoucherStatusType.USE);
        assertThat(voucherHistoryEntity.getDescription()).isEqualTo("테스트 사용");
    }

}