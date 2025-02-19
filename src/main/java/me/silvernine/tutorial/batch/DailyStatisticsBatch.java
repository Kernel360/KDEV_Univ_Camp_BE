package me.silvernine.tutorial.batch;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.silvernine.tutorial.entity.BatchData;
import me.silvernine.tutorial.entity.BatchStatistics;
import me.silvernine.tutorial.repository.BatchDataRepository;
import me.silvernine.tutorial.repository.StatisticsRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DailyStatisticsBatch {

    private final BatchDataRepository batchDataRepository;
    private final StatisticsRepository statisticsRepository;

    @Scheduled(cron = "5 0 0 * * ?") // ✅ 매일 00:05 실행
    @Transactional
    public void calculateDailyStatistics() {
        LocalDateTime yesterdayStart = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime yesterdayEnd = yesterdayStart.plusDays(1);

        log.info("📊 [일 단위 배치] {} 데이터 통계 계산 시작...", yesterdayStart.toLocalDate());

        List<BatchData> dataList = batchDataRepository.findByTimestampBetween(yesterdayStart, yesterdayEnd);

        long count = dataList.size();
        double avgValue = dataList.stream().mapToDouble(BatchData::getValue).average().orElse(0.0);

        BatchStatistics statistics = new BatchStatistics(yesterdayStart, count, avgValue);
        statisticsRepository.save(statistics);

        log.info("✅ [일 단위 배치] {} 데이터 통계 계산 완료!", yesterdayStart.toLocalDate());
    }
}
