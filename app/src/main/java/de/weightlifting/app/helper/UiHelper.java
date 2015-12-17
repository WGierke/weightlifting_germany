package de.weightlifting.app.helper;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import de.weightlifting.app.R;
import de.weightlifting.app.SplashActivity;

public class UiHelper {

    public static void showToast(String message, Context context) {
        try {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void fadeInView(final View v, final float step) {
        final Handler handler = new Handler();
        (new Thread() {
            @Override
            public void run() {
                for (int i = 0; i <= 1000; i++) {
                    final float new_opacity = v.getAlpha() + step;
                    if (new_opacity > 1) {
                        return;
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                v.setAlpha(new_opacity);
                            } catch (Exception ex) {
                            }
                        }
                    });
                    try {
                        sleep(5);
                    } catch (Exception ex) {
                        break;
                    }
                }
            }
        }).start();
    }

    public static void fadeOutView(final View v, final float step) {
        final Handler handler = new Handler();
        (new Thread() {
            @Override
            public void run() {
                for (int i = 0; i <= 1000; i++) {
                    final float new_opacity = v.getAlpha() - step;
                    if (new_opacity < 0) {
                        return;
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                v.setAlpha(new_opacity);
                            } catch (Exception ex) {
                            }
                        }
                    });
                    try {
                        sleep(5);
                    } catch (Exception ex) {
                        break;
                    }
                }
            }
        }).start();
    }

    public static void colorFade(View view, Resources res) {
        ObjectAnimator colorFade = ObjectAnimator.ofObject(view, "backgroundColor", new ArgbEvaluator(), res.getColor(R.color.counter_text_bg), 0xffccc);
        colorFade.setDuration(3000);
        colorFade.start();
    }

    /**
     * Show a notification
     *
     * @param title          Ttile of the notification
     * @param message        Message, lines are seperated by a pipe
     * @param description    Description of the notification
     * @param notificationId Identifier of the notification
     */
    public static void showNotification(String title, String message, String description, int notificationId, Context context) {

        Intent resultIntent = new Intent(context, SplashActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resultIntent.putExtra("fragmentId", notificationId);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder normal = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationCompat.InboxStyle big = new NotificationCompat.InboxStyle(normal);
        big.setSummaryText(description);

        String[] parts = message.split("\\|");
        for (String part : parts) {
            big.addLine(part);
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationId, big.build());
    }
}
