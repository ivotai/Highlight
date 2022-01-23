package com.unicorn.highlight

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.ui.camera.CameraActivity


abstract class RecognizeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAccessToken()
    }

    protected lateinit var launcherRecognize: ActivityResultLauncher<Intent>

    protected fun recognize() {
        if (hasGotToken)
            launcherRecognize.launch(getRecognizeIntent())
    }

    private var hasGotToken = false

    private fun initAccessToken() {
        OCR.getInstance(this).initAccessToken(object : OnResultListener<AccessToken> {
            override fun onResult(result: AccessToken?) {
                hasGotToken = true
            }

            override fun onError(ocrError: OCRError?) {
            }
        }, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 释放内存资源
        OCR.getInstance(this).release()
    }

    private fun getRecognizeIntent(): Intent {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra(
            CameraActivity.KEY_OUTPUT_FILE_PATH,
            FileUtil.getSaveFile(this).absolutePath
        )
        intent.putExtra(
            CameraActivity.KEY_CONTENT_TYPE,
            CameraActivity.CONTENT_TYPE_GENERAL
        )
        return intent
    }

}
