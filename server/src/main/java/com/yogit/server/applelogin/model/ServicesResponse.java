package com.yogit.server.applelogin.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;

@Data
@NoArgsConstructor
public class ServicesResponse {

    private String state;
    private String code;
    private String id_token;
    private JSONObject user;
}
