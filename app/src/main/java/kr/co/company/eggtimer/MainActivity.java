package kr.co.company.eggtimer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    private EditText timerEditText;
    private TextView timerTextView;
    private Button startButton;
    private CountDownTimer countDownTimer;

    private final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    private final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerEditText = findViewById(R.id.timerEditText);
        timerTextView = findViewById(R.id.timer);
        startButton = findViewById(R.id.startButton);
    }

    public void startTimer(View view) {
        String inputTime = timerEditText.getText().toString();
        if (inputTime.isEmpty()) {
            return;
        }

        long timerDurationSeconds = Long.parseLong(inputTime);

        countDownTimer = new CountDownTimer(timerDurationSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                timerTextView.setText("남은 시간: " + secondsRemaining + "초");
            }

            @Override
            public void onFinish() {
                timerTextView.setText("타이머 완료");
                sendNotification();
            }
        };

        countDownTimer.start();
    }

    private void sendNotification() {
        // 알림 클릭 시 열릴 인텐트 생성
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.10000recipe.com/recipe/6850255"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        // Notification 생성 및 설정
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.eggs)
                .setContentTitle("타이머 완료")
                .setContentText("입력한 타이머 시간이 완료되었습니다.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // NotificationManager를 통해 알림 전송
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(notificationManager);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void createNotificationChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Channel description");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
