package com.haowaner;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.UnsupportedEncodingException;

public class QiniuTest {
    //这种上传图片的方式是错误的
    //将秘钥文件名提取出来
    //...生成上传凭证，然后准备上传
    private static String accessKey = "LOIwfHGiOUn2AKPMkRDx_72AYtvuo3nnH5bmjZwi";
    private static String secretKey = "agKrFP2LtOx1_JSdMGDG1ZkGt0z3P-h_i-5CAgwD";
    private static String bucket = "jbb-pricture";
    public static void main(String[] args) {
        String fileName="456.gif";
        String filePath="D:\\yk_temp\\1234.PNG";
        upload(filePath,fileName);
    }
        public static void upload(String filePath,String fileName){
            //构造一个带指定 Region 对象的配置类
            Configuration cfg = new Configuration(Zone.zone2());
            //...其他参数参考类注释
            UploadManager uploadManager = new UploadManager(cfg);
            //默认不指定key的情况下，以文件内容的hash值作为文件名
            String key = fileName;
            try {
                byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
                Auth auth = Auth.create(accessKey, secretKey);
                String upToken = auth.uploadToken(bucket);
                try {
                    Response response = uploadManager.put(uploadBytes, key, upToken);
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    System.out.println(putRet.key);
                    System.out.println(putRet.hash);
                } catch (QiniuException ex) {
                    Response r = ex.response;
                    System.err.println(r.toString());
                    try {
                        System.err.println(r.bodyString());
                    } catch (QiniuException ex2) {
                        //ignore
                    }
                }
            } catch (UnsupportedEncodingException ex) {
                //ignore
            }
        }
  }
