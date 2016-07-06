package ar.edu.unrn.lia.capacitacionhorizonte2.preference;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import ar.edu.unrn.lia.capacitacionhorizonte2.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingsActivity extends  PreferenceActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();

        //checkValues();
    }

    public static class SettingFragment extends PreferenceFragment
    {

        @BindView(R.id.user_name)
        EditTextPreference userName;

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.main);



        }
    }

    private void checkValues()
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String strUserName = sharedPrefs.getString("user_name", "NA");
        boolean bAppUpdates = sharedPrefs.getBoolean("alert_email",false);
        String downloadType = sharedPrefs.getString("alert_email_address","1");

        String msg = "Cur Values: ";
        msg += "\n userName = " + strUserName;
        msg += "\n bAppUpdates = " + bAppUpdates;
        msg += "\n downloadType = " + downloadType;

        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

}