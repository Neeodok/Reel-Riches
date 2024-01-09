package redl.bil.vblin.logic

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import redl.bil.vblin.R

var isSoundOn = true


fun doSounds(activity: AppCompatActivity, sound: SoundConst){
    if (isSoundOn){
        when(sound){
            SoundConst.SPIN -> {
                MediaPlayer.create(activity, R.raw.slotspin).start()
            }
            SoundConst.BET -> {
                MediaPlayer.create(activity, R.raw.slotbet).start()
            }
            SoundConst.ROLL -> {
                MediaPlayer.create(activity, R.raw.slotroll).start()
            }
            SoundConst.WIN -> {
            MediaPlayer.create(activity, R.raw.slotwin).start()
        }

            else -> {}
        }
    }
}

enum class SoundConst {
    SPIN, BET, ROLL, WIN
}

const val prefBankKey = "bank_value_key"

const val prefBetKey = "bet_value_key"

const val prefTotalWin = "total_win_key"


const val prefSoundOnKey = "sound_key"


const val SELECTED_CARD_KEY = "selectedCard"
const val SELECTED_PASS_KEY = "selectedPass"

const val BUTTON_CARD1 = 1
const val BUTTON_CARD2 = 2
const val BUTTON_PASS = 3
