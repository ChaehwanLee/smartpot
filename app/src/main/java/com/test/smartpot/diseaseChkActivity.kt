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
    val sub_topic = "iot/#"
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
        //토픽의 수신을 처리
        //EditText에 내용을 출력하기, 영상출력, 도착된 메시지 안에서 온도랑 습도 데이터를 이용해서 차트그리기,
        //모션 감지시지 알림 발생 등
        val msg = message.payload
        val image= BitmapFactory.decodeByteArray(msg, 0, msg.size);
        mqttImg.setImageBitmap(image)
    }
    override fun onDestroy() {
        super.onDestroy()
        mymqtt?.disconnect()
    }
}

