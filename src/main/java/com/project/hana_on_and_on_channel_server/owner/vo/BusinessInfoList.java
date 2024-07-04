package com.project.hana_on_and_on_channel_server.owner.vo;

import lombok.Getter;

import java.util.List;

@Getter
public class BusinessInfoList {
    private int request_cnt;
    private int match_cnt;
    private String status_code;
    private List<BusinessInfo> data;
}
