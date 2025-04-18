package com.movietix.xiazihao.utils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import com.movietix.xiazihao.constants.ConstantsManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.aliyun.oss.common.comm.SignVersion.V4;

public final class AliyunOssUtils {

    // 内置默认配置（可根据需要修改）
    private static final String DEFAULT_ENDPOINT = ConstantsManager.getInstance().getDefaultEndpoint();
    private static final String DEFAULT_BUCKET_NAME = ConstantsManager.getInstance().getDefaultBucketName();
    private static final String DEFAULT_REGION = ConstantsManager.getInstance().getDefaultRegion();
    private static final boolean DEFAULT_USE_V4_SIGN = ConstantsManager.getInstance().getDefaultUseV4Sign();

    private static OSS ossClient;
    private static String bucketName;
    private static boolean initialized = false;

    static {
        // 类加载时自动初始化默认配置
        try {
            init(DEFAULT_ENDPOINT, DEFAULT_BUCKET_NAME, DEFAULT_REGION, DEFAULT_USE_V4_SIGN);
        } catch (Exception e) {
            System.err.println("Failed to initialize OSS client with default configuration: " + e.getMessage());
        }
    }

    // 私有构造方法防止实例化
    private AliyunOssUtils() {
    }

    /**
     * 初始化OSS客户端（可选，如果默认配置不合适可以调用此方法覆盖）
     */
    public static synchronized void init(String endpoint, String bucketName, String region, boolean useV4Sign) {
        if (initialized) {
            shutdown(); // 如果已经初始化，先关闭现有客户端
        }

        AliyunOssUtils.bucketName = bucketName;

        try {
            // 从环境变量获取凭证
            CredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

            // 创建OSSClient实例
            ClientBuilderConfiguration config = new ClientBuilderConfiguration();
            if (useV4Sign) {
                config.setSignatureVersion(V4);
            }

            ossClient = OSSClientBuilder.create()
                    .endpoint(endpoint)
                    .credentialsProvider(credentialsProvider)
                    .region(region)
                    .build();

            initialized = true;
        } catch (com.aliyuncs.exceptions.ClientException e) {
            throw new RuntimeException("Failed to initialize OSS client", e);
        }
    }

    private static void checkInitialized() {
        if (!initialized) {
            throw new IllegalStateException("OSS client not initialized. Please check your environment variables " +
                    "or call AliyunOssUtils.init() with proper configuration.");
        }
    }

    //上传文件
    public static String uploadFile(String rootPath, File file) throws FileNotFoundException {
        checkInitialized();
        //根目录+UUID+文件后缀
        String objectName = rootPath+"/"+ UUID.randomUUID() +file.getName().substring(file.getName().lastIndexOf("."));
        ossClient.putObject(bucketName, objectName, new FileInputStream(file));
        return objectName;
    }

    public static void deleteFile(String objectName) {
        checkInitialized();
        ossClient.deleteObject(bucketName, objectName);
    }

    public static boolean doesObjectExist(String objectName) {
        checkInitialized();
        return ossClient.doesObjectExist(bucketName, objectName);
    }

    public static synchronized void shutdown() {
        if (ossClient != null) {
            ossClient.shutdown();
            ossClient = null;
            initialized = false;
        }
    }


}