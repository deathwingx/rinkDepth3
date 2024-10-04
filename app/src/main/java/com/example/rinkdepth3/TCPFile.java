package com.example.rinkdepth3;

import android.content.Context;
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
            e.printStackTrace();
        }
        return "I Don't Know How I Got Here!";
    }

    public String uploadDocument(Context cont, File file, String[] data)
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
//            byte[] inBuffer = new byte[1024];
//            int inBytes = 0;
//            while ((inBytes = dis.read(inBuffer)) != -1)
//            {
//                String message = new String(inBuffer, 0, inBytes);
//                System.out.print(message);
//                System.out.flush();
//            }
            socket.close();
            fis.close();
            return "Done!";
        }catch (IOException e)
        {
            e.printStackTrace();
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
                appendToFile(cont, file, data);
            }else
            {
                writeToFile(cont, file, data);
            }
        }catch (IOException e)
        {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    public void appendToFile(Context cont, File file, String[] data)
    {
        try
        {
            FileWriter writer = new FileWriter(file);
            writer.append(data[0]).append(", ");
            writer.append(data[1]).append(", ");
            writer.append(data[2]).append("\n");
            writer.close();
            FileReader reader = new FileReader(file);
            char[] buffer = new char[20];
            int charsRead = reader.read(buffer, 0 , 20);
            while ((charsRead = reader.read(buffer)) != -1)
            {
                System.out.print(buffer);
            }
            reader.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
