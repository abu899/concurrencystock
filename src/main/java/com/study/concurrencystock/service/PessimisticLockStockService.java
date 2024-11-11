package com.study.concurrencystock.service;

import com.study.concurrencystock.domain.Stock;
import com.study.concurrencystock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 충돌이 빈번하게 일어나는 경우 Optimistic Lock 보다 성능이 좋을 수 있음
 * Lock을 통해 업데이트를 제어해서 데이터 정합성 보장
 */
@Service
@RequiredArgsConstructor
public class PessimisticLockStockService implements StockService {

    private final StockRepository stockRepository;

    @Override
    @Transactional
    public void decrease(Long id, Long quantity) {
        Stock stock = stockRepository.findByIdWithPessimisticLock(id);
        stock.decrease(quantity);
    }
}
