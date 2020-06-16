package com.baizhi.lht.test;


//public class OosTest {
//
//    @Test
//    public void oos(){
//        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
//// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
//        String accessKeyId = "LTAI4GAMZo4HjW8m8W5Qv83E";
//        String accessKeySecret = "arUd0arlY1Qi9QWtpBKEmmBYwWDp9j";
//
//// 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//// 创建CreateBucketRequest对象。
//        CreateBucketRequest createBucketRequest = new CreateBucketRequest("yingxue-lht1");
//
//// 如果创建存储空间的同时需要指定存储类型以及数据容灾类型, 可以参考以下代码。
//// 此处以设置存储空间的存储类型为标准存储为例。
//// createBucketRequest.setStorageClass(StorageClass.Standard);
//// 默认情况下，数据容灾类型为本地冗余存储，即DataRedundancyType.LRS。如果需要设置数据容灾类型为同城冗余存储，请替换为DataRedundancyType.ZRS。
//// createBucketRequest.setDataRedundancyType(DataRedundancyType.ZRS)
//
//// 创建存储空间。
//        ossClient.createBucket(createBucketRequest);
//
//// 关闭OSSClient。
//        ossClient.shutdown();
//    }
//}
