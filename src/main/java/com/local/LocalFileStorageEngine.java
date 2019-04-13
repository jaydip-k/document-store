package com.local;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.service.IStorageService;
import com.service.LocalFileStorageServiceImpl;
import com.storage.IStorageEngine;
import com.util.CommonUtils;
import com.util.ObjectMetaData;
import com.util.StorageOperationStatus;

public class LocalFileStorageEngine implements IStorageEngine {

	private Properties prop = null;
	private String defaultStoreName = null;
	IStorageService localFileStorageService = null;

	public LocalFileStorageEngine() throws IOException {
		try (InputStream is = this.getClass().getResourceAsStream("/config.properties")) {
			this.prop = new Properties();
			prop.load(is);
			localFileStorageService = new LocalFileStorageServiceImpl();
			defaultStoreName = this.prop.getProperty("local.uploads");
			if (!CommonUtils.isNullOrEmpty(defaultStoreName)) {
				createStore(defaultStoreName);
			}
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException(" Propetries file not found " + e.getMessage());
		} catch (IOException e) {
			throw new IOException(" Can not create instance . Please check configuration." + e.getCause());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#createStore(java.lang.String)
	 */
	@Override
	public void createStore(String storeName) {
		localFileStorageService.createStore(storeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#createObject(java.io.File)
	 */
	@Override
	public StorageOperationStatus createObject(File file) throws IOException {
		return localFileStorageService.createObject(defaultStoreName, file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#createObject(java.lang.String, java.io.File)
	 */
	@Override
	public StorageOperationStatus createObject(String storeName, File file) throws IOException {
		return localFileStorageService.createObject(storeName, file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#getObject(java.lang.String)
	 */
	@Override
	public File getObject(String fileName) throws IOException {
		return localFileStorageService.getObject(defaultStoreName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#getObject(java.lang.String, java.lang.String)
	 */
	@Override
	public File getObject(String storeName, String fileName) throws IOException {
		return localFileStorageService.getObject(storeName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#getObjectMetaData(java.lang.String)
	 */
	@Override
	public ObjectMetaData getObjectMetaData(String fileName) throws FileNotFoundException, IOException {
		return localFileStorageService.getObjectMetaData(defaultStoreName, fileName);
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
		return localFileStorageService.getObjectMetaData(storeName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#renameTo(java.lang.String, java.lang.String)
	 */
	@Override
	public void renameTo(String fromName, String toName) throws IOException {
		localFileStorageService.renameTo(defaultStoreName, fromName, toName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#renameTo(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void renameTo(String storeName, String fromName, String toName) throws IOException {
		localFileStorageService.renameTo(storeName, fromName, toName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#deleteObject(java.lang.String)
	 */
	@Override
	public void deleteObject(String fileName) throws FileNotFoundException {
		localFileStorageService.deleteObject(defaultStoreName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#deleteObject(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void deleteObject(String storeName, String fileName) throws FileNotFoundException {
		localFileStorageService.deleteObject(storeName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#listObjects(java.lang.String)
	 */
	@Override
	public List<String> listObjects(String storeName) {
		return localFileStorageService.listObjects(storeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#deleteStore(java.lang.String)
	 */
	@Override
	public void deleteStore(String storeName) {
		localFileStorageService.deleteStore(storeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#doesStoreExists(java.lang.String)
	 */
	@Override
	public boolean doesStoreExists(String storeName) {
		return localFileStorageService.doesStoreExists(storeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#uploadDirectory(java.lang.String,
	 * java.lang.String, java.io.File)
	 */
	@Override
	public void uploadDirectory(String storeName, String directoryName, File localDirectory) throws Exception {
		localFileStorageService.uploadDirectory(storeName, directoryName, localDirectory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#uploadDirectory(java.lang.String,
	 * java.io.File)
	 */
	@Override
	public void uploadDirectory(String directoryName, File localDirectory) throws Exception {
		localFileStorageService.uploadDirectory(defaultStoreName, directoryName, localDirectory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#downloadDirectory(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void downloadDirectory(String storeName, String directoryName, String localDirectoryPath) throws Exception {
		localFileStorageService.downloadDirectory(storeName, directoryName, localDirectoryPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.storage.IStorageEngine#downloadDirectory(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void downloadDirectory(String directoryName, String localDirectoryPath) throws Exception {
		localFileStorageService.downloadDirectory(defaultStoreName, directoryName, localDirectoryPath);
	}

}
