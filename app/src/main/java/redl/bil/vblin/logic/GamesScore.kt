package redl.bil.vblin.logic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameScoresModel(): ViewModel() {

    var bankLD : MutableLiveData<Int> = MutableLiveData()
    var betLD : MutableLiveData<Int> = MutableLiveData()
    var totalWinLD : MutableLiveData<Int> = MutableLiveData()

    init {
        bankLD.value = 10000
        betLD.value = 100
        totalWinLD.value = 0

    }

}
