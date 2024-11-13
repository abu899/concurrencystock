package com.study.concurrencystock.facade;

import com.study.concurrencystock.repository.RedisLockRepository;
import com.study.concurrencystock.service.BadStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 재시도가 필요없는 경우에는 Lettuce
 */
@Component
@RequiredArgsConstructor
public class LettuceLockFacade {

    private final RedisLockRepository redisLockRepository;
    private final BadStockService stockService;

    @Transactional
    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (!redisLockRepository.lock(id)) {
            Thread.sleep(100);
        }

        try {
            stockService.decrease(id, quantity);
        } finally {
            redisLockRepository.unlock(id);
        }
    }

}
