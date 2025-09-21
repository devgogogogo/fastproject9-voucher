package com.fastcampus.projectvoucher.app.controller.voucher.request;

import com.fastcampus.projectvoucher.common.type.RequesterType;
import com.fastcampus.projectvoucher.common.type.VoucherAmountType;

public record VoucherPublishV3Request(
        RequesterType requesterType,
        String requesterId,
        String contractCode,
        VoucherAmountType amountType) {
}
