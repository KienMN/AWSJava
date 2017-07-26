import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.util.IOUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by kienmaingoc on 6/28/17.
 */
public class RekognitionProcess {

    static AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
            .withRegion(Main.REGION)
            .withCredentials(new AWSStaticCredentialsProvider(Main.CREDENTIALS))
            .build();

    //compare faces between an image on computer and images on aws s3
    //source image is provided through byte buffer
    //target images on aws s3
    public static CompareFacesResult compareFace(String sourcePath, String s3TargetBucketName, String s3TargetKey) {

        float similarityThreshold = 70F;
        ByteBuffer sourceByteBuffer;
        try {
            InputStream sourceInputStream = new FileInputStream(sourcePath);
            sourceByteBuffer = ByteBuffer.wrap(IOUtils.toByteArray(sourceInputStream));
            CompareFacesRequest request = new CompareFacesRequest()
                    .withSourceImage(new Image().withBytes(sourceByteBuffer))
                    .withTargetImage(new Image()
                            .withS3Object(new S3Object()
                                .withBucket(s3TargetBucketName)
                                .withName(s3TargetKey)))
                    .withSimilarityThreshold(similarityThreshold);

            CompareFacesResult result = rekognitionClient.compareFaces(request);
            return result;

        } catch (InvalidParameterException e1) {
            System.out.println("Invalid Parameter In Rekognition Process");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
