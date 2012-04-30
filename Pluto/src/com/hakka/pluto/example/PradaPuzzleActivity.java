package com.hakka.pluto.example;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;

public class PradaPuzzleActivity extends Activity {
    private pradaView mainView;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainView = new pradaView(this,BitmapFactory.decodeResource(this.getResources(), R.drawable.hakka_wallpaper));
        this.setContentView(mainView);
    }
	
	private class pradaView extends View {

		private Bitmap bitmap;
		
		private final int PUZZLE_WIDTH = 10;
		private final int PUZZLE_HEIGHT = 10;

		public pradaView(Context context , Bitmap bitmap) {
			super(context);
			this.bitmap = bitmap;
		}
		
		@Override
		protected void onDraw(Canvas canvas){
			int gapX = this.bitmap.getWidth() / PUZZLE_WIDTH;
			int gapY = this.bitmap.getWidth() / PUZZLE_HEIGHT;
			int distX = getWindowManager().getDefaultDisplay().getWidth() / PUZZLE_WIDTH;
			int distY = getWindowManager().getDefaultDisplay().getHeight() / PUZZLE_HEIGHT;
			Paint paint = new Paint();
			for(int i=0;i<PUZZLE_WIDTH-1;i++){
				for(int j=0;j<PUZZLE_HEIGHT-1;j++){
					Rect source = new Rect(i*gapX,j*gapY,(i+1)*gapX,(j+1)*gapY);
					RectF dist = new RectF(new Rect(i*distX,j*distY,(i*distX)+gapX,(j*distY)+gapY));
					canvas.drawBitmap(bitmap, source, dist, paint);
				}
			}
			
			
		}
	}
}
