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
import android.graphics.Region.Op;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.os.Bundle;
import android.view.View;

public class TestClipActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(new CustomDrawableView(this));
	}

	private class mView extends View {

		private Bitmap bitmap;
		private Paint paint;

		public mView(Context context) {
			super(context);
			this.bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.hakka_wallpaper);
			this.paint = new Paint();
		}

		protected void onDraw(Canvas canvas) {
			// canvas.clipRect(new Rect(0,0,100,100));
			Path path = new Path();
			path.addCircle(50, 50, 50, Path.Direction.CW);
			// path.addPath(src)
			// path.add
			canvas.clipPath(path);
			canvas.drawBitmap(bitmap, 0, 0, paint);
			// Path path2 = new Path();
			// path.addCircle(200,100, 50, Path.Direction.CW);
			// canvas.clipRect(0, 0, 500, 500);
			// canvas.drawBitmap(bitmap, 100,150, paint);
			canvas.restore();
			// canvas.clipPath(path2);
			canvas.clipRect(new Rect(100, 100, 200, 200));
			canvas.drawBitmap(bitmap, 100, 100, paint);
			canvas.restore();
		}

	}

	public class CustomDrawableView extends View {
		private ShapeDrawable mDrawable;
		private Path path;
		private Bitmap bitmap;
		private Path path2;

		public CustomDrawableView(Context context) {
			super(context);

			int x = 10;
			int y = 10;
			int width = 300;
			int height = 300;

//			mDrawable = new ShapeDrawable(new OvalShape());
			int blickSize = 20;
			
			this.path = new Path();
			path.addRect( blickSize*1 , blickSize*1 , blickSize*2 , blickSize*2 , Path.Direction.CCW);
			path.addRect( blickSize*2 , blickSize*0 , blickSize*3 , blickSize*1 , Path.Direction.CCW);
			path.addRect( blickSize*2 , blickSize*1 , blickSize*3 , blickSize*2 , Path.Direction.CCW);
			path.addRect( blickSize*3 , blickSize*1 , blickSize*4 , blickSize*2 , Path.Direction.CCW);
			
			path.addRect( blickSize*2 , blickSize*2 , blickSize*3 , blickSize*3 , Path.Direction.CCW);
			path.addRect( blickSize*1 , blickSize*3 , blickSize*2 , blickSize*4 , Path.Direction.CCW);
			path.addRect( blickSize*2 , blickSize*3 , blickSize*3 , blickSize*4 , Path.Direction.CCW);
			path.addRect( blickSize*3 , blickSize*3 , blickSize*4 , blickSize*4 , Path.Direction.CCW);
			
			int a = blickSize;
			this.path2 = new Path();
			path2.moveTo(2*a, 0 *a);
			path2.moveTo(3*a, 0 *a);
			path2.moveTo(3*a, 1 *a);
			path2.moveTo(4*a, 1 *a);
			path2.moveTo(4*a, 2 *a);
			path2.moveTo(5*a, 2 *a);
			path2.moveTo(5*a, 3 *a);
			path2.moveTo(4*a, 3 *a);
			
			path2.moveTo(4*a, 4 *a);
			path2.moveTo(3*a, 4 *a);
			path2.moveTo(3*a, 5 *a);
			path2.moveTo(2*a, 5 *a);
			
			path2.moveTo(2*a, 4 *a);
			path2.moveTo(1*a, 4 *a);
			path2.moveTo(1*a, 3*a );
			path2.moveTo(0*a, 3 *a);
			
			path2.moveTo(0*a, 2 *a);
			path2.moveTo(1*a, 2 *a);
			path2.moveTo(1*a, 1*a );
			path2.moveTo(2*a, 1 *a);
			path2.moveTo(2*a, 0 *a);
			
			
			mDrawable = new ShapeDrawable(new PathShape(path,200,200));
			mDrawable.getPaint().setColor(0xff74AC23);
			mDrawable.setBounds(x, y, x + width, y + height);
			
			bitmap = BitmapFactory.decodeResource(TestClipActivity.this.getResources(), R.drawable.hakka_wallpaper);
		}

		protected void onDraw(Canvas canvas) {
			canvas.clipPath(path);
//			canvas.clipPath(path2, Op.INTERSECT);
			canvas.drawBitmap(bitmap, 0 , 0 , null);
			canvas.restore();
		}
	}
}
