import com.amazonaws.auth.BasicAWSCredentials;

/**
 * Created by kienmaingoc on 6/28/17.
 */
public class Main {

    public static String ACCESS_KEY_ID = "AKIAITNPIQ3TMODHVGGA";
    public static String SECRET_ACCESS_KEY = "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";
    public static String REGION = "eu-west-1";
    public static String BUCKET_NAME = "kienimages";
    public static BasicAWSCredentials CREDENTIALS = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);

    public static void main(String[] args) {
        new FileChooserUI();
    }

}
