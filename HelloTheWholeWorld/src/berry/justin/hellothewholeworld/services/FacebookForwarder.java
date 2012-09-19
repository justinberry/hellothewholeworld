package berry.justin.hellothewholeworld.services;

import android.content.Context;

public class FacebookForwarder extends Forwarder {

    public FacebookForwarder(Context context) {
        super(context);
    }

    @Override
    public void submitStatus(String status) {
        System.out.println("Facebook forwarder: " + status);
    }

    @Override
    public void authenticate() {
        System.err.println("Facebook integreation not implemented.");
//        throw new RuntimeException("Not implemented.");
    }

    @Override
    protected Object doInBackground(Object... params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSettingPrefix() {
        // TODO Auto-generated method stub
        return "facebook";
    }
}
