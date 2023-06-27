package com.example.musichub.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musichub.Dialogs.AddLibraryNamePopup;
import com.example.musichub.Interfaces.AddLibraryInterface;
import com.example.musichub.R;

public class LibraryFrag extends Fragment {
    private ImageView addBtn;
    private AddLibraryNamePopup libraryNamePopup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addBtn = view.findViewById(R.id.playList_btn);
        onClickListener();
    }

    private void onClickListener() {
        addBtn.setOnClickListener(view -> {
            libraryNamePopup = new AddLibraryNamePopup(getContext());
            libraryNamePopup.setAddLibraryInterface(new AddLibraryInterface() {
                @Override
                public void cancel() {
                    Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                    libraryNamePopup.dismiss();
                }

                @Override
                public void add(String s) {
                    if (s.isEmpty()) {
                        Toast.makeText(getContext(), "PlayList couldn't Created", Toast.LENGTH_SHORT).show();
                        libraryNamePopup.dismiss();
                    } else {
                        Toast.makeText(getContext(), "PlayList Created", Toast.LENGTH_SHORT).show();
                        libraryNamePopup.dismiss();
                    }
                }
            });
            libraryNamePopup.show();
        });
    }
}
