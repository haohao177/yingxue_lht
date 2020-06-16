package com.baizhi.lht.util;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class AliyunOSSUtil {

    // Endpoint以杭州为例，其它Region请按实际情况填写。
    private static String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private static String accessKeyId = "LTAI4GAMZo4HjW8m8W5Qv83E";
    private static String accessKeySecret = "arUd0arlY1Qi9QWtpBKEmmBYwWDp9j";


    /*上传本地文件
     * 参数：
     *   bucketName:存储空间名
     *   fileName:指定上传至阿里云的文件名
     *   localFilePath:本地文件路径
     * */
    public static void uploadLocalFile(String bucketName, String fileName, String localFilePath) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, new File(localFilePath));

        // 上传文件。
        ossClient.putObject(putObjectRequest);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /*上传字节数组
     * 参数：
     *    headImg：上传MultipartFile类型的文件
     *   bucketName:存储空间名
     *   fileName:指定上传至阿里云的文件名
     * */
    public static void uploadBytes(MultipartFile headImg, String bucketName, String fileName) {

        byte[] bytes = new byte[0];
        try {
            bytes = headImg.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传Byte数组。
        ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(bytes));

        // 关闭OSSClient。
        ossClient.shutdown();

    }

    /*
     * 删除文件
     * 参数：
     *   bucketName:存储空间名
     *   fileName:指定上传至阿里云的文件名
     * */
    public static void deleteFile(String bucketName, String fileName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, fileName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /*
     * 文件下载
     *   参数：
     *     bucketName：存储空间名
     *     fileName：指定上传至阿里云的文件名
     *     localFile：指定下载到本地的路径
     * */
    public static void dowmloadFile(String bucketName, String fileName, String localFile) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest(bucketName, fileName), new File(localFile));

        // 关闭OSSClient。
        ossClient.shutdown();

    }

    /*
     * 视频截取封面
     *   参数：
     *     bucketName：存储空间名
     *     fileName：指定上传至阿里云的文件名
     * */
    public static String videoInterceptImage(String bucketName, String fileName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 设置视频截帧操作。
        String style = "video/snapshot,t_10000,f_jpg,w_800,h_600";
        // 指定过期时间为10分钟。
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10);
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, fileName, HttpMethod.GET);
        req.setExpiration(expiration);
        req.setProcess(style);
        URL signedUrl = ossClient.generatePresignedUrl(req);


        // 关闭OSSClient。
        ossClient.shutdown();

        return signedUrl.toString();
    }

    /*
     * 通过网络流上传
     *   参数：
     *     bucketName：存储空间名
     *     fileName：指定上传至阿里云的文件名
     *     netPath:文件的网络地址
     * */
    public static void uploadNet(String bucketName, String fileName, String netPath) throws IOException {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传网络流。
        InputStream inputStream = new URL(netPath).openStream();
        ossClient.putObject(bucketName, fileName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    public static void main(String[] args) {
        String bucket = "yingx-190";   //存储空间名
        String fileName = "video/1590654192648-打玉米.mp4";  //文件名  可以加上文件夹名称
        //String localFile="C:\\Users\\NANAN\\Desktop\\other\\video\\人民的名义.mp4";  //本地文件路径
        //uploadLocalFile(bucket,fileName,localFile);

        //调用截取方法
        String path = videoInterceptImage(bucket, fileName);

        //上传网路文件
        try {
            uploadNet(bucket, "cover/1590654192648-打玉米.jpg", path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //http://yingx-190.oss-cn-beijing.aliyuncs.com/video/1590654192648-%E6%89%93%E7%8E%89%E7%B1%B3.mp4?Expires=1590655333&OSSAccessKeyId=LTAI4GL7UJq3kZHGo4VXjXVj&Signature=X5SvRyZ8ZzWiW0cfNwOpUxJkclg%3D&x-oss-process=video%2Fsnapshot%2Ct_10000%2Cf_jpg%2Cw_800%2Ch_600
    }

}
