package com.example.rinkdepth3.ui.proRink;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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

public class ProRinkFragment extends Fragment {

    private FragmentProrinkBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProRinkViewModel homeViewModel =
                new ViewModelProvider(this).get(ProRinkViewModel.class);

        binding = FragmentProrinkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewCompare viewCompare = new ViewCompare();
        TCPFile tcpfile = new TCPFile();
        Context cont = root.getContext();
        String[] depthxy = new String[3];
        ImageView rinkImg;
        CalendarView datePicker;
        EditText et;
        Button viewButton;
        Button newButton;
        TextView response;
        ArrayList<TextView> depthData = new ArrayList<>();
        //LinearLayout layout = (LinearLayout) root.findViewById(R.id.layout);
        ConstraintLayout layout = (ConstraintLayout) root.findViewById(R.id.proRinkLayout);

        Format f = new SimpleDateFormat("MM/dd/yy", Locale.US);
        String strDate = f.format(new Date());
        strDate = strDate.replace("/", "");
        File file = new File(cont.getFilesDir(), strDate);
        rinkImg = root.findViewById(R.id.proRinkImg);
        rinkImg.setImageResource(R.drawable.prorink);
        datePicker = root.findViewById(R.id.datePickerView);
        et = root.findViewById(R.id.editTextDate);
        viewButton = root.findViewById(R.id.viewBtn);
        newButton = root.findViewById(R.id.newBtn);
        response = root.findViewById(R.id.textView);

        datePicker.setOnDateChangeListener((calendarView, year, month, day) -> {
            StringBuilder builder = new StringBuilder();
            if (day < 10)
            {
                String temp = "0" + String.valueOf(day);
                builder.append(month + 1).append(temp).append(year-2000);
            }else
            {
                builder.append(month+1).append(day).append(year-2000);
            }
            String date = builder.toString();
            date = date.replace("/", "");
            et.setText(date);
            File viewFile = new File(cont.getFilesDir(), date);
            String finalDate = date;
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean fileExist = false;
                    try {
                        viewButton.setVisibility(Button.INVISIBLE);
                        newButton.setVisibility(Button.INVISIBLE);
                        datePicker.setVisibility(DatePicker.INVISIBLE);
                        rinkImg.setVisibility(ImageView.VISIBLE);
                        et.setVisibility(TextView.INVISIBLE);
                        fileExist = viewCompare.getData(cont, depthData, finalDate, layout);
                    } catch (IOException e) {
                        Log.d("viewData() e: ", e.getMessage());
                    }
                    if (!fileExist)
                    {
                        Toast.makeText(cont, "File Does Not Exist!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}