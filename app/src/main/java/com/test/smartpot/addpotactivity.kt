package com.test.smartpot

import android.app.Activity
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.addpot.*

class addpotactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addpot)
        val intent = intent
        var potlist = intent.getStringExtra("listcount")
        //request_code별로 구분해서 작업할 수 있다.
        addPotCancel.setOnClickListener{
            intent.putExtra("cancel","화분 추가를 취소")
            setResult(Activity.RESULT_CANCELED,intent)
            finish()
        }
        addPotOk.setOnClickListener {
            potlist += 1
            intent.putExtra("potname",potname.text.toString())
            intent.putExtra("plantname",plantName.text.toString())
            intent.putExtra("addok","추가됨 - 현재 화분 갯수 : $potlist")
            intent.putExtra("potlistcount","$potlist")
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }
}