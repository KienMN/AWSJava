import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by kienmaingoc on 6/26/17.
 */
public class GetFile {
    public static BufferedImage getFile(String accessKeyID, String secretAccessKey, String region, String bucketName, String key) {
        //create credentials
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyID, secretAccessKey);

        //create amazon s3 client
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();


        //make request
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName,key);

        //get object
        S3Object s3Object = s3Client.getObject(bucketName, key);

        //read image
        try {
            BufferedImage image = ImageIO.read(ImageIO.createImageInputStream(s3Object.getObjectContent()));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
