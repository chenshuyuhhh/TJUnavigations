package com.chenshuyusc.tjunavigations.base

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import com.chenshuyusc.tjunavigations.R
import java.util.ArrayList

/**
 * 获取权限
 */
class CheckPermissionActivity : AppCompatActivity() {
    /**
     * 需要进行检测的权限数组
     */
    protected var needPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )

    private val PERMISSON_REQUESTCODE = 0

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private var isNeedCheck = true

    protected override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= 23 && applicationInfo.targetSdkVersion >= 23) {
            if (isNeedCheck) {
                checkPermissions(*needPermissions)
            }
        }
    }

    /**
     *
     * @param permissions
     * @since 2.5.0
     */
    private fun checkPermissions(vararg permissions: String) {
        try {
            if (Build.VERSION.SDK_INT >= 23 && applicationInfo.targetSdkVersion >= 23) {
                val needRequestPermissonList = findDeniedPermissions(permissions as Array<String>)
                if (null != needRequestPermissonList && needRequestPermissonList.isNotEmpty()) {
                    val array = needRequestPermissonList.toTypedArray()
                    val method = javaClass.getMethod(
                        "requestPermissions",
                        Array<String>::class.java, Int::class.javaPrimitiveType
                    )

                    method.invoke(this, array, PERMISSON_REQUESTCODE)
                }
            }
        } catch (e: Throwable) {
        }

    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private fun findDeniedPermissions(permissions: Array<String>): List<String> {
        val needRequestPermissonList = ArrayList<String>()
        if (Build.VERSION.SDK_INT >= 23 && applicationInfo.targetSdkVersion >= 23) {
            try {
                for (perm in permissions) {
                    val checkSelfMethod = javaClass.getMethod("checkSelfPermission", String::class.java)
                    val shouldShowRequestPermissionRationaleMethod = javaClass.getMethod(
                        "shouldShowRequestPermissionRationale",
                        String::class.java
                    )
                    if (checkSelfMethod.invoke(
                            this,
                            perm
                        ) as Int != PackageManager.PERMISSION_GRANTED || shouldShowRequestPermissionRationaleMethod.invoke(
                            this,
                            perm
                        ) as Boolean
                    ) {
                        needRequestPermissonList.add(perm)
                    }
                }
            } catch (e: Throwable) {

            }

        }
        return needRequestPermissonList
    }

    /**
     * 检测是否所有的权限都已经授权
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private fun verifyPermissions(grantResults: IntArray): Boolean {
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @TargetApi(23)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, paramArrayOfInt: IntArray
    ) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog()
                isNeedCheck = false
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private fun showMissingPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.notifyTitle)
        builder.setMessage(R.string.notifyMsg)

        // 拒绝, 退出应用
        builder.setNegativeButton(
            R.string.cancel
        ) { _, _ -> finish() }

        builder.setPositiveButton(
            R.string.setting
        ) { _, _ -> startAppSettings() }

        builder.setCancelable(false)

        builder.show()
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private fun startAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        )
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}