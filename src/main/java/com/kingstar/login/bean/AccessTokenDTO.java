package com.kingstar.login.bean;
import lombok.Data;

import java.util.List;


@Data
public class AccessTokenDTO {
    private String applicationName;
    private List<String> acceptanceFormList;
}
