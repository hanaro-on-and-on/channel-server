package com.project.hana_on_and_on_channel_server.account.service;

import com.project.hana_on_and_on_channel_server.paper.domain.SalaryTransferReserve;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    ResponseEntity<Void> sendAccountDebitRequest(SalaryTransferReserve salaryTransferReserve);
}
