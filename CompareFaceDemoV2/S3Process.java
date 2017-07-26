import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.File;

/**
 * Created by kienmaingoc on 6/28/17.
 */
public class S3Process {
    //Create amazon S3 client
    static AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withRegion(Main.REGION)
            .withCredentials(new AWSStaticCredentialsProvider(Main.CREDENTIALS))
            .build();

    //Upload a single file
    public static boolean uploadFile(String bucketName, String key, String filePath) {
        //When bucket hasn't been created yet. Create bucket.

        //When bucket has existed
        TransferManager tm = TransferManagerBuilder.standard()
                .withS3Client(s3Client)
                .build();
        Upload upload = tm.upload(bucketName, key, new File(filePath));
        try {
            upload.waitForCompletion();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;

//        s3Client.putObject(new PutObjectRequest(bucketName, key, new File(filePath)));
//        return true;

    }

}
