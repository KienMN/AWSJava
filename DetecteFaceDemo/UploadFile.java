import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.File;

/**
 * Created by kienmaingoc on 6/26/17.
 */
public class UploadFile {
    public static boolean uploadFile(String accessKeyID, String secretAccessKey, String region, String bucketName, String key, String filePath) {

        //create file
        File file = new File(filePath);

        //create credentials
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyID, secretAccessKey);

        //create AmazonS3 Client
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        //put object to Amazon S3
        try {
            s3Client.putObject(bucketName, key, file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
