package android.csulb.edu.arttherapy;

/**
 * Created by Aditi on 3/20/2017.
 */
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.media.MediaPlayer;

public class Eraser extends IntentService {

    public Eraser() {
        super("HelloIntentService");
    }

    protected void onHandleIntent(Intent intent) {

        MediaPlayer mp = MediaPlayer.create(Eraser.this, R.raw.eraser);
        mp.start();
        while(mp.isPlaying())
        {
        }
        mp.reset();
        mp.release();

    }

}