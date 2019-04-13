package com.remote;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.service.IStorageService;
import com.util.StorageOperationStatus;

@RunWith(MockitoJUnitRunner.class)
public class AwsS3StorageEngineTest2 {

	private static final String FILE_NAME = "fileName";
	private static final String BUCKET = "bucket";
	@InjectMocks
	private AwsS3StorageEngine awsS3StorageEngine;
	@Mock
	private IStorageService awsS3StorageService;
	@Mock
	private File file;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void whenDoesBucketExist_thenExpectMethodCalled() {
		// ACT
		awsS3StorageEngine.doesStoreExists(BUCKET);
		// ASSERT
		verify(awsS3StorageService).doesStoreExists(BUCKET);
	}

	@Test
	public void whenCreateStore_thenExpectMethodCalled() {
		// ACT
		awsS3StorageEngine.createStore(BUCKET);
		// ASSERT
		verify(awsS3StorageService).createStore(BUCKET);
	}

	@Test
	public void whenDeleteStore_thenDeleteMethodCallled() {
		// ACT
		awsS3StorageEngine.deleteStore(BUCKET);
		// ASSERT
		verify(awsS3StorageService).deleteStore(BUCKET);
	}

	@Test
	public void whenListObjects_thenExpectMehtodCalled() {
		// ACT
		awsS3StorageEngine.listObjects(BUCKET);
		// ASSERT
		verify(awsS3StorageService).listObjects(BUCKET);
	}

	@Test
	public void whenGetObject_thenFileObject() throws IOException {
		// ARRANGE
		doReturn(file).when(awsS3StorageService).getObject(BUCKET, FILE_NAME);
		// ACT
		File returnFile = awsS3StorageEngine.getObject(BUCKET, FILE_NAME);
		// ASSERT
		verify(awsS3StorageService).getObject(BUCKET, FILE_NAME);
		assertEquals(file, returnFile);
	}

	@Test
	public void whenDeleteObject_thenExpectMethodCalled() throws IOException {
		// ACT
		awsS3StorageEngine.deleteObject(BUCKET, FILE_NAME);
		// ASSERT
		verify(awsS3StorageService).deleteObject(BUCKET, FILE_NAME);
	}

	@Test
	public void whenCreateObject_thenExpectSuccessStatus() throws IOException {
		// ARRANGE
		doReturn(StorageOperationStatus.SUCCESS).when(awsS3StorageService).createObject(BUCKET, file);
		// ACT
		StorageOperationStatus status = awsS3StorageEngine.createObject(BUCKET, file);
		// ASSERT
		assertEquals(StorageOperationStatus.SUCCESS, status);
	}

	@Test
	public void whenRenameTo_thenExpectMethodCalled() throws IOException {
		// ACT
		awsS3StorageEngine.renameTo(BUCKET, "fromName", "toName");
		// ASSERT
		verify(awsS3StorageService).renameTo(BUCKET, "fromName", "toName");
	}

	@Test
	public void whenGetObjectMetaData_thenExpectMethodCalled() throws IOException {
		// ACT
		awsS3StorageEngine.getObjectMetaData(BUCKET, FILE_NAME);
		// ASSERT
		verify(awsS3StorageService).getObjectMetaData(BUCKET, FILE_NAME);
	}

	@Test
	public void whenUploadDirectory_thenExpectMethodCalled() throws Exception {
		// ARRANGE
		// ACT
		awsS3StorageEngine.uploadDirectory(BUCKET, "my", file);
		// ASSERT
		verify(awsS3StorageService).uploadDirectory(BUCKET, "my", file);

	}

	@Test
	public void whenDownloadDirectory_thenExpectMethodCalled() throws Exception {
		// ACT
		awsS3StorageEngine.downloadDirectory(BUCKET, "my", "local");
		// ASSERT
		verify(awsS3StorageService).downloadDirectory(BUCKET, "my", "local");
	}
}
