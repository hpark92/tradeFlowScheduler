package com.example.domain.scheduler.repository;

import com.example.domain.scheduler.entity.PushInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushInfoRepository extends JpaRepository<PushInfo, Long> {
  @Query(value = "SELECT TOKEN_ID FROM TB_PUSH_INFO", nativeQuery = true)
  List<String> selectAllTokenId();
}
