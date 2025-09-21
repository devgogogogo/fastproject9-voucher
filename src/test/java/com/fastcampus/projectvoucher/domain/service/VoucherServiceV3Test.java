package com.fastcampus.projectvoucher.domain.service;

import com.fastcampus.projectvoucher.common.dto.RequestContext;
import com.fastcampus.projectvoucher.common.type.RequesterType;
import com.fastcampus.projectvoucher.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.storage.voucher.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class VoucherServiceV3Test {

    @Autowired
    VoucherService voucherService;

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    ContractRepository contractRepository;

    @DisplayName("발행된 상품권은 계약정보의 voucherValidPeriodDayCount 만큼 유효기간을 가져야 한다.")
    @Test
    void test1() {
        //Given
        RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        VoucherAmountType amount = VoucherAmountType.KRW_30000;

        String contractCode = "CT0001";

        //When
        String code = voucherService.publishV3(requestContext,contractCode, amount);
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        //Then
        ContractEntity contractEntity = contractRepository.findByCode(contractCode).get();
        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(LocalDate.now());
        assertThat(voucherEntity.getValidTo()).isEqualTo(LocalDate.now().plusDays(contractEntity.getVoucherValidPeriodDayCount()));
        System.out.println("### voucher validTo = " + voucherEntity.getValidTo());
        assertThat(voucherEntity.getAmount()).isEqualTo(amount);

        //history
        VoucherHistoryEntity voucherHistoryEntity = voucherEntity.getHistories().get(0);
        assertThat(voucherHistoryEntity.getOrderId()).isNotNull();
        assertThat(voucherHistoryEntity.getRequesterType()).isEqualTo(requestContext.requesterType());
        assertThat(voucherHistoryEntity.getRequesterId()).isEqualTo(requestContext.requesterId());
        assertThat(voucherHistoryEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherHistoryEntity.getDescription()).isEqualTo("테스트 발행");
    }
    @DisplayName("유효기간이 지난 계약으로 상품권 발행을 할 수 없습니다")
    @Test
    void test0() {
        //Given
        RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        VoucherAmountType amount = VoucherAmountType.KRW_30000;

        String contractCode = "CT0010";

        //When
        assertThatThrownBy(() -> voucherService.publishV3(requestContext, contractCode, amount))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("유효기간이 지난 계약입니다.");

    }

    @DisplayName("상품권은 발행 요청자만 사용 불가 처리를 할 수 있다.")
    @Test
    void test2() {
        //Given
        RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        VoucherAmountType amount = VoucherAmountType.KRW_30000;

        String contractCode = "CT0001";
        String code = voucherService.publishV3(requestContext,contractCode, amount);

        RequestContext otherRequestContext = new RequestContext(RequesterType.USER, UUID.randomUUID().toString());

        //When
        assertThatThrownBy(() -> voucherService.disableV3(otherRequestContext, code))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용 x불가 처리 권한이 없는 상품권 입니다.");

        //Then
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
    }
}