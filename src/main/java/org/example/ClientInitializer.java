package org.example;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientInitializer implements Runnable {
    public static ArrayList<ClientInitializer> clientHandlers = new ArrayList<ClientInitializer>();
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private String clientUsername;

    public ClientInitializer(Socket socket) {
        try {
            this.socket = socket;
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
            this.dataInputStream = new DataInputStream(socket.getInputStream());

            this.clientUsername = dataInputStream.readUTF();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " has joined !");
        } catch (IOException e) {
            closeEverything();
        }
    }


    private void broadcastMessage(String message) {
        for (ClientInitializer clientHandle : clientHandlers) {
            try {
                if (!clientHandle.clientUsername.equals(this.clientUsername)) {
                    clientHandle.dataOutputStream.writeUTF(message);
                    clientHandle.dataOutputStream.flush();
                }
            } catch (IOException e) {
                closeEverything();
            }
        }
    }

    private void closeEverything() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " BYEE!!!");
        try {
            if (dataInputStream != null) dataInputStream.close();
            if (dataOutputStream != null) dataOutputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}