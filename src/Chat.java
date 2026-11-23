import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Connects to server via Socket.
 * Create communication Thread.
 * Take user input - send to Handler.
 * Take image coming from server.
 * Chat can read from the keyboard while ChatSocketHandler is listening to the server.
 */
public class Chat {

    private Socket socket;
    private ChatSocketHandler chatSocketHandler;

    /**
     * Constructor.
     * @param host = server address/host.
     * @param port = port.
     */
    public Chat(String host, int port) throws IOException {
        // Connect socket to server
        socket = new Socket(host, port);
        System.out.println("Connected to " + host + ":" + port);
    }


    /**
     * Takes image name from user.
     * @throws IOException propagates to main in Client
     */
    public void startImageTransfer() throws IOException {

        chatSocketHandler = new ChatSocketHandler(this, socket);
        // Chat can read from the keyboard while
        // ChatSocketHandler is listening to the server.
        Thread thread = new Thread(chatSocketHandler);
        thread.start();

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("Image name including .jpg, .png etc: ('exit' to quit): " );
            String imageName = scanner.nextLine();

            if (imageName.equals("exit")) {
                socket.close();
                break;
            }

            // Find image in working directory.
            // Convert to byte[] and create Storage object.
            byte[] imageBytes = Files.readAllBytes(Paths.get(imageName));
            Storage storage = new Storage(imageBytes, imageName);

            chatSocketHandler.sendImageData(storage);
        }
    }


    /**
     * Storage fetched from server (in chatSocketHandler).
     * Image converted from bytes.
     * Image placed in working directory.
     * @param storage = image object
     */
    public void getImageBytesFromChatHandler(Storage storage) throws IOException {
        byte[] imageBytes = storage.getData();
        String imageName = storage.getId();

        // Save image to working directory
        Files.write(Paths.get(imageName), imageBytes);

        System.out.println("saved: " + Paths.get(imageName).toAbsolutePath());
    }


}



