package com.tu.musichub.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.Url;
import com.cloudinary.utils.ObjectUtils;
import com.tu.musichub.config.CdnConfigData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
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

    private final CdnConfigData configData;

    private Cloudinary cloudinary;

    @Autowired
    public CdnUtil(CdnConfigData configData) {
        this.configData = configData;
        this.configCloudinary();
    }

    private void configCloudinary() {
        Map configMap = ObjectUtils.asMap(
                CdnConfigData.CLOUD_NAME_KEY, configData.getCloudName(),
                CdnConfigData.API_KEY, configData.getApiKey(),
                CdnConfigData.API_SECRET_KEY, configData.getApiSecret()
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
        String resourceFullUrl = this.cloudinary.url()
                .resourceType(resourceType).generate(partialUrl);
        return resourceFullUrl;
    }

    public String getResourceDownloadUrl(String partialUrl, String resourceType) {
        Transformation attachmentFlag = new Transformation().flags(ATTACHMENT_FLAG);
        Url url = this.cloudinary.url().transformation(attachmentFlag);
        String downloadUrl = url.resourceType(resourceType).generate(partialUrl);
        return downloadUrl;
    }

    public void deleteResource(String partialUrl) throws Exception {
        this.cloudinary.api().deleteResources(Arrays.asList(partialUrl),
                ObjectUtils.asMap(RESOURCE_TYPE_KEY, VIDEO_RESOURCE_TYPE));
    }
}
