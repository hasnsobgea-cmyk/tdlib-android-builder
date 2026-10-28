package com.alsala.alalamiya.screensaver;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
public final class MainActivity extends Activity {
  private static final int GOLD=0xFFF3C679,BLUE=0xFF31C8FF,WHITE=0xFFF7F8FA;
  private TextView label(String s,int size,int color){
    TextView v=new TextView(this);v.setText(s);v.setTextColor(color);v.setTextSize(size);v.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);v.setTextDirection(View.TEXT_DIRECTION_RTL);return v;
  }
  private Button button(String title,boolean primary){
    Button b=new Button(this);b.setText(title);b.setAllCaps(false);b.setTextSize(19);
    b.setTextColor(primary?0xFF06111E:WHITE);b.setFocusable(true);
    GradientDrawable bg=new GradientDrawable();bg.setColor(primary?GOLD:0xFF193856);bg.setCornerRadius(18);b.setBackground(bg);
    return b;
  }
  @Override public void onCreate(Bundle b){
    super.onCreate(b);
    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    FrameLayout root=new FrameLayout(this);root.setBackgroundColor(0xFF06111E);
    ImageView background=new ImageView(this);background.setScaleType(ImageView.ScaleType.CENTER_CROP);
    try(java.io.InputStream f=getAssets().open("wallpapers/slide_10.jpg")){background.setImageBitmap(BitmapFactory.decodeStream(f));}catch(Exception ignored){}
    root.addView(background,new FrameLayout.LayoutParams(-1,-1));
    View shade=new View(this);shade.setBackground(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,new int[]{0xC0021226,0xE5040D1A,0xF106101F}));
    root.addView(shade,new FrameLayout.LayoutParams(-1,-1));
    LinearLayout panel=new LinearLayout(this);panel.setOrientation(LinearLayout.VERTICAL);panel.setGravity(Gravity.CENTER_VERTICAL);panel.setPadding(20,12,30,12);
    panel.addView(label("شاشة التوقف الرسمية",19,BLUE));
    panel.addView(label("الصالة العالمية",43,GOLD));
    panel.addView(label("10 صور ألعاب بلايستيشن · صورة جديدة كل 10 ثوانٍ",19,WHITE));
    Button settings=button("تعيين شاشة التوقف الافتراضية",true);panel.addView(settings,new LinearLayout.LayoutParams(-1,70));settings.setOnClickListener(v->openSettings());
    Button preview=button("مشاهدة العرض الآن",false);
    LinearLayout.LayoutParams margin=new LinearLayout.LayoutParams(-1,70);margin.topMargin=12;panel.addView(preview,margin);
    preview.setOnClickListener(v->startActivity(new Intent(this,PreviewActivity.class)));
    Button help=button("تعليمات التفعيل",false);LinearLayout.LayoutParams hp=new LinearLayout.LayoutParams(-1,70);hp.topMargin=12;panel.addView(help,hp);
    help.setOnClickListener(v->showHelp());
    FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(570,-1,Gravity.RIGHT|Gravity.CENTER_VERTICAL);lp.rightMargin=40;root.addView(panel,lp);
    setContentView(root);settings.requestFocus();
    if(!getPreferences(0).getBoolean("seen",false)){
      getPreferences(0).edit().putBoolean("seen",true).apply();
      new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(()->new AlertDialog.Builder(this)
      .setTitle("تفعيل شاشة توقف الصالة العالمية")
      .setMessage("التطبيق جاهز للعرض كل 10 ثوانٍ. اختر الصالة العالمية ضمن إعدادات شاشة التوقف إذا سمح نظام التلفاز بذلك.")
      .setPositiveButton("فتح الإعدادات",(d,w)->openSettings()).setNegativeButton("لاحقًا",null).show(),450);
    }
  }
  private void openSettings(){
    try{startActivity(new Intent(Settings.ACTION_DREAM_SETTINGS));}
    catch(Exception e){try{startActivity(new Intent(Settings.ACTION_DISPLAY_SETTINGS));}catch(Exception ignored){showHelp();}}
  }
  private void showHelp(){new AlertDialog.Builder(this).setTitle("إعداد شاشة التوقف").setMessage("افتح الإعدادات ← النظام ← شاشة التوقف، واختر الصالة العالمية. إذا لم يظهر التطبيق، فقد يمنع نظام Google TV استخدام شاشة توقف خارجية وقد يلزم تفعيلها بواسطة ADB من جهاز موثوق. لا يمكن للتطبيق منح نفسه صلاحيات النظام.").setPositiveButton("حسنًا",null).show();}
}
