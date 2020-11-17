package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener {
    int port;
    DataOutputStream out;

    public Listener(int port) {
        this.port = port;
    }

    public void start() throws IOException, InterruptedException {
        System.out.print("Сервер стартанул. Ждем соединения...");
        try (ServerSocket server = new ServerSocket(port)) {
            // становимся в ожидание подключения к сокету под именем - "client" на серверной стороне
            Socket client = server.accept();

            // после хэндшейкинга сервер ассоциирует подключающегося клиента с этим сокетом-соединением
            System.out.print("Соединение установлено");

            // инициируем каналы для  общения в сокете, для сервера
            // канал записи в сокет
            out = new DataOutputStream(client.getOutputStream());
            System.out.println("канал записи создан");

            // канал чтения из сокета
            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println("канал чтения создан");

            // начинаем диалог с подключенным клиентом в цикле, пока сокет не закрыт
            while (!client.isClosed()) {

                System.out.println("Сервер готов читать из сокета");

                // сервер ждёт в канале чтения (inputstream) получения данных клиента
                String entry = in.readUTF();

                // после получения данных считывает их
                System.out.println("Клиент прислал: " + entry);

                // если клиент прислал кодовое слово quit - сервер выходит из цикла и закрывает всесокеты и соединения
                if (entry.equalsIgnoreCase("quit")) {
                    //отвечаем эхом
                    sendEcho(entry);
                    // засыпаем на 3 сек
                    Thread.sleep(3000);
                    break;
                }

                // если условие окончания работы не верно - продолжаем работу - отправляем эхо-ответ  обратно клиенту
                sendEcho(entry);
            }

            // если условие выхода - верно выключаем соединения
            System.out.println("Клиент отключился");
            System.out.println("Закрываем соединения и сокеты");

            // закрываем сначала каналы сокета !
            in.close();
            out.close();

            // потом закрываем сам сокет общения на стороне сервера!
            client.close();

            // потом закрываем сокет сервера который создаёт сокеты общения
            // хотя при многопоточном применении его закрывать не нужно
            // для возможности поставить этот серверный сокет обратно в ожидание нового подключения

            System.out.println("Пока!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendEcho(String text) throws IOException {
        System.out.println("Отправлям эхо клиенту: "+text);
        out.writeUTF(text);
        // освобождаем буфер сетевых сообщений (по умолчанию сообщение не сразу отправляется в сеть, а сначала накапливается в специальном буфере сообщений, размер которого определяется конкретными настройками в системе, а метод  - flush() отправляет сообщение не дожидаясь наполнения буфера согласно настройкам системы
        out.flush();
    }
}