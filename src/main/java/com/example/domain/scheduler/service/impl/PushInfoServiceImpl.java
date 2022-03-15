package com.example.domain.scheduler.service.impl;

import com.example.NotificationComponent;
import com.example.domain.scheduler.entity.PushInfo;
import com.example.domain.scheduler.repository.PushInfoRepository;
import com.example.domain.scheduler.service.PushInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PushInfoServiceImpl implements PushInfoService {
    private Logger logger = LoggerFactory.getLogger(PushInfoServiceImpl.class);

    @Autowired(required=true)
    PushInfoRepository pushInforepository;

    @Override
    public List<String> selectAllPushInfo() {
        List<String> result = pushInforepository.selectAllTokenId();
        logger.info("result : {}", result);
        return result;
    }
}
