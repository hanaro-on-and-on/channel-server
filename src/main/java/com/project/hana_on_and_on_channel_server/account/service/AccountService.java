package com.project.hana_on_and_on_channel_server.account.service;

import com.project.hana_on_and_on_channel_server.account.dto.AccountGetResponse;
import com.project.hana_on_and_on_channel_server.account.dto.UserLoginRequest;
import com.project.hana_on_and_on_channel_server.account.dto.UserLoginResponse;
import com.project.hana_on_and_on_channel_server.paper.domain.SalaryTransferReserve;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    ResponseEntity<Void> sendAccountDebitRequest(SalaryTransferReserve salaryTransferReserve);
    UserLoginResponse login(UserLoginRequest dto);
    List<AccountGetResponse> findAccountList(Long userId);
}
