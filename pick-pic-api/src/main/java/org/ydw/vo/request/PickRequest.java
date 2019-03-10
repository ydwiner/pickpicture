package org.ydw.vo.request;

import lombok.Data;

@Data
public class PickRequest {

    private String pictruePath;

    public String getPictruePath() {
        return pictruePath;
    }

    public void setPictruePath(String pictruePath) {
        this.pictruePath = pictruePath;
    }
}
