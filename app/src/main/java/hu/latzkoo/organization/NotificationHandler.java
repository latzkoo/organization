package hu.latzkoo.organization;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {

    private static final String CHANNEL_ID = "default";
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager manager;
    private Context context;

    public NotificationHandler(Context context) {
        this.context = context;
        this.manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Organization", NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);

            this.manager.createNotificationChannel(channel);
        }
    }

    public void send(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Organization")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_hospital);

        this.manager.notify(NOTIFICATION_ID, builder.build());
    }

}
