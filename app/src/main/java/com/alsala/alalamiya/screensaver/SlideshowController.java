package com.alsala.alalamiya.screensaver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.io.InputStream;
import java.util.Locale;
public final class SlideshowController {
  private final Context context;
  private final FrameLayout parent;
  private final Handler handler = new Handler(Looper.getMainLooper());
  private final ImageView[] views = new ImageView[2];
  private final Bitmap[] bitmaps = new Bitmap[2];
  private boolean running = false;
  private int current = 0, slot = 0;
  private static final long IMAGE_DURATION_MS = 10000L;
  private static final long FADE_DURATION_MS = 800L;
  public SlideshowController(Context ctx, FrameLayout root) {
    context = ctx.getApplicationContext();
    parent = root;
    for(int i=0;i<2;i++){
      views[i] = new ImageView(ctx);
      views[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
      views[i].setAlpha(0f);
      parent.addView(views[i],new FrameLayout.LayoutParams(-1,-1));
    }
  }
  private Bitmap load(int index){
    String path=String.format(Locale.US,"wallpapers/slide_%02d.jpg",index+1);
    try(InputStream f=context.getAssets().open(path)){
      return BitmapFactory.decodeStream(f);
    }catch(Exception ex){return null;}
  }
  public void start(){
    if(running)return;
    running=true;
    current=0;slot=0;
    Bitmap b=load(current);
    if(b!=null){bitmaps[0]=b;views[0].setImageBitmap(b);views[0].setAlpha(1f);views[0].bringToFront();}
    handler.postDelayed(tick,IMAGE_DURATION_MS);
  }
  private final Runnable tick=new Runnable(){
    @Override public void run(){
      if(!running)return;
      int next=(current+1)%10;
      Bitmap b=load(next);
      if(b!=null){
        final int old=slot, newer=1-slot;
        final Bitmap oldBmp=bitmaps[old];
        ImageView incoming=views[newer];
        incoming.animate().cancel();
        incoming.setImageBitmap(b);bitmaps[newer]=b;
        incoming.setAlpha(0f);incoming.bringToFront();
        incoming.animate().alpha(1f).setDuration(FADE_DURATION_MS).withEndAction(()->{
          views[old].setImageDrawable(null);
          views[old].setAlpha(0f);
          bitmaps[old]=null;
          if(oldBmp!=null&&!oldBmp.isRecycled())oldBmp.recycle();
        }).start();
        slot=newer;current=next;
      }
      handler.postDelayed(this,IMAGE_DURATION_MS);
    }
  };
  public void stop(){
    running=false; handler.removeCallbacksAndMessages(null);
    for(int i=0;i<2;i++){
      views[i].animate().cancel();
      views[i].setImageDrawable(null);
      views[i].setAlpha(0f);
      if(bitmaps[i]!=null&&!bitmaps[i].isRecycled())bitmaps[i].recycle();
      bitmaps[i]=null;
    }
  }
}
