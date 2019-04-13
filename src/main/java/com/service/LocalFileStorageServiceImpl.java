package com.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.util.ObjectMetaData;
import com.util.StorageOperationStatus;

public class LocalFileStorageServiceImpl implements IStorageService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.IStorageService#createStore(java.lang.String)
	 */
	@Override
	public void createStore(String storeName) {
		File targetFile = new File(storeName);
		if (!targetFile.exists()) {
			targetFile.mkdir();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.IStorageService#createObject(java.lang.String, java.io.File)
	 */
	@Override
	public StorageOperationStatus createObject(String storeName, File sourceFile) throws IOException {
		String absoluteFilePath = storeName + File.separator + sourceFile.getName();
		if (!doesStoreExists(storeName)) {
			throw new IllegalArgumentException("Store Does not Exists. Please Create Store First.");
		}
		File destFile = new File(absoluteFilePath);
		if (destFile.createNewFile()) {
		}
		Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES);
		return StorageOperationStatus.SUCCESS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.IStorageService#getObject(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public File getObject(String storeName, String fileName) throws FileNotFoundException {
		String absoluteFilePath = storeName + File.separator + fileName;
		if (!doesStoreExists(storeName)) {
			throw new IllegalArgumentException("Store Does not Exists. Please Create Store First.");
		}

		File file = new File(absoluteFilePath);
		if (!file.exists()) {
			throw new FileNotFoundException(fileName);
		}
		return file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.IStorageService#getObjectMetaData(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ObjectMetaData getObjectMetaData(String storeName, String fileName)
			throws FileNotFoundException, IOException {
		String absoluteFilePath = storeName + File.separator + fileName;
		if (!doesStoreExists(storeName)) {
			throw new IllegalArgumentException("Store Does not Exists. Please Create Store First.");
		}
		File file = new File(absoluteFilePath);
		if (!file.exists()) {
			throw new FileNotFoundException(fileName);
		}
		ObjectMetaData attributes = new ObjectMetaData();
		attributes.setLastModifiedAt(new Date(file.lastModified()));
		attributes.setSize((double) file.length() / 1024 + " kb");
		return attributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.IStorageService#renameTo(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void renameTo(String storeName, String fromName, String toName) throws IOException {
		String absoluteFromPath = storeName + File.separator + fromName;
		if (!doesStoreExists(storeName)) {
			throw new IllegalArgumentException("Store Does not Exists. Please Create Store First.");
		}
		File file = new File(absoluteFromPath);
		if (!file.exists()) {
			throw new FileNotFoundException(fromName);
		}
		Path source = Paths.get(absoluteFromPath);
		Files.move(source, source.resolveSibling(toName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.IStorageService#deleteObject(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void deleteObject(String storeName, String fileName) throws FileNotFoundException {
		String absoluteFilePath = storeName + File.separator + fileName;
		if (!doesStoreExists(storeName)) {
			throw new IllegalArgumentException("Store Does not Exists. Please Create Store First.");
		}
		File file = new File(absoluteFilePath);
		if (!file.exists()) {
			throw new FileNotFoundException(fileName);
		}
		file.delete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.IStorageService#listObjects(java.lang.String)
	 */
	@Override
	public List<String> listObjects(String storeName) {
		if (!doesStoreExists(storeName)) {
			throw new IllegalArgumentException("Store Does not Exists. Please Create Store First.");
		}
		File[] files = new File(storeName).listFiles();
		return Arrays.stream(files).filter(f -> f.isFile()).map(f -> f.getName()).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.IStorageService#deleteStore(java.lang.String)
	 */
	@Override
	public void deleteStore(String storeName) {
		if (!doesStoreExists(storeName)) {
			throw new IllegalArgumentException("Store Does not Exists. Please Create Store First.");
		}
		recursiveDelete(new File(storeName));
	}

	private void recursiveDelete(File file) {
		if (!file.exists())
			return;
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				recursiveDelete(f);
			}
		}
		file.delete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.IStorageService#doesStoreExists(java.lang.String)
	 */
	@Override
	public boolean doesStoreExists(String storeName) {
		File file = new File(storeName);
		if (file.exists() && file.isDirectory()) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.IStorageService#uploadDirectory(java.lang.String,
	 * java.lang.String, java.io.File)
	 */
	@Override
	public void uploadDirectory(String storeName, String directoryName, File localDirectory) throws Exception {
		String absoluteFilePath = storeName + File.separator + directoryName;
		File storeDirectory = new File(absoluteFilePath);
		copyDirectory(localDirectory, storeDirectory);
	}

	protected void copyDirectory(File source, File target) throws FileNotFoundException, IOException {
		if (source.isDirectory()) {
			if (!target.exists()) {
				target.mkdir();
			}
			String[] children = source.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(source, children[i]), new File(target, children[i]));
			}
		} else {
			InputStream in = new FileInputStream(source);
			OutputStream out = new FileOutputStream(target);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.IStorageService#downloadDirectory(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void downloadDirectory(String storeName, String directoryName, String localDirectoryPath) throws Exception {
		String absoluteFilePath = storeName + File.separator + directoryName;
		File storeDirectory = new File(absoluteFilePath);
		File localDirectory = new File(localDirectoryPath);
		copyDirectory(storeDirectory, localDirectory);
	}
}
