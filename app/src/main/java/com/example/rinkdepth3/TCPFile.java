package com.example.rinkdepth3;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Objects;

public class TCPFile
{
    public String uploadData(Context cont, File file, String path, String data)
    {
        try {
            Socket socket = new Socket("192.168.1.8", 9090);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(file);
            String response = in.readLine();
            socket.close();
            if (Objects.equals(data, response))
            {
                socket.close();
                return "Worked!";
            }else
            {
                socket.close();
                return "Didn't Work!";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "I Don't Know How I Got Here!";
    }

    public String uploadDocument(Context cont, File file, String path, String data)
    {
        try
        {
            Socket socket = new Socket("192.168.1.8", 9090);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            int bytes = 0;
            FileInputStream fis = new FileInputStream(file);
            //dos.writeLong(file.length());
            byte[] buffer = new byte[1024];
            while ((bytes = fis.read(buffer)) != -1)
            {
                dos.write(buffer, 0, bytes);
                dos.flush();
            }
            socket.close();
            fis.close();
            return "Done!";
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return "Didn't work!";
    }

    public String createFile(Context cont, File file, String path, String data)
    {
        try
        {
            File newFile = new File(cont.getFilesDir(), file.getName());
            if (!newFile.createNewFile()) {
                appendToFile(cont, file, path, data);
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return writeToFile(cont,file, path, data);
    }

    public String writeToFile(Context cont, File file, String path, String data)
    {
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
        try
        {
            FileWriter writer = new FileWriter(file);
            writer.append(data);
            writer.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return uploadDocument(cont, file, path, data);
    }
}
