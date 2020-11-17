package server;

import java.io.IOException;

public class ServerStart {
    public static void main(String[] args){

        Listener listener = new Listener(12345);
        try {
            listener.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}