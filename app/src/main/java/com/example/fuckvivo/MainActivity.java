package com.example.fuckvivo;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by xsc on 2020/1/31
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAccessibility(null);
    }

    public void checkAccessibility(View view) {
        boolean accessibilitySettingsOn = AccessibilityUtils.isAccessibilityServiceEnabled(this, FuckVivoService.class.getName());
        LogUtils.e(TAG, "accessibilitySettingsOn:" + accessibilitySettingsOn);
        if (!accessibilitySettingsOn) {
            AccessibilityUtils.goToAccessibilitySettings(this);
        } else {
            Toast.makeText(this, "无障碍服务已开启", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkOverlay(View view) {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1);
        }else {
            Toast.makeText(this, "悬浮窗已开启", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                }else {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    Toast.makeText(this, "未被授予权限，相关功能不可用", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
