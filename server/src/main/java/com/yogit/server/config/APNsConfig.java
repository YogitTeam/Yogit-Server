package com.yogit.server.config;

import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
class APNsConfig {

//    @Value("${APN.DESTINATION.DEVICE.TOKEN}")
//    private String DEV_PW;

    @Bean
    public ApnsClient someAppDevApnsClient(@Value("${APN.DEV.P12.PW}") String DEV_PW, @Value("${APN.DEV.CERTIFICATE.PATH}") String DEV_CERTIFICATE_PATH) throws IOException {
        // 개발용 인증 키 파일
        return new ApnsClientBuilder()
                .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                .setClientCredentials(new ClassPathResource(DEV_CERTIFICATE_PATH).getInputStream(), DEV_PW)
                .build();
    }

//    @Bean
//    public ApnsClient someAppProdApnsClient() throws IOException {
//        // 배표용 인증 키 파일
//        return new ApnsClientBuilder()
//                .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
//                .setClientCredentials(new ClassPathResource("some_app_prod_pkcs_12_file.p12").getInputStream(), "some_app_prod_password")
//                .build();
//    }

}
