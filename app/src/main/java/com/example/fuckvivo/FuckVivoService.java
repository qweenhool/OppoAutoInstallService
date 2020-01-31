package com.example.fuckvivo;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import java.util.List;

/**
 * 参考：
 * https://blog.csdn.net/sw69366/article/details/99691377
 * https://blog.csdn.net/ybf326/article/details/83928353
 * https://www.jb51.net/article/172238.htm
 */
public class FuckVivoService extends BaseAccessibilityService {

    private static final String TAG = "FuckVivoService";

    private Handler handler = new Handler();

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {


        CharSequence packageName = accessibilityEvent.getPackageName();

        LogUtils.e(TAG, packageName.toString());


        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();


        {
            if (rootInActiveWindow != null) {
                List<AccessibilityNodeInfo> installBtns = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.android.packageinstaller:id/continue_button");
                if (installBtns != null && installBtns.size() > 0) {
                    if (installBtns.get(0).getText().toString().equalsIgnoreCase("继续安装")) {
                        installBtns.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        LogUtils.e(TAG, "点击“继续安装”");
                    }
                }
            }
        }

        {
            if (rootInActiveWindow != null) {
                List<AccessibilityNodeInfo> installBtns = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.android.packageinstaller:id/ok_button");
                if (installBtns != null && installBtns.size() > 0) {

                    AccessibilityNodeInfo installNode = installBtns.get(0);
                    if (installNode != null) {
                        // installNode无法调用performAction直接点击，需要根据坐标点击
                        clickOnScreen(100, 1950, new GestureResultCallback() {
                            @Override
                            public void onCompleted(GestureDescription gestureDescription) {
                                super.onCompleted(gestureDescription);
                                LogUtils.e(TAG, "点击“安装” 成功");
                            }

                            @Override
                            public void onCancelled(GestureDescription gestureDescription) {
                                super.onCancelled(gestureDescription);
                                LogUtils.e(TAG, "installNode Gesture onCancelled() called");

                            }
                        }, null);

                    }
                }
            }
        }

        {
            if (rootInActiveWindow != null) {
                List<AccessibilityNodeInfo> dialog_pwd = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.coloros.safecenter:id/et_login_passwd_edit");
                if (dialog_pwd != null && dialog_pwd.size() > 0) {
                    LogUtils.e(TAG, "找到输入框 - 自动填入密码");
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "c12345678");
                    dialog_pwd.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);

                    List<AccessibilityNodeInfo> button1 = rootInActiveWindow.findAccessibilityNodeInfosByViewId("android:id/button1");

                    if (button1 != null && button1.size() > 0) {
                        button1.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }

                    // fix: 2020/1/31 密码框消失后不会自动点击“安装”按钮
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e(TAG, "1:show");
                            AlertDialog.Builder builder = new AlertDialog.Builder(FuckVivoService.this);
                            builder.setTitle("提示");
                            builder.setMessage("该下车了");
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            final AlertDialog dialog = builder.create();
                            //在dialog  show方法之前添加如下代码，表示该dialog是一个系统的dialog**
                            dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY));
                            dialog.show();

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    LogUtils.e(TAG, "2:dismiss");
                                    dialog.dismiss();
                                }
                            }, 1000);
                        }
                    }, 2000);


                } else {
                    LogUtils.e(TAG, "没找到输入框");
                }
            }
        }


//        {
//            if (rootInActiveWindow != null) {
//                List<AccessibilityNodeInfo> button2 = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.android.packageinstaller:id/launch_button");
//                if (button2 != null && button2.size() > 0) {
//                    button2.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                }
//            }
//        }


    }

    /**
     * 通过AccessibilityService在屏幕上某个位置单击
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void clickOnScreen(float x,
                              float y,
                              AccessibilityService.GestureResultCallback callback,
                              Handler handler) {

        Path path = new Path();
        path.moveTo(x, y);
        gestureOnScreen(path, 0, 100, callback, handler);

    }

    /**
     * 通过AccessibilityService在屏幕上模拟手势
     *
     * @param path 手势路径
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void gestureOnScreen(Path path,
                                long startTime,
                                long duration,
                                AccessibilityService.GestureResultCallback callback,
                                Handler handler) {

        GestureDescription description = new GestureDescription.Builder()
                .addStroke(new GestureDescription.StrokeDescription(path, startTime, duration))
                .build();
        dispatchGesture(description, callback, handler);
    }

    @Override
    public void onInterrupt() {

    }
}
