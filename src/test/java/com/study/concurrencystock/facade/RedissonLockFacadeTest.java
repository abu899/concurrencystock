package com.study.concurrencystock.facade;

import com.study.concurrencystock.domain.Stock;
import com.study.concurrencystock.repository.StockRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class RedissonLockFacadeTest {
    @Autowired
    RedissonLockFacade redissonLockFacade;

    @Autowired
    StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        stockRepository.save(new Stock(1L, 100L));
    }

    @AfterEach
    void tearDown() {
        stockRepository.deleteAll();
    }


    @Test
    @DisplayName("동시에 100개의 재고감소 요청")
    void decreaseConcurrent() throws InterruptedException {
        // given
        Long productId = 1L;
        Long quantity = 1L;
        int threadCount = 100;

        ExecutorService es = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        // when
        for (int i = 0; i < threadCount; i++) {
            es.submit(() -> {
                try {
                    redissonLockFacade.decrease(productId, quantity);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        // then
        Stock stock = stockRepository.findById(productId).orElseThrow();
        Assertions.assertThat(stock.getQuantity()).isEqualTo(0L);
    }
}