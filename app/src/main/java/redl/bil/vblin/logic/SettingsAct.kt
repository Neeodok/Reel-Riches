package redl.bil.vblin.logic

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import redl.bil.vblin.databinding.SettScreenBinding


class SettingsAct : AppCompatActivity() {

    private val binding by lazy { SettScreenBinding.inflate(layoutInflater)}

    private val sharedPrefs by lazy { getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) }

    private val rbSoundOn by lazy { binding.soundOn }
    private val rbSoundOff by lazy { binding.soundOff }
    private val btnRules by lazy { binding.btnRule }
    private val rulesinfo by lazy {binding.rulesinfo  }
    private var rulesVisible = false
    private val isSoundOn by lazy { sharedPrefs.getBoolean(prefSoundOnKey,true) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnResScore.setOnClickListener {
            sharedPrefs.edit {
                putInt(prefBetKey, 100)
                putInt(prefBankKey, 10000)
                putInt(prefTotalWin, 0)
                apply()
            }

            Toast.makeText(this, "score deleted", Toast.LENGTH_SHORT).show()
        }


        getvalues()

        rbSoundOn.setOnClickListener {
            sharedPrefs.edit().putBoolean(prefSoundOnKey, true).apply()
        }

        rbSoundOff.setOnClickListener {
            sharedPrefs.edit().putBoolean(prefSoundOnKey, false).apply()

        }

        btnRules.setOnClickListener {
            animClickView(it,this)

            rulesVisible = !rulesVisible
            rulesinfo.visibility = if (rulesVisible) View.VISIBLE else View.INVISIBLE
        }

    }
    private fun getvalues (){

        rbSoundOn.isChecked = isSoundOn
        rbSoundOff.isChecked = !isSoundOn


    }

}
