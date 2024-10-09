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

public class ViewCompare
{
    public boolean getData(Context cont, ArrayList<TextView> depthData, File file, ConstraintLayout layout) throws IOException {
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

    public void viewData(Context cont, ArrayList<TextView> depthData, ArrayList<String[]> data, ConstraintLayout layout)
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
            params.setMargins((xVal + 100), (yVal + 10), 0, 0);
            SpannableString spannableString = SpannableString.valueOf(line[0]);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);
            newText.setText(spannableString);
            newText.setLayoutParams(params);
            layout.addView(newText, params);
            depthData.add(newText);
            Log.d(String.format("data[%s][]: ", x), Arrays.toString(data.get(x)));
        }
    }
}
