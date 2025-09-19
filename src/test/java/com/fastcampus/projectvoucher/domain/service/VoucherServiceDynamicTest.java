package com.fastcampus.projectvoucher.domain.service;

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
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.*;


@SpringBootTest
public class VoucherServiceDynamicTest {

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
                    LocalDate validFrom = LocalDate.now();
                    LocalDate validTo = LocalDate.now().plusDays(30);
                    Long amount = 10000L;

                    //When
                    String code = voucherService.publish(validFrom, validTo, amount);
                    codes.add(code);

                    //then
                    VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
                }),
                dynamicTest("[0]상품권을 사용 불가 처리합니다",()->{
                    //Given
                    String code = codes.get(0);

                    //When
                    voucherService.disable(code);
                    //Then
                    VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);
                }),
                dynamicTest("[0]사용 불가 상태의 상품권은 사용할 수 없습니다.",()->{
                    //Given
                    String code = codes.get(0);

                    //When
                    assertThatThrownBy(() -> voucherService.use(code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용할 수 없는 상태의 상품권 입니다.");
                    //Then
                    VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);
                }),
                dynamicTest("[1]상품권을 사용 합니다.",()->{
                    //Given
                    LocalDate validFrom = LocalDate.now();
                    LocalDate validTo = LocalDate.now().plusDays(30);
                    Long amount = 10000L;
                    String code = voucherService.publish(validFrom, validTo, amount);
                    codes.add(code);


                    //When
                    voucherService.use(code);
                    VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.USE);
                }),
                dynamicTest("[1]사용한 상품권은 사용 불가 처리 할 수 없습니다.",()->{
                    //Given
                    String code = codes.get(1);
                    //When
                    assertThatThrownBy(() -> voucherService.disable(code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용 불가 처리할 수 없는 상태의 상품권 입니다.");
                    //Then
                    VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.USE);

                }),
                dynamicTest("[1]사용한 상품권은 또 사용할 수 없습니다.",()->{
                    //Given
                    String code = codes.get(1);

                    //When
                    assertThatThrownBy(() -> voucherService.use(code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용할 수 없는 상태의 상품권 입니다.");
                    //Then
                    VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.USE);
                })
        );
    }


}
