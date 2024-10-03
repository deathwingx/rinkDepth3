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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rinkdepth3.R;
import com.example.rinkdepth3.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String depthxy[] = {"","",""};
        TextView touchLocation;
        ImageView jrRinkImage;
        TextView depth;


        touchLocation = root.findViewById(R.id.touchLocation);
        jrRinkImage = root.findViewById(R.id.jrRinkImg);
        jrRinkImage.setImageResource(R.drawable.jrrink);
        depth = root.findViewById(R.id.textView2);

//        jrRinkImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                depthxy[0] = showBuilder(root.getContext(), depthxy);
//            }
//        });
        jrRinkImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                if (action == MotionEvent.ACTION_UP)
                {
                    depthxy[0] = showBuilder(root.getContext(), depthxy);
                    depthxy[1] = String.valueOf(event.getX());
                    depthxy[2] = String.valueOf(event.getY());
                    depth.setText(depthxy[0]);
                }
                return false;
            }
        });
//        jrRinkImage.setOnTouchListener(((view, motionEvent) -> {
//            String location = motionEvent.getX() + " " + motionEvent.getY();
//            touchLocation.setText(location);
//            depthxy[0] = showBuilder(root.getContext(), depthxy);
//            depth.setText(depthxy[0]);
//            return true;
//        }));

        return root;
    }

    public String showBuilder(Context cont, String[] data)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setTitle("Depth");
        EditText input = new EditText(cont);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data[0] = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        return data[0];
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}