package com.project.hana_on_and_on_channel_server.owner.vo;

import lombok.Getter;

@Getter
public class BusinessInfoData {
    private String b_no;
    private String valid;
    private String valid_msg;
    private BusinessInfoRequestParam request_param;
    private BusinessInfoStatus status;
}