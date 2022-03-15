package com.example.domain.scheduler.service;

import org.springframework.stereotype.Service;
import java.util.List;

public interface PushInfoService {
    List<String> selectAllPushInfo();
}
