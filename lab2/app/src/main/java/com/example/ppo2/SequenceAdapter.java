package com.example.ppo2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

class SequenceAdapter extends ArrayAdapter<Sequence>
{
    private LayoutInflater inflater;
    private int layout;
    private List<Sequence> sequenceList;
    private Context mCtx;
    private DatabaseAdapter adapter;

    SequenceAdapter(Context context, int resource, List<Sequence> sequences)
    {
        super(context, resource, sequences);
        this.sequenceList = sequences;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.mCtx = context;
        adapter = new DatabaseAdapter(context);
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {

        final ViewHolder viewHolder;
        if(convertView==null)
        {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Sequence sequence = sequenceList.get(position);

        viewHolder.tvTitleItem.setText(sequence.title);

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mCtx);
        try
        {
            float fSize = Float.parseFloat(prefs.getString(
                    mCtx.getString(R.string.pref_size), "16"));
            viewHolder.tvTitleItem.setTextSize(fSize);
        }
        catch (Exception ex)
        {
            viewHolder.tvTitleItem.setTextSize(18);
        }

        viewHolder.item.setBackgroundColor(sequence.color);
        viewHolder.item.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(sequence!=null)
                {
                    Intent intent = new Intent(mCtx, TimerActivity.class);
                    intent.putExtra("num", sequence.id);
                    intent.putExtra("click", 20);
                    mCtx.startActivity(intent);
                }
            }
        });

            viewHolder.tvOptions.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    switch (v.getId())
                    {
                        case R.id.tvOptions:

                            PopupMenu popup = new PopupMenu(mCtx, v);
                            popup.getMenuInflater().inflate(R.menu.item_menu,
                                    popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                            {
                                @Override
                                public boolean onMenuItemClick(MenuItem item)
                                {
                                    switch (item.getItemId())
                                    {
                                        case R.id.edit:
                                            Intent intent = new Intent(mCtx, AddAction.class);
                                            intent.putExtra("id", sequence.id);
                                            intent.putExtra("click", 25);
                                            mCtx.startActivity(intent);
                                            break;
                                        case R.id.delete:
                                            adapter.open();
                                            adapter.delete(sequence.id);
                                            adapter.close();
                                            mCtx.startActivity(new Intent(mCtx, MainActivity.class));
                                            break;
                                        default:
                                            break;
                                    }
                                    return true;
                                }
                            });
                            break;
                        default:
                            break;
                    }
                }
            });



        return convertView;
    }

    private class ViewHolder
    {
        final TextView tvTitleItem, tvOptions;
        final LinearLayout item;
        ViewHolder(View view)
        {
            tvTitleItem = (TextView) view.findViewById(R.id.tvTitleItem);
            item = (LinearLayout) view.findViewById(R.id.item);
            tvOptions = (TextView) view.findViewById(R.id.tvOptions);
        }
    }
}
