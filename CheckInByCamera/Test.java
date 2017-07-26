import java.util.Date;

/**
 * Created by kienmaingoc on 7/9/17.
 */
public class Test {

    public static void main(String[] args) {
        Camera cam = new Camera();
        Thread thread = new Thread(cam);
        thread.start();
//        new Capture();
        new UI();

    }

}
