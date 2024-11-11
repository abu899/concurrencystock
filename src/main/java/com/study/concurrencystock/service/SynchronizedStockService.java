package com.study.concurrencystock.service;

import com.study.concurrencystock.domain.Stock;
import com.study.concurrencystock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 단점: 하나의 프로세스 내에서만 동시성 보장
 */
@Service
@RequiredArgsConstructor
public class SynchronizedStockService implements StockService {

    private final StockRepository stockRepository;

    @Override
    public synchronized void decrease(Long id, Long quantity) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("재고가 존재하지 않습니다."));
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }
}
