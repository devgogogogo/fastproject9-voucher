package com.fastcampus.projectvoucher.common.exception;

import java.time.LocalDate;
import java.util.UUID;

public record ErrorResponse(
        String message,
        LocalDate timestamp,
        UUID traceId) {

}
