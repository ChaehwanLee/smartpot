package com.test.smartpot

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat

import kotlinx.android.synthetic.main.activity_pot1.*
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.math.roundToInt

class pot1Activity : AppCompatActivity() {
    private var soilWaterFlag = 0 // 정상상태 - 0
    private var tempModeFlag = 0 // 정상상태 - 0
    private var soilWaterValue = 0.0
    private var waterPumpValue = 0
    private var soilWaterAlertValue = 20.0
    private var tempAlertValue = 8

    private val sub_topic = "sensor1/#" // 구독할 토픽
    private val server_uri = "tcp://:1883" //broker의 ip와 port
    private var mymqtt: MyMqtt? = null




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
        Thread{
            // 알림의 설정
            //val myTopic = topic.split('/')
            val myPayload = String(message.payload).split(':')
            var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_flowerpot_320)
            var builder1 = getNotificationBuilder("channel1","첫번째 채널")
            var builder2 = getNotificationBuilder("channel2", "두번째 채널")
            soilWaterValue = myPayload[1].toDouble()
            runOnUiThread {
                waterLevel.text = (myPayload[0].toDouble() * 5.0).roundToInt().toString()

            }

            // 토양 수분이 일정 값 보다 작으면 알림을 보내는 코드
            if (soilWaterValue < soilWaterAlertValue){
                if(soilWaterFlag == 0){
                    soilWaterFlag = 1 // 경고값보다 낮음
                    // 알림
                    builder1.setTicker("토양 수분 값")
                    builder1.setSmallIcon(R.drawable.ic_flowerpot_320)
                    builder1.setLargeIcon(bitmap)
                    builder1.setNumber(100)
                    builder1.setAutoCancel(true)
                    builder1.setContentTitle("토양 수분 경고")
                    builder1.setContentText(myPayload[1])
                    var notication1 = builder1.build()
                    var mng = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    mng.notify(10, notication1)

                }
                runOnUiThread{
                    soilWater.setTextColor(Color.RED)
                    soilWater.text = myPayload[1]
                }
            }else{
                soilWaterFlag = 0
                runOnUiThread{
                    soilWater.setTextColor(Color.BLUE)
                    soilWater.text = myPayload[1]
                }
            }
            // 온도가 일정 값보다 작으면 경고를 보내는 코드
            if(myPayload[2].toInt() < tempAlertValue){
                if(tempModeFlag == 0){
                    tempModeFlag = 1 // 경고값보다 낮음
                    // 알림
                    builder2.setTicker("온도값")
                    builder2.setSmallIcon(R.drawable.ic_flowerpot_320)
                    builder2.setLargeIcon(bitmap)
                    builder2.setNumber(100)
                    builder2.setAutoCancel(true)
                    builder2.setContentTitle("온도 경고")
                    builder2.setContentText(myPayload[2])
                    var notication2 = builder2.build()
                    var mng = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    mng.notify(10, notication2)
                }
                runOnUiThread {
                    airtemp.text = myPayload[2]
                }
            }else{
                tempModeFlag = 0
                runOnUiThread {
                    airtemp.text = myPayload[2]
                }
            }
        }.start()
    }

    //
    fun getNotificationBuilder(id: String, name: String): NotificationCompat.Builder {
        var builder: NotificationCompat.Builder? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager // 채널 정보를 시스템에 등록할 객체 생성
            var channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            manager.createNotificationChannel(channel) //채널 정보를 시스템에 등록
            builder = NotificationCompat.Builder(this, id) // 빌더를 생성만 해 놓음
        } else {
            builder = NotificationCompat.Builder(this)
        }
        return builder
    }

    override fun onDestroy() {
        super.onDestroy()
        mymqtt?.disconnect()
    }
}