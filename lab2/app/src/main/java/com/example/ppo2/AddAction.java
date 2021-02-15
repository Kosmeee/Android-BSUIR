package com.example.ppo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

import java.io.Console;

public class AddAction extends AppCompatActivity implements ColorPickerDialogListener {

    private EditText tsTitle;
    private EditText tsPrepare;
    private EditText tsWork;
    private EditText tsCalm;
    private EditText tsCyc;
    private EditText tsSets;
    private EditText ettsCalm;
    private DatabaseAdapter adapter;

    private int color;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        tsTitle = (EditText)findViewById(R.id.tsTitle);
        tsPrepare = (EditText)findViewById(R.id.tsPrepare);
        tsWork = (EditText)findViewById(R.id.tsWork);
        tsCalm = (EditText)findViewById(R.id.tsCalm);
        tsCyc = (EditText)findViewById(R.id.tsCyc);
        tsSets = (EditText)findViewById(R.id.tsSets);
        ettsCalm = (EditText)findViewById(R.id.ettsCalm);

        adapter = new DatabaseAdapter(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            id = extras.getInt("id");
        }

        if (id > 0)
        {
            adapter.open();

            Sequence sequence = adapter.getSequence(id);
            tsTitle.setText(sequence.title);
            tsPrepare.setText(String.valueOf(sequence.prepare));
            tsWork.setText(String.valueOf(sequence.work));
            tsCalm.setText(String.valueOf(sequence.calm));
            tsCyc.setText(String.valueOf(sequence.cycles));
            tsSets.setText(String.valueOf(sequence.sets));
            ettsCalm.setText(String.valueOf(sequence.stsCalm));
            color = sequence.color;
            adapter.close();
        }
        else{
        }
    }
    private void createColorPickerDialog()
    {
        ColorPickerDialog.newBuilder()
                .setColor(Color.BLUE)
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.SQUARE)
                .show(this);
    }
    @Override
    public void onColorSelected(int dialogId, int color)
    {
        this.color = color;
    }

    @Override
    public void onDialogDismissed(int dialogId) {
    }


    public void onBtnColorClick(View view) {
                createColorPickerDialog();
    }

    public void onBtnSaveClick(View view)
    {
        String title = tsTitle.getText().toString();
        int calm = Integer.parseInt(tsCalm.getText().toString());
        int cycles = Integer.parseInt(tsCyc.getText().toString());
        int sets = Integer.parseInt(tsSets.getText().toString());
        int work = Integer.parseInt(tsWork.getText().toString());
        int stsCalm = Integer.parseInt(ettsCalm.getText().toString());
        int prepare = Integer.parseInt(tsPrepare.getText().toString());


        adapter.open();
        if (id > 0)
        {
            adapter.update(new Sequence(id, this.color, title, prepare, work, calm, cycles, sets, stsCalm));
        } else {
            adapter.insert(new Sequence(id, this.color, title, prepare, work, calm, cycles, sets, stsCalm));
        }
        adapter.close();
        goHome();
    }

    private void goHome()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}