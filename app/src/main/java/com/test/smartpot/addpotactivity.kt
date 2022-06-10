package com.test.smartpot

import android.app.Activity
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import kotlinx.android.synthetic.main.addpot.*
import java.util.regex.Pattern

class addpotactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addpot)
        val intent = intent
        var potlist = intent.getIntExtra("listcount", 0)
        plantName.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            val chkPtn: Pattern =
                Pattern.compile("^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힇]+$")
            if (source == "" || chkPtn.matcher(source).matches()) {
                return@InputFilter source
            }
            else{
                Toast.makeText( this, "문자만 입력이 가능합니다", Toast.LENGTH_SHORT).show()
                return@InputFilter ""
            }
        })
        //request_code별로 구분해서 작업할 수 있다.
        addPotCancel.setOnClickListener{
            intent.putExtra("cancel","화분 추가를 취소")
            setResult(Activity.RESULT_CANCELED,intent)
            finish()
        }
        addPotOk.setOnClickListener {
            if(potname.text.isEmpty() || plantName.text.isEmpty()){
                Toast.makeText(this,"정보를 모두 입력해주세요",Toast.LENGTH_LONG).show()
            }else{
                if(potname.text.toString().trim().isEmpty() or plantName.text.toString().trim().isEmpty()){
                    Toast.makeText(this,"정보를 모두 입력해주세요",Toast.LENGTH_LONG).show()
                }else{
                    potlist += 1
                    intent.putExtra("potname",potname.text.toString())
                    intent.putExtra("plantname",plantName.text.toString())
                    intent.putExtra("addok","추가됨 - 현재 화분 갯수 : $potlist")
                    intent.putExtra("potlistcount",potlist)
                    setResult(Activity.RESULT_OK,intent)
                    finish()
                }
            }


        }
    }
}