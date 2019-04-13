package com.remote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.service.AwsS3StorageServiceImpl;
import com.service.IStorageService;
import com.storage.IStorageEngine;
import com.util.CommonUtils;
import com.util.ObjectMetaData;
import com.util.StorageOperationStatus;

public class AwsS3StorageEngine implements IStorageEngine {

	private Properties prop = null;
	private String defaultStoreName = null;
	private IStorageService awsS3StorageService;

	public AwsS3StorageEngine() throws Exception {
		try (InputStream is = this.getClass().getResourceAsStream("/config.properties");) {
			this.prop = new Properties();
			prop.load(is);
			defaultStoreName = this.prop.getProperty("remote.s3.storeName");
			AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(getCredentials()))
					.withRegion(Regions.fromName(getRegionName())).build();

			awsS3StorageService = new AwsS3StorageServiceImpl(amazonS3);
		} catch (Exception e) {
			throw e;
		}
	}

	public String getRegionName() {
		String regionName = this.prop.getProperty("remote.s3.region");
		if (CommonUtils.isNullOrEmpty(regionName)) {
			throw new IllegalArgumentException(" Please Provide Region to access AWS S3 ");
		}
		return regionName;
	}

	public BasicAWSCredentials getCredentials() {
		String awsKey = this.prop.getProperty("remote.s3.accesskey");
		String awsSecret = this.prop.getProperty("remote.s3.secretkey");
		if (CommonUtils.isNullOrEmpty(awsKey) || CommonUtils.isNullOrEmpty(awsSecret)) {
			throw new IllegalArgumentException(" Please Provide AWS Access Key and AWS Secret to access AWS S3 ");
		}
		return new BasicAWSCredentials(awsKey, awsSecret);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#createStore(java.lang.String)
	 */
	@Override
	public void createStore(String storeName) {
		awsS3StorageService.createStore(storeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#doesStoreExists(java.lang.String)
	 */
	@Override
	public boolean doesStoreExists(String storeName) {
		return awsS3StorageService.doesStoreExists(storeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#deleteStore(java.lang.String)
	 */
	@Override
	public void deleteStore(String storeName) {
		awsS3StorageService.deleteStore(storeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#listObjects(java.lang.String)
	 */
	@Override
	public List<String> listObjects(String storeName) {
		return awsS3StorageService.listObjects(storeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#getObject(java.lang.String)
	 */
	@Override
	public File getObject(String fileName) throws IOException {
		return awsS3StorageService.getObject(defaultStoreName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#getObject(java.lang.String, java.lang.String)
	 */
	@Override
	public File getObject(String storeName, String fileName) throws IOException {
		return awsS3StorageService.getObject(storeName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#deleteObject(java.lang.String)
	 */
	@Override
	public void deleteObject(String fileName) throws FileNotFoundException {
		awsS3StorageService.deleteObject(defaultStoreName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#deleteObject(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void deleteObject(String storeName, String fileName) throws FileNotFoundException {
		awsS3StorageService.deleteObject(storeName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#createObject(java.io.File)
	 */
	@Override
	public StorageOperationStatus createObject(File file) throws IOException {
		return awsS3StorageService.createObject(defaultStoreName, file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#createObject(java.io.File, java.lang.String)
	 */
	@Override
	public StorageOperationStatus createObject(String storeName, File file) throws IOException {
		return awsS3StorageService.createObject(storeName, file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#renameTo(java.lang.String, java.lang.String)
	 */
	@Override
	public void renameTo(String fromName, String toName) throws IOException {
		awsS3StorageService.renameTo(defaultStoreName, fromName, toName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#renameTo(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void renameTo(String storeName, String fromName, String toName) throws IOException {
		awsS3StorageService.renameTo(storeName, fromName, toName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#getObjectMetaData(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ObjectMetaData getObjectMetaData(String storeName, String fileName)
			throws FileNotFoundException, IOException {
		return awsS3StorageService.getObjectMetaData(storeName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#getObjectMetaData(java.lang.String)
	 */
	@Override
	public ObjectMetaData getObjectMetaData(String fileName) throws FileNotFoundException, IOException {
		return awsS3StorageService.getObjectMetaData(defaultStoreName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#uploadDirectory(java.lang.String,
	 * java.io.File)
	 */
	@Override
	public void uploadDirectory(String directoryName, File localDirectory) throws Exception {
		awsS3StorageService.uploadDirectory(defaultStoreName, directoryName, localDirectory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#uploadDirectory(java.lang.String,
	 * java.lang.String, java.io.File)
	 */
	@Override
	public void uploadDirectory(String storeName, String directoryName, File localDirectory) throws Exception {
		awsS3StorageService.uploadDirectory(storeName, directoryName, localDirectory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#downloadDirectory(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void downloadDirectory(String directoryName, String localDirectoryPath) throws Exception {
		awsS3StorageService.downloadDirectory(defaultStoreName, directoryName, localDirectoryPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#downloadDirectory(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void downloadDirectory(String storeName, String directoryName, String localDirectoryPath) throws Exception {
		awsS3StorageService.downloadDirectory(storeName, directoryName, localDirectoryPath);
	}
}
