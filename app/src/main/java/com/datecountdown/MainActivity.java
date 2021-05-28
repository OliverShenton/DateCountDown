package com.datecountdown;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAppOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static com.datecountdown.R.style.ThemeDay;
import static com.datecountdown.R.style.ThemeNight;
import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    CustomAdapter customAdapter;
    ImageView emptyImage;
    ConstraintLayout constraintlayout;
    DrawerLayout drawerLayout;
    LinearLayout mainActivity;

    final private String APP_ID = "app477db134c7db4b1a83";
    final private String ZONE_ID = "vz2a170f6bf594450f99";
    final String consent = "1";

    MyDatabaseHelper mydb;
    ArrayList<String> date_id;
    ArrayList<String> date_title;
    ArrayList<String> date_date;
    ArrayList<String> date_display;
    ArrayList<String> date_toggle;
    ArrayList<String> date_days;
    ArrayList<String> date_time;
    ArrayList<String> date_hour;
    ArrayList<String> date_minute;
    ArrayList<String> date_occasion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Load_Settings();

        //Navigation bar setup
        drawerLayout = findViewById(R.id.navigationDrawer);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Ad Setup
        AdColonyAppOptions options = new AdColonyAppOptions()
                .setPrivacyFrameworkRequired(AdColonyAppOptions.GDPR, true)
                .setPrivacyConsentString(AdColonyAppOptions.GDPR, consent)
                .setUserID("unique_user_id")
                .setKeepScreenOn(true);

        AdColony.configure(MainActivity.this, options, APP_ID, ZONE_ID);

        recyclerView = findViewById(R.id.recylcerView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        emptyImage = findViewById(R.id.emptyImage);
        constraintlayout = findViewById(R.id.constraintlayout);
        mainActivity = findViewById(R.id.mainActivityLayout);

        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, addDate.class);
            startActivity(intent);

        });

        mydb = new MyDatabaseHelper(MainActivity.this);
        date_id = new ArrayList<>();
        date_title = new ArrayList<>();
        date_date = new ArrayList<>();
        date_display = new ArrayList<>();
        date_toggle = new ArrayList<>();
        date_days = new ArrayList<>();
        date_time = new ArrayList<>();
        date_hour = new ArrayList<>();
        date_minute = new ArrayList<>();
        date_occasion = new ArrayList<>();

        storeDataInArray();

        customAdapter = new CustomAdapter(MainActivity.this, this, date_id, date_title, date_date, date_display, date_toggle, date_days, date_time, date_hour, date_minute, date_occasion);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(customAdapter);
        if (customAdapter.getItemCount() == 0) {
            constraintlayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            constraintlayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.setAdapter(adapter);

        recyclerView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);

                        for (int i = 0; i < recyclerView.getChildCount(); i++) {
                            View v = recyclerView.getChildAt(i);
                            v.setAlpha(0.0f);
                            v.animate().alpha(1.0f)
                                    .setDuration(200)
                                    .setStartDelay(i * 100)
                                    .start();
                        }

                        return true;
                    }
                });

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, @Nullable Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if (requestcode == 1
        ) {
            recreate();
        }
        if (requestcode == UPDATE_REQUEST_CODE) {
            Toast.makeText(this, "Update Starting", Toast.LENGTH_SHORT).show();
        }
        if (resultcode != RESULT_OK) {
            Log.d("Update Failed", "onActivityResult: Update failed " + resultcode);
        }
    }

    void storeDataInArray() {
        Cursor cursor = mydb.readAllData();
        if (cursor == null) {
            Toast.makeText(this, "No Dates Available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                date_id.add(cursor.getString(0));
                date_title.add(cursor.getString(1));
                date_toggle.add(cursor.getString(2));
                date_date.add(cursor.getString(3));
                date_display.add(cursor.getString(4));
                date_days.add(cursor.getString(5));
                date_time.add(cursor.getString(6));
                date_hour.add(cursor.getString(7));
                date_minute.add(cursor.getString(8));
                date_occasion.add(cursor.getString(9));
            }
        }
    }

    //Exit App when Pressing Back Button
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            exitFromApp();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void exitFromApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    void confirmDialog() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to Delete All?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
                myDB.deleteAllData();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.create().show();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();
            MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
            myDB.deleteOneRow(date_id.get(position));
            customAdapter.removeItem(position);
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red))
                    .addActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }

    };

    // On Resume
    @Override
    protected void onResume() {
        super.onResume();
        updateCheck();
    }

    private final int UPDATE_REQUEST_CODE = 1612;

    //Update Checker
    private void updateCheck() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, MainActivity.this, UPDATE_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    Log.d("Update", "callInAppUpdate: " + e.getMessage());
                }
            }
        });

    }

    //Navigation Click Menu
    public void ClickMenu(View view) {
        //Open drawer
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        //close draw
        closeDrawer(drawerLayout);
    }

    private void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickRate(View view) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + this.getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    public void ClickShare(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + this.getPackageName());
        startActivity(Intent.createChooser(intent, "Share"));
    }

    public void ClickSupport(View view) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.gofundme.com/f/help-improve-time-piece?utm_source=customer&utm_medium=copy_link&utm_campaign=p_cf+share-flow-1")));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.gofundme.com/f/help-improve-time-piece?utm_source=customer&utm_medium=copy_link&utm_campaign=p_cf+share-flow-1")));
        }
    }


    public void ClickSettings(View view) {
        Intent intent2 = new Intent(MainActivity.this, settings.class);
        startActivity(intent2);
        closeDrawer(drawerLayout);
    }

    public void DeleteAll(View view) {
        confirmDialog();
    }

    public void ClickFeedback(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:ThatAppD3v3lop3r@gmail.com")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Time Piece: Feedback!");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    public void clickBirthday(View view){
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, this, date_id, date_title, date_date, date_display, date_toggle, date_days, date_time, date_hour, date_minute, date_occasion);
        customAdapter.clickBirthday(view);
        closeDrawer(drawerLayout);
    }

    public void clickDay(View view){
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, this, date_id, date_title, date_date, date_display, date_toggle, date_days, date_time, date_hour, date_minute, date_occasion);
        customAdapter.clickDay(view);
        closeDrawer(drawerLayout);
    }

    public void clickWeek(View view){
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, this, date_id, date_title, date_date, date_display, date_toggle, date_days, date_time, date_hour, date_minute, date_occasion);
        customAdapter.clickWeek(view);
        closeDrawer(drawerLayout);
    }

    public void clickMonth(View view){
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, this, date_id, date_title, date_date, date_display, date_toggle, date_days, date_time, date_hour, date_minute, date_occasion);
        customAdapter.clickMonth(view);
        closeDrawer(drawerLayout);
    }

    public void clickAll(View view){
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, this, date_id, date_title, date_date, date_display, date_toggle, date_days, date_time, date_hour, date_minute, date_occasion);
        customAdapter.clickAll(view);
        closeDrawer(drawerLayout);
    }

    public void clickFavourite(View view){
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, this, date_id, date_title, date_date, date_display, date_toggle, date_days, date_time, date_hour, date_minute, date_occasion);
        customAdapter.clickFavourite(view);
        closeDrawer(drawerLayout);
    }

    public void clickHoliday(View view){
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, this, date_id, date_title, date_date, date_display, date_toggle, date_days, date_time, date_hour, date_minute, date_occasion);
        customAdapter.clickHoliday(view);
        closeDrawer(drawerLayout);
    }

    public void clickSpecial(View view){
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, this, date_id, date_title, date_date, date_display, date_toggle, date_days, date_time, date_hour, date_minute, date_occasion);
        customAdapter.clickSpecial(view);
        closeDrawer(drawerLayout);
    }

    public void clickWork(View view){
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, this, date_id, date_title, date_date, date_display, date_toggle, date_days, date_time, date_hour, date_minute, date_occasion);
        customAdapter.clickWork(view);
        closeDrawer(drawerLayout);
    }

    public void clickOther(View view){
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, this, date_id, date_title, date_date, date_display, date_toggle, date_days, date_time, date_hour, date_minute, date_occasion);
        customAdapter.clickOther(view);
        closeDrawer(drawerLayout);
    }

    public void Clicktitle(View view) {
        return;
    }

    private void Load_Settings() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Orientation
        String orientation = sharedPreferences.getString("ORIENTATION", "false");

        if ("1".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);

        } else if ("2".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        } else if ("3".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        String theme = sharedPreferences.getString("THEME", "false");
        if ("1".equals(theme)) {
            getApplicationContext().setTheme(ThemeDay);
        } else if ("2".equals(theme)) {
            getApplicationContext().setTheme(ThemeNight);
        }

    }

}