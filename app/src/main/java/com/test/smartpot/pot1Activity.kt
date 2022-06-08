package com.test.smartpot

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_pot1.*

class pot1Activity : AppCompatActivity() {

    private val sub_topic = "iot/sensor"
    private val server_uri = "tcp://192.168.0.24:1883" //broker의 ip와 port
    private var mymqtt: MyMqtt? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pot1)
        val intent = intent
        //인텐트에서 가져오기
        pot1name.text = intent.getStringExtra("potname").toString()
        plantName.text = intent.getStringExtra("plantname").toString()
        //리스트에 추가

        diseaseChk.setOnClickListener {
            val disintent = Intent(this, diseaseChkActivity::class.java).apply {
                putExtra("potname","${pot1name.text}")
            }
            startActivity(disintent)
        }
        levelChk.setOnClickListener {
            val levelIntent = Intent(this,potLevelActivity::class.java).apply{
                putExtra("potname",pot1name.text.toString())
                putExtra("plantname",plantName.text.toString())
            }
            startActivity(levelIntent)
        }
    }
}