package com.zhutieju.testservice;

import java.util.ArrayList;

import android.graphics.Bitmap;
/**
 * �洢���ϵĹ�����
 * @author Administrator
 *
 */
public class Util {
	public static ArrayList<Bitmap> list = new ArrayList<Bitmap>();
	/**
	 * ���û��ȡλͼ
	 * @param type	0��ʾ����,1��ʾ��ȡ
	 * @param mBitmap
	 * @return
	 */
	public static synchronized Bitmap setOrgetBitmap(int type,Bitmap mBitmap) {
		switch (type) {
		case 0:
			list.add(mBitmap);
			return null;
		case 1:
			Bitmap bitmap = list.get(0);
			list.remove(0);
			return bitmap;
		}
		return null;
	}
}
