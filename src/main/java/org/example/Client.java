package org.example;


import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private String username;
    private MessageListener messageListener;

    public Client(String host, String port, String username) throws IOException {
        this.socket = new Socket(host, Integer.parseInt(port));
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.username = username;

        sendMessage(username); //sending the username to the server

        listenForMessages();
    }

    public void sendMessage(String message) {
        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        } catch (IOException e) {
            closeEverything();
        }
    }

    private void listenForMessages() {

        //connecting socket
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    String message = dataInputStream.readUTF();
                    if (messageListener != null) {
                        messageListener.onMessageReceived(message);
                    }
                } catch (IOException e) {
                    closeEverything();
                    break;
                }
            }
        }).start();
    }

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    private void closeEverything() {
        try {
            if (dataInputStream != null) dataInputStream.close();
            if (dataOutputStream != null) dataOutputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface MessageListener {
        void onMessageReceived(String message);
    }


}