package com.test.smartpot

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var potlist = 0
    var datalist = ArrayList<PotList>()
    var imglist = ArrayList<Int>()
    private lateinit var resultLauncher1: ActivityResultLauncher<Intent>
    //var pot1 = PotList("Pot1", "상추")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        potListInit()
        //화분 데이터 추가를 위해 인텐트를 통해 이동 후 돌아왔을 때 화분을 리스트에 추가하기 위한 런처
        resultLauncher1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            when(result.resultCode){
                Activity.RESULT_OK -> {
                    datalist.add(PotList(result.data?.getStringExtra("potname"),result.data?.getStringExtra("plantname")))
                    imglist.add(R.drawable.ic_flowerpot_320)
                    potlist = result.data?.getIntExtra("potlistcount",0)!!
                    addpot()
                    when(potlist){
                        1 -> {
                            result.data?.getStringExtra("potname")?.let { myApp.prefs.setString("pot1name", it) }
                            result.data?.getStringExtra("plantname")?.let { myApp.prefs.setString("pot1plantname", it) }
                            myApp.prefs.setString("potlist","1")
                        }
                        2 -> {
                            result.data?.getStringExtra("potname")?.let { myApp.prefs.setString("pot2name", it) }
                            result.data?.getStringExtra("plantname")?.let { myApp.prefs.setString("pot2plantname", it) }
                            myApp.prefs.setString("potlist","2")
                        }
                    }

                }
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(this,"화분 추가 취소",Toast.LENGTH_LONG).show()
                }
            }
        }
        add_pot.setOnClickListener {
            if(potlist >= 2){
                Toast.makeText(this,"화분을 더 추가할 수 없습니다",Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(this, addpotactivity::class.java).apply{
                    putExtra("listcount",potlist)
                }
                resultLauncher1.launch(intent)
            }
        }
        view_friend.setOnClickListener {
            val intent = Intent(this, friendActivity::class.java)
            startActivity(intent)
        }
        //datalist.add(pot1)
        //imglist.add(R.drawable.ic_flag_of_germany)
        addpot()
    }


    private fun addpot(){
        val adapter = PotListAdapter(this, R.layout.pot_list,datalist,imglist)
        val manager = GridLayoutManager(this,1)
        myrecycler1.layoutManager = manager
        myrecycler1.adapter = adapter
    }

    private fun potListInit(){
        when(myApp.prefs.getString("potlist","0").toInt()){
            1->{
                datalist.add(PotList(myApp.prefs.getString("pot1name","none"),myApp.prefs.getString("pot1plantname","none")))
                imglist.add(R.drawable.ic_flowerpot_320)
                potlist = 1

            }
            2-> {
                datalist.add(PotList(myApp.prefs.getString("pot1name","none"),myApp.prefs.getString("pot1plantname","none")))
                datalist.add(PotList(myApp.prefs.getString("pot2name","none"),myApp.prefs.getString("pot2plantname","none")))
                imglist.add(R.drawable.ic_flowerpot_320)
                imglist.add(R.drawable.ic_flowerpot_320)
                potlist = 2

            }
        }
    }
}