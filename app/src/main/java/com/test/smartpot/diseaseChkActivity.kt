package com.test.smartpot

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_disease_chk.*
import org.eclipse.paho.client.mqttv3.MqttMessage

class diseaseChkActivity : AppCompatActivity() {
    var sub_topic = ""
    val sub_topic2 = "sensor1/disease"
    val server_uri = "tcp://:1883" //broker의 ip와 port
    var mymqtt: MyMqtt? = null
    var plantname = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disease_chk)
        val intent = intent
        diseasepotchkname.text = intent.getStringExtra("potname").toString()
        plantname = intent.getStringExtra("plantname").toString()
        sub_topic = "iot/awslevelDisease/$plantname" // 해당하는 식물이름에 따른 주제를 구독

        mymqtt = MyMqtt(this, server_uri)
        mymqtt?.mysetCallback(::onReceived)
        mymqtt?.connect(arrayOf<String>(sub_topic, sub_topic2))
        //mymqtt?.publish("sensor1/getDisease", "getPot1disease") // 질병자료를 요청


        diseaseCameraBtn.setOnClickListener {
            mymqtt?.publish("iot/actDiseaseCamera", "cameraon")
        }
    }

    fun onReceived(topic:String,message: MqttMessage){
        //토픽 설정 필요
        when(topic){
            // 이미지를 받은 경우 mqttImg로 출력
            sub_topic -> {
                val msg = message.payload
                val image= BitmapFactory.decodeByteArray(msg, 0, msg.size);
                mqttImg.setImageBitmap(image)
            }
            sub_topic2 -> {
                val disease = String(message.payload).split(':')
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mymqtt?.disconnect()
    }
}

