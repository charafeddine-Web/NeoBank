package com.neobank.repository;

import com.neobank.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByOperation_Id(Long operationId);
}

