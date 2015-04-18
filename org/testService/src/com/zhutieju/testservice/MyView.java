package com.zhutieju.testservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * �Զ���View������ʾ���ܵ��Ļ���
 * @author Administrator
 *
 */
public class MyView extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder holder;// ����SurfaceView��Holder����
	private MyThread t;
	private Bitmap ivBitmap;//��Ҫ������BMPͼƬ
	private boolean running = true;//����MyThread��run����
	private Paint paint = new Paint();
	
	public MyView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.holder = this.getHolder();
		holder.addCallback(this);
		t = new MyThread();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		t.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (t.isAlive()) {
			running = false;
		}
	}
	/**
	 * ���Ƹ���MyView�Ļ�ͼ�߳�
	 * @author Administrator
	 *
	 */
	private class MyThread extends Thread {
		Canvas canvas = null;
		@Override
		public void run() {
			while (running) {			
				if (Util.list.size() > 0) {
					canvas = holder.lockCanvas();
					if (canvas != null) {
						onDraw(canvas);
						System.out.println("------һ֡ͼ�񱻻滭-------");
					}
					holder.unlockCanvasAndPost(canvas);
				}	
				try {
					sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	/**
	 * ��дView��onDraw����
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Matrix m = new Matrix();
		ivBitmap = Util.setOrgetBitmap(1, null);
		canvas.drawBitmap(ivBitmap, m, null);
		canvas.drawText("2013��5��2��", 0, 10, paint);
	}

}
