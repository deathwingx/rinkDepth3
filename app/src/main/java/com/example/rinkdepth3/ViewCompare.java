package com.example.rinkdepth3;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ViewCompare
{
    private float[] getAverage(ArrayList<String[]> data)
    {
        float[] averageArray = new float[3];
        float average = 0;
        float outsideAverage = 0;
        float insideAverage = 0;
        for (int x = 0; x < data.size(); x++)
        {
            String[] depthLine = data.get(x);
            float depthFloat = Float.parseFloat(depthLine[0]);
            float xVal = Float.parseFloat(depthLine[1]);
            float yVal = Float.parseFloat(depthLine[2]);
            average += depthFloat;
            boolean leftXval = (xVal <= 255);
            boolean rightXVal = (xVal >= 805);
            boolean topYVal = (yVal <= 425);
            boolean bottomYVal = (yVal >= 1750);
            if (leftXval || rightXVal || topYVal || bottomYVal)
            {
                average += depthFloat;
                outsideAverage += depthFloat;
            }else
            {
                average += depthFloat;
                insideAverage += depthFloat;
            }
        }
        return averageArray;
    }

    public boolean getData(Context cont, ArrayList<TextView> depthData, File file, float[] average, ConstraintLayout layout) throws IOException {
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
                if (read.charAt(x) == ',')
                {
                    if (!commaOne)
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

    private void viewData(Context cont, ArrayList<TextView> depthData, ArrayList<String[]> data, ConstraintLayout layout)
    {
        for (int x = 0; x < data.size(); x++)
        {
            TextView newText = new TextView(cont);
            String[] line = data.get(x);
            float xFloat = Float.parseFloat(line[1]);
            xFloat = Math.round(xFloat);
            float yFloat = Float.parseFloat(line[2]);
            yFloat = Math.round(yFloat);
            int xVal = (int) xFloat;
            int yVal = (int) yFloat;
            newText.setTextColor(ContextCompat.getColor(cont, R.color.black));
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
            params.setMargins((xVal - 10), (yVal - 260), 0, 0);
            SpannableString spannableString = SpannableString.valueOf(line[0]);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);
            newText.setText(spannableString);
            newText.setLayoutParams(params);
            layout.addView(newText, params);
            depthData.add(newText);
            Log.d(String.format("data[%s][]: ", x), Arrays.toString(data.get(x)));
        }
    }

    public TextView viewData(Context cont, String[] data, ConstraintLayout layout)
    {

        TextView newText = new TextView(cont);
        float xFloat = Float.parseFloat(data[1]);
        xFloat = Math.round(xFloat);
        float yFloat = Float.parseFloat(data[2]);
        yFloat = Math.round(yFloat);
        int xVal = (int) xFloat;
        int yVal = (int) yFloat;
        newText.setTextColor(ContextCompat.getColor(cont, R.color.black));
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        params.setMargins((xVal - 10), (yVal - 260), 0, 0);
        SpannableString spannableString = SpannableString.valueOf(data[0]);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);
        newText.setText(spannableString);
        newText.setLayoutParams(params);
        layout.addView(newText, params);
        return newText;
    }

    public void compareData(Context cont, File firstDate, File secondDate, ConstraintLayout layout) throws IOException {
        ArrayList<String[]> fileOneData = readData(firstDate);
        ArrayList<String[]> fileTwoData = readData(secondDate);
        //TODO: *sort by x(will sort values left to right on screen) then iterate through y to find one within a certain amount* or reverse and sort by y (top to bottom so rows across
        //TODO: are grouped together) then iterate through x to find one within a certain amount
        for (int x = 0; x < fileOneData.size(); x++)
        {
            Log.i(String.format("fileOneData[%d]: ", x), Arrays.toString(fileOneData.get(x)));
        }
        fileOneData.sort((o1, o2) -> Float.compare(Float.parseFloat(o1[2]), Float.parseFloat(o2[2])));
        fileTwoData.sort((o1, o2) -> Float.compare(Float.parseFloat(o1[2]), Float.parseFloat(o2[2])));
        for (int x = 0; x < fileOneData.size(); x++)
        {
            Log.i(String.format("fileOneData[%d]: ", x), Arrays.toString(fileOneData.get(x)));
        }

    }

    private ArrayList<String[]> readData(File file) throws IOException {
        ArrayList<String[]> returnData = new ArrayList<>();
        BufferedReader readerOne = new BufferedReader(new FileReader(file));
        String read;
        boolean commaOne = false, commaTwo = false;
        while ((read = readerOne.readLine()) != null)
        {
            String[] lineData = new String[3];
            StringBuilder line = new StringBuilder();
            for(int x = 0; x<read.length(); x++)
            {
                if (read.charAt(x) == ',')
                {
                    if (!commaOne)
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
            returnData.add(lineData);
        }
        return returnData;
    }
}
