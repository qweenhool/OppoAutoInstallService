package com.example.fuckvivo;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * author : xsc
 * date   : 2019/11/22 17:31
 * desc   :
 */
public class AccessibilityUtils {
    private static final String TAG = "AccessibilityUtils";

    /**
     * 无障碍服务是否开启
     *
     * @param context
     * @param service 服务全类名
     * @return
     */
    public static boolean isAccessibilityServiceEnabled(Context context, String service) {
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        String settingValue = Settings.Secure.getString(
                context.getApplicationContext().getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (settingValue != null) {
            mStringColonSplitter.setString(settingValue);
            while (mStringColonSplitter.hasNext()) {
                String accessibilityService = mStringColonSplitter.next();
                LogUtils.e(TAG, "accessibilityService:" + accessibilityService);
                if (accessibilityService.contains(service)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 前往开启辅助服务界面
     */
    public static void goToAccessibilitySettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
