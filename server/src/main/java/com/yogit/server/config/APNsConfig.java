//package com.yogit.server.config;
//
//import com.turo.pushy.apns.ApnsClient;
//import com.turo.pushy.apns.ApnsClientBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.IOException;
//
//@Configuration
//class APNsConfig {
//
//    @Bean
//    public ApnsClient someAppDevApnsClient() throws IOException {
//        // 인증 키 파일
//        return new ApnsClientBuilder()
//                .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
//                .setClientCredentials(new ClassPathResource("some_app_dev_pkcs_12_file.p12").getInputStream(), "some_app_dev_password")
//                .build();
//    }
//
//    @Bean
//    public ApnsClient someAppProdApnsClient() throws IOException {
//        // 인증키를 복호화하는 파일
//        return new ApnsClientBuilder()
//                .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
//                .setClientCredentials(new ClassPathResource("some_app_prod_pkcs_12_file.p12").getInputStream(), "some_app_prod_password")
//                .build();
//    }
//
//}
