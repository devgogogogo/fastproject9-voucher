package com.fastcampus.projectvoucher.app.controller.voucher.request;

import com.fastcampus.projectvoucher.common.type.RequesterType;

public record VoucherDisableV2Request(
        RequesterType requesterType,
        String requesterId,
        String code
) {
}
