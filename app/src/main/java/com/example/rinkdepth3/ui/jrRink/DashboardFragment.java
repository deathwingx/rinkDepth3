package com.example.rinkdepth3.ui.jrRink;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rinkdepth3.R;
import com.example.rinkdepth3.TCPFile;
import com.example.rinkdepth3.databinding.FragmentDashboardBinding;

import java.io.File;
import java.util.Objects;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TCPFile tcpFile = new TCPFile();
        Context cont = root.getContext();
        File file = new File(cont.getFilesDir(), "test1.csv");
        String[] depthxy = {"","",""};
        TextView touchLocation;
        ImageView jrRinkImage;
        TextView depth;
        Button viewButton;

        touchLocation = root.findViewById(R.id.touchLocation);
        jrRinkImage = root.findViewById(R.id.jrRinkImg);
        jrRinkImage.setImageResource(R.drawable.jrrink);
        depth = root.findViewById(R.id.textView2);
        viewButton = root.findViewById(R.id.viewBtn);

        jrRinkImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN)
                {
                    depthxy[0] = showBuilder(cont, depthxy);
                    depthxy[1] = String.valueOf(event.getX());
                    depthxy[2] = String.valueOf(event.getY());
                    depth.setText(depthxy[0]);
                }
                return false;
            }
        });
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String res = tcpFile.uploadDocument(cont, file, depthxy);
                        touchLocation.post(new Runnable() {
                            @Override
                            public void run() {
                                if (Objects.equals(res, "Done!"))
                                {
                                    Toast.makeText(cont, "Sent", Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    Toast.makeText(cont, "Failed to send!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
        return root;
    }

    public String showBuilder(Context cont, String[] depthxy)
    {
        File file = new File(cont.getFilesDir(), "test1.csv");
        TCPFile tcpFile = new TCPFile();
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setTitle("Depth");
        EditText input = new EditText(cont);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                depthxy[0] = input.getText().toString();
                tcpFile.createFile(cont, file, depthxy);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
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