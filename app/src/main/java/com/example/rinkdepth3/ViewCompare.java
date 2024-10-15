package com.example.rinkdepth3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ViewCompare
{
    public float[] getAverage(ArrayList<String[]> data)
    {
        float[] averageArray = new float[3];
        float average = 0;
        float outsideAverage = 0;
        float insideAverage = 0;
        int outsideCount = 0, insideCount = 0;
        for (int x = 0; x < data.size(); x++)
        {
            String[] depthLine = data.get(x);
            float depthFloat = Float.parseFloat(depthLine[0]);
            float xVal = Float.parseFloat(depthLine[1]);
            float yVal = Float.parseFloat(depthLine[2]);
            boolean leftXval = (xVal <= 255);
            boolean rightXVal = (xVal >= 805);
            boolean topYVal = (yVal <= 425);
            boolean bottomYVal = (yVal >= 1750);
            if (leftXval || rightXVal || topYVal || bottomYVal)
            {
                average += depthFloat;
                outsideAverage += depthFloat;
                outsideCount += 1;
            }else
            {
                average += depthFloat;
                insideAverage += depthFloat;
                insideCount +=1;
            }
        }
        average = average / data.size();
        outsideAverage = outsideAverage / outsideCount;
        insideAverage = insideAverage / insideCount;
        averageArray[0] = average;
        averageArray[1] = outsideAverage;
        averageArray[2] = insideAverage;
        return averageArray;
    }

    public float[] getData(Context cont, ArrayList<TextView> depthData, File file, ConstraintLayout layout) throws IOException {
        ArrayList<String[]> data = new ArrayList<>();
        float[] average = new float[3];
        if (!file.exists())
        {
            Toast.makeText(cont, "File Does Not Exist!", Toast.LENGTH_SHORT).show();
            return average;
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
        average = getAverage(data);
        viewData(cont, depthData, data, layout);
        return average;
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
            params.setMargins((xVal - 50), (yVal - 260), 0, 0);
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
        params.setMargins((xVal - 50), (yVal - 260), 0, 0);
        SpannableString spannableString = SpannableString.valueOf(data[0]);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);
        newText.setText(spannableString);
        newText.setLayoutParams(params);
        layout.addView(newText, params);
        return newText;
    }

    public void compareData(Context cont, File firstDate, File secondDate, ArrayList<TextView> depthData, ConstraintLayout layout) throws IOException {
        ArrayList<String[]> fileOneData = readData(firstDate);
        ArrayList<String[]> fileTwoData = readData(secondDate);
        fileOneData.sort((o1, o2) -> Float.compare(Float.parseFloat(o1[2]), Float.parseFloat(o2[2])));
        fileTwoData.sort((o1, o2) -> Float.compare(Float.parseFloat(o1[2]), Float.parseFloat(o2[2])));
        for (int fileOneXY = 0; fileOneXY < fileOneData.size(); fileOneXY++)
        {
            String[] fileOne = fileOneData.get(fileOneXY);
            float xValOne = Float.parseFloat(fileOne[1]);
            float yValOne = Float.parseFloat(fileOne[2]);

            for (int fileTwoXY = 0; fileTwoXY < fileTwoData.size(); fileTwoXY++)
            {
                String[] fileTwo = fileTwoData.get(fileTwoXY);
                float xValTwo = Float.parseFloat(fileTwo[1]);
                float yValTwo = Float.parseFloat(fileTwo[2]);
                boolean yCheck = (Math.abs(yValOne - yValTwo) < 50);
                if (yCheck)
                {
                    boolean xCheck = (Math.abs(xValOne - xValTwo) < 50);
                    if (xCheck)
                    {
                        int xVal = (int)((xValOne + xValTwo) / 2);
                        int yVal = (int)((yValOne + yValTwo) / 2);
                        TextView newText = new TextView(cont);
                        newText.setTextColor(ContextCompat.getColor(cont, R.color.black));
                        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
                        params.setMargins((xVal - 50), (yVal - 260), 0, 0);
                        float fileOneDepth = Float.parseFloat(fileOne[0]);
                        float fileTwoDepth = Float.parseFloat(fileTwo[0]);
                        float difference = fileTwoDepth - fileOneDepth;
                        @SuppressLint("DefaultLocale") SpannableString spannableString = SpannableString.valueOf(String.format("%.3f", difference));
                        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);;
                        newText.setText(spannableString);
                        newText.setLayoutParams(params);
                        layout.addView(newText, params);
                        depthData.add(newText);
                        break;
                    } else if ((fileOneXY + 1) == fileOneData.size())
                    {
                        int xVal = (int)Float.parseFloat(fileOne[1]);
                        int yVal = (int)Float.parseFloat(fileOne[2]);
                        TextView newText = new TextView(cont);
                        newText.setTextColor(ContextCompat.getColor(cont, R.color.black));
                        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
                        params.setMargins((xVal - 50), (yVal - 260), 0, 0);
                        float fileOneDepth = Float.parseFloat(fileOne[0]);
                        @SuppressLint("DefaultLocale") SpannableString spannableString = SpannableString.valueOf(String.format("%.3f", fileOneDepth));
                        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);;
                        newText.setText(spannableString);
                        newText.setLayoutParams(params);
                        layout.addView(newText, params);
                        depthData.add(newText);
                        break;
                    }
                }
            }
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
