package com.example.gachisikyeo_be.global.init;

import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.app.repository.region.LawDongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

@Slf4j
@Component
@RequiredArgsConstructor
public class LawDongCsvInitializer {

    private final LawDongRepository lawDongRepository;

    // classpath:src/main/resources/data/law_dong.csv
    @Value("classpath:data/law_dong.csv")
    private Resource lawDongCsv;

    @Transactional
    public void initIfEmpty() {
        if (lawDongRepository.count() > 0) {
            log.info("LawDong 테이블에 이미 데이터가 있어서 초기 적재를 건너뜀");
            return;
        }

        log.info("LawDong CSV 초기 적재 시작");

        // ✅ 한글 깨지면 MS949로 바꿔보기
        Charset charset = Charset.forName("UTF-8"); // 또는 UTF-8

        int count = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(lawDongCsv.getInputStream(), charset))) {

            String line = br.readLine(); // 헤더 한 줄 스킵

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                // 간단 split (필드 안에 콤마가 들어가는 경우 거의 없음)
                String[] cols = line.split(",", -1); // 빈 문자열도 유지

                // 컬럼 순서는 data.go.kr 설명 기준:
                // 0:법정동코드, 1:시도명, 2:시군구명, 3:읍면동명, 4:리명, 5:순위, 6:생성일자, 7:삭제일자, 8:과거법정동코드

                String lawCode = cols[0].trim();
                String sido    = cols[1].trim();
                String sigungu = cols[2].trim();
                String eupMyeonDong = cols[3].trim(); // 읍/면/동
                String ri      = cols[4].trim();
                String created = cols[6].trim();
                String deleted = cols[7].trim();

                // 👉 우리 서비스 용도: "읍/면/동"까지만 쓰고, 리는 일단 제외
                if (eupMyeonDong.isEmpty()) {
                    // 시도/시군구 레벨만 있는 행은 드롭다운 3단계에서는 안 씀
                    continue;
                }

                boolean isActive = deleted.isEmpty(); // 삭제일자 없으면 현행

                LawDong lawDong = LawDong.builder()
                        .lawCode(lawCode)
                        .sido(sido)
                        .sigungu(sigungu)
                        .dong(eupMyeonDong)  // 리까지 포함하고 싶으면 정책 바꾸면 됨
                        .isActive(isActive)
                        .updateDate(created) // 생성일자를 updateDate로 저장
                        .build();

                lawDongRepository.save(lawDong);
                count++;
            }

        } catch (Exception e) {
            log.error("LawDong CSV 적재 중 오류", e);
            throw new RuntimeException("LawDong CSV 적재 실패", e);
        }

        log.info("LawDong CSV 초기 적재 완료, 총 {}건 저장", count);
    }
}
