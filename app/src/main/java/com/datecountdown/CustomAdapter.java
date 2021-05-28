package com.datecountdown;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.RECEIVER_VISIBLE_TO_INSTANT_APPS;
import static com.datecountdown.notificationService.CHANNEL_1_ID;
import static com.datecountdown.notificationService.CHANNEL_2_ID;
import static com.datecountdown.notificationService.CHANNEL_3_ID;
import static com.datecountdown.notificationService.CHANNEL_4_ID;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.myViewHolder>{

    private final Context context;
    Activity activity;
    Calendar end_cal, yearcal, monthcal, weekcal;
    String yearcal2, monthcal2, weekcal2;

    private NotificationManagerCompat notificationManager;

    private final ArrayList date_id;
    private final ArrayList date_title;
    private final ArrayList date_date;
    private final ArrayList date_display;
    private final ArrayList date_toggle;
    private final ArrayList date_days;
    private final ArrayList date_time;
    private final ArrayList date_hour;
    private final ArrayList date_minute;
    private final ArrayList date_occasion;

    CustomAdapter(Activity activity, Context context,
                  ArrayList date_id,
                  ArrayList date_title,
                  ArrayList date_date,
                  ArrayList date_display,
                  ArrayList date_toggle,
                  ArrayList date_days,
                  ArrayList date_time,
                  ArrayList date_hour,
                  ArrayList date_minute,
                  ArrayList date_occasion) {

        this.activity = activity;
        this.context = context;
        this.date_id = date_id;
        this.date_title = date_title;
        this.date_date = date_date;
        this.date_display = date_display;
        this.date_toggle = date_toggle;
        this.date_days = date_days;
        this.date_time = date_time;
        this.date_hour = date_hour;
        this.date_minute = date_minute;
        this.date_occasion = date_occasion;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new myViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        notificationManager = NotificationManagerCompat.from(context);

        //Very important code for some problems
        if (holder.timer != null) {
            holder.timer.cancel();
        }

        try {
            end_cal = Calendar.getInstance();
            String myFormat = "yyyy MM dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
            end_cal.setTime(sdf.parse(String.valueOf(date_date.get(position))));
            end_cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(date_hour.get(position))));
            end_cal.set(Calendar.MINUTE, Integer.parseInt(String.valueOf(date_minute.get(position))));
        } catch (Exception e) {
            Toast.makeText(context, "NAH didnt work man", Toast.LENGTH_SHORT).show();
        }

        Calendar todayCalendar = Calendar.getInstance();
        long one_start_millis = todayCalendar.getTimeInMillis();
        long one_end_millis = end_cal.getTimeInMillis();
        long final_millis = one_end_millis - one_start_millis;

        holder.timer = new CountDownTimer(final_millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long day = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                millisUntilFinished -= TimeUnit.DAYS.toMillis(day);

                long hour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hour);

                long minute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minute);

                long second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                holder.date_days.setText(day + " Days / " + hour + " Hrs / " + minute + " Mins /  " + second + " Secs");

                if (day == 7 && hour == 0 && minute == 0 && second == 0) {

                    String message2 = date_title.get(position) + " is in ONE WEEK!";
                    String title2 = "Not long to go!";

                    Intent activityIntent2 = new Intent(context, MainActivity.class);
                    PendingIntent contentIntent2 = PendingIntent.getActivity(context, 0, activityIntent2, 0);

                    Notification notification = new NotificationCompat.Builder(context, CHANNEL_2_ID)
                            .setSmallIcon(R.drawable.ic_baseline_access_time_24)
                            .setContentTitle(title2)
                            .setContentText(message2)
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(message2))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setContentIntent(contentIntent2)
                            .build();

                    notificationManager.notify(2, notification);
                }

                if (day == 1 && hour == 0 && minute == 0 && second == 0) {

                    String message3 = date_title.get(position) + " is in ONE DAY!";
                    String title3 = "Are you prepared?";

                    Intent activityIntent3 = new Intent(context, MainActivity.class);
                    PendingIntent contentIntent3 = PendingIntent.getActivity(context, 0, activityIntent3, 0);

                    Notification notification1 = new NotificationCompat.Builder(context, CHANNEL_3_ID)
                            .setSmallIcon(R.drawable.ic_baseline_access_time_24)
                            .setContentTitle(title3)
                            .setContentText(message3)
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(message3))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setContentIntent(contentIntent3)
                            .build();

                    notificationManager.notify(3, notification1);
                }

                if (day == 0 && hour == 1 && minute == 0 && second == 0) {

                    String message4 = date_title.get(position) + " is in ONE HOUR!";
                    String title4 = "Times almost up!";

                    Intent activityIntent4 = new Intent(context, MainActivity.class);
                    PendingIntent contentIntent4 = PendingIntent.getActivity(context, 0, activityIntent4, 0);

                    Notification notification2 = new NotificationCompat.Builder(context, CHANNEL_4_ID)
                            .setSmallIcon(R.drawable.ic_baseline_access_time_24)
                            .setContentTitle(title4)
                            .setContentText(message4)
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(message4))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setContentIntent(contentIntent4)
                            .build();

                    notificationManager.notify(4, notification2);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFinish() {

                if (date_toggle.get(position).toString().equals("One Time Only")) {

                    setFadeAnimation(holder.itemView);

                    String message = date_title.get(position) + " has ended!";
                    String title = "Do not miss your event!";

                    Intent activityIntent = new Intent(context, MainActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

                    Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                            .setSmallIcon(R.drawable.ic_baseline_access_time_24)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(message))
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setContentIntent(contentIntent)
                            .build();

                    notificationManager.notify(1, notification);


                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                            myDB.deleteOneRow(String.valueOf(date_id.get(position)));
                            ((MainActivity) context).recreate();
                        }
                    }, 2000);

                }

                if (date_toggle.get(position).toString().equals("Repeat Every Month")) {

                    setFadeAnimation(holder.itemView);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                            myDB.deleteOneRow(String.valueOf(date_id.get(position)));
                            ((MainActivity) context).recreate();
                        }
                    }, 2000);

                    String message = date_title.get(position) + " has ended!";
                    String title = "Do not miss your event!";

                    Intent activityIntent = new Intent(context, MainActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

                    Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                            .setSmallIcon(R.drawable.ic_baseline_access_time_24)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(message))
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setContentIntent(contentIntent)
                            .build();

                    notificationManager.notify(1, notification);


                    //Creating new date

                    try {
                        monthcal = Calendar.getInstance();

                        String myFormat = "dd MMMM yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                        monthcal.add(Calendar.MONTH, +1);
                        monthcal2 = (sdf.format(monthcal.getTime()));
//                        yearcal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(date_hour.get(position))));
//                        yearcal.set(Calendar.MINUTE, Integer.parseInt(String.valueOf(date_minute.get(position))));


                    } catch (Exception e) {
                        Toast.makeText(context, "Wrong answer bro", Toast.LENGTH_SHORT).show();
                    }

                    Calendar todayCalendar2 = Calendar.getInstance();
                    long year_start_millis = todayCalendar2.getTimeInMillis();
                    long year_end_millis = monthcal.getTimeInMillis();
                    long final_millis1 = year_end_millis - year_start_millis;
                    long daysleft = TimeUnit.MILLISECONDS.toDays(final_millis1);

                    MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                    myDB.addDateFinish(date_title.get(position).toString(), //works
                            monthcal2,
                            date_display.get(position).toString(),//nope
                            date_toggle.get(position).toString(), //works
                            Math.toIntExact((daysleft)), //nope
                            date_time.get(position).toString(), //nope
                            (Integer.parseInt((String) date_hour.get(position))), //works
                            (Integer.parseInt((String) date_minute.get(position))), //works
                            date_occasion.get(position).toString());//works


                }

                if (date_toggle.get(position).toString().equals("Repeat Every Week")) {

                    setFadeAnimation(holder.itemView);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                            myDB.deleteOneRow(String.valueOf(date_id.get(position)));
                            ((MainActivity) context).recreate();
                        }
                    }, 2000);


                        String message = date_title.get(position) + " has ended!";
                        String title = "Do not miss your event!";

                        Intent activityIntent = new Intent(context, MainActivity.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

                        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                                .setSmallIcon(R.drawable.ic_baseline_access_time_24)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(message))
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .setContentIntent(contentIntent)
                                .build();

                        notificationManager.notify(1, notification);


                    //Creating new date

                    try {
                        weekcal = Calendar.getInstance();

                        String myFormat = "dd MMMM yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                        weekcal.add(Calendar.WEEK_OF_YEAR, +1);
                        weekcal2 = (sdf.format(weekcal.getTime()));
//                        yearcal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(date_hour.get(position))));
//                        yearcal.set(Calendar.MINUTE, Integer.parseInt(String.valueOf(date_minute.get(position))));


                    } catch (Exception e) {
                        Toast.makeText(context, "Wrong answer bro", Toast.LENGTH_SHORT).show();
                    }

                    Calendar todayCalendar2 = Calendar.getInstance();
                    long year_start_millis = todayCalendar2.getTimeInMillis();
                    long year_end_millis = weekcal.getTimeInMillis();
                    long final_millis1 = year_end_millis - year_start_millis;
                    long daysleft = TimeUnit.MILLISECONDS.toDays(final_millis1);

                    MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                    myDB.addDateFinish(date_title.get(position).toString(), //works
                            monthcal2,
                            date_display.get(position).toString(),//nope
                            date_toggle.get(position).toString(), //works
                            Math.toIntExact((daysleft)), //nope
                            date_time.get(position).toString(), //nope
                            (Integer.parseInt((String) date_hour.get(position))), //works
                            (Integer.parseInt((String) date_minute.get(position))), //works
                            date_occasion.get(position).toString());//works


                }

                if (date_toggle.get(position).toString().equals("Repeat Every Year")) {

                    setFadeAnimation(holder.itemView);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                            myDB.deleteOneRow(String.valueOf(date_id.get(position)));
                            ((MainActivity) context).recreate();
                        }
                    }, 2000);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    Boolean onfinish = sharedPreferences.getBoolean("notificationonfinish",true);
                    if (onfinish) {

                        String message = date_title.get(position) + " has ended!";
                        String title = "Do not miss your event!";

                        Intent activityIntent = new Intent(context, MainActivity.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

                        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                                .setSmallIcon(R.drawable.ic_baseline_access_time_24)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(message))
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .setContentIntent(contentIntent)
                                .build();

                        notificationManager.notify(1, notification);
                    } else {
                        return;
                    }


                    //Creating new date

                    try {
                        yearcal = Calendar.getInstance();

                        String myFormat = "dd MMMM yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                        yearcal.add(Calendar.YEAR, +1);
                        yearcal2 = (sdf.format(yearcal.getTime()));
//                        yearcal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(date_hour.get(position))));
//                        yearcal.set(Calendar.MINUTE, Integer.parseInt(String.valueOf(date_minute.get(position))));


                    } catch (Exception e) {
                        Toast.makeText(context, "Wrong answer bro", Toast.LENGTH_SHORT).show();
                    }

                    Calendar todayCalendar2 = Calendar.getInstance();
                    long year_start_millis = todayCalendar2.getTimeInMillis();
                    long year_end_millis = yearcal.getTimeInMillis();
                    long final_millis1 = year_end_millis - year_start_millis;
                    long daysleft = TimeUnit.MILLISECONDS.toDays(final_millis1);

                    MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                    myDB.addDateFinish(date_title.get(position).toString(), //works
                            monthcal2,
                            date_display.get(position).toString(),//nope
                            date_toggle.get(position).toString(), //works
                            Math.toIntExact((daysleft)), //nope
                            date_time.get(position).toString(), //nope
                            (Integer.parseInt((String) date_hour.get(position))), //works
                            (Integer.parseInt((String) date_minute.get(position))), //works
                            date_occasion.get(position).toString());//works

                }

            }
        }.start();

        holder.date_id.setText(String.valueOf(date_id.get(position)));
        holder.date_title.setText(String.valueOf(date_title.get(position)));
        holder.date_date.setText(String.valueOf(date_display.get(position)));
        holder.date_toggle.setText(String.valueOf(date_toggle.get(position)));
        holder.date_days.setText(String.valueOf(date_days.get(position)));
        holder.date_time.setText(String.valueOf(date_time.get(position)));
        holder.date_hour.setText(String.valueOf(date_hour.get(position)));
        holder.date_minute.setText(String.valueOf(date_minute.get(position)));
        holder.date_occasion.setText(String.valueOf(date_occasion.get(position)));

        int colourRED = ContextCompat.getColor(context, R.color.red);
        int colourGREEN = ContextCompat.getColor(context, R.color.green);
        int colourBLUE = ContextCompat.getColor(context, R.color.blue);
        int colourPURPLE = ContextCompat.getColor(context, R.color.purple_500);
        int colourYELLOW = ContextCompat.getColor(context, R.color.yellow);
        int colourORANGE = ContextCompat.getColor(context, R.color.orange);

        if (date_occasion.get(position).equals("Birthday")){
            holder.date_colour.setBackgroundColor(colourRED);
        } else if (date_occasion.get(position).equals("Holiday")){
            holder.date_colour.setBackgroundColor(colourORANGE);
        } else if (date_occasion.get(position).equals("Special Occasion")){
            holder.date_colour.setBackgroundColor(colourPURPLE);
        }else if (date_occasion.get(position).equals("Work")){
            holder.date_colour.setBackgroundColor(colourYELLOW);
        }else if (date_occasion.get(position).equals("Other")){
            holder.date_colour.setBackgroundColor(colourBLUE);
        } else if (date_occasion.get(position).equals("Payment")){
            holder.date_colour.setBackgroundColor(colourGREEN);
        }

        holder.cardLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, update_date.class);
                intent.putExtra("id", String.valueOf(date_id.get(position)));
                intent.putExtra("title", String.valueOf(date_title.get(position)));
                intent.putExtra("date", String.valueOf(date_date.get(position)));
                intent.putExtra("repeat", String.valueOf(date_toggle.get(position)));
                intent.putExtra("days", String.valueOf(date_days.get(position)));
                intent.putExtra("time", String.valueOf(date_time.get(position)));
                intent.putExtra("hour", String.valueOf(date_hour.get(position)));
                intent.putExtra("minute", String.valueOf(date_minute.get(position)));
                intent.putExtra("occasion", String.valueOf(date_occasion.get(position)));
                activity.startActivityForResult(intent, 1);

            }

        });

    }

    @Override
    public int getItemCount() {
        return date_id.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView date_id, date_title, date_date, date_toggle, date_days, date_time, date_hour, date_minute, date_colour, date_occasion;
        LinearLayout cardLinearLayout;
        CountDownTimer timer;
        ImageButton favourite;

        public myViewHolder(@NonNull View itemView) {

            super(itemView);
            date_id = itemView.findViewById(R.id.date_id_text);
            date_title = itemView.findViewById(R.id.date_title_text);
            date_date = itemView.findViewById(R.id.date_date_text);
            date_toggle = itemView.findViewById(R.id.date_toggle_text);
            date_days = itemView.findViewById(R.id.date_day_text);
            date_time = itemView.findViewById(R.id.date_time_text);
            date_hour = itemView.findViewById(R.id.date_hour_text);
            date_minute = itemView.findViewById(R.id.date_minute_text);
            date_colour = itemView.findViewById(R.id.date_colour_colour);
            favourite = itemView.findViewById(R.id.favouriteButton);
            cardLinearLayout = itemView.findViewById(R.id.cardViewLinearLayout);
            date_occasion = itemView.findViewById(R.id.date_occasion);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            //Text Size
            String textsize = sharedPreferences.getString("TEXTSIZE", "false");

            int small = 10;
            int medium = 16;
            int large = 22;

            if ("1".equals(textsize)) {
                date_id.setTextSize(small);
                date_title.setTextSize(12);
                date_date.setTextSize(small);
                date_toggle.setTextSize(small);
                date_days.setTextSize(small);
                date_time.setTextSize(small);
                date_hour.setTextSize(small);
                date_minute.setTextSize(small);
            } else if ("2".equals(textsize)) {
                date_id.setTextSize(medium);
                date_title.setTextSize(18);
                date_date.setTextSize(medium);
                date_toggle.setTextSize(medium);
                date_days.setTextSize(medium);
                date_time.setTextSize(medium);
                date_hour.setTextSize(medium);
                date_minute.setTextSize(medium);
            } else if ("3".equals(textsize)) {
                date_id.setTextSize(large);
                date_title.setTextSize(24);
                date_date.setTextSize(large);
                date_toggle.setTextSize(large);
                date_days.setTextSize(large);
                date_time.setTextSize(large);
                date_hour.setTextSize(large);
                date_minute.setTextSize(large);
            }

        }

    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(2500);
        view.startAnimation(anim);

    }

    public void removeItem(int position) {
        date_id.remove(position);
        notifyItemRemoved(position);

    }

    public void clickBirthday(View view) {
        Toast.makeText(context, "SHOWING BIRTHDAY...", Toast.LENGTH_SHORT).show();
    }

    public void clickDay(View view) {
        Toast.makeText(context, "SHOWING DAY...", Toast.LENGTH_SHORT).show();
    }

    public void clickWeek(View view) {
        Toast.makeText(context, "SHOWING WEEK...", Toast.LENGTH_SHORT).show();
    }

    public void clickMonth(View view) {
        Toast.makeText(context, "SHOWING MONTH...", Toast.LENGTH_SHORT).show();
    }

    public void clickAll(View view) {
        Toast.makeText(context, "SHOWING ALL...", Toast.LENGTH_SHORT).show();
    }

    public void clickHoliday(View view) {
        Toast.makeText(context, "SHOWING HOLIDAY...", Toast.LENGTH_SHORT).show();
    }

    public void clickSpecial(View view) {
        Toast.makeText(context, "SHOWING SPECIAL OCCASION...", Toast.LENGTH_SHORT).show();
    }

    public void clickWork(View view) {
        Toast.makeText(context, "SHOWING WORK...", Toast.LENGTH_SHORT).show();
    }

    public void clickOther(View view) {
        Toast.makeText(context, "SHOWING OTHER...", Toast.LENGTH_SHORT).show();
    }

    public void clickFavourite(View view) {
        Toast.makeText(context, "SHOWING FAVOURITE...", Toast.LENGTH_SHORT).show();
    }
}