package com.fastcampus.projectvoucher.app.controller.voucher.request;

import com.fastcampus.projectvoucher.common.type.RequesterType;

public record VoucherUseV2Request(
        RequesterType requesterType,
        String requestId,
        String code) {
}
