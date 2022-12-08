package com.yogit.server.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NationRes {
    private String country_iso_alp2;
    private String country_eng_nm;
    private String download_url;

    public static NationRes create(String country_iso_alp2, String country_eng_nm, String download_url){
        NationRes nationRes = new NationRes();

        nationRes.country_iso_alp2 = country_iso_alp2;
        nationRes.country_eng_nm = country_eng_nm;
        nationRes.download_url = download_url;

        return nationRes;
    }
}
