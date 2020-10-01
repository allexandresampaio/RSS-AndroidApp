package br.ufpe.cin.android.rss;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class PrefsFragment extends PreferenceFragmentCompat {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ListPreference mListPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferencias);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mListPreference = (ListPreference) getPreferenceManager().findPreference("list_servicos");
        editor =  preferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor.putString("rssfeed", newValue.toString());
                editor.apply();
                return true;
            }
        });
        return inflater.inflate(R.layout.activity_main, container, false);
    }
}
