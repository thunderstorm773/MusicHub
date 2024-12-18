package com.tu.musichub.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.Url;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class CdnUtil {

    private static final String RESOURCE_TYPE_KEY = "resource_type";

    private static final String FOLDER_KEY = "folder";

    public static final String VIDEO_RESOURCE_TYPE = "video";

    public static final String PUBLIC_ID = "public_id";

    public static final String SONGS_FOLDER = "songs";

    private static final String ATTACHMENT_FLAG = "attachment";

    public static final String CLOUD_NAME_KEY = "cloud_name";

    public static final String API_KEY = "api_key";

    public static final String API_SECRET_KEY = "api_secret";

    @Value("${CLOUD_NAME}")
    private String cloudName;

    @Value("${API_KEY}")
    private String apiKey;

    @Value("${API_SECRET}")
    private String apiSecret;

    private Cloudinary cloudinary;

    @PostConstruct
    private void configCloudinary() {
        Map configMap = ObjectUtils.asMap(
                CLOUD_NAME_KEY, this.cloudName,
                API_KEY, this.apiKey,
                API_SECRET_KEY, this.apiSecret
        );

        this.cloudinary = new Cloudinary(configMap);
    }

    @Async
    public CompletableFuture<Map> upload(File file, String resourceType,
                                         String folderToUpload) throws IOException {
        Map fileConfig = ObjectUtils.asMap(
                RESOURCE_TYPE_KEY, resourceType,
                FOLDER_KEY, folderToUpload);
        Map uploadResult = this.cloudinary.uploader().upload(file, fileConfig);
        return CompletableFuture.completedFuture(uploadResult);
    }

    public String getResourceFullUrl(String partialUrl, String resourceType) {
        return this.cloudinary.url().resourceType(resourceType).generate(partialUrl);
    }

    public String getResourceDownloadUrl(String partialUrl, String resourceType) {
        Transformation attachmentFlag = new Transformation().flags(ATTACHMENT_FLAG);
        Url url = this.cloudinary.url().transformation(attachmentFlag);
        return url.resourceType(resourceType).generate(partialUrl);
    }

    public void deleteResource(String partialUrl) throws Exception {
        this.cloudinary.api().deleteResources(Arrays.asList(partialUrl),
                ObjectUtils.asMap(RESOURCE_TYPE_KEY, VIDEO_RESOURCE_TYPE));
    }
}
