package com.study.concurrencystock.service;

import org.springframework.transaction.annotation.Transactional;

public interface StockService {

    void decrease(Long id, Long quantity);
}
