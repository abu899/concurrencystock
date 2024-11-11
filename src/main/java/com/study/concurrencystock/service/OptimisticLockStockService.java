package com.study.concurrencystock.service;

import com.study.concurrencystock.domain.Stock;
import com.study.concurrencystock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 충돌이 적은 경우 별도의 Lock을 사용하지 않기에 Pessemistic Lock 보다 성능이 좋을 수 있음
 * 업데이트가 실패했을 때 재시도와 같은 로직을 개발자가 직접 작성해줘야 함
 */
@Service
@RequiredArgsConstructor
public class OptimisticLockStockService implements StockService {

    private final StockRepository stockRepository;

    @Override
    @Transactional
    public void decrease(Long id, Long quantity) {
        Stock stock = stockRepository.findByIdWithOptimisticLock(id);
        stock.decrease(quantity);
    }
}
