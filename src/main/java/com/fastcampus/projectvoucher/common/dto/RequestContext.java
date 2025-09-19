package com.fastcampus.projectvoucher.common.dto;

import com.fastcampus.projectvoucher.common.type.RequesterType;

public record RequestContext(
        RequesterType requesterType,
        String requesterId
) {
}
