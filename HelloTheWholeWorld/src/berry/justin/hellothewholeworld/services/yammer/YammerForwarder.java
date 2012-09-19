package berry.justin.hellothewholeworld.services.yammer;

import android.content.Context;
import berry.justin.hellothewholeworld.services.Forwarder;

public class YammerForwarder extends Forwarder {

    private final static String CONSUMER_KEY = "SCZEEYhSTWVqBu71j2nzOw";
    private final static String CONSUMER_SECRET = "5Gx6glJCVaraoQVsvrty4Z9tXN37ii8unbdtUlHKQ";
    
    public YammerForwarder(Context context) {
        super(context);
    }

    @Override
    public void submitStatus(String status) {
        System.out.println("Yammer forwarder: " + status);
    }

    @Override
    public void authenticate() {
        System.err.println("Yammer integreation not implemented.");
//        throw new RuntimeException("Not implemented.");        
    }

    @Override
    protected Object doInBackground(Object... params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSettingPrefix() {
        return "yammer";
    }
}
