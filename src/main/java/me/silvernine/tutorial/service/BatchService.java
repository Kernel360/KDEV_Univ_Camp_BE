package me.silvernine.tutorial.service;

import me.silvernine.tutorial.entity.BatchData;
import me.silvernine.tutorial.entity.BatchStatistics;
import me.silvernine.tutorial.repository.DataRepository;
import me.silvernine.tutorial.repository.StatisticsRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class BatchService {

    private final StatisticsRepository statisticsRepository;
    private final DataRepository dataRepository;

    public BatchService(StatisticsRepository statisticsRepository, DataRepository dataRepository) {
        this.statisticsRepository = statisticsRepository;
        this.dataRepository = dataRepository;
    }

    //@Scheduled(cron = "0 5 0 * * ?") // ✅ 매일 00:05 실행
    @Scheduled(cron = "0 * * * * ?") //1분에 한번씩 테스트
    @Transactional
    public void runDailyBatch() {
        log.info("📊 [일 단위 배치] 실행 시작 - {}", LocalDateTime.now());

        LocalDateTime yesterdayStart = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime yesterdayEnd = yesterdayStart.plusDays(1);

        log.info("📅 대상 날짜: {} ~ {}", yesterdayStart, yesterdayEnd);

        List<BatchData> yesterdayData = dataRepository.findByTimestampBetween(yesterdayStart, yesterdayEnd);
        log.info("📊 총 데이터 개수: {}", yesterdayData.size());

        BatchStatistics statistics = calculateStatistics(yesterdayData, yesterdayStart);
        statisticsRepository.save(statistics);

        log.info("✅ [일 단위 배치] 완료 - {}", LocalDateTime.now());
    }

    private BatchStatistics calculateStatistics(List<BatchData> dataList, LocalDateTime timestamp) {
        long count = dataList.size();
        double avgValue = dataList.stream().mapToDouble(BatchData::getValue).average().orElse(0.0);
        return new BatchStatistics(timestamp, count, avgValue);
    }
}
