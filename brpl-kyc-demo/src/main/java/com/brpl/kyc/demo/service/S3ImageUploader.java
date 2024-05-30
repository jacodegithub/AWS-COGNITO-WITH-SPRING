package com.brpl.kyc.demo.service;

import java.io.File;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

public class S3ImageUploader {

	private static final String BUCKET_NAME = "brpl-aadhar-pan-bucket";

    public static void uploadImageToS3(AmazonS3 s3Client, String keyName, File file) {
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, keyName, file);
        PutObjectResult result = s3Client.putObject(request);
        System.out.println("Image uploaded successfully. ETag: " + result.getETag());
    }
}
