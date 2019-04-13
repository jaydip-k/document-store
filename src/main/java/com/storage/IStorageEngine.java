package com.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.util.ObjectMetaData;
import com.util.StorageOperationStatus;

public interface IStorageEngine {
	/**
	 * create object in default store
	 * @param file
	 * @return
	 * @throws IOException
	 */
	StorageOperationStatus createObject(File file) throws IOException;

	/**
	 * get object from default store
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	File getObject(String fileName) throws IOException;

	/**
	 * delete object in default store
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	void deleteObject(String fileName) throws FileNotFoundException;

	/**
	 * rename object in default store
	 * @param fromName
	 * @param toName
	 * @throws IOException
	 */
	void renameTo(String fromName, String toName) throws IOException;

	/**
	 * get meta data / file attributes of file in default store
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	ObjectMetaData getObjectMetaData(String fileName) throws FileNotFoundException, IOException;

	/**
	 * list objects in store 
	 * @param storeName
	 * @return
	 */
	List<String> listObjects(String storeName);

	/**
	 * get object in store
	 * @param storeName
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	File getObject(String storeName, String fileName) throws IOException;

	/**
	 * delete object from store
	 * @param storeName
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	void deleteObject(String storeName, String fileName) throws FileNotFoundException;
	/**
	 * rename file in store
	 * @param storeName
	 * @param fromName
	 * @param toName
	 * @throws IOException
	 */
	void renameTo(String storeName, String fromName, String toName) throws IOException;

	/**
	 * get meta data / file attributes of file in store
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
	 * @param file
	 * @return
	 * @throws IOException
	 */
	StorageOperationStatus createObject(String storeName, File file) throws IOException;

	/**
	 * delete store
	 * @param storeName
	 */
	void deleteStore(String storeName);

	/**
	 * create store
	 * @param storeName
	 */
	void createStore(String storeName);

	/**
	 * check give store exists in system or not
	 * @param storeName
	 * @return
	 */
	boolean doesStoreExists(String storeName);
	
	/**
	 * upload directory to store
	 * @param storeName
	 * @param directoryName
	 * @param localDirectory
	 * @throws Exception
	 */
	void uploadDirectory(String storeName, String directoryName, File localDirectory) throws Exception;

	/**
	 * upload directory to default store
	 * @param directoryName
	 * @param localDirectory
	 * @throws Exception
	 */
	void uploadDirectory(String directoryName, File localDirectory) throws Exception;

	/**
	 * download directory from store
	 * @param storeName
	 * @param directoryName
	 * @param localDirectoryPath
	 * @throws Exception
	 */
	void downloadDirectory(String storeName, String directoryName, String localDirectoryPath) throws Exception;

	/**
	 * download directory from default store
	 * @param directoryName
	 * @param localDirectoryPath
	 * @throws Exception
	 */
	void downloadDirectory(String directoryName, String localDirectoryPath) throws Exception;
}
