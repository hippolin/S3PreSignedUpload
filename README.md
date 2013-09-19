#利用Pre-Signed URL透過cURL上傳檔案至S3
因為遇到AWS的網友有pre-singed upload的問題，在解決問題後，把方法分享出來。

## 1.請在S3Sample.java的目錄裡加上AwsCredentials.properties內容如下：
```properties
accessKey=<your access key>
secretKey=<your secret key>    
```

## 2.修改S3Sample.java
```java
private static String bucketName = "<your bucket name>";
private static String objectKey = "<your filename on s3>";
```

## 3.執行後會有一個pre-signed URL：
```code
Generating pre-signed URL.
Pre-Signed URL = https://hippo.test.s3-ap-northeast-1.amazonaws.com/20130916.zip?Expires=1379587357&AWSAccessKeyId=AKIAJZHVMBQ5SEZPXMDQ&Signature=piEWbsPr2a6Tbu9TeINm1/gIt60%3D
```

## 4.用cURL上傳檔案
```sh
curl --insecure -X PUT --data-binary @<要上傳的filename> "<Pre-Signed URL>"
```
因為URL中有"&",shell會把它當成background script而拆成好幾條指令去跑。
所以在pre-signed URL的前後一定要加上『 " 』雙引號。

## Reference
* Upload an Object Using Pre-Signed URL - AWS SDK for Java
http://docs.aws.amazon.com/AmazonS3/latest/dev/PresignedUrlUploadObjectDotNetSDK.html