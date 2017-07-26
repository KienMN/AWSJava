import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;

import java.util.List;

/**
 * Created by kienmaingoc on 6/26/17.
 */
public class DetectFaces {
    public static List<FaceDetail> detectFaces(String accessKeyID, String secretAccessKey, String region, String bucketName, String key) {
        //create credentials
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyID, secretAccessKey);

        //create rekognition client
        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        //make request
        DetectFacesRequest detectFacesRequest = new DetectFacesRequest()
                .withImage(new Image()
                    .withS3Object(new S3Object()
                        .withBucket(bucketName)
                        .withName(key)))
                .withAttributes(Attribute.ALL);

        //get result
        DetectFacesResult result = rekognitionClient.detectFaces(detectFacesRequest);

        //return face detail
        List<FaceDetail> faceDetails = result.getFaceDetails();

        return faceDetails;

    }
}
