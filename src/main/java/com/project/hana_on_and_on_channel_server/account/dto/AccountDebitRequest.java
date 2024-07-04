package com.project.hana_on_and_on_channel_server.account.dto;

import com.project.hana_on_and_on_channel_server.paper.domain.SalaryTransferReserve;

public record AccountDebitRequest(String senderNm, String senderAccountNumber, String receiverNm, String receiverAccountNumber, String description, Long amount) {

    public static AccountDebitRequest fromEntity(SalaryTransferReserve entity){
        String description = "급여";
        return new AccountDebitRequest(entity.getSenderNm(), entity.getSenderAccountNumber(), entity.getReceiverNm(), entity.getReceiverAccountNumber(), description, entity.getTotalPay());
    }
}
