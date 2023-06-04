import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private PrintWriter out;

    public void start(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            new Thread(new MessageReceiver()).start();
            sendMessages();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessages() {
        Scanner scanner = new Scanner(System.in);
        String message;
        while (true) {
            System.out.print("Enter a message (or 'quit' to exit): ");
            message = scanner.nextLine();
            out.println(message);
            if (message.equalsIgnoreCase("quit")) {
                break;
            }
        }
        scanner.close();
    }

    private class MessageReceiver implements Runnable {
        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received from server: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start("localhost", 9999);
    }
}
