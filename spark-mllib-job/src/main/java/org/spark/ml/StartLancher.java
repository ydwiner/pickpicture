package org.spark.ml;

import org.apache.spark.mllib.classification.SVMModel;
import org.spark.ml.job.SparkJob;

import java.util.HashMap;

public class StartLancher {

    public static void main(String[] args) {
        SparkJob sparkJob = new SparkJob(args[3]);

        try {
            SVMModel model = sparkJob.trainData(args[0],args[1],200);
            HashMap<String, Boolean> result = sparkJob.checkData(model, args[2]);
            System.out.println(result.toString());
//            System.out.println(sparkJob.checkData5(args[0],args[1],200,args[2]).toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
