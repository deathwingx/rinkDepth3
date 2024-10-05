package com.example.rinkdepth3;

import android.content.Context;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ViewCompare
{
    public boolean viewData(Context cont, ArrayList<TextView> depthData, String date) throws IOException {
        File file = new File(cont.getFilesDir(), (date + ".csv"));
        ArrayList<String[]> data = new ArrayList<>();
        if (!file.exists())
        {
            return false;
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String read;
        boolean commaOne = false, commaTwo = false;
        while ((read = reader.readLine()) != null)
        {
            String[] lineData = new String[3];
            String line = "";
            for(int x = 0; x<read.length(); x++)
            {
                if (read.charAt(x) == ' ')
                {
                    x++;
                }else if (read.charAt(x) == ',')
                {
                    if (!commaOne && !commaTwo)
                    {
                        commaOne = true;
                        lineData[0] = line;
                        line = "";
                    }else if (!commaTwo)
                    {
                        commaTwo = true;
                        lineData[1] = line;
                        line = "";
                    }
                }else
                {
                    line += read.charAt(x);
                }
            }
            lineData[2] = line;
            data.add(lineData);
        }

        for (int x = 0; x < depthData.size(); x++)
        {
            x++;
        }
        return true;
    }
}
