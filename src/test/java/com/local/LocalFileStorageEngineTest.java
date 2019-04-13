package com.local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.service.IStorageService;
import com.util.ObjectMetaData;
import com.util.StorageOperationStatus;

@RunWith(MockitoJUnitRunner.class)
public class LocalFileStorageEngineTest {

	private static final String FILE_DOES_NOT_EXISTS = "fileDoesNotExists";
	private static final String FILE_ALREADY_EXISTS = "fileAlreadyExists";
	private static final String TO_NAME = "toName";
	private static final String FORM_NAME = "formName";
	private static final String ANY_FILE_NAME = "anyFileName";
	private static final String WRONG_STORE_NAME = "wrongStoreName";
	private static final String DEFAULT_STORE = "defaultStore";
	private static final String STORE_NAME = "storeName";

	@InjectMocks
	LocalFileStorageEngine localFileStorageEngine;
	@Mock
	IStorageService localFileStorageService;
	@Mock
	File file;

	@Before
	public void setUpForTest() throws IOException {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = Test.None.class)
	public void whenCreateNewStore_thenExpecteNoException() throws IOException {
		// ACT
		localFileStorageEngine.createStore(STORE_NAME);
		// ASSERT
		verify(localFileStorageService).createStore(STORE_NAME);
	}

	@Test
	public void whenCreateObjectWithDefaultStore_thenExpectSuccessStatus() throws IOException {
		// ARRANGE
		doReturn(StorageOperationStatus.SUCCESS).when(localFileStorageService).createObject(DEFAULT_STORE, file);
		// ACT
		StorageOperationStatus status = localFileStorageEngine.createObject(file);
		// ASSERT
		assertEquals(StorageOperationStatus.SUCCESS, status);
	}

	@Test
	public void whenCreateObjectWithStore_thenExpectSuccessStatus() throws IOException {
		// ARRANGE
		doReturn(StorageOperationStatus.SUCCESS).when(localFileStorageService).createObject(STORE_NAME, file);
		// ACT
		StorageOperationStatus status = localFileStorageEngine.createObject(STORE_NAME, file);
		// ASSERT
		assertEquals(StorageOperationStatus.SUCCESS, status);
	}

	@Test(expected = IllegalArgumentException.class)
	public void whenCreateObjectWithWrongStore_thenExpectException() throws IOException {
		// ARRANGE
		doThrow(IllegalArgumentException.class).when(localFileStorageService).createObject(WRONG_STORE_NAME, file);
		// ACT
		localFileStorageEngine.createObject(WRONG_STORE_NAME, file);
	}

	@Test
	public void whenGetObjectWithDefaultStore_thenExpectObject() throws IOException {
		// ARRANGE
		doReturn(file).when(localFileStorageService).getObject(DEFAULT_STORE, ANY_FILE_NAME);
		// ACT
		File file = localFileStorageEngine.getObject(ANY_FILE_NAME);
		// ASSERT
		assertNotNull(file);
	}

	@Test
	public void whenGetObjectWithStore_thenExpectObject() throws IOException {
		// ARRANGE
		doReturn(file).when(localFileStorageService).getObject(STORE_NAME, ANY_FILE_NAME);
		// ACT
		File file = localFileStorageEngine.getObject(STORE_NAME, ANY_FILE_NAME);
		// ASSERT
		assertNotNull(file);
	}

	@Test(expected = IllegalArgumentException.class)
	public void whenGetObjectWithWrongStore_thenExpectException() throws IOException {
		// ARRANGE
		doThrow(IllegalArgumentException.class).when(localFileStorageService).getObject(WRONG_STORE_NAME,
				ANY_FILE_NAME);
		// ACT
		localFileStorageEngine.getObject(WRONG_STORE_NAME, ANY_FILE_NAME);
	}

	@Test
	public void whenGetObjectMetaDataWithDefaultStore_thenExpectObject() throws IOException {
		// ARRANGE
		ObjectMetaData metaMock = mock(ObjectMetaData.class);
		doReturn(metaMock).when(localFileStorageService).getObjectMetaData(DEFAULT_STORE, ANY_FILE_NAME);
		// ACT
		ObjectMetaData meta = localFileStorageEngine.getObjectMetaData(ANY_FILE_NAME);
		// ASSERT
		assertNotNull(meta);
	}

	@Test
	public void whenGetObjectMetaDataWithStore_thenExpectObject() throws IOException {
		// ARRANGE
		ObjectMetaData metaMock = mock(ObjectMetaData.class);
		doReturn(metaMock).when(localFileStorageService).getObjectMetaData(STORE_NAME, ANY_FILE_NAME);
		// ACT
		ObjectMetaData meta = localFileStorageEngine.getObjectMetaData(STORE_NAME, ANY_FILE_NAME);
		// ASSERT
		assertNotNull(meta);
	}

	@Test(expected = IllegalArgumentException.class)
	public void whenGetObjectMetaDataWithWithWrongStore_thenExpectException() throws IOException {
		// ARRANGE
		doThrow(IllegalArgumentException.class).when(localFileStorageService).getObjectMetaData(WRONG_STORE_NAME,
				ANY_FILE_NAME);
		// ACT
		localFileStorageEngine.getObjectMetaData(WRONG_STORE_NAME, ANY_FILE_NAME);
	}

	@Test
	public void whenRenameToWithDefaultStore_thenExpectMethodCalled() throws IOException {
		// ACT
		localFileStorageEngine.renameTo(FORM_NAME, TO_NAME);
		// ASSERT
		verify(localFileStorageService).renameTo(DEFAULT_STORE, FORM_NAME, TO_NAME);
	}

	@Test
	public void whenRenameToWithStoreName_thenExpectMethodCalled() throws IOException {
		// ACT
		localFileStorageEngine.renameTo(STORE_NAME, FORM_NAME, TO_NAME);
		// ASSERT
		verify(localFileStorageService).renameTo(STORE_NAME, FORM_NAME, TO_NAME);
	}

	@Test(expected = FileNotFoundException.class)
	public void whenRenameToWithStore_thenExpectFileNotFoundException() throws IOException {
		// ARRANGE
		doThrow(FileNotFoundException.class).when(localFileStorageService).renameTo(STORE_NAME, FILE_DOES_NOT_EXISTS,
				ANY_FILE_NAME);
		// ACT
		localFileStorageEngine.renameTo(STORE_NAME, FILE_DOES_NOT_EXISTS, ANY_FILE_NAME);
	}

	@Test(expected = FileAlreadyExistsException.class)
	public void whenRenameToWithStore_thenExpectFileExistException() throws IOException {
		// ARRANGE
		doThrow(FileAlreadyExistsException.class).when(localFileStorageService).renameTo(STORE_NAME, ANY_FILE_NAME,
				FILE_ALREADY_EXISTS);
		// ACT
		localFileStorageEngine.renameTo(STORE_NAME, ANY_FILE_NAME, FILE_ALREADY_EXISTS);
	}

	@Test(expected = FileNotFoundException.class)
	public void whenRenameToWithDefaultStore_thenExpectFileNotFoundException() throws IOException {
		// ARRANGE
		doThrow(FileNotFoundException.class).when(localFileStorageService).renameTo(DEFAULT_STORE, FILE_DOES_NOT_EXISTS,
				ANY_FILE_NAME);
		// ACT
		localFileStorageEngine.renameTo(FILE_DOES_NOT_EXISTS, ANY_FILE_NAME);
	}

	@Test(expected = FileAlreadyExistsException.class)
	public void whenRenameToWithDefaultStore_thenExpectFileExistException() throws IOException {
		// ARRANGE
		doThrow(FileAlreadyExistsException.class).when(localFileStorageService).renameTo(DEFAULT_STORE, ANY_FILE_NAME,
				FILE_ALREADY_EXISTS);
		// ACT
		localFileStorageEngine.renameTo(ANY_FILE_NAME, FILE_ALREADY_EXISTS);
	}

	@Test
	public void whenDeletedObjectWithDefaultStore_thenExpectMethodCalled() throws IOException {
		// ACT
		localFileStorageEngine.deleteObject(ANY_FILE_NAME);
		// ASSERT
		verify(localFileStorageService).deleteObject(DEFAULT_STORE, ANY_FILE_NAME);
	}

	@Test
	public void whenDeletedObjectWithStore_thenExpectMethodCalled() throws IOException {
		// ACT
		localFileStorageEngine.deleteObject(STORE_NAME, ANY_FILE_NAME);
		// ASSERT
		verify(localFileStorageService).deleteObject(STORE_NAME, ANY_FILE_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void whenDeleteObjectWithWithWrongStore_thenExpectException() throws IOException {
		// ARRANGE
		doThrow(IllegalArgumentException.class).when(localFileStorageService).deleteObject(WRONG_STORE_NAME,
				ANY_FILE_NAME);
		// ACT
		localFileStorageEngine.deleteObject(WRONG_STORE_NAME, ANY_FILE_NAME);
	}

	@Test(expected = FileNotFoundException.class)
	public void whenDeleteObjectWithDefaultStore_thenExpectFileNotFoundException() throws IOException {
		// ARRANGE
		doThrow(FileNotFoundException.class).when(localFileStorageService).deleteObject(DEFAULT_STORE, ANY_FILE_NAME);
		// ACT
		localFileStorageEngine.deleteObject(ANY_FILE_NAME);
	}

	@Test(expected = FileNotFoundException.class)
	public void whenDeleteObjectWithStore_thenExpectFileNotFoundExceptiont() throws IOException {
		// ARRANGE
		doThrow(FileNotFoundException.class).when(localFileStorageService).deleteObject(STORE_NAME, ANY_FILE_NAME);
		// ACT
		localFileStorageEngine.deleteObject(STORE_NAME, ANY_FILE_NAME);
	}

	@Test
	public void whenListObjects_thenExpectNonEmptyList() throws IOException {
		// ARRANGE
		doReturn(Arrays.asList("file1", "file2")).when(localFileStorageService).listObjects(STORE_NAME);
		// ACT
		List<String> objects = localFileStorageEngine.listObjects(STORE_NAME);
		// ASSERT
		assertTrue(!objects.isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void whenListObjectsWithWithWrongStore_thenExpectException() throws IOException {
		// ARRANGE
		doThrow(IllegalArgumentException.class).when(localFileStorageService).listObjects(WRONG_STORE_NAME);
		// ACT
		localFileStorageEngine.listObjects(WRONG_STORE_NAME);
	}

	@Test
	public void whenUploadDirectoryWithStore_thenExpectMethodCalled() throws Exception {
		// ACT
		localFileStorageEngine.uploadDirectory(STORE_NAME, "my", file);
		// ASSERT
		verify(localFileStorageService).uploadDirectory(STORE_NAME, "my", file);
	}

	@Test
	public void whenUploadDirectoryWithDefaultStore_thenExpectMethodCalled() throws Exception {
		// ACT
		localFileStorageEngine.uploadDirectory("my", file);
		// ASSERT
		verify(localFileStorageService).uploadDirectory(DEFAULT_STORE, "my", file);
	}

	@Test
	public void whenDownloadDirectoryWithStore_thenExpectMethodCalled() throws Exception {
		// ACT
		localFileStorageEngine.downloadDirectory(STORE_NAME, "my", "localDirectoryPath");
		// ASSERT
		verify(localFileStorageService).downloadDirectory(STORE_NAME, "my", "localDirectoryPath");
	}

	@Test
	public void whenDownloadDirectoryWithDefaultStore_thenExpectMethodCalled() throws Exception {
		// ACT
		localFileStorageEngine.downloadDirectory("my", "localDirectoryPath");
		// ASSERT
		verify(localFileStorageService).downloadDirectory(DEFAULT_STORE, "my", "localDirectoryPath");
	}
}
