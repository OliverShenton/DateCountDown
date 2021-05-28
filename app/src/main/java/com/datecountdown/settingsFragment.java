package com.datecountdown;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import static com.datecountdown.R.style.ThemeDay;
import static com.datecountdown.R.style.ThemeNight;

public class settingsFragment extends PreferenceFragmentCompat{

    private void LoadSettings() {

        //SharedPreference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());

        //Notifications
//        CheckBoxPreference notificationpref = (CheckBoxPreference) findPreference("sendnotifications");
//        notificationpref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue)
//            {
//                boolean checked = Boolean.valueOf(newValue.toString());
//
//                //set your shared preference value equal to checked
//
//                return true;
//            }
//        });

//        Theme
        ListPreference themepreference = (ListPreference) findPreference("THEME");
        String theme = sharedPreferences.getString("THEME", "false");
        if ("1".equals(theme)) {
            getActivity().setTheme(ThemeDay);
            themepreference.setSummary(themepreference.getEntry());
        } else if ("2".equals(theme)) {
            getActivity().setTheme(ThemeNight);
            themepreference.setSummary(themepreference.getEntry());
        }

        themepreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object object) {
                String items = (String) object;
                if (preference.getKey().equals("THEME")) {
                    switch (items) {
                        case "1":
                            getActivity().setTheme(ThemeDay);
                            break;

                        case "2":
                            getActivity().setTheme(ThemeNight);
                            break;
                    }

                    ListPreference themeword = (ListPreference) preference;
                    themeword.setSummary(themeword.getEntries()[themeword.findIndexOfValue(items)]);

                }
                return true;
            }
        });


        //Text Size
        ListPreference textsizepreference = (ListPreference) findPreference("TEXTSIZE");
        String textsize = sharedPreferences.getString("TEXTSIZE", "false");
        if ("1".equals(textsize)) {
            textsizepreference.setSummary(textsizepreference.getEntry());

        } else if ("2".equals(textsize)) {
            textsizepreference.setSummary(textsizepreference.getEntry());

        } else if ("3".equals(textsize)) {
            textsizepreference.setSummary(textsizepreference.getEntry());
        }

        textsizepreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object object) {
                String items = (String) object;
                if (preference.getKey().equals("TEXTSIZE")) {
                    switch (items) {
                        case "1":
                            break;

                        case "2":
                            break;

                        case "3":
                            break;
                    }

                    ListPreference textsizeword = (ListPreference) preference;
                    textsizeword.setSummary(textsizeword.getEntries()[textsizeword.findIndexOfValue(items)]);

                }

                return true;
            }
        });

        //Orientation
        ListPreference orientationpreference = (ListPreference) findPreference("ORIENTATION");

        String orientation = sharedPreferences.getString("ORIENTATION", "false");

        if ("1".equals(orientation)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
            orientationpreference.setSummary(orientationpreference.getEntry());

        } else if ("2".equals(orientation)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            orientationpreference.setSummary(orientationpreference.getEntry());

        } else if ("3".equals(orientation)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            orientationpreference.setSummary(orientationpreference.getEntry());
        }

        orientationpreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object object) {
                String items = (String) object;
                if (preference.getKey().equals("ORIENTATION")) {
                    switch (items) {
                        case "1":
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
                            break;

                        case "2":
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            break;

                        case "3":
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;
                    }

                    ListPreference orientationword = (ListPreference) preference;
                    orientationword.setSummary(orientationword.getEntries()[orientationword.findIndexOfValue(items)]);

                }

                return true;
            }
        });
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.root_preferences);

    }


    @Override
    public void onResume() {
        super.onResume();
        LoadSettings();
    }
}
