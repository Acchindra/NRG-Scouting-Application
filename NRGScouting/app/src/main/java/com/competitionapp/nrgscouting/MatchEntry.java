
package com.competitionapp.nrgscouting;


import android.os.Bundle;
import android.os.Environment;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Button;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchEntry extends Fragment {
    private EditText matchNumber;
    private Spinner position;
    private EditText gears;
    private EditText ballsShot;
    private EditText autoGears;
    private EditText autoBallsShot;
    private RatingBar rating;
    private CheckBox death;
    private CheckBox baseline;
    private CheckBox ropeClimb;
    private String teamName="";
    private Button save;
    private Button plusGears;
    private Button minusGears;
    private Button plusAutoGears;
    private Button minusAutoGears;
    private Button minusBallsShot;
    private Button plusBallsShot;
    private Button plusAutoBallsShot;
    private Button minusAutoBallsShot;
    private static ArrayList<Entry> listOfEntriesInFile=new ArrayList<Entry>();
    public MatchEntry() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_entry, container, false);
    }
    public void saveEntry() throws IOException{
        Entry newOne;
        final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/NRGScouting/");
        dir.mkdirs();
        final File entryFile = new File(dir,"Entries.txt");
        newOne = new Entry(getPosition(position.getSelectedItemPosition()), String.valueOf(teamName),
                Integer.parseInt(String.valueOf(matchNumber.getText())), Integer.parseInt(String.valueOf(gears.getText())),
                Integer.parseInt(String.valueOf(ballsShot.getText())), Integer.parseInt(String.valueOf(autoGears.getText())),
                Integer.parseInt(String.valueOf(autoBallsShot.getText())), rating.getNumStars(), death.isChecked(), baseline.isChecked(),
                ropeClimb.isChecked());
            if (entryFile.exists()) {
                getAllEntriesInFileIntoObjectForm(entryFile);//loading entry into the array list

                        entryFile.createNewFile();
                        for (Entry a : listOfEntriesInFile) {
                          if (a.matchNumber != newOne.matchNumber) {
                            a.writeEntry(entryFile);
                        }
                        }
                        }
            else{//File doesn't exist
                 entryFile.createNewFile();
            }
            newOne.writeEntry(entryFile);
        }
            public static ArrayList<Entry> getAllEntriesInFileIntoObjectForm (File entries){
                Scanner fileScanner;
                try {
                    String fileText = "";
                    fileScanner = new Scanner(entries);
                    ArrayList<String> lines = new ArrayList<String>();
                    while (fileScanner.hasNextLine()) {
                        lines.add(fileScanner.nextLine());
                    }
                    for (String line : lines) {
                        String[] properties = line.split("\t");
                        Entry newEntry = new Entry();
                        //USE AN INITIALIZER TO DO THE THING DONE BELOW USING A LOOP AND WITH A LOT LESS CODE
                        String[] matchNum = properties[0].split(":");
                        newEntry.matchNumber = Integer.parseInt(matchNum[1]);
                        String[] gears = properties[1].split(":");
                        newEntry.gearsRetrieved = Integer.parseInt(gears[1]);
                        String[] autoGears = properties[2].split(":");
                        newEntry.autoGearsRetrieved = Integer.parseInt(autoGears[1]);
                        String[] balls = properties[3].split(":");
                        newEntry.ballsShot = Integer.parseInt(balls[1]);
                        String[] autoBalls = properties[4].split(":");
                        newEntry.autoBallsShot = Integer.parseInt(autoBalls[1]);
                        String[] rating = properties[5].split(":");
                        newEntry.rating = Integer.parseInt(rating[1]);
                        String[] death = properties[6].split(":");
                        newEntry.death = Boolean.getBoolean(death[1]);
                        String[] baseline = properties[7].split(":");
                        newEntry.crossedBaseline = Boolean.getBoolean(baseline[1]);
                        String[] rope = properties[8].split(":");
                        newEntry.climbsRope = Boolean.getBoolean(rope[1]);
                        String[] name = properties[9].split(":");
                        newEntry.teamName = name[1];
                        //ADD THE NEW ENTRY IN THE LINE TO THE LISTOFENTRIES ARRAYLIST OBJECT
                        listOfEntriesInFile.add(newEntry);
                    }
                    return listOfEntriesInFile;
                } catch (FileNotFoundException e) {
                    System.out.println("The file does not exist");
                }

                return null;
            }

        public Entry.Position getPosition ( int row){
            if (row == 0)
                return Entry.Position.RED1;
            else if (row == 1)
                return Entry.Position.RED2;
            else if (row == 2)
                return Entry.Position.RED3;
            else if (row == 3)
                return Entry.Position.BLUE1;
            else if (row == 4)
                return Entry.Position.BLUE2;
            return Entry.Position.BLUE3;
        }

        @Override
        public void onStart () {
            matchNumber = (EditText) (getView().findViewById(R.id.matchNumber));
            position = (Spinner) (getView().findViewById(R.id.teamPosition));
            gears = (EditText) (getView().findViewById(R.id.gearsRetrieved));
            ballsShot = (EditText) (getView().findViewById(R.id.ballsShot));
            autoGears = (EditText) (getView().findViewById(R.id.autoGearsRetrieved));
            autoBallsShot = (EditText) (getView().findViewById(R.id.autoBallsShot));
            ropeClimb = (CheckBox) (getView().findViewById((R.id.ropeClimb)));
            baseline = (CheckBox) (getView().findViewById(R.id.baseline));
            death = (CheckBox) (getView().findViewById((R.id.death)));
            rating = (RatingBar) (getView().findViewById(R.id.sportsmanship));
            save = (Button) (getView().findViewById(R.id.save));
            plusGears = (Button) (getView().findViewById(R.id.plusGears));
            minusGears = (Button) (getView().findViewById(R.id.minusGears));
            plusAutoGears = (Button) (getView().findViewById(R.id.plusAutoGears));
            minusAutoGears = (Button) (getView().findViewById(R.id.minusAutoGears));
            minusBallsShot = (Button) (getView().findViewById(R.id.minusBallsShot));
            plusBallsShot = (Button) (getView().findViewById(R.id.plusBallsShot));
            plusAutoBallsShot = (Button) (getView().findViewById(R.id.plusAutoBallsShot));
            minusAutoBallsShot = (Button) (getView().findViewById(R.id.minusAutoBallsShot));

            save.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        saveEntry();

                    } catch (Exception e) {

                    }
                }
            });


        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                try {
                    saveEntry();




                    } catch (IOException e) {
                        //Just crash and do nothing
                    }
                }

            });

            plusGears.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gears.getText().toString().equals("")) {
                        gears.setText("1");
                    } else {
                        int gears1 = Integer.parseInt(String.valueOf(gears.getText()));
                        gears1++;
                        gears.setText(String.valueOf(gears1));
                    }
                }
            });
            minusGears.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gears.getText().toString().equals("")) {

                    } else {
                        int gears1 = Integer.parseInt(String.valueOf(gears.getText()));
                        gears1--;
                        gears.setText(String.valueOf(gears1));
                    }
                }
            });
            plusAutoGears.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (autoGears.getText().toString().equals("")) {
                        autoGears.setText("1");
                    } else {
                        int gears1 = Integer.parseInt(String.valueOf(autoGears.getText()));
                        gears1++;
                        autoGears.setText(String.valueOf(gears1));
                    }
                }
            });
            minusAutoGears.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (autoGears.getText().toString().equals("")) {

                    } else {
                        int gears1 = Integer.parseInt(String.valueOf(autoGears.getText()));
                        gears1--;
                        autoGears.setText(String.valueOf(gears1));
                    }
                }
            });
            minusBallsShot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ballsShot.getText().toString().equals("")) {

                    } else {
                        int gears1 = Integer.parseInt(String.valueOf(ballsShot.getText()));
                        gears1--;
                        ballsShot.setText(String.valueOf(gears1));
                    }
                }
            });
            plusBallsShot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ballsShot.getText().toString().equals("")) {
                        ballsShot.setText("1");
                    } else {
                        int gears1 = Integer.parseInt(String.valueOf(ballsShot.getText()));
                        gears1++;
                        ballsShot.setText(String.valueOf(gears1));
                    }
                }
            });
            plusAutoBallsShot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (autoBallsShot.getText().toString().equals("")) {
                        autoBallsShot.setText("1");
                    } else {
                        int gears1 = Integer.parseInt(String.valueOf(autoBallsShot.getText()));
                        gears1++;
                        autoBallsShot.setText(String.valueOf(gears1));
                    }
                }
            });
            minusAutoBallsShot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (autoBallsShot.getText().toString().equals("")) {
                    } else {
                        int gears1 = Integer.parseInt(String.valueOf(autoBallsShot.getText()));
                        gears1--;
                        autoBallsShot.setText(String.valueOf(gears1));
                    }
                }
            });


            super.onStart();
        }

    }



