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

    var potlist = 1
    var datalist = ArrayList<PotList>()
    var imglist = ArrayList<Int>()
    private lateinit var resultLauncher1: ActivityResultLauncher<Intent>
    //var pot1 = PotList("Pot1", "상추")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultLauncher1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            when(result.resultCode){
                Activity.RESULT_OK -> {
                    datalist.add(PotList(result.data?.getStringExtra("potname"),result.data?.getStringExtra("plantname")))
                    imglist.add(R.drawable.ic_flowerpot_320)
                    potlist += 1
                    Toast.makeText(this,"${result.data?.getStringExtra("potlistcount")}",Toast.LENGTH_LONG).show()
                    addpot()
                }
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(this,"화분 추가 취소",Toast.LENGTH_LONG).show()
                }
            }
        }
        add_pot.setOnClickListener {
            val intent = Intent(this, addpotactivity::class.java).apply{
                putExtra("listcount","$potlist")
            }
            resultLauncher1.launch(intent)
        }
        //datalist.add(pot1)
        //imglist.add(R.drawable.ic_flag_of_germany)
        addpot()
    }

    fun addpot(){
        val adapter = PotListAdapter(this, R.layout.pot_list,datalist,imglist)
        val manager = GridLayoutManager(this,1)
        myrecycler1.layoutManager = manager
        myrecycler1.adapter = adapter
    }
}