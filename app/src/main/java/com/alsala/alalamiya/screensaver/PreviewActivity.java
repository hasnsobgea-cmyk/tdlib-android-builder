package com.alsala.alalamiya.screensaver;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
public final class PreviewActivity extends Activity {
  private SlideshowController slideshow;
  @Override public void onCreate(Bundle b){
    super.onCreate(b);
    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    FrameLayout root=new FrameLayout(this);root.setBackgroundColor(0xFF06111E);setContentView(root);
    slideshow=new SlideshowController(this,root);
  }
  @Override public void onStart(){super.onStart();slideshow.start();}
  @Override public void onStop(){slideshow.stop();super.onStop();}
}
