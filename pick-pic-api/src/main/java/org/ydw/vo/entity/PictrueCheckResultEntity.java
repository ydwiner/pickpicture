package org.ydw.vo.entity;

import lombok.Data;

@Data
public class PictrueCheckResultEntity {

    private String fileName;

    private boolean checkResult;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isCheckResult() {
        return checkResult;
    }

    public void setCheckResult(boolean checkResult) {
        this.checkResult = checkResult;
    }
}
