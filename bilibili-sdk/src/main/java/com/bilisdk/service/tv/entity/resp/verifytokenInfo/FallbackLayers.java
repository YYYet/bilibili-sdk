package com.bilisdk.service.tv.entity.resp.verifytokenInfo;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class FallbackLayers {
    private List<Layers> layers;
    private boolean is_critical_group;
}