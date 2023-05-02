package com.bilisdk.service.tv.entity.resp.senddanmukuinfo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ModeInfo {
    private String extra;
    private int mode;
    private int show_player_type;
}