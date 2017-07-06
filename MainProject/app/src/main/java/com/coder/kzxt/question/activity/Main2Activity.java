//package com.coder.kzxt.question.activity;
//
//import android.support.animation.SpringAnimation;
//import android.support.animation.SpringForce;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//
//import com.coder.kzxt.activity.R;
//
//import java.net.URL;
//
//import android.app.Activity;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.text.Html;
//import android.text.Html.ImageGetter;
//import android.text.method.LinkMovementMethod;
//import android.text.method.ScrollingMovementMethod;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MotionEvent;
//import android.view.VelocityTracker;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
///**
// * 演示text加载HTML数据以及新的弹簧动画效果
// *
// * */
//public class Main2Activity extends AppCompatActivity {
//ImageView ball;
//    private float downX, downY;
//    private VelocityTracker velocityTracker;
//    SpringAnimation animX ;
//    SpringAnimation animY ;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//      //  struct();
//        setContentView(R.layout.activity_main2);
//        TextView tv = (TextView) findViewById(R.id.text);
//        ball= (ImageView) findViewById(R.id.ball);
//        animX= new SpringAnimation(ball, SpringAnimation.TRANSLATION_X, 0);
//        animY= new SpringAnimation(ball, SpringAnimation.TRANSLATION_Y, 0);
//        velocityTracker = VelocityTracker.obtain();
//
//        String html = "<p>VR不是新名词，它早已在游戏、电影等领域名声赫赫，但是在教育领域，它是个新手，却也很快会成为强手。我们耳熟能详的Facebook、Sony、腾讯等科技巨头都在VR教育领域投入了巨资，试图抢占先机。</p>\n" +
//                "\n" +
//                "<p><strong>那么， VR到底是什么，这两个英文代表什么呢?</strong></p>\n" +
//                "\n" +
//                "<p>VR即Virtual Reality，中文翻译就是“虚拟现实”，是指综合利用计算机图形系统和各种现实及控制等接口设备，在计算机上生成的、可交互的三维环境中提供沉浸感觉的技术。它利用计算机生成一种模拟环境，是一种多源信息融合的交互式三维动态视景和实体行为的系统仿真使用户沉浸到该环境中。</p>\n" +
//                "\n" +
//                "<p>通俗地解释VR，就是利用计算机技术，带给你身临其境的“真实”感受，比如国内在这方面重要的开发商北京微视酷科技有限责任公司开发的“IES沉浸式课堂”系统就是典型的VR课堂，重点就是“沉浸式”三个字，让你沉浸其中，“身临其境”。</p>\n" +
//                "\n" +
//                "<p style=\"text-align:center\"><img alt=\"ͼƬ2.png\" height=\"291\" src=\"http://upload.chinaz.com/2016/0920/201609201523388903.png\" width=\"500\"></p>\n" +
//                "\n" +
//                "<p>VR的出现，将使包括医疗、教育、电影、游戏等在内的领域发生根本性的变化。如果将VR技术运用到教育上，会有怎样的反应?</p>\n" +
//                "\n" +
//                "<p>前段时间，北京微视酷科技有限责任公司在北京市东城区培新小学进行了一场“IES沉浸式课堂”系统的体验课，让老师、学生、家长和教育官员们直观的感受了VR课堂的“沉浸式”体验，中央2套还做了专门的报道。</p>";
//
////        tv.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
////        tv.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
////        tv.setText(Html.fromHtml(html, imgGetter, null));
//        tv.setText("点击跳跃");
//
//        SpringForce spring = new SpringForce(0)
//                .setDampingRatio(0.05f)
//                .setStiffness(SpringForce.STIFFNESS_VERY_LOW);
//        final SpringAnimation anim = new SpringAnimation(ball ,SpringAnimation.TRANSLATION_Y).setSpring(spring);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                anim.cancel();
//                anim.setStartValue(-700);
//                anim.start();
//            }
//        });
//        ball.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        downX = event.getX();
//                        downY = event.getY();
//                       // velocityTracker.addMovement(event);
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        ball.setTranslationX(event.getX() );
//                       // ball.setTranslationY(event.getY() - downY);
//                      //  velocityTracker.addMovement(event);
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        //velocityTracker.computeCurrentVelocity(1000);
//                        if (ball.getTranslationX() != 0) {
//                            //animX.cancel();
//                           // animX.getSpring().setStiffness(50f);
//                            //animX.getSpring().setDampingRatio(0.5f);
//                           // animX.setStartVelocity(velocityTracker.getXVelocity());
//                         //   animY.animateToFinalPosition(ball.getTranslationX());
//                            //animX.start();
//                        }
//                        if (ball.getTranslationY() != 0) {
////                            animY.cancel();
////                            animY.getSpring().setStiffness(50f);
////                            animY.getSpring().setDampingRatio(0.8f);
////                            animY.setStartVelocity(velocityTracker.getYVelocity());
////                            animY.start();
//                        }
//                     //   velocityTracker.clear();
//                        return true;
//                }
//                return false;
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//       // getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
//    //这里面的resource就是fromhtml函数的第一个参数里面的含有的url
//    ImageGetter imgGetter = new Html.ImageGetter() {
//        public Drawable getDrawable(String source) {
//            Log.i("RG", "source---?>>>" + source);
//            Drawable drawable = null;
//            URL url;
//            try {
//                url = new URL(source);
//                Log.i("RG", "url---?>>>" + url);
//                drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
//                    drawable.getIntrinsicHeight());
//            Log.i("RG", "url---?>>>" + url);
//            return drawable;
//        }
//    };
//
//    public static void struct() {
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads().detectDiskWrites().detectNetwork() // or
//                // .detectAll()
//                // for
//                // all
//                // detectable
//                // problems
//                .penaltyLog().build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
//                .penaltyLog() // 打印logcat
//                .penaltyDeath().build());
//    }
//}
