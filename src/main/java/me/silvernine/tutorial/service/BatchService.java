package me.silvernine.tutorial.service;

import me.silvernine.tutorial.entity.BatchData;
import me.silvernine.tutorial.entity.BatchStatistics;
import me.silvernine.tutorial.repository.DataRepository;
import me.silvernine.tutorial.repository.StatisticsRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    // 매일 00시 05분에 실행 (Cron 표현식: "0 5 0 * * ?")
    @Scheduled(cron = "0 5 0 * * ?")
    @Transactional
    public void runDailyBatch() {
        log.info("📊 [일 단위 배치] 실행 시작 - {}", LocalDateTime.now());

        // 어제 날짜 구하기
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("📅 대상 날짜: {}", yesterday);

        // 전날 데이터 조회 및 통계 계산
        List<BatchData> yesterdayData = dataRepository.findByDate(yesterday);
        BatchStatistics statistics = calculateStatistics(yesterdayData, yesterday);

        // 통계 테이블에 저장
        statisticsRepository.save(statistics);

        log.info("✅ [일 단위 배치] 완료 - {}", LocalDateTime.now());
    }

    private BatchStatistics calculateStatistics(List<BatchData> dataList, LocalDate date) {
        long count = dataList.size();
        double avgValue = dataList.stream().mapToDouble(BatchData::getValue).average().orElse(0);

        return new BatchStatistics(date, count, avgValue);
    }
}