package com.competitionapp.nrgscouting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Peyton Lee on 2/17/2018.
 */

public class EventListEntry extends Fragment {

    public ArrayList<Entry.TimeEvent> timeEventList = new ArrayList<Entry.TimeEvent>();
    ListView eventListView;
    TimeEventAdapter timeEventAdapter;
    Button save;
    Button back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_event_list, container, false);

    }

    public void onStart() {
        super.onStart();

        //1:36.450 =
        /*
        timeEventList.add(new Entry.TimeEvent(96450, Entry.EventType.PICKED_CUBE_0, Entry.CubeDropType.NONE_0));
        timeEventList.add(new Entry.TimeEvent(123150, Entry.EventType.DROPPED_CUBE_1, Entry.CubeDropType.EXCHANGE_4));
        timeEventList.add(new Entry.TimeEvent(1234, Entry.EventType.ALLY_END_3, Entry.CubeDropType.NONE_0));
        */

        eventListView = (ListView) this.getView().findViewById(R.id.timeEventList);
        timeEventAdapter = new TimeEventAdapter(this.getContext(), timeEventList);
        eventListView.setAdapter(timeEventAdapter);

        eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Event")
                        .setMessage("Are you sure you want to delete this event?\n" + Entry.getEventName(timeEventList.get(position))
                                + " @ " + convertTimeToText(timeEventList.get(position).timestamp))
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Entry newEntry = ((TabbedActivity) getActivity()).newEntry;
                                if(newEntry.timeEvents.size() > position) {
                                    newEntry.timeEvents.remove(position);
                                }
                                ((TabbedActivity) getActivity()).cacheEntry();
                                Collections.sort(newEntry.timeEvents);
                                timeEventList = newEntry.timeEvents;
                                UpdateView();
                                Toast.makeText(getActivity(), "Deleted Timestamp!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .show();

                return false;
            }
        });

        if(timeEventList.isEmpty()) {
            getView().findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.emptyView).setVisibility(View.GONE);
        }

        save = (Button) getView().findViewById(R.id.save);
        back = (Button) getView().findViewById(R.id.back);


        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    //Toast.makeText(getActivity(), "Trying to save...", Toast.LENGTH_SHORT);
                    ((TabbedActivity) getActivity()).saveAndExit();

                } catch (Exception e) {

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }

    public void UpdateView() {
        if(timeEventList.isEmpty()) {
            getView().findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.emptyView).setVisibility(View.GONE);
        }
        Collections.sort(timeEventList);
        timeEventAdapter = new TimeEventAdapter(this.getContext(), timeEventList);
        eventListView.setAdapter(timeEventAdapter);
    }


    public static String convertTimeToText(int timestamp) {
        String min = "0";
        String sec = "00";
        String mSec = "000";

        min = String.valueOf(timestamp/60000);
        timestamp = timestamp%60000;

        sec = String.valueOf(timestamp/1000);
        timestamp = timestamp%1000;

        mSec = String.valueOf(timestamp/10);

        return "" + min +":" + formatToLength(sec, 2) + "." + formatToLength(mSec, 2);
    }

    public static String formatToLength(String input, int length) {
        for(int i = length - input.length(); i > 0; i--) {
            input = "0" + input;
        }
        return input;
    }

    public class TimeEventAdapter extends ArrayAdapter<Entry.TimeEvent> {

        ArrayList<Entry.TimeEvent> timeEventList;

        public TimeEventAdapter(Context context, ArrayList<Entry.TimeEvent> timeEventList) {
            super(context, 0, timeEventList);
            this.timeEventList = timeEventList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            if(timeEventList.size() <= position) { return null;}
            Entry.TimeEvent timeEvent = timeEventList.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_time_event, parent, false);
            }
            ImageView eventIcon = (ImageView) convertView.findViewById(R.id.eventIcon);
            TextView eventName = (TextView) convertView.findViewById(R.id.eventName);
            TextView timestamp = (TextView) convertView.findViewById(R.id.eventTimestamp);
            //NEEDS TO BE FORMATTED
            timestamp.setText(convertTimeToText(timeEvent.timestamp));
            eventIcon.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            switch (timeEvent.type) {
                case PICKED_CUBE_0:
                    eventIcon.setImageResource(R.drawable.ic_picked_cube);
                    eventName.setText("Gained Cube");
                    break;
                case CLIMB_START_10:
                    eventIcon.setImageResource(R.drawable.ic_climb);
                    eventName.setText("Started Climbing");
                    break;
                case DROPPED_CUBE_1:
                    switch (timeEvent.cubeDropType) {
                        case NONE_0:
                            eventName.setText("Dropped Cube (None)");
                            eventIcon.setImageResource(R.drawable.ic_drop_cube);
                            break;
                        case ALLY_SWITCH_1:
                            eventName.setText("Dropped Cube (Ally Switch)");
                            eventIcon.setImageResource(R.drawable.ic_switch);
                            break;
                        case OPP_SWITCH_2:
                            eventName.setText("Dropped Cube (Opponent Switch)");
                            eventIcon.setImageResource(R.drawable.ic_switch);
                            break;
                        case SCALE_3:
                            eventName.setText("Dropped Cube (Scale)");
                            eventIcon.setImageResource(R.drawable.ic_scale);
                            break;
                        case EXCHANGE_4:
                            eventName.setText("Dropped Cube (Exchange)");
                            eventIcon.setImageResource(R.drawable.ic_exchange);
                    }
                    break;
            }

            return convertView;
        }

    }

}
