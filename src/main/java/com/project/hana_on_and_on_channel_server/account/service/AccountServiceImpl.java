package com.project.hana_on_and_on_channel_server.account.service;

import com.project.hana_on_and_on_channel_server.account.dto.AccountDebitRequest;
import com.project.hana_on_and_on_channel_server.account.dto.UserLoginRequest;
import com.project.hana_on_and_on_channel_server.account.dto.UserLoginResponse;
import com.project.hana_on_and_on_channel_server.paper.domain.SalaryTransferReserve;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * Account 서버(계정계) 통신 전용 Service 클래스
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Value("${account.base-url}")
    private String BASE_URL;

    public ResponseEntity<Void> sendAccountDebitRequest(SalaryTransferReserve salaryTransferReserve){
        String uri = "/accounts/account-debit";
        WebClient webClient = WebClient.create(BASE_URL);

        AccountDebitRequest request = AccountDebitRequest.fromEntity(salaryTransferReserve);
        return webClient.post()
                .uri(BASE_URL + uri)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .doOnError(error -> logger.info("AccountDebit Failed: " + error.getMessage()))
                .block();
    }

    public UserLoginResponse login(UserLoginRequest dto) {
        String uri = "/users/login";
        WebClient webClient = WebClient.create(BASE_URL);

        return webClient.post()
                .uri(BASE_URL + uri)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(UserLoginResponse.class)
                .doOnError(error -> logger.info("Login Failed: " + error.getMessage()))
                .block();
    }
}
