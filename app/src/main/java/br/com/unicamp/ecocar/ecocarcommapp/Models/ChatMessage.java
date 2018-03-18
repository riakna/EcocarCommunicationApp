package br.com.unicamp.ecocar.ecocarcommapp.Models;

/**
 * Created by Nagaishi on 17/03/2018.
 */

public class ChatMessage {

    private String message;
    private String senderId;
    private String receiverId;

    public ChatMessage() {
    }

    public ChatMessage(String message, String senderId, String receiverId) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
