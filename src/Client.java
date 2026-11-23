import java.io.IOException;

/**
 * Entry point.
 * Default host and port.
 */
public class Client {

    private static final String DEFAULT_HOST =  "localhost";
    private static final int DEFAULT_PORT = 4848;

    public static void main(String[] args) throws IOException {

        // 0 args -> default host + port
        if (args.length == 0) {
            try {
                Chat chat = new Chat(DEFAULT_HOST, DEFAULT_PORT);
                chat.startImageTransfer();
            } catch (IOException e) {
                System.err.println("Server connection failed.");
            }
        }
        else {
            System.err.println("Usage: java -cp target/classes Client");
        }

    }

}