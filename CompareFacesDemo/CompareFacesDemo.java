import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesisanalytics.model.Input;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by kienmaingoc on 6/27/17.
 */
public class CompareFacesDemo {
    public static CompareFacesResult compareFaces(String sourcePath, String targetPath) {

        if (sourcePath != "" && targetPath != "") {

            float similarityThreshold = 70F;
            try {
                BasicAWSCredentials credentials = new BasicAWSCredentials(Main.ACCESS_KEY_ID, Main.SECRET_ACCESS_KEY);

                AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withRegion(Main.REGION)
                        .build();

                InputStream sourceStream = new FileInputStream(new File(sourcePath));
                ByteBuffer sourceByteBuffer = ByteBuffer.wrap(IOUtils.toByteArray(sourceStream));
                Image sourceImage = new Image().withBytes(sourceByteBuffer);

                InputStream targetStream = new FileInputStream(new File(targetPath));
                ByteBuffer targetByteBuffer = ByteBuffer.wrap(IOUtils.toByteArray(targetStream));
                Image targetImage = new Image().withBytes(targetByteBuffer);

                CompareFacesRequest request = new CompareFacesRequest()
                        .withSimilarityThreshold(similarityThreshold)
                        .withSourceImage(sourceImage)
                        .withTargetImage(targetImage);

                CompareFacesResult compareFacesResult = rekognitionClient.compareFaces(request);

                return compareFacesResult;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
