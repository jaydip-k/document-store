package com.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.util.ObjectMetaData;
import com.util.StorageOperationStatus;

public interface IStorageService {

	/**
	 * 
	 * @param storeName
	 */
	void createStore(String storeName);

	/**
	 * 
	 * @param storeName
	 * @param sourceFile
	 * @return
	 * @throws IOException
	 */
	StorageOperationStatus createObject(String storeName, File sourceFile) throws IOException;

	/**
	 * 
	 * @param storeName
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException 
	 */
	File getObject(String storeName, String fileName) throws FileNotFoundException, IOException;

	/**
	 * 
	 * @param storeName
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	ObjectMetaData getObjectMetaData(String storeName, String fileName) throws FileNotFoundException, IOException;

	/**
	 * 
	 * @param storeName
	 * @param fromName
	 * @param toName
	 * @throws IOException
	 */
	void renameTo(String storeName, String fromName, String toName) throws IOException;

	/**
	 * 
	 * @param storeName
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	void deleteObject(String storeName, String fileName) throws FileNotFoundException;

	/**
	 * 
	 * @param storeName
	 * @return
	 */
	List<String> listObjects(String storeName);

	/**
	 * 
	 * @param storeName
	 */
	void deleteStore(String storeName);

	/**
	 * 
	 * @param storeName
	 * @return
	 */
	boolean doesStoreExists(String storeName);

	/**
	 * 
	 * @param storeName
	 * @param directoryName
	 * @param localDirectory
	 * @throws Exception
	 */
	void uploadDirectory(String storeName, String directoryName, File localDirectory) throws Exception;

	/**
	 * 
	 * @param storeName
	 * @param directoryName
	 * @param localDirectoryPath
	 * @throws Exception
	 */
	void downloadDirectory(String storeName, String directoryName, String localDirectoryPath) throws Exception;
}
