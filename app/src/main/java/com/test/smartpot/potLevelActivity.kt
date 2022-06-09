package com.test.smartpot


import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_pot_level.*
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class potLevelActivity : AppCompatActivity() {
    val sub_topic = "iot/currentLevelCamera"
    val server_uri = "tcp://192.168.0.24:1883" //broker의 ip와 port
    var mymqtt: MyMqtt? = null
    lateinit var image1: Bitmap
    var msg1: ByteArray? = null
    lateinit var imageuri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pot_level)
        mymqtt = MyMqtt(this, server_uri)
        mymqtt?.mysetCallback(::onReceived)
        mymqtt?.connect(arrayOf<String>(sub_topic))
        val intent = intent
        potlevelname.text = intent.getStringExtra("potname")
        //공유하기 클릭시 원하는 곳으로 공유
        snsShareBtn.setOnClickListener {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,"지금 ${potlevelname.text}의 레벨은 ${potLevel.text}")
                putExtra(Intent.EXTRA_STREAM, imageuri,)
                type = "image/png"
            }
            startActivity(Intent.createChooser(shareIntent, "Helllo"))

        }
        levelCameraBtn.setOnClickListener {
            mymqtt?.publish("iot/actLevelCamera", "cameraon")
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    fun onReceived(topic:String, message: MqttMessage){
        when(topic){
            "iot/currentLevelCamera" -> {
                val msg = message.payload
                val image= BitmapFactory.decodeByteArray(msg, 0, msg.size);
                image1 = image
                levelchkcamera.setImageBitmap(image)
                saveImageOnAboveAndroidQ(image1)
            }
            "iot/actLevelCamera" -> {
                Log.d("test","카메라 한번 찍기 테스트")
            }
        }
    }
    //Android Q (Android 10, API 29 이상에서는 이 메서드를 통해서 이미지를 저장한다.)
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageOnAboveAndroidQ(bitmap: Bitmap) {
        val fileName = System.currentTimeMillis().toString() + ".png" // 파일이름 현재시간.png
        /*
        * ContentValues() 객체 생성.
        * ContentValues는 ContentResolver가 처리할 수 있는 값을 저장해둘 목적으로 사용된다.
        * */
        val contentValues = ContentValues()
        contentValues.apply {
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/ImageSave") // 경로 설정
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName) // 파일이름을 put해준다.
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.IS_PENDING, 1) // 현재 is_pending 상태임을 만들어준다.
            // 다른 곳에서 이 데이터를 요구하면 무시하라는 의미로, 해당 저장소를 독점할 수 있다.
        }
        // 이미지를 저장할 uri를 미리 설정해놓는다.
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            imageuri = uri
        }
        try {
            if(uri != null) {
                val image = contentResolver.openFileDescriptor(uri, "w", null)
                // write 모드로 file을 open한다.

                if(image != null) {
                    val fos = FileOutputStream(image.fileDescriptor)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    //비트맵을 FileOutputStream를 통해 compress한다.
                    fos.close()

                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0) // 저장소 독점을 해제한다.
                    contentResolver.update(uri, contentValues, null, null)
                }
            }
        } catch(e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}