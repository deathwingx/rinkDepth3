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
import android.widget.DatePicker;
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
import java.util.concurrent.atomic.AtomicReference;

public class ProRinkFragment extends Fragment {

    private FragmentProrinkBinding binding;

    @SuppressLint("ClickableViewAccessibility")
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
        Button viewButton;
        Button newButton;
        Button uploadButton;
        String[] depthxy = new String[3];
        ArrayList<TextView> depthData = new ArrayList<>();
        ConstraintLayout layout = (ConstraintLayout) root.findViewById(R.id.proRinkLayout);

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
                    datePicker.setVisibility(DatePicker.INVISIBLE);
                    proRinkImg.setVisibility(ImageView.VISIBLE);
                    fileExist = viewCompare.getData(cont, depthData, viewFile, layout);
                } catch (IOException e) {
                    Log.d("viewData() e: ", e.getMessage());
                }
                if (!fileExist)
                {
                    Toast.makeText(cont, "File Does Not Exist!", Toast.LENGTH_SHORT).show();
                }
            });
            newButton.setOnClickListener(v -> {
                viewButton.setVisibility(Button.INVISIBLE);
                newButton.setVisibility(Button.INVISIBLE);
                datePicker.setVisibility(DatePicker.INVISIBLE);
                proRinkImg.setVisibility(ImageView.VISIBLE);
                uploadButton.setVisibility(Button.VISIBLE);
                file.set(new File(cont.getFilesDir(), newDate + "pro.csv"));
            });
        });

        proRinkImg.setOnTouchListener((v, event) -> {
            final int action = event.getAction();
            if (action == MotionEvent.ACTION_UP)
            {
                depthxy[0] = showBuilder(cont, depthxy, file.get());
                depthxy[1] = String.valueOf(event.getRawX());
                depthxy[2] = String.valueOf(event.getRawY());
            }
            return false;
        });

        uploadButton.setOnClickListener(v -> new Thread(() -> {
            final String res = tcpfile.uploadDocument(cont, file.get(), depthxy);
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

        newButton.setOnClickListener(v -> Toast.makeText(cont, "Select a date first!", Toast.LENGTH_SHORT).show());

        viewButton.setOnClickListener(v -> Toast.makeText(cont, "Select a date first!", Toast.LENGTH_SHORT).show());

        return root;
    }

    public String showBuilder(Context cont, String[] depthxy, File file)
    {
        TCPFile tcpFile = new TCPFile();
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setTitle("Depth");
        EditText input = new EditText(cont);
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            depthxy[0] = input.getText().toString();
            tcpFile.createFile(cont, file, depthxy);
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