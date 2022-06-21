package com.test.smartpot

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_disease_chk.*
import kotlinx.android.synthetic.main.activity_pot1.*
import org.eclipse.paho.client.mqttv3.MqttMessage

class diseaseChkActivity : AppCompatActivity() {
    var sub_topic = ""
    val sub_topic2 = "sensor1/growmode"
    val server_uri = "tcp://:1883" //broker의 ip와 port
    var mymqtt: MyMqtt? = null
    var plantname = ""
    var diseaseToggleFlag1 = 0 // 첫번째 칸 접혀있는 상태
    var diseaseToggleFlag2 = 0 // 두번째 칸 접혀있는 상태
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

        //식물에 따라서 질병 예방 설명이 달라짐
        when(plantname){
            "Strawberry" ->{
                methodBtn1.text = "잿빛곰팡이병"
                methodBtn2.text = "흰가루병"
                methodBtn1.setOnClickListener { // 버튼 터치시
                    when(diseaseToggleFlag1){
                        0 -> { // 접혀있는 상태였다면
                            methodText1.text = "11111111111111\n11111111111111111\n111111111"
                            diseaseToggleFlag1 = 1
                        }
                        1 -> { // 펴져있는 상태였다면
                            methodText1.text = ""
                            diseaseToggleFlag1 = 0
                        }
                    }
                }
                methodBtn2.setOnClickListener {
                    when(diseaseToggleFlag2){
                        0 -> { // 접혀있는 상태였다면
                            methodText2.text = "11111111111111\n11111111111111111\n111111111"
                            diseaseToggleFlag2 = 1
                        }
                        1 -> { // 펴져있는 상태였다면
                            methodText2.text = ""
                            diseaseToggleFlag2 = 0
                        }
                    }
                }
            }
            "Lettuce" ->{
                methodBtn1.text = "균핵병"
                methodBtn2.text = "노균병"
                methodBtn1.setOnClickListener { // 버튼 터치시
                    when(diseaseToggleFlag1){
                        0 -> { // 접혀있는 상태였다면
                            methodText1.text = "11111111111111\n11111111111111111\n111111111"
                            diseaseToggleFlag1 = 1
                        }
                        1 -> { // 펴져있는 상태였다면
                            methodText1.text = ""
                            diseaseToggleFlag1 = 0
                        }
                    }
                }
                methodBtn2.setOnClickListener {
                    when(diseaseToggleFlag2){
                        0 -> { // 접혀있는 상태였다면
                            methodText2.text = "11111111111111\n11111111111111111\n111111111"
                            diseaseToggleFlag2 = 1
                        }
                        1 -> { // 펴져있는 상태였다면
                            methodText2.text = ""
                            diseaseToggleFlag2 = 0
                        }
                    }
                }
            }
            "Rosemary" ->{
                methodBtn1.text = "흰가루병"
                methodBtn2.text = "점무늬병"
                methodBtn1.setOnClickListener { // 버튼 터치시
                    when(diseaseToggleFlag1){
                        0 -> { // 접혀있는 상태였다면
                            methodText1.text = "11111111111111\n11111111111111111\n111111111"
                            diseaseToggleFlag1 = 1
                        }
                        1 -> { // 펴져있는 상태였다면
                            methodText1.text = ""
                            diseaseToggleFlag1 = 0
                        }
                    }
                }
                methodBtn2.setOnClickListener {
                    when(diseaseToggleFlag2){
                        0 -> { // 접혀있는 상태였다면
                            methodText2.text = "11111111111111\n11111111111111111\n111111111"
                            diseaseToggleFlag2 = 1
                        }
                        1 -> { // 펴져있는 상태였다면
                            methodText2.text = ""
                            diseaseToggleFlag2 = 0
                        }
                    }
                }
            }
            "Geranium" ->{
                methodBtn1.text = "갈색무늬병"
                methodBtn2.text = "잿빛곰팡이병"
                methodBtn1.setOnClickListener { // 버튼 터치시
                    when(diseaseToggleFlag1){
                        0 -> { // 접혀있는 상태였다면
                            methodText1.text = "11111111111111\n11111111111111111\n111111111"
                            diseaseToggleFlag1 = 1
                        }
                        1 -> { // 펴져있는 상태였다면
                            methodText1.text = ""
                            diseaseToggleFlag1 = 0
                        }
                    }
                }
                methodBtn2.setOnClickListener {
                    when(diseaseToggleFlag2){
                        0 -> { // 접혀있는 상태였다면
                            methodText2.text = "11111111111111\n11111111111111111\n111111111"
                            diseaseToggleFlag2 = 1
                        }
                        1 -> { // 펴져있는 상태였다면
                            methodText2.text = ""
                            diseaseToggleFlag2 = 0
                        }
                    }
                }
            }
        }
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
            sub_topic2 -> { // 질병값 받을 때 식물에 따라서 걸리는 질병이 다르므로 분류하여 적용
                when(plantname){
                    "Strawberry" ->{

                    }
                    "Lettuce" ->{

                    }
                    "Rosemary" ->{

                    }
                    "Geranium" ->{

                    }
                }
                var growpayload = String(message.payload).split(':')
                diseaseResult.text = growpayload[1]
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mymqtt?.disconnect()
    }
}

