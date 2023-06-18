package com.pompast.state.client;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;

public class EntityClient {
    protected Socket client;
    protected static InputStream inputStream;
    protected static OutputStream outputStream;
    private final String HOST;
    private final int PORT;

    public EntityClient(String host, int port) {
        HOST = host;
        PORT = port;
    }

    public void open() throws IOException {
        client = new Socket(HOST, PORT);

        inputStream = client.getInputStream();
        outputStream = client.getOutputStream();
    }

    /**
     * attempt = количество попыток
     * 1 попытка = 1000 мили
     */
    public void waitServer(int attempt) throws InterruptedException {

        for (int i = 0; i != attempt; i++) {

            try {
                open();
                return;
            } catch (IOException e) {

                if (i == attempt - 1)
                    throw new RuntimeException(e);
                Thread.sleep(500);
            }
        }

    }

    public void sendMessage(@NotNull String message) throws IOException, InterruptedException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

        writer.write(message);
        writer.newLine();
        writer.flush();
        Thread.sleep(100);
    }

    public String read() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return reader.readLine();
    }


    public void close(boolean open) throws IOException {
        try {
            if (client != null && !client.isClosed())
                client.close();

            if (inputStream != null)
                inputStream.close();

            if (outputStream != null)
                outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (open)
            open();

    }

}
