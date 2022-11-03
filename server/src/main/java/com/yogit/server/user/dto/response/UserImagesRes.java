package com.yogit.server.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserImagesRes {
    String profileImageUrl;
    List<String> imageUrls = new ArrayList<>();

    public void addImage(String url){
        this.imageUrls.add(url);
    }
}