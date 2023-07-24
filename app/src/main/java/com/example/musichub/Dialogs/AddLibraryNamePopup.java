package com.example.musichub.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musichub.Interfaces.AddLibraryInterface;
import com.example.musichub.R;

import org.w3c.dom.Text;


public class AddLibraryNamePopup extends Dialog {

    private TextView add, cancel;
    private EditText libName;
    private AddLibraryInterface addLibraryInterface;

    public AddLibraryNamePopup(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_library_name_popup);
        add = findViewById(R.id.create_playlist);
        cancel = findViewById(R.id.cancel_playlist);
        libName = findViewById(R.id.playlist_name);
        setCancelable(false);
        onClickListener();
    }

    private void onClickListener() {
        cancel.setOnClickListener(view -> {
            addLibraryInterface.cancel();
        });
        add.setOnClickListener(view -> {
                addLibraryInterface.add(libName.getText().toString());
        });
    }

    public void setAddLibraryInterface(AddLibraryInterface addLibraryInterface) {
        this.addLibraryInterface = addLibraryInterface;
    }
}
