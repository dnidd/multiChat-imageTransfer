import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class used in separate threads for each connected client.
 * implements Runnable to be used in thread.
 * Communicator to server via socket.
 * Takes incoming images from server.
 * Forward incoming images to Chat.
 * ChatSocketHandler is listening to the server while Chat can read from keyboard.
 */
public class ChatSocketHandler implements Runnable {

    private Chat chat;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;


    /**
     * Constructor.
     * Create out stream.
     * @param chat = chat
     * @param socket = connected socket
     * @throws IOException propagates to startChat in Chat
     */
    public ChatSocketHandler(Chat chat, Socket socket) throws IOException {
        this.chat = chat;
        this.socket = socket;
        createOutputStream();
    }


    /**
     * Continually reading from server.
     * Image fetched from Chat.
     */
    @Override
    public void run() {
        try {
            while (true) {
                in = new ObjectInputStream(socket.getInputStream());
                Storage storage = (Storage) in.readObject();

                if (storage == null) {
                    break;
                }
                chat.getImageBytesFromChatHandler(storage);

            }
        } catch (IOException e) {
            System.err.println("Connection down.");
        } catch (ClassNotFoundException e) {
            System.err.println("Class Not Found");
        }
    }


    /**
     * Send image data to server via socket (out).
     * @param storage = image object
     */
    public void sendImageData(Storage storage) throws IOException {
        out.writeObject(storage);
        out.flush();
    }


    /**
     * Create object streams to server via socket.
     * out: to server
     * in: from server
     * @throws IOException propagates to constructor.
     */
    private void createOutputStream() throws IOException {
        // write object to socket
        out = new ObjectOutputStream(socket.getOutputStream());
    }

}
