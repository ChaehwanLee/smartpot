package com.test.smartpot

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_disease_chk.*
import kotlinx.android.synthetic.main.activity_pot1.*
import kotlinx.android.synthetic.main.activity_pot1.pot1name
import org.eclipse.paho.client.mqttv3.MqttMessage

class diseaseChkActivity : AppCompatActivity() {
    val sub_topic = "iot/diseaseChkCamera"
    val server_uri = "tcp://192.168.0.24:1883" //broker의 ip와 port
    var mymqtt: MyMqtt? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disease_chk)
        mymqtt = MyMqtt(this, server_uri)
        mymqtt?.mysetCallback(::onReceived)
        mymqtt?.connect(arrayOf<String>(sub_topic))
        val intent = intent
        diseasepotchkname.text = intent.getStringExtra("potname").toString()
    }

    fun onReceived(topic:String,message: MqttMessage){
        //토픽 설정 필요
//        when(topic){
//
//        }
        // 이미지를 받은 경우 mqttImg로 출력
        val msg = message.payload
        val image= BitmapFactory.decodeByteArray(msg, 0, msg.size);
        mqttImg.setImageBitmap(image)
    }
    override fun onDestroy() {
        super.onDestroy()
        mymqtt?.disconnect()
    }
}

