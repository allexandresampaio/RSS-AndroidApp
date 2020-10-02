package br.ufpe.cin.android.rss;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

public class PrefsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //este frfagmento apenas busca o layout definido em preferencias.xml para ser exibido na tela de configurações
        addPreferencesFromResource(R.xml.preferencias);
    }
}
