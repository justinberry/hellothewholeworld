package berry.justin.hellothewholeworld.services.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import berry.justin.hellothewholeworld.Preferences;

public class GetTwitterAuthorisationRequestTask extends
        AsyncTask<Object, Object, Uri> {

    private static Twitter twitter = TwitterFactory.getSingleton();
    private Context context;
    
    public GetTwitterAuthorisationRequestTask(Context context) {
        this.context = context;
    }
    
    @Override
    protected Uri doInBackground(Object... forwarder) {
        
        RequestToken requestToken = null;
        try {
            clearStoredRequestToken();
            requestToken = twitter.getOAuthRequestToken();
            storeRequestToken(requestToken);
        } catch (TwitterException e) {
            System.out.println("Failed to get request token from Twitter: ");
            e.printStackTrace();
        }

        return Uri.parse(requestToken.getAuthorizationURL());
    }

    private void storeRequestToken(RequestToken requestToken) {
        SharedPreferences preferences = context
                .getSharedPreferences(Preferences.APPLICATION_PREFERENCES,
                        Context.MODE_PRIVATE);
        
        Editor editor = preferences.edit();
        editor.putString(Preferences.OAUTH_REQUEST_TOKEN, requestToken.getToken());
        editor.putString(Preferences.OAUTH_REQUEST_SECRET, requestToken.getTokenSecret());
        editor.commit();
    }
    
    private void clearStoredRequestToken() {
        SharedPreferences preferences = context
                .getSharedPreferences(Preferences.APPLICATION_PREFERENCES,
                        Context.MODE_PRIVATE);
        
        Editor editor = preferences.edit();
        editor.putString(Preferences.OAUTH_REQUEST_TOKEN, null);
        editor.putString(Preferences.OAUTH_REQUEST_SECRET, null);
        editor.commit();
    }

    @Override
    protected void onPostExecute(Uri authUri) {
        Intent authWebIntent = new Intent(Intent.ACTION_VIEW, authUri);
        // Might not be good form doing this:
        authWebIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(authWebIntent);
    }
}
