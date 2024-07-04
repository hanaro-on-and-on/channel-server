package com.project.hana_on_and_on_channel_server.paper.dto;

public record SalaryTransferReserveRequest(String senderAccountNumber, String senderNm, String receiverAccountNumber, String receiverNm, Long amount) {
}
