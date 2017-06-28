package com.oracle.miecs.botsdemo;

/**
 * Created by weizeng on 6/28/17.
 */

import android.util.Log;

//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.drafts.Draft_10;
//import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;
import okio.ByteString;

import static okhttp3.ws.WebSocket.BINARY;
import static okhttp3.ws.WebSocket.TEXT;

public final class BotsWebSocketListener implements WebSocketListener {
    private final ExecutorService writeExecutor = Executors.newSingleThreadExecutor();
    private static final String TAG = "BotsWebSocketListener";

    public void run() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0,  TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url("ws://46a3ae4b.ngrok.io/chat/ws?user=Weishi")
                .build();
        WebSocketCall.create(client, request).enqueue(this);


        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();
    }

    @Override
    public void onOpen(final WebSocket webSocket, Response response) {
//        writeExecutor.execute(new Runnable() {
//            @Override public void run() {
                try {
                    Log.d(TAG, "Writing Messages!");

                    webSocket.sendMessage(RequestBody.create(TEXT, "{\n" +
                            "    \"to\": {\n" +
                            "        \"type\": \"bot\",\n" +
                            "        \"id\": \"367117DD-B099-4408-B07D-C334BBA2E6A4\"\n" +
                            "    },\n" +
                            "    \"text\": \"Hello\"\n" +
                            "}"));

//                    //webSocket.close(1000, "Goodbye, World!");
                } catch (IOException e) {
                    Log.e(TAG, "Unable to send messages: " + e.getMessage());
                }
//            }
//        });
    }

    @Override
    public void onMessage(ResponseBody message) throws IOException {
        if (message.contentType() == TEXT) {
            Log.d(TAG, "MESSAGE: " + message.string());

        } else {
            Log.d(TAG, "MESSAGE: " + message.source().readByteString().hex());
        }
        message.close();
    }

    @Override
    public void onPong(Buffer payload) {
        Log.d(TAG, "PONG: " + payload.readUtf8());

    }

    @Override
    public void onClose(int code, String reason) {
        Log.d(TAG, "CLOSE: " + code + " " + reason);

        writeExecutor.shutdown();
    }

    @Override
    public void onFailure(IOException e, Response response) {
        e.printStackTrace();
        writeExecutor.shutdown();
    }


    public static void main(String... args) {
    }

}