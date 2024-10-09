package com.example.rinkdepth3.ui.jrRink;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.example.rinkdepth3.databinding.FragmentJrrinkBinding;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class JrRinkFragment extends Fragment {

    private FragmentJrrinkBinding binding;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        JrRinkViewModel jrRinkViewModel =
                new ViewModelProvider(this).get(JrRinkViewModel.class);

        binding = FragmentJrrinkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TCPFile tcpFile = new TCPFile();
        Context cont = root.getContext();
        Format f = new SimpleDateFormat("MM/dd/yy", Locale.US);
        String strDate = f.format(new Date());
        strDate = strDate.replace("/", "");
        File file = new File(cont.getFilesDir(), strDate + "jr.csv");
        String[] depthxy = new String[3];
        TextView touchLocation;
        ImageView jrRinkImage;
        TextView depth;
        Button uploadButton;

        touchLocation = root.findViewById(R.id.touchLocation);
        jrRinkImage = root.findViewById(R.id.jrRinkImg);
        jrRinkImage.setImageResource(R.drawable.jrrink);
        depth = root.findViewById(R.id.textView2);
        uploadButton = root.findViewById(R.id.uploadBtn);

        jrRinkImage.setOnTouchListener((v, event) -> {
            final int action = event.getAction();
            if (action == MotionEvent.ACTION_UP)
            {
                depthxy[0] = showBuilder(cont, depthxy, file);
                depthxy[1] = String.valueOf(event.getRawX() - 109);
                depthxy[2] = String.valueOf(event.getRawY() - 285);
                depth.setText(depthxy[0]);
            }
            return false;
        });
        uploadButton.setOnClickListener(v -> new Thread(() -> {
            final String res = tcpFile.uploadDocument(cont, file, depthxy);
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