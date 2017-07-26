import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.util.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by kienmaingoc on 7/9/17.
 */
public class RekognitionProcess {
    public static final String COLLECTION_ID = "facesID";
    public static final String S3_BUCKET = "kienimages";
    public static final String ACCESS_KEY_ID = "ACCESS_KEY_ID";
    public static final String SECRET_ACCESS_KEY = "SECRET_ACCESS_KEY";
    private static final Float THRESHOLD = 70F;
    public static BasicAWSCredentials CREDENTIALS = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
    public static AmazonRekognition AWS_REKOGNITION = AmazonRekognitionClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(CREDENTIALS))
            .withRegion(Regions.EU_WEST_1)
            .build();

    public static CreateCollectionResult createCollection() {
        CreateCollectionRequest request = new CreateCollectionRequest().withCollectionId(COLLECTION_ID);
        return AWS_REKOGNITION.createCollection(request);
    }

    public static IndexFacesResult indexFaces(Image image, String externalImageId) {
        IndexFacesRequest request = new IndexFacesRequest()
                .withImage(image)
                .withCollectionId(COLLECTION_ID)
                .withExternalImageId(externalImageId)
                .withDetectionAttributes("ALL");
        return AWS_REKOGNITION.indexFaces(request);
    }

    public static SearchFacesByImageResult searchFaces(Image image) {
        SearchFacesByImageRequest request = new SearchFacesByImageRequest()
                .withCollectionId(COLLECTION_ID)
                .withFaceMatchThreshold(THRESHOLD)
                .withMaxFaces(1)
                .withImage(image);
        return AWS_REKOGNITION.searchFacesByImage(request);
    }

    public static ListFacesResult listFaces() {
        ListFacesRequest request = new ListFacesRequest()
                .withCollectionId(COLLECTION_ID);
        return AWS_REKOGNITION.listFaces(request);

    }

    public static void main(String[] args) {
//        System.out.println("Creating collection");
//        CreateCollectionResult createCollectionResult = createCollection();
//        System.out.println("Result:");
//        System.out.println(createCollectionResult.toString());

//      Insert kien's face to collection
//        try {
//            InputStream inputStream = new FileInputStream("/Users/kienmaingoc/Desktop/KienMN.jpg");
//            ByteBuffer byteBuffer = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
//            Image image = new Image().withBytes(byteBuffer);
//            IndexFacesResult result = indexFaces(image, "kienmn");
//            //FaceId: 032763c9-2208-5869-9470-72f8a51671f2, ImageId: 776ae58d-8ef4-566d-89f1-c9df08c86c4e, ExternalImageId: kienmn
//            System.out.println("Result:");
//            System.out.println(result.toString());
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        System.out.println("Face collection:");
//        System.out.println(listFaces().getNextToken());

        //add new face
//        try {
//            InputStream inputStream = new FileInputStream("/Users/kienmaingoc/Desktop/source1.jpg");
//            ByteBuffer byteBuffer = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
//            Image image = new Image().withBytes(byteBuffer);
//            indexFaces(image, "");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //list face collection
//        ListFacesResult result = listFaces();
//        List<Face> faceDetails = result.getFaces();
//        for (Face face: faceDetails) {
//            System.out.println(face.toString());
//        }
        long startTime = System.currentTimeMillis();

        try {
//            InputStream inputStream = new FileInputStream("image0.jpg");
//            ByteBuffer byteBuffer = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
//            SearchFacesByImageResult result1 = searchFaces(new Image().withBytes(byteBuffer));  //Faster

            //Slower
//            SearchFacesByImageRequest request = new SearchFacesByImageRequest()
//                    .withImage(new Image().withBytes(byteBuffer))
//                    .withFaceMatchThreshold(THRESHOLD)
//                    .withCollectionId(COLLECTION_ID)
//                    .withMaxFaces(1);
//            SearchFacesByImageResult result1 = AWS_REKOGNITION.searchFacesByImage(request);

            ListFacesResult result = listFaces();
            System.out.println(result.toString());
            System.out.println(AWS_REKOGNITION.toString());;


        } catch (Exception e) {
            e.printStackTrace();
        }

        long stopTime = System.currentTimeMillis();
        System.out.println(stopTime - startTime);

        ListCollectionsRequest request = new ListCollectionsRequest();
        ListCollectionsResult result = AWS_REKOGNITION.listCollections(request);
        System.out.println(result.toString());


    }
}
