package com.zhutieju.testservice;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;

/**
 * �������Activity
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends Activity {
	public static final String TAG = "�������־";
	//ServerThread thread;
	ReadFileThread thread;
	boolean isActivity = true;// �����߳�ServerThread��run����
	long decoder;
	H264Android h264;
	byte[] mPixel = new byte[352 * 288*4];
	ByteBuffer buffer = ByteBuffer.wrap(mPixel);
	public static ArrayList<byte[]> framebuf = new ArrayList<byte[]>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MyView(this));
		h264 = new H264Android();
		decoder =  h264.initDecoder(352,288);
		int i = mPixel.length;
		for (i = 0; i < mPixel.length; i++) {
			mPixel[i] = (byte) 0x00;
		}
		//thread = new ServerThread();
		thread = new ReadFileThread();
		thread.start();
		new Thread(new DecordeThread()).start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (thread.isAlive()) {
			isActivity = false;
		}
		finish();
		System.exit(0);
	}

	/**
	 * ��ȡ�ͻ������ݵ��߳���
	 * 
	 * @author Administrator
	 * 
	 */
/*	public class ServerThread extends Thread {
		ServerSocket ss;// �����ServerSocket	
		public ServerThread() {
			
		}

		Socket s;
		InputStream is;
		int len, size;
		StringBuilder sb;;

		@Override
		public void run() {
			try {
				Log.v(TAG, "����������ɹ�--------------->");
				ss = new ServerSocket(5000);	
				s = ss.accept();
				Log.v(TAG, "��ͻ������ӳɹ�------------->"+s.toString());
				DataInputStream dis = new DataInputStream(s.getInputStream());
				ByteArrayOutputStream baos;
				while (isActivity) {
					int len2 = 0;
					baos = new ByteArrayOutputStream();
					
					byte[] b = new byte[20];
					dis.read(b);
					try {
						System.out.println("--->" + 
						JTools.toStringList(b, 0, "UTF-8").get(0));
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					sb = new StringBuilder();
					while ((len = dis.read()) != '\r') {
						if (len == -1) {
							break;
						}
						sb.append((char) len);
					}
					if (sb.length() > 0) {
						size = Integer.parseInt(sb.toString());
						byte[] data = new byte[size];
						while (size > 0
								&& (len = dis.read(data, 0, size)) != -1) {
							baos.write(data, 0, len);
							size = size - len;
						}
					}
					
					len = dis.readInt();
					Log.i(TAG, "len�Ĵ�СΪ"+len);
					byte[] data = new byte[len];
					while(len>0 && (len2 = dis.read(data,0,len)) != -1) {
						System.out.println("-------len2------->" + len2);
						baos.write(data, 0, len2);
						len = len - len2;
					}
					if(framebuf.size()>25) {
						try {
							sleep(800);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Log.i(TAG, "-------------------------------------------");
				setOrget(1, baos.toByteArray());
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void destroy() {
			// TODO Auto-generated method stub
			h264.releaseDecoder();
			try {
				if (ss != null) {
					ss.close();
				}
				if (s != null) {
					s.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
			super.destroy();
		}
	} */
	
	/**
	 * ��ȡ�ļ���
	 * @author Administrator
	 *
	 */
	public class ReadFileThread extends Thread {
		int file_index = 0;
		File file = new File("/mnt/sdcard/ljl.264");
		@Override
		public void run() {
			try {
				while(file_index<file.length()) {
					byte[] data = new byte[1024*50];
					RandomAccessFile raf = new RandomAccessFile(file, "r");
					int len = readOneFrame(raf, data);
					Log.i(TAG, "һ֡����Ϊ:"+len);
					byte[] newData = new byte[len];
					System.arraycopy(data, 0, newData, 0, len);
					//Log.i(TAG, "ǰ�ĸ��ֽ�Ϊ��"+newData[0]+" "+newData[1]+" "+newData[2]+" "+newData[3]);
					setOrget(1, newData);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**
		 * ��ȡһ֡����
		 * @param raf	��ȡ�ļ���
		 * @param data	�����ȡ������
		 * @return		����һ֡����
		 * @throws IOException
		 */
		private int readOneFrame(RandomAccessFile raf,byte[] data) throws IOException {
			int len = 0;//��ʾһ֡����
			raf.seek(file_index);
			while(true) {
				if((data[len++] = raf.readByte())==0 && (data[len++] = raf.readByte())==0 ) {
					if((data[len++] = raf.readByte()) < 2 && len>6) {
						if(data[len - 1] == 0) {
							if((data[len++]=raf.readByte())==1) {
								file_index+=(len - 4);
								return len - 4;
							} else {
								continue;
							}
						} else {
							file_index+=(len - 3);
							return len - 3;
						}
					} else {
						continue;
					}
				} else {
					continue;
				}
			}
		}
	}
	

	
	/**
	 * ������ȡ����
	 * @param type
	 * @param data
	 * @return
	 */
	public synchronized byte[] setOrget(int type,byte[] data) {
		switch (type) {
		case 1:	//��������
			framebuf.add(data);
			return null;
		case 0: //��ȡ����
			if(framebuf.size()>0) {
				byte[] b = framebuf.get(0);
				framebuf.remove(0);
				return b;
			}
		}
		return null;
	}
	
	
	
	/**
	 * �����߳���
	 * @author Administrator
	 *
	 */
	class DecordeThread implements Runnable {
		@Override
		public void run() {
			while(true) {
				byte[] dataa = setOrget(0, null);
				if (dataa != null&&dataa.length > 0) {//һ֡�����յ�����
					int resout = h264.dalDecoder(dataa, dataa.length, mPixel);
					if(resout>0) {
						Bitmap videoBit = Bitmap.createBitmap(352, 288, Config.RGB_565);
						videoBit.copyPixelsFromBuffer(buffer);
						Util.setOrgetBitmap(0, videoBit);
						Log.i(TAG, "�����е����ݣ�"+Util.list.size());
					}
				} 
			}
		}
	}
	
	public static String nowTime() {
		  Calendar c = Calendar.getInstance();
		  c.setTimeInMillis(new Date().getTime());
		  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  return dateFormat.format(c.getTime());
		 }
}
