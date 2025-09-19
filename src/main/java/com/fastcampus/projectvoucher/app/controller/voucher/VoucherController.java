package com.fastcampus.projectvoucher.app.controller.voucher;


import com.fastcampus.projectvoucher.app.controller.voucher.request.*;
import com.fastcampus.projectvoucher.app.controller.voucher.response.VoucherDisableV2Response;
import com.fastcampus.projectvoucher.app.controller.voucher.response.VoucherPublishResponse;
import com.fastcampus.projectvoucher.app.controller.voucher.response.VoucherPublishV2Response;
import com.fastcampus.projectvoucher.app.controller.voucher.response.VoucherUseV2Response;
import com.fastcampus.projectvoucher.domain.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    //상품권 발행
    @PostMapping("/api/v1/voucher")
    public VoucherPublishResponse publish(@RequestBody VoucherPublishRequest request) {
        final String publishedVoucherCode = voucherService.publish(LocalDate.now(), LocalDate.now().plusDays(1830L), request.amountType());
        return new VoucherPublishResponse(publishedVoucherCode);
    }

    //상품권 사용
    @PutMapping("/api/v1/voucher/use")
    public void use(@RequestBody String code) {
        voucherService.use(code);
    }

    //상품권 폐기
    @PutMapping("/api/v1/voucher/disable")
    public void disable(@RequestBody String code) {
        voucherService.disable(code);
    }

    //상품권 발행 v2
    @PostMapping("/api/v2/voucher")
    public VoucherPublishV2Response publishV2(@RequestBody VoucherPublishV2Request request) {
        final String publishedVoucherCode = voucherService.publishV2(request.requesterType(), request.requesterId(),LocalDate.now(), LocalDate.now().plusDays(1830L), request.amountType());
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        return new VoucherPublishV2Response(orderId,publishedVoucherCode);
    }

    //상품권 사용
    @PutMapping("/api/v2/voucher/use")
    public VoucherUseV2Response useV2(@RequestBody VoucherUseV2Request request) {
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        voucherService.useV2(request.requesterType(),request.requestId(),request.code());

        return new VoucherUseV2Response(orderId);
    }

    //상품권 폐기
    @PutMapping("/api/v2/voucher/disable")
    public VoucherDisableV2Response disableV2(@RequestBody VoucherDisableV2Request request) {
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        voucherService.disableV2(request.requesterType(),request.requesterId(),request.code());

        return new VoucherDisableV2Response(orderId);
    }

}
