package com.ztkj.tool;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Toast;
/**
 * 无论你是在UI线程还是非UI线程，让你弹出一个Toast还是可以办到的，利用handler就oK了，所以我决定在非UI线程中
 * 弹出Toast，避免对UI线程的占用。
 * @author joychine	245621356@QQ.COM
 * @since 2014年7月6日 15:48:58
 * @category	类只有在创建对象时才加载~~~，所以这个要创建对象先
 */
public class ToastUtil {
	public static final int TOAST_MSG_SHOW = 0X01;
	public static final String TAG = "ToastUtil";
	private static ToastUtil mToastUtil;
	private static Handler mHandler;
	private static Context mContext;
	private static int count;
	
	private ToastUtil() {
//		//Log.e(TAG, "构造函数执行。。。");
	}
	
	static{
//		//Log.e("ToastUtil", "类已经在次数："+(++count));
		new HandlerThread("toast_handler_thread_"+(Thread.currentThread().getId())){//这里还在主线程id一定是1
			@Override
			protected void onLooperPrepared() {
//				//Log.e("ToastUtil", "tname："+Thread.currentThread().getName()+"--tid:"+Thread.currentThread().getId());
				Thread.currentThread().setName("toast_handler_thread_"+(Thread.currentThread().getId()));
				mHandler = new Handler(Looper.myLooper(), new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						switch (msg.what) {
						case TOAST_MSG_SHOW:
							MsgVo msgVo = (MsgVo) msg.obj;
							showToast(msgVo.msg, msgVo.length, msgVo.gravity, msgVo.xOffset, msgVo.yOffset);
							break;
						}
						return false;
					}
				});
			}
			
			private Toast mToast;
			
			/**
			 * @param resID		字符串资源的id
			 * @param length	Toast显示时长 有Toast.SHORT_LENGTH和 Toast.LONG_LENGTH
			 * @param gravity	mToast.setGravity(gravity, xOffset, yOffset);设置Toast的显示位置。
			 * @param xOffset	x方向的偏移量
			 * @param yOffset	y方向的偏移量
			 */
			private  void showToast(String msg,int length,int gravity, int xOffset, int yOffset){
				if (mToast!=null) {
				/*	Resources mResources = Resources.getSystem();  //getResources()测试也可以
					int id = mResources.getIdentifier("message", "id", "android");	//知识啊，记得做笔记哦。如何获取系统的布局文件
					View v = mToast.getView();
//					TextView tv = (TextView)v.findViewById(android.R.id.message); //com.android.internal.R.id.message 包下的能和android下的通用来鬼了。
					TextView tv = (TextView)v.findViewById(id); 
//					TextView tv = (TextView) mToast.getView();
*/				
					if (android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH > Build.VERSION.SDK_INT ) {
						//如果直接 不cancel 每次都 setText效果是挺好，没有闪动而且 各大系统显示效果一样挺好。
						mToast.cancel(); //如果4.0以上的系统不 cancel()会让这个toast对象失效只好重新创建一个咯,安卓2.2奇怪，直接cancel再去创建和不cancle一直创建效果一样。慢悠悠的出现
					}else{
						//安卓4.0 以上系统代码不同，没时间研究系统代码了。算了。
						mToast.cancel(); //如果4.0以上的系统不 cancel()会好像有问题的。我记得。所以要在建一个。
						mToast = Toast.makeText(mContext, msg, length);
					}
//					mToast.setText(msg+Math.());  //测试时为了显示位置写的而已
					mToast.setText(msg);
						
				}else {
					mToast = Toast.makeText(mContext, msg, length);
				}
				mToast.setGravity(gravity, xOffset, yOffset); //最终证明还是不能每次指定位置，，我一定要让你这句话生效，等着！！！
				mToast.setDuration(length);		//这个我也不能确定是不是每次都有效毕竟时间太短了，点击速度又快。
				mToast.show();
			}
			
		}.start();;
		
	}
	
	public static synchronized ToastUtil getInstance(Context context){
		ToastUtil.mContext = context;
		if (mToastUtil==null) {
			mToastUtil = new ToastUtil();
		}
		return mToastUtil;
	}
	
	/**
	 * 定义的Toast消息提示方法，在任何线程中都可以调用。
	 * 默认 是底下居中显示， x方向偏移量0，y方向的偏移量是80dp
	 */
	public synchronized void showMsg(String msg,int length){
		this.showMsg(msg, length, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 80);
	}
	
	/**
	 * 定义的Toast消息提示方法，在任何线程中都可以调用。
	 * 默认 是居中显示， x方向偏移量0，y方向的偏移量是80dp
	 */
	public synchronized void showMsg(int resId,int length){
		this.showMsg(mContext.getResources().getString(resId), length);
	}
	
	/**
	 * 定义的Toast消息提示方法，在任何线程中都可以调用。
	 * 根据调用者 指定 的gravity 和x，y的偏移量显示位置。单位dp
	 */
	public synchronized void showMsg(String msg,int length,int gravity, int xOffset, int yOffset){
		if(mHandler ==null ){ //有时候handler还没初始化好了。。。;
			if( Thread.currentThread().getId()==1){ 
				Toast.makeText(mContext, msg, length).show();
			}
			return;
		}
		mHandler.removeMessages(TOAST_MSG_SHOW);  //我觉得这个倒是也没必要。 但是有更好应该，懒得测试了。
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		int xoff  = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, xOffset,metrics ));
		int yoff  = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, yOffset,metrics ));
		MsgVo msgVo = new MsgVo(msg, length, gravity, xoff,yoff);
		mHandler.sendMessage(Message.obtain(null,TOAST_MSG_SHOW , msgVo));
	}
	
	/**
	 * 定义的Toast消息提示方法，在任何线程中都可以调用。
	 * 根据调用者 指定 的gravity 和x，y的偏移量显示位置。单位dp
	 */
	public synchronized void showMsg(int resId,int length,int gravity, int xOffset, int yOffset){
		this.showMsg(mContext.getResources().getString(resId), length, gravity, xOffset, yOffset);
	}
	
	/**
	 * 一个Toast的配置和信息
	 * @author joychine
	 *
	 */
	private static class MsgVo{
		String msg;
		int length;
		int gravity; 
		int xOffset; 
		int yOffset;
		public MsgVo() {}
		public MsgVo(String msgS, int length, int gravity, int xOffset,
				int yOffset) {
			this.msg = msgS;
			this.length = length;
			this.gravity = gravity;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
		}
		
	}
	
	
}
