package com.hakka.pluto.example.test;

import com.hakka.pluto.example.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

public class TestClipActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(new mView(this));
    }
	
	private class mView extends View {

		private Bitmap bitmap;
		private Paint paint;

		public mView(Context context) {
			super(context);
			this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.hakka_wallpaper);
			this.paint = new Paint();
		}
		
		protected void onDraw (Canvas canvas){
			//canvas.clipRect(new Rect(0,0,100,100));
			Path path = new Path();
			path.addCircle(100,100, 50, Path.Direction.CW);
			canvas.clipPath(path);
			canvas.drawBitmap(bitmap, 0,0, paint);
			
			Path path2 = new Path();
			path.addCircle(200,100, 50, Path.Direction.CW);
			canvas.clipRect(0, 0, 500, 500);
			canvas.drawBitmap(bitmap, 100,150, paint);
				
			canvas.clipPath(path2);
			canvas.drawBitmap(bitmap, 0,0, paint);
		}
		
	}
}
