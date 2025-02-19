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

    // ✅ 매일 00시 05분 실행 (실제 환경에서는 아래 cron 사용)
    @Scheduled(cron = "0 5 0 * * ?")
    @Transactional
    public void runDailyBatch() {
        log.info("📊 [일 단위 배치] 실행 시작 - {}", LocalDateTime.now());

        // ✅ 어제 날짜 기준으로 시간 범위 설정
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startOfDay = yesterday.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        log.info("📅 대상 날짜: {} ({} ~ {})", yesterday, startOfDay, endOfDay);

        // ✅ 전날 데이터 조회
        List<BatchData> yesterdayData = dataRepository.findByTimestampBetween(startOfDay, endOfDay);
        log.info("📊 총 데이터 개수: {}", yesterdayData.size());

        // ✅ 통계 계산
        BatchStatistics statistics = calculateStatistics(yesterdayData, startOfDay);

        // ✅ 통계 저장
        statisticsRepository.save(statistics);
        log.info("✅ [일 단위 배치] 완료 - {}", LocalDateTime.now());
    }

    // ✅ 통계 계산 메서드 수정 (LocalDateTime 사용)
    private BatchStatistics calculateStatistics(List<BatchData> dataList, LocalDateTime timestamp) {
        long count = dataList.size();
        double avgValue = dataList.stream().mapToDouble(BatchData::getValue).average().orElse(0.0);
        return new BatchStatistics(timestamp, count, avgValue);
    }
}
