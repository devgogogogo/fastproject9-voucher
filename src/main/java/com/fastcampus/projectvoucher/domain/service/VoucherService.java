package com.fastcampus.projectvoucher.domain.service;

import com.fastcampus.projectvoucher.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.storage.voucher.VoucherEntity;
import com.fastcampus.projectvoucher.storage.voucher.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;

    //상품권 발행
    @Transactional
    public String publish(LocalDate validFrom, LocalDate validTo, Long amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        VoucherEntity voucherEntity = new VoucherEntity(code, VoucherStatusType.PUBLISH, validFrom, validTo, amount);
        return voucherRepository.save(voucherEntity).getCode();
    }

    //상품권 사용 불가 처리
    @Transactional
    public void disable(String code) {
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).orElseThrow(() -> new IllegalArgumentException("존재하지 않은 상품권입니다"));
        voucherEntity.disable();
    }

    //상품권 사용
    @Transactional
    public void use(String code) {
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품권입니다."));
        voucherEntity.use();
    }
}
