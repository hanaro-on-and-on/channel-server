package com.project.hana_on_and_on_channel_server.owner.vo;

import lombok.Getter;

import java.util.List;

@Getter
public class BusinessInfoResponse {
    private String status_code;
    private int request_cnt;
    private int valid_cnt;
    private List<BusinessInfoData> data;
}
