package com.fastcampus.projectvoucher.app.controller.voucher;


import com.fastcampus.projectvoucher.app.controller.voucher.request.*;
import com.fastcampus.projectvoucher.app.controller.voucher.response.*;
import com.fastcampus.projectvoucher.common.dto.RequestContext;
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

    //상품권 사용 v2
    @PutMapping("/api/v2/voucher/use")
    public VoucherUseV2Response useV2(@RequestBody VoucherUseV2Request request) {
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        voucherService.useV2(new RequestContext(request.requesterType(), request.requestId()),request.code());

        return new VoucherUseV2Response(orderId);
    }

    //상품권 폐기 v2
    @PutMapping("/api/v2/voucher/disable")
    public VoucherDisableV2Response disableV2(@RequestBody VoucherDisableV2Request request) {
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        voucherService.disableV2(new RequestContext(request.requesterType(),request.requesterId()),request.code());

        return new VoucherDisableV2Response(orderId);
    }

    //상품권 발행 v3
    @PostMapping("/api/v3/voucher")
    public VoucherPublishV3Response publishV3(@RequestBody VoucherPublishV3Request request) {
        final String publishedVoucherCode = voucherService.publishV3(new RequestContext(request.requesterType(), request.requesterId()),
                request.contractCode(),
                request.amountType());
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        return new VoucherPublishV3Response(orderId,publishedVoucherCode);
    }

}
