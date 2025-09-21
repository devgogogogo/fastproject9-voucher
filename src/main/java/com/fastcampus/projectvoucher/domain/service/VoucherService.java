package com.fastcampus.projectvoucher.domain.service;

import com.fastcampus.projectvoucher.common.dto.RequestContext;
import com.fastcampus.projectvoucher.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.domain.service.validator.VoucherDisableValidator;
import com.fastcampus.projectvoucher.domain.service.validator.VoucherPublishValidator;
import com.fastcampus.projectvoucher.storage.voucher.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final ContractRepository contractRepository;

    private final VoucherPublishValidator voucherPublishValidator;
    private final VoucherDisableValidator voucherDisableValidator;


    //상품권 사용 v2
    @Transactional
    public void useV2(RequestContext requestContext, String code) {
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품권입니다."));
        VoucherHistoryEntity voucherHistoryEntity = new VoucherHistoryEntity(orderId, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.USE, "테스트 사용");

        voucherEntity.use(voucherHistoryEntity);
    }

    //상품권 발행 v3
    @Transactional
    public String publishV3(RequestContext requestContext, String contractCode, VoucherAmountType amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");

        ContractEntity contractEntity = contractRepository.findByCode(contractCode).orElseThrow(() -> new IllegalArgumentException("존재하는 않는 계약입니다."));

        voucherPublishValidator.validate(contractEntity);

        VoucherHistoryEntity voucherHistoryEntity = new VoucherHistoryEntity(orderId, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.PUBLISH, "테스트 발행");
        VoucherEntity voucherEntity = new VoucherEntity(code, VoucherStatusType.PUBLISH, amount, voucherHistoryEntity, contractEntity);
        return voucherRepository.save(voucherEntity).getCode();
    }

    //상품권 사용 불가 처리 v3
    @Transactional
    public void disableV3(RequestContext requestContext, String code) {
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");

        VoucherEntity voucherEntity = voucherRepository.findByCode(code).orElseThrow(() -> new IllegalArgumentException("존재하지 않은 상품권입니다"));

        voucherDisableValidator.validate(voucherEntity,requestContext);

        VoucherHistoryEntity voucherHistoryEntity = new VoucherHistoryEntity(orderId, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.DISABLE, "테스트 사용 불가");

        voucherEntity.disable(voucherHistoryEntity);

    }
}
