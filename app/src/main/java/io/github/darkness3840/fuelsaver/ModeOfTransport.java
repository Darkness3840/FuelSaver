package io.github.darkness3840.fuelsaver;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

import static java.lang.Character.getType;

public class ModeOfTransport extends IntentService{
    public static final String ACTION = "com.hrupin.activityrecognitionwithgoogle.ACTIVITY_RECOGNITION_DATA";
    private String TAG = this.getClass().getSimpleName();

    private static int mode = DetectedActivity.STILL;

    public ModeOfTransport() {
        super("My Activity Recognition Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        /*if(ActivityRecognitionResult.hasResult(intent)){
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            Log.i(TAG, getType(result.getMostProbableActivity().getType()) +"\t" + result.getMostProbableActivity().getConfidence());
            Intent i = new Intent(ACTION);
            i.putExtra("Activity", getType(result.getMostProbableActivity().getType()) );
            i.putExtra("Confidence", result.getMostProbableActivity().getConfidence());
            sendBroadcast(i);

        }*/
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities( result.getProbableActivities() );
        }
    }
    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        int maxConfinence = 0;
        mode = -1;
        for(int i = probableActivities.size()-1; i >=0; i--) {
            DetectedActivity activity = probableActivities.get(i);
            if (activity.getConfidence() > maxConfinence && activity.getConfidence() > 20 && activity.getType() != DetectedActivity.TILTING && activity.getType() != DetectedActivity.ON_FOOT && activity.getType() != DetectedActivity.UNKNOWN) {
                mode = activity.getType();
                maxConfinence = activity.getConfidence();
            }
        }
        if (mode == -1) mode = DetectedActivity.UNKNOWN;
    }

    public static String getType(int type){
        if(type == DetectedActivity.IN_VEHICLE) {
            return "In Vehicle";
        }else if(type == DetectedActivity.ON_BICYCLE) {
            return "On Bicycle";
        }else if(type == DetectedActivity.ON_FOOT) {
            return "On Foot";
        }else if(type == DetectedActivity.RUNNING) {
            return "Running";
        }else if(type == DetectedActivity.STILL) {
            return "None";
        }else if(type == DetectedActivity.TILTING) {
            return "Tilting";
        }else if(type == DetectedActivity.UNKNOWN) {
            return "Unknown";
        }else if(type == DetectedActivity.WALKING) {
            return "Walking";
        }else {
            return "...";
        }
    }
    public static int getMode () {
        return mode;
    }
}