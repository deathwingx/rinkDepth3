package com.example.rinkdepth3;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
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
import java.util.Arrays;
import java.util.Objects;

public class TCPFile
{
    public String uploadData(Context cont, File file, String[] data)
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
            Log.d("uploadData() e:", e.getMessage());        }
        return "I Don't Know How I Got Here!";
    }

    public String uploadDocument(Context cont, File file, String[] data)
    {
        try
        {
            Socket socket = new Socket("192.168.1.8", 9090);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            int bytes = 0;
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            if (socket.isConnected())
            {
                while ((bytes = fis.read(buffer)) != -1)
                {
                    dos.write(buffer, 0, bytes);
                    dos.flush();
                }
            }
            socket.shutdownOutput();
            String res = read.readLine();
            Log.d("Received: ", res);
            if (res == "Received!")
            {
                socket.close();
                dos.close();
                fis.close();
                read.close();
                dis.close();
                return "Done!";
            }else
            {
                socket.close();
                dos.close();
                fis.close();
                read.close();
                dis.close();
                return "Didn't work!";
            }
        }catch (IOException e)
        {
            Log.d("uploadDocument() e:",e.getMessage());
        }
        return "Didn't work!";
    }

    public void createFile(Context cont, File file, String[] data)
    {
        try
        {
            File newFile = new File(cont.getFilesDir(), file.getName());
            if (!newFile.createNewFile())
            {
                appendToFile(cont, newFile, data);
            }else
            {
                writeToFile(cont, newFile, data);
            }
        }catch (IOException e)
        {
            Log.d("createFile() e:",e.getMessage());
        }
    }

    public void writeToFile(Context cont, File file, String[] data)
    {
        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(data[0] + ", ");
            writer.write(data[1] + ", ");
            writer.write(data[2] + "\n");
            writer.close();
        }catch (IOException e)
        {
            Log.d("writeToFile() e:", e.getMessage());
        }
    }

    public void appendToFile(Context cont, File file, String[] data)
    {
        try
        {
            FileWriter writer = new FileWriter(file, true);
            writer.append(data[0]).append(", ");
            writer.append(data[1]).append(", ");
            writer.append(data[2]).append("\n");
            writer.close();
            FileReader reader = new FileReader(file);
            char[] buffer = new char[20];
            int charsRead = reader.read(buffer, 0 , 20);
            while ((charsRead = reader.read(buffer)) != -1)
            {
                Log.d("buffer: ", Arrays.toString(buffer));
            }
            reader.close();
        }catch (IOException e)
        {
            Log.d("appendToFile() e:", e.getMessage());
        }
    }
}
