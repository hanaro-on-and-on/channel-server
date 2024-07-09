package com.project.hana_on_and_on_channel_server.account.service;

import com.project.hana_on_and_on_channel_server.account.dto.*;
import com.project.hana_on_and_on_channel_server.account.exception.UserNotFoundException;
import com.project.hana_on_and_on_channel_server.common.util.JwtUtil;
import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.employee.repository.EmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.domain.Owner;
import com.project.hana_on_and_on_channel_server.owner.repository.OwnerRepository;
import com.project.hana_on_and_on_channel_server.paper.domain.SalaryTransferReserve;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;


/**
 * Account 서버(계정계) 통신 전용 Service 클래스
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final JwtUtil jwtUtil;
    private final OwnerRepository ownerRepository;
    private final EmployeeRepository employeeRepository;

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

        AccountUserLoginResponse accountUserLoginResponse = null;
        Long ownerId = null;
        Long employeeId = null;
        try {
            accountUserLoginResponse = webClient.post()
                    .uri(BASE_URL + uri)
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(AccountUserLoginResponse.class)
                    .doOnError(error -> logger.info("Login Failed: " + error.getMessage()))
                    .block();

            Long userId = jwtUtil.getAuthValue(accountUserLoginResponse.accessToken(), Long.class);
            ownerId = ownerRepository.findByUserId(userId).map(Owner::getOwnerId).orElse(null);
            employeeId = employeeRepository.findByUserId(userId).map(Employee::getEmployeeId).orElse(null);

        } catch (WebClientResponseException e) {
            e.printStackTrace();
            throw new UserNotFoundException();
        }

        return new UserLoginResponse(accountUserLoginResponse.accessToken(), ownerId, employeeId);
    }

    public List<AccountGetResponse> findAccountList(Long userId){
        String uri = "/accounts/" + userId;
        WebClient webClient = WebClient.create(BASE_URL);
        return webClient.get()
                .uri(BASE_URL + uri)
                .retrieve()
                .bodyToFlux(AccountGetResponse.class)
                .collectList()
                .block();
    }
}
