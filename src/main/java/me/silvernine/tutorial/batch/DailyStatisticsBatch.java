package me.silvernine.tutorial.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Slf4j
@Component // Spring Bean 등록
public class DailyStatisticsBatch {

    //@Scheduled(cron = "0 5 0 * * ?") // 매일 00:05 실행
    @Scheduled(cron = "0 * * * * ?") //테스트용 1분 간격 실행
    public void runDailyBatch() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("📊 [일 단위 배치] {} 데이터 통계 계산 시작...", yesterday);

        // TODO: 여기에 전날 데이터 통계를 계산하고 통계 테이블에 저장하는 로직 추가

        log.info("✅ [일 단위 배치] {} 데이터 통계 계산 완료!", yesterday);
    }
}
