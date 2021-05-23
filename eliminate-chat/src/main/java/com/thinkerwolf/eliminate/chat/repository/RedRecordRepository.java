package com.thinkerwolf.eliminate.chat.repository;

import com.thinkerwolf.eliminate.chat.entity.RedRecord;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedRecordRepository extends PagingAndSortingRepository<RedRecord, String> {
    List<RedRecord> findByRedId(String redId);
}
