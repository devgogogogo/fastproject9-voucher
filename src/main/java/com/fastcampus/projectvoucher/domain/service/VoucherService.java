package com.fastcampus.projectvoucher.domain.service;

import com.fastcampus.projectvoucher.common.dto.RequestContext;
import com.fastcampus.projectvoucher.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.storage.voucher.VoucherEntity;
import com.fastcampus.projectvoucher.storage.voucher.VoucherHistoryEntity;
import com.fastcampus.projectvoucher.storage.voucher.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;

    // 점진적으로 리팩토링할 예정이라 v1은 그냥 null 할 것들이 있음
    //상품권 발행 v1
    @Transactional
    public String publish(LocalDate validFrom, LocalDate validTo, VoucherAmountType amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        VoucherEntity voucherEntity = new VoucherEntity(code, VoucherStatusType.PUBLISH, validFrom, validTo, amount, null);
        return voucherRepository.save(voucherEntity).getCode();
    }

    //상품권 사용 불가 처리 v1
    @Transactional
    public void disable(String code) {
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).orElseThrow(() -> new IllegalArgumentException("존재하지 않은 상품권입니다"));
        voucherEntity.disable(null);
    }

    //상품권 사용 v1
    @Transactional
    public void use(String code) {
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품권입니다."));
        voucherEntity.use(null);
    }

    //상품권 발행 v2
    @Transactional
    public String publishV2(RequestContext requestContext, LocalDate validFrom, LocalDate validTo, VoucherAmountType amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        VoucherHistoryEntity voucherHistoryEntity = new VoucherHistoryEntity(orderId, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.PUBLISH, "테스트 발행");
        VoucherEntity voucherEntity = new VoucherEntity(code, VoucherStatusType.PUBLISH, validFrom, validTo, amount,voucherHistoryEntity);
        return voucherRepository.save(voucherEntity).getCode();
    }

    //상품권 사용 불가 처리 v2
    @Transactional
    public void disableV2(RequestContext requestContext, String code) {
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");

        VoucherEntity voucherEntity = voucherRepository.findByCode(code).orElseThrow(() -> new IllegalArgumentException("존재하지 않은 상품권입니다"));
        VoucherHistoryEntity voucherHistoryEntity = new VoucherHistoryEntity(orderId, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.DISABLE, "테스트 사용 불가");

        voucherEntity.disable(voucherHistoryEntity);
    }

    //상품권 사용 v2
    @Transactional
    public void useV2(RequestContext requestContext, String code) {
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품권입니다."));
        VoucherHistoryEntity voucherHistoryEntity = new VoucherHistoryEntity(orderId, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.USE, "테스트 사용");

        voucherEntity.use(voucherHistoryEntity);
    }
}
