package client;

public class ClienStart {
    public static void main(String[] args) {
        ConsoleSender consoleSender = new ConsoleSender("localhost", 12345);

        try {
            consoleSender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}