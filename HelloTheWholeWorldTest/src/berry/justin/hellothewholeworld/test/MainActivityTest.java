package berry.justin.hellothewholeworld.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CheckBox;
import berry.justin.hellothewholeworld.MainActivity;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity activity;
    private CheckBox twitterCheckbox;
    private CheckBox facebookCheckbox;
    private CheckBox yammerCheckbox;
    private Button announceButton;
    
    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() {
        // Disables touch mode in device/emulator
        // If not set, any test events are ignored!
        setActivityInitialTouchMode(false);
        
        activity = getActivity();
        
        twitterCheckbox = (CheckBox) activity.findViewById(berry.justin.hellothewholeworld.R.id.twitterCheckbox);
        facebookCheckbox = (CheckBox) activity.findViewById(berry.justin.hellothewholeworld.R.id.facebookCheckbox);
        yammerCheckbox = (CheckBox) activity.findViewById(berry.justin.hellothewholeworld.R.id.yammerCheckbox);
        
        announceButton = (Button) activity.findViewById(berry.justin.hellothewholeworld.R.id.announceButton);
    }
    
    public void testCheckboxesSupplyForwarders() {
        activity.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                twitterCheckbox.requestFocus();
                
                twitterCheckbox.setChecked(false);
                facebookCheckbox.setChecked(false);
                yammerCheckbox.setChecked(false);
            }
        });
        
        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
        this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
        this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
        
        getInstrumentation().waitForIdleSync();
        
        assertTrue(twitterCheckbox.isChecked());
        assertFalse(facebookCheckbox.isChecked());
        assertTrue(yammerCheckbox.isChecked());
        
//        activity.runOnUiThread(new Runnable() {
//            
//            @Override
//            public void run() {
//                announceButton.requestFocus();
//            }
//        });
//        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER)
        // Instead of using the DPad as above, can instead use TouchUtils
        TouchUtils.clickView(this, announceButton);
    }
}
