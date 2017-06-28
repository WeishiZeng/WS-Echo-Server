package com.oracle.miecs.botsdemo;

/**
 * Created by weizeng on 6/28/17.
 */

public class ChatMessage {
    public boolean right;
    public String message;

    public ChatMessage(boolean right, String message) {
        super();
        this.right = right;
        this.message = message;
    }
}