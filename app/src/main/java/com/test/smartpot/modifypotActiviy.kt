package com.test.smartpot

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_modifypot_activiy.*


import java.util.regex.Pattern

class modifypotActiviy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifypot_activiy)
        val intent = intent
        modifypotname.hint = intent.getStringExtra("potname")
        modifyplantName.hint = intent.getStringExtra("potplantname")
        modifyPotOk.setOnClickListener {
            if(modifypotname.text.isEmpty() || modifyplantName.text.isEmpty()){
                Toast.makeText(this,"정보를 모두 입력해주세요",Toast.LENGTH_LONG).show()
            }else {
                intent.putExtra("potname", modifypotname.text.toString())
                intent.putExtra("plantname", modifyplantName.text.toString())
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        modifyplantName.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
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
        modifyPotCancel.setOnClickListener{
            intent.putExtra("cancel","화분 추가를 취소")
            setResult(Activity.RESULT_CANCELED,intent)
            finish()
        }
    }
}