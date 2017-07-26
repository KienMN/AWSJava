import com.amazonaws.auth.BasicAWSCredentials;

/**
 * Created by kienmaingoc on 6/28/17.
 */
public class Main {

    public static String ACCESS_KEY_ID = "YOUR_ACCESS_KEY";
    public static String SECRET_ACCESS_KEY = "YOUR_SECRET_KEY";
    public static String REGION = "eu-west-1";
    public static String BUCKET_NAME = "BucketName";
    public static BasicAWSCredentials CREDENTIALS = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);

    public static void main(String[] args) {
        new FileChooserUI();
    }

}
