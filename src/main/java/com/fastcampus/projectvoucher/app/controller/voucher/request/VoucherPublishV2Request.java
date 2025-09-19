package com.fastcampus.projectvoucher.app.controller.voucher.request;

import com.fastcampus.projectvoucher.common.type.RequesterType;
import com.fastcampus.projectvoucher.common.type.VoucherAmountType;

public record VoucherPublishV2Request(
        RequesterType requesterType,
        String requesterId,
        VoucherAmountType amountType) {
}
