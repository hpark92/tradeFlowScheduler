package com.example;
import com.example.domain.scheduler.entity.PushInfo;
import com.example.domain.scheduler.service.PushInfoService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class NotificationComponent {
    private Logger logger = LoggerFactory.getLogger(NotificationComponent.class);
    @Value("${custom.properties.firebase-create-scoped}")
    private String fireBaseCreateScoped;

    private FirebaseMessaging instance;
    private PushInfoService pushInfoService;

    @Autowired
    public NotificationComponent(PushInfoService pushInfoService) {
        this.pushInfoService = pushInfoService;
    }

    @PostConstruct
    public void firebaseSetting() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource("tradeflow-aff7b-firebase-adminsdk-o8whh-92d628a852.json").getInputStream())
                .createScoped((Arrays.asList(fireBaseCreateScoped)));
        FirebaseOptions secondaryAppConfig = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(secondaryAppConfig);
        this.instance = FirebaseMessaging.getInstance(app);
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void pushAlarmToWakeUpApp() throws FirebaseMessagingException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        Date toDate = new Date();
        String strDate = simpleDateFormat.format(toDate);
        // push list 의 토큰 아이디를 리스트에 담는다.
        List<String> registrationTokens = pushInfoService.selectAllPushInfo();
        logger.info(" ### registrationTokens : ", registrationTokens);
        MulticastMessage message = MulticastMessage.builder()
                .putData("data", "push Test")
                .addAllTokens(registrationTokens)
                .build();
        logger.info("## TIME : {}", strDate);
        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
        logger.info("## Successfully sent message : {}", response);

        if (response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();
            List<String> failedTokens = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    // The order of responses corresponds to the order of the registration tokens.
                    failedTokens.add(registrationTokens.get(i));
                }
            }
            logger.info("## failedTokens : {}", failedTokens);
        }
    }
}
