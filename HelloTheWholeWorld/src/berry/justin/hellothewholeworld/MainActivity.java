package berry.justin.hellothewholeworld;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import berry.justin.hellothewholeworld.services.FacebookForwarder;
import berry.justin.hellothewholeworld.services.Forwarder;
import berry.justin.hellothewholeworld.services.twitter.TwitterForwarder;
import berry.justin.hellothewholeworld.services.yammer.YammerForwarder;

public class MainActivity extends Activity {

    private SparseArray<Forwarder> forwarders = new SparseArray<Forwarder>();
    private EditText statusField;

    private static final String ENABLED_SUFFIX = "_enabled";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusField = (EditText) findViewById(R.id.statusField);

        forwarders.put(R.id.twitterCheckbox, new TwitterForwarder(
                getApplicationContext()));
        forwarders.put(R.id.facebookCheckbox, new FacebookForwarder(
                getApplicationContext()));
        forwarders.put(R.id.yammerCheckbox, new YammerForwarder(
                getApplicationContext()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveCheckboxState();
    }

    private void saveCheckboxState() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 0; i < forwarders.size(); ++i) {
            Forwarder forwarder = forwarders.valueAt(i);
            editor.putBoolean(forwarder.getSettingPrefix() + ENABLED_SUFFIX,
                    forwarder.isEnabled());
        }
        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(
                Preferences.APPLICATION_PREFERENCES, Context.MODE_PRIVATE);

        boolean twitterChecked = sharedPreferences.getBoolean("twitter"
                + ENABLED_SUFFIX, false);
        ((CheckBox) findViewById(R.id.twitterCheckbox))
                .setChecked(twitterChecked);
        boolean yammerChecked = sharedPreferences.getBoolean("yammer"
                + ENABLED_SUFFIX, false);
        ((CheckBox) findViewById(R.id.yammerCheckbox))
                .setChecked(yammerChecked);
        boolean facebookChecked = sharedPreferences.getBoolean("facebook"
                + ENABLED_SUFFIX, false);
        ((CheckBox) findViewById(R.id.facebookCheckbox))
                .setChecked(facebookChecked);

        for (int i = 0; i < forwarders.size(); ++i) {
            Forwarder forwarder = forwarders.valueAt(i);

            if ("twitter".equals(forwarder.getSettingPrefix())) {
                forwarder.setEnabled(twitterChecked);
            } else if ("yammer".equals(forwarder.getSettingPrefix())) {
                forwarder.setEnabled(yammerChecked);
            } else if ("facebook".equals(forwarder.getSettingPrefix())) {
                forwarder.setEnabled(facebookChecked);
            }

            if (forwarder.isEnabled()) {
                forwarder.authenticate();
            }
        }
    }

    public void onForwarderCheckboxClick(View view) {
        CheckBox checkbox = (CheckBox) view;
        Forwarder forwarder = (Forwarder) forwarders.get(checkbox.getId());
        forwarder.setEnabled(checkbox.isChecked());
        
        saveCheckboxState();

        if (checkbox.isChecked()) {
            forwarder.authenticate();
        }
    }

    public void onAnnounceClick(View button) {
        for (int i = 0; i < forwarders.size(); ++i) {
            Forwarder forwarder = forwarders.valueAt(i);
            if (forwarder.isEnabled()) {
                forwarder.submitStatus(statusField.getText().toString());
            }
        }
    }
}
