package com.example.rinkdepth3;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ViewCompare
{
    public boolean getData(Context cont, ArrayList<TextView> depthData, String date, ConstraintLayout layout) throws IOException {
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
            StringBuilder line = new StringBuilder();
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
                        lineData[0] = line.toString();
                        line = new StringBuilder();
                    }else if (!commaTwo)
                    {
                        commaTwo = true;
                        lineData[1] = line.toString();
                        line = new StringBuilder();
                    }
                }else
                {
                    line.append(read.charAt(x));
                }
            }
            commaOne = false;
            commaTwo = false;
            lineData[2] = line.toString();
            data.add(lineData);
        }
        viewData(cont, depthData, data, layout);
        return true;
    }

    public void viewData(Context cont, ArrayList<TextView> depthData, ArrayList<String[]> data, ConstraintLayout layout)
    {
        for (int x = 0; x < data.size(); x++)
        {
            TextView newText = new TextView(cont);
            String[] line = data.get(x);
            newText.setText(line[0]);
            float xFloat = Float.parseFloat(line[1]);
            xFloat = Math.round(xFloat);
            float yFloat = Float.parseFloat(line[2]);
            yFloat = Math.round(yFloat);
            int xVal = (int) xFloat;
            int yVal = (int) yFloat;
            newText.setTextColor(ContextCompat.getColor(cont, R.color.teal_200));
            //newText.setPadding(xVal + 109, yVal + 285, 0, 0);
            //newText.setWidth(250);
            //newText.setHeight(500);
            newText.setPaddingRelative(xVal + 109, yVal + 285,0,0);
            //newText.layout(xVal + 109, yVal +285, 0, 0);
            layout.addView(newText);
            depthData.add(newText);
            Log.d(String.format("data[%s][]: ", x), Arrays.toString(data.get(x)));
        }
    }
}
