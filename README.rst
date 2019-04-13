This is library for document store with basic functions like create /delete/open/rename etc. etc. documents on storage engines.
Currently it supports two types of storage engines.
1.	Local storage engine
2.	Remote storage engine
Local Storage Engine
	User can perform basic file operation in local system via LocalFileStorageEngine class.
Remote storage engine
	User can perform basic file operation on remote storage currently supported storage is AWS S3, via AwsS3StorageEngine class.

Library gives user option to choose any storage implementation, and will take care the rest of the things.
These Storage engines are exposed via IStorageEngine interface.
User can create Local Storage engine.

IStorageEngine storage = new LocalFileStorageEngine();

If any time user wanted to change this implementation to remote storage,
he/she can do it by changing the implementation class like,

IStorageEngine  storage = new AwsS3StorageEngine ();

This can be done with minimal changes to exisisting implementation. 
Just changing the instance of (implementation of) IStorageEngine

Also any time user can provide his/her own storage implementation, by implementing the interface IStorageEngine.
Let’s say user wanted to have remote storage with Azure Blob he/she can do it with implementing the required interface.

User have to add all the configuration to the config.properties file like,
local.uploads=<default local store>
remote.s3.accesskey=<aws access key>
remote.s3.secretkey=<aws access secrete>
remote.s3.storeName=<remote default store>
remote.s3.region=<aws- region name>

Additionally this library also supports uploads and downloads for directories.
user can uploads/downloads full directory to either remote or local file system.
To package the jar from source code do
mvn package with goal  as assembly:single
