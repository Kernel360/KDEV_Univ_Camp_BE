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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DailyStatisticsBatch {

    private final BatchDataRepository batchDataRepository;
    private final StatisticsRepository statisticsRepository;

    //@Scheduled(cron = "5 0 0 * * ?") // ✅ 매일 00:05 실행
    @Transactional
    public void calculateDailyStatistics() {
        LocalDateTime yesterdayStart = LocalDate.now().minusDays(1).atStartOfDay(); // ✅ timestamp 통일
        LocalDateTime yesterdayEnd = yesterdayStart.plusDays(1);

        // ✅ 중복 저장 방지
        if (statisticsRepository.existsByTimestamp(yesterdayStart)) {
            log.info("⚠️ 이미 처리된 날짜: {} - 배치 실행 취소", yesterdayStart);
            return;
        }

        log.info("📊 [일 단위 배치] {} 데이터 통계 계산 시작...", yesterdayStart.toLocalDate());

        List<BatchData> dataList = batchDataRepository.findByTimestampBetween(yesterdayStart, yesterdayEnd);

        // ✅ 데이터 불러오기 확인 로그 추가
        log.info("📊 조회된 데이터 개수: {}", dataList.size());
        log.info("📊 데이터 샘플: {}", dataList.stream().limit(5).toList()); // 첫 5개만 확인

        // ✅ 데이터가 없을 경우 배치 중단
        if (dataList.isEmpty()) {
            log.warn("⚠️ 해당 날짜({})의 데이터가 없습니다. 통계 계산을 건너뜁니다.", yesterdayStart);
            return;
        }

        // ✅ 평균값 계산
        long count = dataList.size();
        double avgValue = dataList.stream().mapToDouble(BatchData::getValue).average().orElse(0.0);

        // ✅ 중복 방지된 저장 로직
        BatchStatistics statistics = new BatchStatistics(yesterdayStart, count, avgValue);
        statisticsRepository.save(statistics);

        log.info("✅ [일 단위 배치] {} 데이터 통계 계산 완료! 저장된 데이터: {}", yesterdayStart.toLocalDate(), statistics);
    }
}