package com.example.rinkdepth3;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Objects;

public class TCPFile
{
    public String uploadDocument(Context cont, File file, String path, String data)
    {
        try
        {
            Socket socket = new Socket("192.168.1.8", 9090);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(data);
            String response = in.readLine();
            if (Objects.equals(data, response))
            {
                socket.close();
                return "Worked!";
            }else
            {
                socket.close();
                return "Didnt Work!";
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return "Didnt work!";
    }

    public String createFile(Context cont, File file, String path, String data)
    {
        boolean newFileCreated = false;
        try
        {
            File newFile = new File(cont.getFilesDir(), file.getName());
            if (!newFile.createNewFile()) {
                newFileCreated = false;
                //appendToFile(cont, file, path, data);
            } else {
                newFileCreated = true;
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return writeToFile(cont,file, path, data);
    }

    public String writeToFile(Context cont, File file, String path, String data)
    {
//        try (FileOutputStream fos = cont.openFileOutput(file.getName(), Context.MODE_PRIVATE))
//        {
//            fos.write(data.toByteArray());
//        }catch (IOException e)
//        {
//            e.printStackTrace();
//        }
        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return uploadDocument(cont, file, path, data);
    }

    public String appendToFile(Context cont, File file, String path, String data)
    {
        return uploadDocument(cont, file, path, data);
    }
}
