package com.example.aimusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class ReadSongsActivity extends AppCompatActivity {
    //variable decleration
    private String[] itemsAll;
    private ListView mSongsList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_songs);

        mSongsList = findViewById(R.id.songsList);

        appExternalStoragePermission();
    }

    public void appExternalStoragePermission()
    {
        //copied from link https://github.com/Karumi/Dexter
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        displaySongName();

                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response)
                    {

                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
                    {
                        token.continuePermissionRequest();

                    }
                }).check();
    }

    //this method will read all the audio file from mobile storage
    public ArrayList<File> readOnlyAudioFile(File file)
        {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] allFiles = file.listFiles();
        for(File individualFile : allFiles)
        {
            if(individualFile.isDirectory() && !individualFile.isHidden())
            {
                //storing all the files in arraylist
                arrayList.addAll(readOnlyAudioFile(individualFile));
            }
            else
            {
                //seperating file types
                if (individualFile.getName().endsWith(".mp3") ||
                        individualFile.getName().endsWith(".aac") ||
                        individualFile.getName().endsWith(".wav") ||
                        individualFile.getName().endsWith(".wma"))
                {
                    arrayList.add(individualFile);
                }
            }
        }
        return arrayList;
    }

    //method to display songs name
    private void displaySongName()
    {
        final ArrayList<File> audioSongs = readOnlyAudioFile(Environment.getExternalStorageDirectory());
        itemsAll = new String[audioSongs.size()];

        for (int songCounter = 0;songCounter<audioSongs.size(); songCounter++)
        {
            //this will get the name of the song and store it in itemsAll variable.
            itemsAll[songCounter] = audioSongs.get(songCounter).getName();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ReadSongsActivity.this, android.R.layout.simple_list_item_1, itemsAll);
        mSongsList.setAdapter(arrayAdapter);
    }
}
