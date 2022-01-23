package com.unicorn.highlight

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson
import com.jaychang.st.SimpleText
import com.unicorn.refuel.data.model.RecognizeResult

class MainActivity : RecognizeActivity() {

  private  var text = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val simple = SimpleText.from(text).all("测试")  .textColor(android.R.color.darker_gray)
        textView.text = simple


        //

        launcherRecognize =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                if (activityResult.data == null) return@registerForActivityResult
                RecognizeService.recognizeAccurateBasic(
                    this,
                    FileUtil.getSaveFile(this).absolutePath
                ) {
                        result ->
                    val recognizeResult = Gson().fromJson(result, RecognizeResult::class.java)
                     text = recognizeResult.words_result.joinToString { it.words }
                    textView.text = text
                }
            }


        val button1 = findViewById<Button>(R.id.button)
        button1.setOnClickListener { recognize() }

        val button2 = findViewById<Button>(R.id.button2)
        val editText = findViewById<EditText>(R.id.editText)
        button2.setOnClickListener {
            val query = editText.text.toString()
            val simple = SimpleText.from(text).all(query)  .textColor(android.R.color.darker_gray)
            textView.text = simple
        }
    }

}