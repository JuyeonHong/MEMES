package com.example.memes;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.memes.R;

public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);

        ActionBar actionBar = getSupportActionBar(); //환경설정 화면의 액션바 생성
        actionBar.setDisplayHomeAsUpEnabled(true); //액션바의 뒤로가기 버튼 생성

        ListPreference alarmMethodListPreference = (ListPreference)findPreference("alarmMethodList");
        final ListPreference popupLocationListPreference = (ListPreference)findPreference("popupLocationList");
        ListPreference alarmPeriodListPreference = (ListPreference)findPreference("alarmPeriodList");
        ListPreference appExeMethodListPreference = (ListPreference)findPreference("appExeMethodList");

        //현재 선택된 Entry로 Summary설정
        alarmMethodListPreference.setSummary(alarmMethodListPreference.getEntry());
        popupLocationListPreference.setSummary(popupLocationListPreference.getEntry());
        alarmPeriodListPreference.setSummary(alarmPeriodListPreference.getEntry());
        appExeMethodListPreference.setSummary(appExeMethodListPreference.getEntry());

        //현재 알림방법이 진동으로 선택되어있으면 팝업위치 선택하는 List Disable
        if(alarmMethodListPreference.getEntry() != null && alarmMethodListPreference.getEntry().equals("진동")){

            popupLocationListPreference.setEnabled(false);
        }
        else{
            popupLocationListPreference.setEnabled(true);
        }

        //모든 리스트에 리스너를 달아서 사용자가 선택한 항목이 바뀔 때 반응하게 한다.
        alarmMethodListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();

                //바뀐 값이 vibration(EntryValue)일 때, 팝업위치 선택하는 List Disable
                if(stringValue.equals("vibration")){
                    popupLocationListPreference.setEnabled(false);
                }
                else{ //아니면 enable
                    popupLocationListPreference.setEnabled(true);
                }

                // ListPreference의 경우 stringValue가 entryValues이기 때문에 바로 Summary를 적용하지 못한다. 따라서 설정한 entries에서 String을 로딩하여 적용한다.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                return true; //변경사항 적용
            }
        });

        popupLocationListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();

                // ListPreference의 경우 stringValue가 entryValues이기 때문에 바로 Summary를 적용하지 못한다. 따라서 설정한 entries에서 String을 로딩하여 적용한다.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                return true; //변경사항 적용?
            }
        });

        alarmPeriodListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();

                // ListPreference의 경우 stringValue가 entryValues이기 때문에 바로 Summary를 적용하지 못한다. 따라서 설정한 entries에서 String을 로딩하여 적용한다.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                return true; //변경사항 적용?
            }
        });

        appExeMethodListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();

                // ListPreference의 경우 stringValue가 entryValues이기 때문에 바로 Summary를 적용하지 못한다. 따라서 설정한 entries에서 String을 로딩하여 적용한다.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                return true; //변경사항 적용?
            }
        });
    }
    //액션바의 뒤로가기 버튼이 눌렸을 때, 뒤로가도록 하는 기능인가?.. AndroidManifest.xml에 바뀔 화면이 등록되어있어야한다.
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
