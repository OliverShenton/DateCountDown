package com.datecountdown;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyUserMetadata;
import com.adcolony.sdk.AdColonyZone;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import petrov.kristiyan.colorpicker.ColorPicker;

public class update_date extends AppCompatActivity {

    Button updateButton;
    EditText nameEditText, dateEditText, yearmonthinput, timeEditText, datemean;
    TextView datetostring, daysBetween, hourTextView, minuteTextView;
    Calendar myCalendar, todayCalendar;
    ToggleButton repeatToggle, yearlyToggle, monthlyToggle, weeklyToggle;
    LinearLayout lineartoggle, toggleLayout5;
    String displaydate;
    CountDownTimer cdt;
    String id;
    String title;
    String date;
    String repeat;
    String days;
    String display;
    String time;
    String hour;
    String minute;
    String occasion;
    TextInputLayout itemTitleLayout1, itemTitleLayout2, dateMeaning;
    Spinner dateMeaningSpinner;

    final private String ZONE_ID = "vz2a170f6bf594450f99";
    final private String TAG = "AdColonyDemo";

    private AdColonyInterstitial ad;
    private AdColonyInterstitialListener listener;
    private AdColonyAdOptions adOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_date);

        AdColonyUserMetadata metadata = new AdColonyUserMetadata()
                .setUserAge(26)
                .setUserEducation(AdColonyUserMetadata.USER_EDUCATION_BACHELORS_DEGREE)
                .setUserGender(AdColonyUserMetadata.USER_MALE);

        adOptions = new AdColonyAdOptions().setUserMetadata(metadata);

        listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                // Ad passed back in request filled callback, ad can now be shown
                update_date.this.ad = ad;
                Log.d(TAG, "onRequestFilled");
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                // Ad request was not filled
                Log.d(TAG, "onRequestNotFilled");
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                // Ad opened, reset UI to reflect state change
                Log.d(TAG, "onOpened");
            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                // Request a new ad if ad is expiring
                AdColony.requestInterstitial(ZONE_ID, this, adOptions);
                Log.d(TAG, "onExpiring");
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        // It's somewhat arbitrary when your ad request should be made. Here we are simply making
        // a request if there is no valid ad available onResume, but really this can be done at any
        // reasonable time before you plan on showing an ad.
        if (ad == null || ad.isExpired()) {
            // Optionally update location info in the ad options for each request:
            // LocationManager locationManager =
            //     (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Location location =
            //     locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // adOptions.setUserMetadata(adOptions.getUserMetadata().setUserLocation(location));
            AdColony.requestInterstitial(ZONE_ID, listener, adOptions);
        }

        slideInTransition();

        updateButton = findViewById(R.id.updateButton);
        nameEditText = findViewById(R.id.nameInput2);
        dateEditText = findViewById(R.id.dateInput2);
        repeatToggle = findViewById(R.id.repeatToggle2);
        yearlyToggle = findViewById(R.id.yearToggle2);
        timeEditText = findViewById(R.id.timeInput2);
        monthlyToggle = findViewById(R.id.monthToggle2);
        lineartoggle = findViewById(R.id.toggleLayout2);
        yearmonthinput = findViewById(R.id.yearmonthInput2);
        datetostring = findViewById(R.id.dateToString2);
        myCalendar = Calendar.getInstance();
        todayCalendar = Calendar.getInstance();
        daysBetween = findViewById(R.id.daysBetween2);
        hourTextView = findViewById(R.id.hour2);
        minuteTextView = findViewById(R.id.minute2);
        itemTitleLayout1 = findViewById(R.id.itemTitleLayout1);
        itemTitleLayout2 = findViewById(R.id.itemTitleLayout2);
        toggleLayout5 = findViewById(R.id.toggleLayout5);
        weeklyToggle = findViewById(R.id.weektoggle2);
        dateMeaning = findViewById(R.id.dateMeaning);
        dateMeaningSpinner = findViewById(R.id.SpinnerFireGrowth);
        datemean = findViewById(R.id.date);

        datemean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateMeaningSpinner.performClick();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.daterepresentation, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateMeaningSpinner.setAdapter(adapter);

        dateMeaningSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    ((TextView) view).setTextColor(Color.RED);
//                    ((TextView) view).setGravity(Gravity.CENTER);

                datemean.setText((CharSequence) parent.getAdapter().getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        itemTitleLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateEditText.performClick();
            }
        });

        itemTitleLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeEditText.performClick();
            }
        });

        Calendar todayCalendar = Calendar.getInstance();
        Date today1Calendar = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        String formattedDate = df.format(today1Calendar);

        datetostring.setText(formattedDate);

        //Show & Hide Toggle Bar when Repeat Date toggle selected
        repeatToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((ToggleButton) v).isChecked();
                if (repeatToggle.isChecked()) {
                    toggleLayout5.setVisibility(View.VISIBLE);
                    if (yearmonthinput.getText().toString().equals("Repeat Every Year")) {
                        yearlyToggle.setChecked(true);
                        monthlyToggle.setChecked(false);
                        weeklyToggle.setChecked(false);
                    } else {
                        return;
                    }
                    if (yearmonthinput.getText().toString().equals("Repeat Every Week")) {
                        yearlyToggle.setChecked(false);
                        monthlyToggle.setChecked(false);
                        weeklyToggle.setChecked(true);
                    } else {
                        return;
                    }
                    if (yearmonthinput.getText().toString().equals("Repeat Every Month")) {
                        yearlyToggle.setChecked(false);
                        monthlyToggle.setChecked(true);
                        weeklyToggle.setChecked(false);
                    } else {
                        return;
                    }
                } else {
                    toggleLayout5.setVisibility(View.GONE);
                    yearlyToggle.setChecked(false);
                    monthlyToggle.setChecked(false);
                    weeklyToggle.setChecked(false);
                    yearmonthinput.setText("One Time Only");
                }
            }
        });

        //Turn Toggles On & Off when selected
        yearlyToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((ToggleButton) v).isChecked();
                if (yearlyToggle.isChecked()) {
                    monthlyToggle.setChecked(false);
                    weeklyToggle.setChecked(false);
                    yearmonthinput.setText("Repeat Every Year");
                } else {
                    return;
                }
            }
        });

        monthlyToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((ToggleButton) v).isChecked();
                if (monthlyToggle.isChecked()) {
                    yearlyToggle.setChecked(false);
                    weeklyToggle.setChecked(false);
                    yearmonthinput.setText("Repeat Every Month");
                } else {
                    return;
                }
            }
        });

        weeklyToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((ToggleButton) v).isChecked();
                if (weeklyToggle.isChecked()){
                    yearlyToggle.setChecked(false);
                    monthlyToggle.setChecked(false);
                    yearmonthinput.setText("Repeat Every Week");
                } else {
                    return;
                }
            }
        });


        //Generate A Date Picker
        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCalendar.set(Calendar.HOUR_OF_DAY, 0);
                myCalendar.set(Calendar.MINUTE, 0);
                myCalendar.set(Calendar.SECOND, 0);

                String myFormat = "dd MMMM yyyy"; //In which you need put here
                String myFormat2 = "yyyy MM dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat2, Locale.getDefault());

                displaydate = sdf.format(myCalendar.getTime());
                dateEditText.setText(sdf1.format(myCalendar.getTime()));

                long start_Millis = todayCalendar.getTimeInMillis();
                long end_millis = myCalendar.getTimeInMillis();
                long total_millis = (end_millis - start_Millis);

                cdt = new CountDownTimer(total_millis, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        cdt.cancel();
                        long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                        millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

                        daysBetween.setText("" + days);

                    }

                    @Override
                    public void onFinish() {

                        Toast.makeText(update_date.this, "Done", Toast.LENGTH_SHORT).show();

                    }
                };

                cdt.start();

            }
        };

        dateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickDialog = new DatePickerDialog(update_date.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickDialog.getDatePicker().setMinDate(System.currentTimeMillis()+24*60*60*1000); datePickDialog.show();
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                TimePickerDialog picker = new TimePickerDialog(update_date.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                timeEditText.setText( "(" +String.format("%02d", sHour) + ":"+ String.format("%02d", sMinute) + ")");
                                hourTextView.setText("" + String.format("%02d", sHour));
                                minuteTextView.setText("" + String.format("%02d", sMinute));
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        getAndSetIntentData();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(update_date.this);
                title = nameEditText.getText().toString().trim();
                date = dateEditText.getText().toString();
                repeat = yearmonthinput.getText().toString().trim();
                days = daysBetween.getText().toString().trim();
                time = timeEditText.getText().toString().trim();
                hour = hourTextView.getText().toString().trim();
                minute = minuteTextView.getText().toString().trim();
                occasion =  datemean.getText().toString().trim();
                myDB.updateData(id, title, date, display, repeat, Integer.parseInt(days), time, Integer.parseInt(hour), Integer.parseInt(minute), occasion);
                try {
                    ad.show();
                } catch (Exception e) {
                    Log.d (TAG, "No Ads");
                }
                Intent intent = new Intent(update_date.this, MainActivity.class);
                slideOutTransition();
                startActivity(intent);

            }
        });

    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("date") && getIntent().hasExtra("repeat") && getIntent().hasExtra("days")
                && getIntent().hasExtra("hour") && getIntent().hasExtra("minute") && getIntent().hasExtra("time") && getIntent().hasExtra("occasion")) {

            //Getting Date from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            date = getIntent().getStringExtra("date");
            display = getIntent().getStringExtra("datedisplay");
            repeat = getIntent().getStringExtra("repeat");
            time = getIntent().getStringExtra("time");
            days = getIntent().getStringExtra("days");
            hour = getIntent().getStringExtra("hour");
            minute = getIntent().getStringExtra("minute");
            occasion = getIntent().getStringExtra("occasion");

            //Setting Intent Data
            nameEditText.setText(title);
            dateEditText.setText(date);
            timeEditText.setText(time);
            yearmonthinput.setText(repeat);
            daysBetween.setText(days);
            hourTextView.setText(hour);
            minuteTextView.setText(minute);
            datemean.setText(occasion);
            
        } else {
            Toast.makeText(this, "No Date Set", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        Toast.makeText(update_date.this, "Update Cancelled", Toast.LENGTH_SHORT).show();
        slideOutTransition();
        return true;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(update_date.this, MainActivity.class));
        Toast.makeText(update_date.this, "Update Cancelled", Toast.LENGTH_SHORT).show();
        slideOutTransition();
        finish();
    }

    protected void slideInTransition() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_fade_back);
    }

    protected void slideOutTransition() {
        overridePendingTransition(R.anim.slide_fade_forward, R.anim.slide_out_right);
    }

    public void ClickBackupdate(View view){
        Intent intent2 = new Intent(update_date.this, MainActivity.class);
        startActivity(intent2);
        Toast.makeText(update_date.this, "Update Cancelled", Toast.LENGTH_SHORT).show();
    }

}