package berry.justin.hellothewholeworld.services.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import berry.justin.hellothewholeworld.MainActivity;
import berry.justin.hellothewholeworld.Preferences;

public class GetTwitterOAuthAccessToken extends
        AsyncTask<Object, Object, Object> {

    private static Twitter twitter = TwitterFactory.getSingleton();
    
    private Context context;
    
    public GetTwitterOAuthAccessToken(Context context) {
        this.context = context;
    }

    @Override
    protected Uri doInBackground(Object... pin) {

        AccessToken accessToken;
        try {
            accessToken = twitter.getOAuthAccessToken(getRequestToken(), (String) pin[0]);
            twitter.setOAuthAccessToken(accessToken);
            storeOAuthAccessToken(accessToken);
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
    
    public RequestToken getRequestToken() {
            SharedPreferences preferences = context
                    .getSharedPreferences(Preferences.APPLICATION_PREFERENCES,
                            Context.MODE_PRIVATE);
            
            String requestToken = preferences.getString(Preferences.OAUTH_REQUEST_TOKEN, null);
            String requestSecret = preferences.getString(Preferences.OAUTH_REQUEST_SECRET, null);
            
            return new RequestToken(requestToken, requestSecret);
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

    @Override
    protected void onPostExecute(Object obj) {
        Intent intent = new Intent(context, MainActivity.class);
        // Might not be good form doing this:
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
