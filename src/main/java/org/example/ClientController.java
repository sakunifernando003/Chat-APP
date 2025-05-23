package org.example;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientController {

    private Client client;
    private String username;
    private Socket socket;

    @FXML
    public TextArea chatArea;

    @FXML
    private Button joinBtn;

    @FXML
   public TextField msg;

    @FXML
    private Button sendBtn;

    @FXML
    public TextField userName;
   

    @FXML
    void joinBtnOnAction(ActionEvent event) {
        String userName = this.msg.getText();
        if(userName.isEmpty()) {
            appendMessage("System","enter msg");
            return;
        }
        try {
            client = new Client("localhost","1234",userName);
            this.username = userName;


        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    private void appendMessage(String message) {
        chatArea.appendText(message + "\n");
    }

    private void appendMessage(String sender, String message) {
        chatArea.appendText(sender + ": " + message + "\n");


    }

    @FXML
    void sentBtnOnAction(ActionEvent event) throws IOException {

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(msg.getText());
        dataOutputStream.flush();


    }

}

