package org.spark.ml.job;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithSGD;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.sql.SparkSession;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SparkJob implements Serializable {

    private SparkSession session;

    public SparkJob(String dllPath){
        session = SparkSession.builder().appName("pickPic").master("local[2]").getOrCreate();
        System.load(dllPath);
    }

    public SVMModel trainData(String TrueDataPath, String FalseDataPath, int num) throws Exception{
        File fileDirT = new File(TrueDataPath);
        File fileDirF = new File(FalseDataPath);

        if(!fileDirT.isDirectory() || !fileDirF.isDirectory()){
            throw new Exception("参数需要文件夹");
        }
        List<LabeledPoint> list = new ArrayList<>();
        int i = 0;
        int j = 0;
        for(String fileName : fileDirT.list()){
            LabeledPoint labeledPoint = new LabeledPoint(1.0, Vectors.dense(openvc(TrueDataPath + File.separator + fileName)));
            list.add(labeledPoint);
            i++;
            System.out.println(i);
        }
        for(String fileName : fileDirF.list()){
            LabeledPoint labeledPoint = new LabeledPoint(0.0, Vectors.dense(openvc(FalseDataPath + File.separator + fileName)));
            list.add(labeledPoint);
            j++;
            System.out.println(j);
        }

        JavaRDD<LabeledPoint> rdd = JavaSparkContext.fromSparkContext(session.sparkContext()).parallelize(list);

        SVMModel model = SVMWithSGD.train(rdd.rdd(), num);

        return model;
    }

    public HashMap<String, Boolean> checkData(SVMModel model,String checkDataPath) throws Exception{
        HashMap<String,Boolean> result = new HashMap<>();
        File currentFileDir = new File(checkDataPath);
        if(!currentFileDir.isDirectory()){
            throw new Exception("参数必须是文件夹");
        }
        for(String str  : currentFileDir.list()){
//            System.out.println(model.predict(Vectors.dense(openvc(checkDataPath + "\\" + str))));
            result.put(checkDataPath + File.separator + str,model.predict(Vectors.dense(openvc(checkDataPath + File.separator + str))) == 1.0 ? true:false);
        }
        return result;
    }

    public HashMap<String, Boolean> checkData2(SVMModel model,String checkDataPath) throws Exception{
        HashMap<String,Boolean> result = new HashMap<>();
        File currentFileDir = new File(checkDataPath);
        if(currentFileDir.isDirectory()){
            throw new Exception("参数必须是文件");
        }
        result.put(checkDataPath,model.predict(Vectors.dense(openvc(checkDataPath))) == 1.0 ? true:false);
        return result;
    }

    public HashMap<String, Boolean> checkData3(String TrueDataPath, String FalseDataPath,
                                               int num,String checkDataPath) throws Exception{
        SVMModel model = trainData(TrueDataPath,FalseDataPath,num);
        HashMap<String,Boolean> result = new HashMap<>();
        File currentFileDir = new File(checkDataPath);
        if(!currentFileDir.isDirectory()){
            throw new Exception("参数必须是文件夹");
        }
        for(String str  : currentFileDir.list()){
            result.put(checkDataPath + File.separator + str,model.predict(Vectors.dense(openvc(checkDataPath + File.separator + str))) == 1.0 ? true:false);
        }
        return result;
    }

    public HashMap<String, Boolean> checkData4(String TrueDataPath, String FalseDataPath,
                                               int num,String checkDataFilePath) throws Exception{
        SVMModel model = trainData(TrueDataPath,FalseDataPath,num);
        HashMap<String,Boolean> result = new HashMap<>();
        File currentFile = new File(checkDataFilePath);
        if(currentFile.isDirectory()){
            throw new Exception("参数必须是文件");
        }
        result.put(checkDataFilePath,model.predict(Vectors.dense(openvc(checkDataFilePath))) == 1.0 ? true:false);
        return result;
    }

    public HashMap<String,Double> checkData5(String TrueDataPath, String FalseDataPath,
                                             int num,String checkDataPath) throws Exception{

        HashMap<String,Double> result = new HashMap<>();
        File fileDirT = new File(TrueDataPath);
        File fileDirF = new File(FalseDataPath);

        if(!fileDirT.isDirectory() || !fileDirF.isDirectory()){
            throw new Exception("参数需要文件夹");
        }
        List<LabeledPoint> list = new ArrayList<>();
        int i = 0;
        int j = 0;
        for(String fileName : fileDirT.list()){
            LabeledPoint labeledPoint = new LabeledPoint(1.0, Vectors.dense(openvc(TrueDataPath + File.separator + fileName)));
            list.add(labeledPoint);
            i++;
            System.out.println(i);
        }
        for(String fileName : fileDirF.list()){
            LabeledPoint labeledPoint = new LabeledPoint(0.0, Vectors.dense(openvc(FalseDataPath + File.separator + fileName)));
            list.add(labeledPoint);
            j++;
            System.out.println(j);
        }

        JavaRDD<LabeledPoint> rdd = JavaSparkContext.fromSparkContext(session.sparkContext()).parallelize(list);

        LogisticRegressionModel model = LogisticRegressionWithSGD.train(rdd.rdd(), num);

        File currentFileDir = new File(checkDataPath);
        if(!currentFileDir.isDirectory()){
            throw new Exception("参数必须是文件夹");
        }
        for(String str  : currentFileDir.list()){
            result.put(checkDataPath + File.separator + str,model.predict(Vectors.dense(openvc(checkDataPath + File.separator + str))));
        }

        return result;
    }

    private double[] openvc(String filePath) throws Exception{
        try{
            Mat scr = Imgcodecs.imread(filePath);
            Mat scrGray = new Mat();
            Imgproc.cvtColor(scr, scrGray,Imgproc.COLOR_RGB2GRAY);
            FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
            DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
            MatOfKeyPoint matOfKeyPoint = new MatOfKeyPoint();
            detector.detect(scr,matOfKeyPoint);
            descriptorExtractor.compute(scr, matOfKeyPoint, scrGray);
            List<Double> list = new ArrayList<>();
            for(int i = 0; i < scrGray.rows(); i++){
                for(int j = 0; j < scrGray.cols(); j++){
                    double[] data = scrGray.get(i,j);
                    for(int m = 0; m < data.length; m++){
                        list.add(data[m]);
                    }
                }
            }
            double[] result = new double[list.size()];
            for(int i = 0; i < list.size(); i++){
                result[i] = list.get(i);
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
