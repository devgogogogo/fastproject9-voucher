package com.fastcampus.projectvoucher.domain.service;

import com.fastcampus.projectvoucher.common.dto.RequestContext;
import com.fastcampus.projectvoucher.common.type.RequesterType;
import com.fastcampus.projectvoucher.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.storage.voucher.VoucherEntity;
import com.fastcampus.projectvoucher.storage.voucher.VoucherRepository;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


@SpringBootTest
public class VoucherServiceV2DynamicTest {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;

    @TestFactory
    Stream<DynamicTest> test() {
        final List<String> codes = new ArrayList<>();

        return Stream.of(
                dynamicTest("[0]상품권을 발행합니다",()->{
                    //Given
                    RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
                    LocalDate validFrom = LocalDate.now();
                    LocalDate validTo = LocalDate.now().plusDays(30);
                    VoucherAmountType amount = VoucherAmountType.KRW_30000;

                    //When
                    String code = voucherService.publishV2(requestContext,validFrom, validTo, amount);
                    codes.add(code);

                    //then
                    VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
                }),
                dynamicTest("[0]상품권을 사용 불가 처리합니다",()->{
                    //Given
                    RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
                    String code = codes.get(0);

                    //When
                    voucherService.disableV2(requestContext,code);
                    //Then
                    VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);
                }),
                dynamicTest("[0]사용 불가 상태의 상품권은 사용할 수 없습니다.",()->{
                    //Given
                    String code = codes.get(0);
                    RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
                    //When
                    assertThatThrownBy(() -> voucherService.useV2(requestContext,code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용할 수 없는 상태의 상품권 입니다.");
                    //Then
                    VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);
                }),
                dynamicTest("[1]상품권을 사용 합니다.",()->{
                    //Given
                    RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
                    LocalDate validFrom = LocalDate.now();
                    LocalDate validTo = LocalDate.now().plusDays(30);
                    VoucherAmountType amount = VoucherAmountType.KRW_30000;
                    String code = voucherService.publishV2(requestContext,validFrom, validTo, amount);
                    codes.add(code);


                    //When
                    voucherService.useV2(requestContext,code);
                    VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.USE);
                }),
                dynamicTest("[1]사용한 상품권은 사용 불가 처리 할 수 없습니다.",()->{
                    //Given
                    RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
                    String code = codes.get(1);
                    //When
                    assertThatThrownBy(() -> voucherService.disableV2(requestContext,code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용 불가 처리할 수 없는 상태의 상품권 입니다.");
                    //Then
                    VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.USE);

                }),
                dynamicTest("[1]사용한 상품권은 또 사용할 수 없습니다.",()->{
                    //Given
                    String code = codes.get(1);
                    RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());

                    //When
                    assertThatThrownBy(() -> voucherService.useV2(requestContext,code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용할 수 없는 상태의 상품권 입니다.");
                    //Then
                    VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.USE);
                })
        );
    }


}
