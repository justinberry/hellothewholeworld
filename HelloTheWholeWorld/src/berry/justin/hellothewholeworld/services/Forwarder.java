package berry.justin.hellothewholeworld.services;

import twitter4j.auth.RequestToken;
import android.content.Context;
import android.os.AsyncTask;

public abstract class Forwarder extends AsyncTask<Object, Object, Object> {

    private boolean isEnabled = false;
    
    protected Context context;
    protected RequestToken requestToken;
    
    public Forwarder(Context context) {
        this.context = context;
    }
    
    public void setEnabled(boolean value) {
        isEnabled = value;
    }
    
    public boolean isEnabled() {
        return isEnabled;
    }
    
    abstract public void submitStatus(String string);
    abstract public void authenticate();
    abstract public String getSettingPrefix();
}