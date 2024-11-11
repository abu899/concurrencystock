package com.study.concurrencystock.facade;

import com.study.concurrencystock.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService optimisticLockStockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                optimisticLockStockService.decrease(id, quantity);
                break;
            } catch (Exception e) {
                log.warn("충돌 발생 - 50ms 후 재시도");
                Thread.sleep(50);
            }
        }
    }
}
