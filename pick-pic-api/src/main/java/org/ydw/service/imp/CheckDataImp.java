package org.ydw.service.imp;

import org.spark.ml.job.SparkJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.ydw.service.ICheckData;
import org.ydw.vo.entity.PictrueCheckResultEntity;
import org.ydw.vo.response.PickResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckDataImp implements ICheckData {

    @Value("${train.trueDataPath}")
    String trueDataPaht;

    @Value("${train.falseDataPath}")
    String falseDataPaht;

    @Value("${opencv.libPath}")
    String libDllPaht;

    @Override
    public PickResponse checkData(String picktruePath) {
        PickResponse response = new PickResponse();
        SparkJob job = new SparkJob(libDllPaht);
        try {
            HashMap<String,Boolean> result = job.checkData4(trueDataPaht, falseDataPaht, 200,picktruePath);
            List<PictrueCheckResultEntity> list = new ArrayList<>();
            for(Map.Entry entry : result.entrySet()){
                PictrueCheckResultEntity en = new PictrueCheckResultEntity();
                en.setFileName(entry.getKey().toString());
                en.setCheckResult((Boolean) entry.getValue());
                list.add(en);
            }
            response.setResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
