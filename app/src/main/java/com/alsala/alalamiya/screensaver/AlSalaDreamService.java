package com.alsala.alalamiya.screensaver;
import android.service.dreams.DreamService;
import android.widget.FrameLayout;
public final class AlSalaDreamService extends DreamService {
  private SlideshowController slideshow;
  @Override public void onAttachedToWindow(){
    super.onAttachedToWindow();setFullscreen(true);setInteractive(false);
    FrameLayout root=new FrameLayout(this);root.setBackgroundColor(0xFF06111E);setContentView(root);
    slideshow=new SlideshowController(this,root);
  }
  @Override public void onDreamingStarted(){super.onDreamingStarted();if(slideshow!=null)slideshow.start();}
  @Override public void onDreamingStopped(){if(slideshow!=null)slideshow.stop();super.onDreamingStopped();}
  @Override public void onDetachedFromWindow(){if(slideshow!=null)slideshow.stop();super.onDetachedFromWindow();}
}
