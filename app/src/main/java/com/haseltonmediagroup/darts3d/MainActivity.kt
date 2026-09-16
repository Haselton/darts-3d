package com.haseltonmediagroup.darts3d

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.graphics.drawable.*
import android.view.*
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import kotlin.math.*
import kotlin.random.Random

class MainActivity: Activity() {
 override fun onCreate(b: Bundle?) { super.onCreate(b); window.decorView.systemUiVisibility = 5894; setContentView(DartsView(this)) }
}

class DartsView(c: Context): View(c) {
 private val p=Paint(3); private var player=501; private var bot=501; private var darts=3; private var turnPlayer=true; private var msg="FLICK UP TO THROW"; private var sx=0f; private var sy=0f; private val stuck=mutableListOf<Pair<Float,Float>>()
 private val nums=intArrayOf(20,1,18,4,13,6,10,15,2,17,3,19,7,16,8,11,14,9,12,5)
 override fun onDraw(c:Canvas){ super.onDraw(c); val w=width.toFloat(); val h=height.toFloat(); c.drawColor(Color.rgb(8,10,12));
  p.textAlign=Paint.Align.CENTER; p.typeface=Typeface.create("sans",Typeface.BOLD); p.color=Color.rgb(231,182,75); p.textSize=w*.07f; c.drawText("3D DARTS",w/2,h*.065f,p)
  p.color=Color.LTGRAY;p.textSize=w*.032f;c.drawText("501  •  DOUBLE OUT  •  PLAYER vs AI",w/2,h*.10f,p)
  val cy=h*.40f; val r=w*.43f; drawBoard(c,w/2,cy,r)
  p.color=Color.WHITE;p.textSize=w*.045f;c.drawText("YOU",w*.25f,h*.77f,p);c.drawText("BOT",w*.75f,h*.77f,p)
  p.color=if(turnPlayer) Color.rgb(231,182,75) else Color.GRAY;p.textSize=w*.105f;c.drawText(player.toString(),w*.25f,h*.84f,p)
  p.color=if(!turnPlayer) Color.rgb(231,182,75) else Color.GRAY;c.drawText(bot.toString(),w*.75f,h*.84f,p)
  p.color=Color.LTGRAY;p.textSize=w*.038f;c.drawText(msg,w/2,h*.91f,p); p.textSize=w*.03f;c.drawText(if(turnPlayer) "DARTS LEFT  $darts" else "OPPONENT THROWING…",w/2,h*.95f,p)
 }
 private fun drawBoard(c:Canvas,cx:Float,cy:Float,r:Float){ p.style=Paint.Style.FILL;p.color=Color.rgb(28,28,27);c.drawCircle(cx,cy,r*1.08f,p);p.style=Paint.Style.STROKE;p.strokeWidth=2f
  for(i in 0..19){val a0=Math.toRadians((i*18-99).toDouble());val a1=Math.toRadians((i*18-81).toDouble()); val path=Path();path.moveTo(cx,cy);path.arcTo(cx-r,cy-r,cx+r,cy+r,Math.toDegrees(a0).toFloat(),18f);path.close();p.style=Paint.Style.FILL;p.color=if(i%2==0) Color.rgb(222,207,171) else Color.rgb(30,31,29);c.drawPath(path,p)}
  ring(c,cx,cy,r*.63f,r*.70f); ring(c,cx,cy,r*.94f,r); p.color=Color.rgb(31,115,69);c.drawCircle(cx,cy,r*.075f,p);p.color=Color.rgb(185,44,48);c.drawCircle(cx,cy,r*.035f,p)
  p.style=Paint.Style.STROKE;p.color=Color.rgb(190,190,185);p.strokeWidth=2f; for(i in 0..19){val a=Math.toRadians((i*18-90).toDouble());c.drawLine(cx+(cos(a)*r*.075).toFloat(),cy+(sin(a)*r*.075).toFloat(),cx+(cos(a)*r).toFloat(),cy+(sin(a)*r).toFloat(),p)}
  p.style=Paint.Style.FILL;p.color=Color.WHITE;p.textSize=r*.09f;p.textAlign=Paint.Align.CENTER; for(i in 0..19){val a=Math.toRadians((i*18-90).toDouble());c.drawText(nums[i].toString(),cx+(cos(a)*r*1.045).toFloat(),cy+(sin(a)*r*1.045+r*.03).toFloat(),p)}
  for(q in stuck){p.color=Color.rgb(225,225,230);p.strokeWidth=6f;c.drawLine(q.first,q.second,q.first+r*.10f,q.second+r*.13f,p);p.color=Color.rgb(231,182,75);c.drawCircle(q.first+r*.11f,q.second+r*.14f,r*.025f,p)}
 }
 private fun ring(c:Canvas,cx:Float,cy:Float,inner:Float,outer:Float){for(i in 0..19){val a=i*18f-99f;val path=Path();path.arcTo(cx-outer,cy-outer,cx+outer,cy+outer,a,18f);path.arcTo(cx-inner,cy-inner,cx+inner,cy+inner,a+18f,-18f);path.close();p.color=if(i%2==0) Color.rgb(186,45,48) else Color.rgb(28,116,69);c.drawPath(path,p)}}
 override fun onTouchEvent(e:android.view.MotionEvent):Boolean{if(!turnPlayer)return true;when(e.action){0->{sx=e.x;sy=e.y};1->{val dy=sy-e.y;if(dy>60){val tx=width/2f+(e.x-sx)*1.4f+Random.nextInt(-18,19);val ty=height*.40f-dy*.10f+Random.nextInt(-18,19);throwAt(tx,ty)}}};return true}
 private fun throwAt(x:Float,y:Float){val cx=width/2f;val cy=height*.40f;val r=width*.43f;val dx=x-cx;val dy=y-cy;val d=sqrt(dx*dx+dy*dy);var score=0;if(d<r){if(d<r*.035)score=50 else if(d<r*.075)score=25 else {var ang=(Math.toDegrees(atan2(dy.toDouble(),dx.toDouble()))+99+360)%360;val idx=(ang/18).toInt()%20;val base=nums[idx];score=when{d in r*.63..r*.70->base*3;d in r*.94..r->base*2;else->base}};stuck.add(Pair(x,y))};if(score<=player)player-=score;msg=if(score==0)"MISS" else "$score";darts--;vibe();if(player==0){msg="MATCH! YOU WIN";darts=0}else if(darts==0){turnPlayer=false;invalidate();postDelayed({botTurn()},700)};invalidate()}
 private fun botTurn(){var total=0;repeat(3){val s=when(Random.nextInt(10)){0->60;1->45;2->40;else->Random.nextInt(18,61)};if(s<=bot){bot-=s;total+=s}};msg="BOT SCORES $total";if(bot<=0){bot=0;msg="BOT WINS";invalidate();return};darts=3;turnPlayer=true;stuck.clear();invalidate()}
 private fun vibe(){try{val v=context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator;v.vibrate(VibrationEffect.createOneShot(35,120))}catch(_:Exception){}}
}
