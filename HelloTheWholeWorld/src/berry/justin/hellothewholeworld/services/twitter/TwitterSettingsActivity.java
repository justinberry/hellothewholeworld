package berry.justin.hellothewholeworld.services.twitter;

import twitter4j.TwitterException;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import berry.justin.hellothewholeworld.R;

public class TwitterSettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.setProperty("http.keepAlive", "false");
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_settings);
    }
    
    public void onSubmitPin(View button) throws TwitterException {
        EditText pidField = (EditText) findViewById(R.id.pinField);
        String pin = pidField.getText().toString();
        new GetTwitterOAuthAccessToken(getApplicationContext()).execute(pin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_twitter_settings, menu);
        return true;
    }
}
