package com.pompast.state.client;

import android.os.Environment;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class ClientSendFile extends  EntityClient {
    public ClientSendFile(String host, int port) {
        super(host, port);
    }
    public static final int SIZE_IN_BYTES = 1024;
    private static final String DOWNLOAD_FILE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "11";

    public void send(@NotNull String path) {
        assert new File(path).exists();
        send(new File(path));
    }

    public void send(@NotNull File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            sendMessage(file.getName());

            BufferedOutputStream bos = new BufferedOutputStream(outputStream);

            byte[] buffer = new byte[SIZE_IN_BYTES];
            int bytesRead;
            while ( (bytesRead = fis.read(buffer, 0, SIZE_IN_BYTES)) != -1 ) {
                bos.write(buffer, 0, bytesRead);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void get() throws IOException {
        File file = new File(DOWNLOAD_FILE + File.separator + read());

        try (FileOutputStream fos = new FileOutputStream(file)) {

            BufferedInputStream bis = new BufferedInputStream(inputStream);

            byte[] buffer = new byte[SIZE_IN_BYTES];
            int bytesRead;
            while ( (bytesRead = bis.read(buffer, 0, SIZE_IN_BYTES)) != -1 ) {
                fos.write(buffer, 0, bytesRead);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
