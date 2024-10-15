package com.example.rinkdepth3.ui.compare;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rinkdepth3.R;
import com.example.rinkdepth3.databinding.FragmentCompareBinding;
import com.example.rinkdepth3.ViewCompare;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CompareFragment extends Fragment {

    private FragmentCompareBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CompareViewModel notificationsViewModel =
                new ViewModelProvider(this).get(CompareViewModel.class);

        binding = FragmentCompareBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewCompare viewCompare = new ViewCompare();
        AtomicBoolean proSelected = new AtomicBoolean(false);
        AtomicBoolean jrSelected = new AtomicBoolean(false);
        AtomicBoolean dateOneSelected = new AtomicBoolean(false);
        AtomicBoolean dateTwoSelected = new AtomicBoolean(false);
        Context cont = root.getContext();
        File temp = new File(cont.getFilesDir(), "temp.csv");
        AtomicReference<File> fileOne = new AtomicReference<>(temp);
        AtomicReference<File> fileTwo = new AtomicReference<>(temp);
        ConstraintLayout layout = root.findViewById(R.id.compareLayout);
        ArrayList<TextView> depthData = new ArrayList<>();
        Button proButton;
        Button jrButton;
        Button backButton;
        Button selectButton;
        ImageView jrRinkImage;
        ImageView proRinkImage;
        TextView dateOne;
        TextView dateTwo;
        CalendarView datePicker;

        proButton = root.findViewById(R.id.proBtn);
        jrButton = root.findViewById(R.id.jrBtn);
        backButton = root.findViewById(R.id.backBtn);
        selectButton = root.findViewById(R.id.selectBtn);
        jrRinkImage = root.findViewById(R.id.jrRinkImg);
        jrRinkImage.setImageResource(R.drawable.jrrink);
        proRinkImage = root.findViewById(R.id.proRinkImg);
        proRinkImage.setImageResource(R.drawable.prorink);
        dateOne = root.findViewById(R.id.dateOne);
        dateTwo = root.findViewById(R.id.dateTwo);
        datePicker = root.findViewById(R.id.datePickerView);
        datePicker.setVisibility(CalendarView.INVISIBLE);

        proButton.setOnClickListener(v -> {
            proButton.setVisibility(Button.INVISIBLE);
            jrButton.setVisibility(Button.INVISIBLE);
            selectButton.setVisibility(Button.VISIBLE);
            backButton.setVisibility(Button.VISIBLE);
            datePicker.setVisibility(CalendarView.VISIBLE);
            dateOne.setVisibility(TextView.VISIBLE);
            dateTwo.setVisibility(TextView.VISIBLE);
            proSelected.set(true);
        });

        jrButton.setOnClickListener(v -> {
            proButton.setVisibility(Button.INVISIBLE);
            jrButton.setVisibility(Button.INVISIBLE);
            selectButton.setVisibility(Button.VISIBLE);
            backButton.setVisibility(Button.VISIBLE);
            datePicker.setVisibility(CalendarView.VISIBLE);
            dateOne.setVisibility(TextView.VISIBLE);
            dateTwo.setVisibility(TextView.VISIBLE);
            jrSelected.set(true);
        });

        datePicker.setOnDateChangeListener((view, year, month, day) -> {
            if (dateOneSelected.get())
            {
                dateTwoSelected.set(true);
                StringBuilder builder = new StringBuilder();
                if (day < 10)
                {
                    String temp1 = "0" + day;
                    builder.append(month + 1).append("/").append(temp1).append("/").append(year-2000);
                }else
                {
                    builder.append(month + 1).append("/").append(day).append("/").append(year-2000);
                }
                String date = builder.toString();
                dateTwo.setText(date);
                final String newDate = date.replace("/", "");
                if (proSelected.get())
                {
                    File temp1 = new File(cont.getFilesDir(), newDate + "pro.csv");
                    fileTwo.set(temp1);
                }else
                {
                    File temp1 = new File(cont.getFilesDir(), newDate + "jr.csv");
                    fileTwo.set(temp1);
                    try {
                        viewCompare.compareData(cont, fileOne.get(), fileTwo.get(), depthData, layout);
                    } catch (IOException e) {
                        Log.d("compareData() e: ", e.getMessage());
                    }
                }

            }else
            {
                dateOneSelected.set(true);
                StringBuilder builder = new StringBuilder();
                if (day < 10)
                {
                    String temp1 = "0" + day;
                    builder.append(month + 1).append("/").append(temp1).append("/").append(year-2000);
                }else
                {
                    builder.append(month + 1).append("/").append(day).append("/").append(year-2000);
                }
                String date = builder.toString();
                dateOne.setText(date);
                final String newDate = date.replace("/", "");
                if (proSelected.get())
                {
                    File temp1 = new File(cont.getFilesDir(), newDate + "pro.csv");
                    fileOne.set(temp1);
                }else
                {
                    File temp1 = new File(cont.getFilesDir(), newDate + "jr.csv");
                    fileOne.set(temp1);
                }
            }
            selectButton.setOnClickListener(v -> {
                if (!dateTwoSelected.get())
                {
                    Toast.makeText(cont, "Select date first!", Toast.LENGTH_SHORT).show();
                }else
                {
                    try {
                        proButton.setVisibility(Button.INVISIBLE);
                        jrButton.setVisibility(Button.INVISIBLE);
                        selectButton.setVisibility(Button.INVISIBLE);
                        backButton.setVisibility(Button.VISIBLE);
                        datePicker.setVisibility(CalendarView.INVISIBLE);
                        dateOne.setVisibility(TextView.INVISIBLE);
                        dateTwo.setVisibility(TextView.INVISIBLE);
                        if (proSelected.get())
                        {
                            proRinkImage.setVisibility(ImageView.VISIBLE);
                        }else
                        {
                            jrRinkImage.setVisibility(ImageView.VISIBLE);
                        }
                        viewCompare.compareData(cont, fileOne.get(), fileTwo.get(),depthData, layout);
                    } catch (IOException e) {
                        Log.d("CompareData() e: ", e.getMessage());
                    }
                }
            });
        });

        backButton.setOnClickListener(v -> {
            proButton.setVisibility(Button.VISIBLE);
            jrButton.setVisibility(Button.VISIBLE);
            selectButton.setVisibility(Button.INVISIBLE);
            backButton.setVisibility(Button.INVISIBLE);
            datePicker.setVisibility(CalendarView.INVISIBLE);
            dateOne.setVisibility(TextView.INVISIBLE);
            dateTwo.setVisibility(TextView.INVISIBLE);
            proRinkImage.setVisibility(ImageView.INVISIBLE);
            jrRinkImage.setVisibility(ImageView.INVISIBLE);
            if (!depthData.isEmpty())
            {
                for (int x = 0; x < depthData.size(); x++)
                {
                    TextView tempText = depthData.get(x);
                    tempText.setVisibility(TextView.INVISIBLE);
                }
            }
            dateOne.setText("");
            dateTwo.setText("");
            dateOneSelected.set(false);
            dateTwoSelected.set(false);
            jrSelected.set(false);
            proSelected.set(false);
//TODO: Reset dates selected back to empty string and reset dayOneSelected to false 
        });

        selectButton.setOnClickListener(v -> Toast.makeText(cont, "Select date first!", Toast.LENGTH_SHORT).show());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}