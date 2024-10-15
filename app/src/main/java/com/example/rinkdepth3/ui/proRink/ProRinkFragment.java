package com.example.rinkdepth3.ui.proRink;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rinkdepth3.R;
import com.example.rinkdepth3.TCPFile;
import com.example.rinkdepth3.databinding.FragmentProrinkBinding;
import com.example.rinkdepth3.ViewCompare;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class ProRinkFragment extends Fragment {

    private FragmentProrinkBinding binding;

    @SuppressLint({"ClickableViewAccessibility", "DefaultLocale"})
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProRinkViewModel homeViewModel =
                new ViewModelProvider(this).get(ProRinkViewModel.class);

        binding = FragmentProrinkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewCompare viewCompare = new ViewCompare();
        TCPFile tcpfile = new TCPFile();
        Context cont = root.getContext();
        ImageView proRinkImg;
        CalendarView datePicker;
        Button viewButton, newButton, uploadButton, backButton;
        TextView overallAverage, outsideAverage, insideAverage, overallAverageLabel, outsideAverageLabel, insideAverageLabel;
        String[] depthxy = new String[3];
        //AtomicReferenceArray averages = new AtomicReferenceArray<>(new float[3]);
        AtomicReference<float[]> averages = new AtomicReference<>(new float[3]);
        ArrayList<TextView> depthData = new ArrayList<>();
        ArrayList<TextView> newData = new ArrayList<>();
        ConstraintLayout layout = root.findViewById(R.id.proRinkLayout);

        Format f = new SimpleDateFormat("MM/dd/yy", Locale.US);
        String strDate = f.format(new Date());
        strDate = strDate.replace("/", "");
        File atomicFile = new File(cont.getFilesDir(), strDate + "pro.csv");
        AtomicReference<File> file = new AtomicReference<>(atomicFile);
        proRinkImg = root.findViewById(R.id.proRinkImg);
        proRinkImg.setImageResource(R.drawable.prorink);
        datePicker = root.findViewById(R.id.datePickerView);
        viewButton = root.findViewById(R.id.viewBtn);
        newButton = root.findViewById(R.id.newBtn);
        uploadButton = root.findViewById(R.id.uploadBtn);
        backButton = root.findViewById(R.id.backBtn);
        overallAverage = root.findViewById(R.id.averageNumber);
        outsideAverage = root.findViewById(R.id.outsideNumber);
        insideAverage = root.findViewById(R.id.insideNumber);
        overallAverageLabel = root.findViewById(R.id.averageText);
        outsideAverageLabel = root.findViewById(R.id.outsideAverage);
        insideAverageLabel = root.findViewById(R.id.insideAverage);

        datePicker.setOnDateChangeListener((calendarView, year, month, day) -> {
            StringBuilder builder = new StringBuilder();
            if (day < 10)
            {
                String temp = "0" + day;
                builder.append(month + 1).append(temp).append(year-2000);
            }else
            {
                builder.append(month + 1).append(day).append(year-2000);
            }
            String date = builder.toString();
            final String newDate = date.replace("/", "");
            File viewFile = new File(cont.getFilesDir(), newDate + "pro.csv");
            viewButton.setOnClickListener(v -> {
                boolean fileExist = false;
                try {
                    viewButton.setVisibility(Button.INVISIBLE);
                    newButton.setVisibility(Button.INVISIBLE);
                    datePicker.setVisibility(CalendarView.INVISIBLE);
                    proRinkImg.setVisibility(ImageView.VISIBLE);
                    backButton.setVisibility(Button.VISIBLE);
                    overallAverageLabel.setVisibility(TextView.VISIBLE);
                    outsideAverageLabel.setVisibility(TextView.VISIBLE);
                    insideAverageLabel.setVisibility(TextView.VISIBLE);
                    overallAverage.setVisibility(TextView.VISIBLE);
                    outsideAverage.setVisibility(TextView.VISIBLE);
                    insideAverage.setVisibility(TextView.VISIBLE);
                    final float[] averageFloat = viewCompare.getData(cont, depthData, viewFile, layout);
                    averages.set(averageFloat);
                    overallAverage.setText(String.format("%.3f", averageFloat[0]));
                    outsideAverage.setText(String.format("%.3f", averageFloat[1]));
                    insideAverage.setText(String.format("%.3f", averageFloat[2]));
                } catch (IOException e) {
                    Log.d("viewData() e: ", e.getMessage());
                }
            });
            newButton.setOnClickListener(v -> {
                viewButton.setVisibility(Button.INVISIBLE);
                newButton.setVisibility(Button.INVISIBLE);
                datePicker.setVisibility(CalendarView.INVISIBLE);
                proRinkImg.setVisibility(ImageView.VISIBLE);
                uploadButton.setVisibility(Button.VISIBLE);
                backButton.setVisibility(Button.VISIBLE);
                file.set(new File(cont.getFilesDir(), newDate + "pro.csv"));
                proRinkImg.setOnTouchListener((v1, event) -> {
                    final int action = event.getAction();
                    if (action == MotionEvent.ACTION_UP)
                    {
                        depthxy[0] = showBuilder(cont, depthxy, file.get(), newData, layout);
                        depthxy[1] = String.valueOf(event.getRawX());
                        depthxy[2] = String.valueOf(event.getRawY());
                    }
                    return false;
                });
            });
        });

        uploadButton.setOnClickListener(v -> new Thread(() -> {
            final String res = tcpfile.uploadDocument(cont, file.get());
            uploadButton.post(() -> {
                if (Objects.equals(res, "Received!"))
                {
                    Toast.makeText(cont, "Sent!", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(cont, "Failed to send!", Toast.LENGTH_SHORT).show();
                }
            });
        }).start());

        backButton.setOnClickListener(v -> {
            viewButton.setVisibility(Button.VISIBLE);
            newButton.setVisibility(Button.VISIBLE);
            backButton.setVisibility(Button.INVISIBLE);
            uploadButton.setVisibility(Button.INVISIBLE);
            datePicker.setVisibility(CalendarView.VISIBLE);
            proRinkImg.setVisibility(ImageView.INVISIBLE);
            overallAverageLabel.setVisibility(TextView.INVISIBLE);
            outsideAverageLabel.setVisibility(TextView.INVISIBLE);
            insideAverageLabel.setVisibility(TextView.INVISIBLE);
            overallAverage.setVisibility(TextView.INVISIBLE);
            outsideAverage.setVisibility(TextView.INVISIBLE);
            insideAverage.setVisibility(TextView.INVISIBLE);
            for (int x = 0; x < depthData.size(); x++)
            {
                TextView tempText = depthData.get(x);
                tempText.setVisibility(TextView.INVISIBLE);
            }
            for (int x = 0; x < newData.size(); x++)
            {
                TextView tempText = newData.get(x);
                tempText.setVisibility(TextView.INVISIBLE);
            }
        });

        newButton.setOnClickListener(v -> Toast.makeText(cont, "Select a date first!", Toast.LENGTH_SHORT).show());

        viewButton.setOnClickListener(v -> Toast.makeText(cont, "Select a date first!", Toast.LENGTH_SHORT).show());

        return root;
    }

    public String showBuilder(Context cont, String[] depthxy, File file, ArrayList<TextView> newData, ConstraintLayout layout)
    {
        ViewCompare viewCompare = new ViewCompare();
        TCPFile tcpFile = new TCPFile();
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setTitle("Depth");
        EditText input = new EditText(cont);
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            depthxy[0] = input.getText().toString();
            tcpFile.createFile(cont, file, depthxy);
            newData.add(viewCompare.viewData(cont, depthxy, layout));
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
        return depthxy[0];
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}