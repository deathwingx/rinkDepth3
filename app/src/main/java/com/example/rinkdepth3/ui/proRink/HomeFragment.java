package com.example.rinkdepth3.ui.proRink;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rinkdepth3.R;
import com.example.rinkdepth3.TCPFile;
import com.example.rinkdepth3.databinding.FragmentHomeBinding;

import java.io.File;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TCPFile tcpfile = new TCPFile();
        ImageView rinkImg;
        CalendarView datePicker;
        EditText et;
        Button viewButton;
        TextView response;

        String path = "http://192.168.1.8:8080/C:\\Users\\berna\\Documents\\rinkDepth";
        Context cont = root.getContext();
        File file = new File(cont.getFilesDir(), "test.csv");
        String data = "1.25, 565.34563, 234.234523452";

        rinkImg = root.findViewById(R.id.proRinkImg);
        rinkImg.setImageResource(R.drawable.prorink);
        datePicker = root.findViewById(R.id.datePickerView);
        et = root.findViewById(R.id.editTextDate);
        viewButton = root.findViewById(R.id.viewBtn);
        response = root.findViewById(R.id.textView);

        datePicker.setOnDateChangeListener((calendarView, year, month, day) -> {
            String date = day + " / " + month + " / " + year;
            et.setText(date);
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String res = tcpfile.createFile(cont, file, path, data);
                        response.post(new Runnable() {
                            @Override
                            public void run() {
                                response.setText(res);
                            }
                        });
                    }
                }).start();
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