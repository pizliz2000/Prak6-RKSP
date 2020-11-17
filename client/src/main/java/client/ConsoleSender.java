package client;

import java.io.*;
import java.net.Socket;

public class ConsoleSender {
    String host;
    int port;

    public ConsoleSender(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws IOException, InterruptedException {
        System.out.println("Клиент стартанул");
        // запускаем подключение сокета по известным координатам и нициализируем приём сообщений с консоли клиента
        try (Socket socket = new Socket(host, port)) {

            // консоль
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            // сокет для записи данных на сервер
            DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            // сокет для чтения ответа с сервера
            DataInputStream ois = new DataInputStream(socket.getInputStream());


            System.out.println("Клиент подключился к сокету");
            System.out.println("Введите данные...");
            while (!socket.isOutputShutdown()) {

                // ждём консоли клиента на предмет появления в ней данных
                if (br.ready()) {

                    // данные появились - работаем

                    String clientCommand = br.readLine();

                    // пишем данные с консоли в канал сокета для сервера
                    oos.writeUTF(clientCommand);
                    oos.flush();
                    System.out.println("Отправили на сервер: " + clientCommand);

                    // проверяем условие выхода из соединения
                    if (clientCommand.equalsIgnoreCase("quit")) {

                        // если условие выхода достигнуто разъединяемся
                        System.out.println("Пришла команда завершения");

                        // смотрим что нам ответил сервер на последок перед закрытием ресурсов
                        String in = ois.readUTF();
                        System.out.println(in);


                        // после предварительных приготовлений выходим из цикла записи чтения
                        break;
                    }

                    // если условие разъединения не достигнуто продолжаем работу
                    System.out.println("Данные отправлены - ждем ответа с сервера...");

                    // если успел забираем ответ из канала сервера в сокете и сохраняем её в ois переменную,  печатаем на свою клиентскую консоль
                    System.out.print("читаем... ");
                    String in = ois.readUTF();
                    double countString = 0;
                    for (int y = 0; y < in.length(); y++) {
                        if (Character.isLowerCase(in.charAt(y)) || Character.isUpperCase(in.charAt(y))) {
                            countString = countString + 1;
                        }
                    }
                    String percentString = String.valueOf((countString / in.length()) * 100);
                    System.out.println(in);
                    System.out.println("Percent in string: " + percentString);
                    System.out.println("Введите данные...");
                }
            }
            // на выходе из цикла общения закрываем свои ресурсы
            System.out.println("Закрываем соединения и сокеты");
        }
    }
}