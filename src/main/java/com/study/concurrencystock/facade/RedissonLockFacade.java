package com.study.concurrencystock.facade;

import com.study.concurrencystock.service.BadStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * 재시도가 필요한 경우에는 Redisson
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockFacade {

    private final RedissonClient redissonClient;
    private final BadStockService stockService;

    @Transactional
    public void decrease(Long id, Long quantity) throws InterruptedException {

        RLock lock = redissonClient.getLock(id.toString());
        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                log.error("lock not available");
                return;
            }
            stockService.decrease(id, quantity);
        } finally {
            lock.unlock();
        }
    }
}
