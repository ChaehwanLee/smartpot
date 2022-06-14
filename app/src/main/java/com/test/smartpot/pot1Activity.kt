package com.test.smartpot

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_disease_chk.*
import kotlinx.android.synthetic.main.activity_pot1.*
import org.eclipse.paho.client.mqttv3.MqttMessage

class pot1Activity : AppCompatActivity() {

    private val sub_topic = "sensor1/#"
    private val server_uri = "tcp://:1883" //broker의 ip와 port
    private var mymqtt: MyMqtt? = null
    private var soilWaterValue = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pot1)
        //mqtt 기본 설정
        mymqtt = MyMqtt(this, server_uri)
        mymqtt?.mysetCallback(::onReceived)
        mymqtt?.connect(arrayOf<String>(sub_topic))
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
    //mqtt 값을 받는 경우
    fun onReceived(topic:String,message: MqttMessage){
        val myTopic = topic.split('/')
        val myPayload = String(message.payload).split(':')
        Log.d("text123",myTopic[1])
        when(myTopic[0]){
            "sensor1" -> {
                Log.d("text123", myPayload[1])
                soilWaterValue = myPayload[1].toDouble()
                Log.d("text123", soilWaterValue.toString())
                if (soilWaterValue < 20){
                    soilWater.setTextColor(Color.RED)
                    soilWater.text = myPayload[1]
                }else{
                    soilWater.setTextColor(Color.BLUE)
                    soilWater.text = myPayload[1]
                }
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        mymqtt?.disconnect()
    }
}