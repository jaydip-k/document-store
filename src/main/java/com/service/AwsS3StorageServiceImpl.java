package com.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.util.ObjectMetaData;
import com.util.StorageOperationStatus;

public class AwsS3StorageServiceImpl implements IStorageService {

	private AmazonS3 amazonS3;
	private TransferManager transferManager;

	public AwsS3StorageServiceImpl(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
		this.transferManager = TransferManagerBuilder.standard().withS3Client(amazonS3).build();
	}

	/*
	 * (non-Javadoc)
	 * @see com.service.IStorageService#createStore(java.lang.String)
	 */
	@Override
	public void createStore(String storeName) {
		if (!amazonS3.doesBucketExist(storeName)) {
			amazonS3.createBucket(storeName);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.service.IStorageService#doesStoreExists(java.lang.String)
	 */
	@Override
	public boolean doesStoreExists(String storeName) {
		return amazonS3.doesBucketExist(storeName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.service.IStorageService#deleteStore(java.lang.String)
	 */
	@Override
	public void deleteStore(String storeName) {
		amazonS3.deleteBucket(storeName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.service.IStorageService#listObjects(java.lang.String)
	 */
	@Override
	public List<String> listObjects(String storeName) {
		ObjectListing objectListing = amazonS3.listObjects(storeName);
		return objectListing.getObjectSummaries().stream().map(o -> o.getKey()).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * @see com.service.IStorageService#getObject(java.lang.String, java.lang.String)
	 */
	@Override
	public File getObject(String storeName, String fileName) throws IOException {
		S3Object s3object = amazonS3.getObject(storeName, fileName);
		File tmp = getFileFromObject(fileName, s3object);
		return tmp;
	}

	public File getFileFromObject(String fileName, S3Object s3object) throws IOException {
		InputStream inputStream = s3object.getObjectContent();
		File tmp = File.createTempFile(fileName, "");
		Files.copy(inputStream, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
		return tmp;
	}

	/*
	 * (non-Javadoc)
	 * @see com.service.IStorageService#deleteObject(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteObject(String storeName, String fileName) {
		amazonS3.deleteObject(storeName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.service.IStorageService#createObject(java.lang.String, java.io.File)
	 */
	@Override
	public StorageOperationStatus createObject(String storeName, File file) {
		PutObjectResult res = amazonS3.putObject(storeName, file.getName(), file);
		return res != null ? StorageOperationStatus.SUCCESS : StorageOperationStatus.FAILED;
	}

	/*
	 * (non-Javadoc)
	 * @see com.service.IStorageService#renameTo(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void renameTo(String storeName, String fromName, String toName) {
		amazonS3.copyObject(storeName, fromName, storeName, toName);
		amazonS3.deleteObject(storeName, fromName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.service.IStorageService#getObjectMetaData(java.lang.String, java.lang.String)
	 */
	@Override
	public ObjectMetaData getObjectMetaData(String storeName, String fileName) {
		ObjectMetadata meta = amazonS3.getObjectMetadata(storeName, fileName);
		ObjectMetaData attributes = new ObjectMetaData();
		attributes.setLastModifiedAt(meta.getLastModified());
		attributes.setSize((double) meta.getContentLength() / 1024 + " kb");
		return attributes;
	}

	/*
	 * (non-Javadoc)
	 * @see com.service.IStorageService#uploadDirectory(java.lang.String, java.lang.String, java.io.File)
	 */
	@Override
	public void uploadDirectory(String storeName, String directoryName, File localDirectory) throws Exception {
		MultipleFileUpload multipleFileDownload = transferManager.uploadDirectory(storeName, directoryName,
				localDirectory, true);
		multipleFileDownload.waitForCompletion();
	}

	/*
	 * (non-Javadoc)
	 * @see com.service.IStorageService#downloadDirectory(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void downloadDirectory(String storeName, String directoryName, String localDirectoryPath) throws Exception {
		MultipleFileDownload multipleFileDownload = transferManager.downloadDirectory(storeName, directoryName,
				new File(localDirectoryPath));
		multipleFileDownload.waitForCompletion();
	}
}
