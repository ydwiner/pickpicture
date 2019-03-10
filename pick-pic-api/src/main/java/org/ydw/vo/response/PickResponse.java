package org.ydw.vo.response;

import lombok.Data;
import org.ydw.vo.entity.PictrueCheckResultEntity;

import java.util.List;

@Data
public class PickResponse {

    private List<PictrueCheckResultEntity> result;

    public List<PictrueCheckResultEntity> getResult() {
        return result;
    }

    public void setResult(List<PictrueCheckResultEntity> result) {
        this.result = result;
    }
}
