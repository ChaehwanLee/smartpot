package com.test.smartpot

import android.content.Intent

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import kotlinx.android.synthetic.main.activity_pot1.*
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.math.roundToInt

class pot1Activity : AppCompatActivity() {

    private val sub_topic = "sensor1/#"
    private val server_uri = "tcp://35.182.237.235:1883" //broker의 ip와 port
    private var mymqtt: MyMqtt? = null
    private var soilWaterValue = 0.0
    private var waterPumpValue = 0
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
        // 워터펌프 양 출력
        waterPump.setOnClickListener {
            waterPumpValue = water_Amount.text.toString().toInt()
            mymqtt?.publish("iot/waterPump", waterPumpValue.toString())
        }
    }
    //mqtt 값을 받는 경우
    fun onReceived(topic:String,message: MqttMessage){
        val myTopic = topic.split('/')
        val myPayload = String(message.payload).split(':')
        var mywaterLevel = 500.0
        when(myTopic[0]){
            "sensor1" -> {
                soilWaterValue = myPayload[1].toDouble()
                waterLevel.text = (myPayload[0].toDouble() * 5.0).roundToInt().toString()
                Log.d("testt",myPayload.toString())
                airtemp.text = myPayload[2]

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