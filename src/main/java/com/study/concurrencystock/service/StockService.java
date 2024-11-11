package com.study.concurrencystock.service;

import com.study.concurrencystock.domain.Stock;
import com.study.concurrencystock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {
        // stock 조회
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("재고가 존재하지 않습니다."));
        // 재고 감소
        stock.decrease(quantity);

        // 갱신 데이터 저장
        stockRepository.saveAndFlush(stock);
    }
}
