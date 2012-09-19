package berry.justin.hellothewholeworld.services.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import berry.justin.hellothewholeworld.Preferences;
import berry.justin.hellothewholeworld.services.Forwarder;

public class TwitterForwarder extends Forwarder {

    private static Twitter twitter = TwitterFactory.getSingleton();

    private final static String CONSUMER_KEY = "Ik7oTf4Am9EQFbxv4f53cg";
    private final static String CONSUMER_SECRET = "cH65LjSby1B4z1UgR94bYH5n0idOCY6VVQyZxPY9Qw";
    
    private static boolean initialised = false;
    
    public TwitterForwarder(Context context) {
        super(context);
        
        if (!initialised) {
            twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
            initialised = true;
        }
    }

    private AccessToken loadOAuthToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                Preferences.APPLICATION_PREFERENCES, Context.MODE_PRIVATE);
        String token = preferences.getString(Preferences.OAUTH_KEY_PREFERENCE, null);
        String tokenSecret = preferences.getString(Preferences.OAUTH_SECRET_KEY_PREFERENCE,
                null);

        AccessToken accessToken = null;
        if (token != null && tokenSecret != null) {
            accessToken = new AccessToken(token, tokenSecret);
            twitter.setOAuthAccessToken(accessToken);
        }

        return accessToken;
    }

    public void storeOAuthAccessToken(AccessToken accessToken) {
        SharedPreferences preferences = context
                .getSharedPreferences(Preferences.APPLICATION_PREFERENCES,
                        Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(Preferences.OAUTH_KEY_PREFERENCE, accessToken.getToken());
        editor.putString(Preferences.OAUTH_SECRET_KEY_PREFERENCE,
                accessToken.getTokenSecret());
        editor.commit();
    }
    
    public void clearAccessToken() {
        SharedPreferences preferences = context
                .getSharedPreferences(Preferences.APPLICATION_PREFERENCES,
                        Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(Preferences.OAUTH_KEY_PREFERENCE, null);
        editor.putString(Preferences.OAUTH_SECRET_KEY_PREFERENCE, null);
        editor.commit();
    }

    @Override
    public void submitStatus(String status) {
        // FIXME - Argh, can only execute a task once!
        new TwitterForwarder(context).execute(status);
    }

    @Override
    public void authenticate() {
        
        AccessToken accessToken = loadOAuthToken(context);
        
        // If token isn't already stored we need to get it
        if (accessToken == null) {
            Intent intent = new Intent(context, TwitterSettingsActivity.class);
            // Might not be good form doing this:
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            
            new GetTwitterAuthorisationRequestTask(context).execute();
        } else {
            twitter.setOAuthAccessToken(accessToken);
        }
    }

    @Override
    protected Object doInBackground(Object... params) {
        String status = (String) params[0];

        try {
            twitter.updateStatus(status);
        } catch (TwitterException e) {
            // FIXME - for now, if there's an error assume the access token is bad.
            clearAccessToken();
            twitter.setOAuthAccessToken(null);
            System.err.println("Status update failed!");
            e.printStackTrace();
//            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public String getSettingPrefix() {
        return "twitter";
    }
}
