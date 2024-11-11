package com.study.concurrencystock.repository;

import com.study.concurrencystock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
