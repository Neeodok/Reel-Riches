package redl.bil.vblin.logic

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import redl.bil.vblin.R
import redl.bil.vblin.databinding.GameScreenBinding
import kotlin.random.Random

class GameLogi : AppCompatActivity() {

    private val binding by lazy { GameScreenBinding.inflate(layoutInflater) }

    private lateinit var viewModel: GameScoresModel



    private val btnSpin by lazy { binding.btnSpin }

    private val btnBetUpp by lazy { binding.btnUp }
    private val btnBetDowns by lazy { binding.btnDown }
    private val tvBetScore by lazy { binding.tvBetScore }
    private val tvBankScore by lazy { binding.tvBankScore }
    private val tvWinScore by lazy { binding.tvWinScore }
    private val line1 by lazy { binding.linewin3 }
    private val line2 by lazy { binding.linewin2 }
    private val line3 by lazy { binding.linewin1 }
    private val line4 by lazy { binding.linewin4 }
    private val line5 by lazy { binding.linewin5 }
    private val line6 by lazy { binding.linewin6 }
    private val line7 by lazy { binding.linewin7 }
    private var isClickable = false



    private val drawab = listOf(
        R.drawable.element_1,
        R.drawable.element_2,
        R.drawable.element_3,
        R.drawable.element_4,
        R.drawable.element_5,
        R.drawable.element_6,
        R.drawable.element_7,
        R.drawable.element_8,
        R.drawable.element_9,
    )


    private val scrolls by lazy {
        listOf(
            binding.scroll1,
            binding.scroll2,
            binding.scroll3,
            binding.scroll4,
            binding.scroll5,
        )
    }

    private val holderSlots by lazy {
        listOf<ViewGroup>(
            binding.holder1,
            binding.holder2,
            binding.holder3,
            binding.holder4,
            binding.holder5,
        )
    }

    private var firstViewsMatrix = mutableListOf(
        mutableListOf<View>(),
        mutableListOf<View>(),
        mutableListOf<View>(),
        mutableListOf<View>(),
        mutableListOf<View>(),
    )

    private var animsMatrix = mutableListOf(
        mutableListOf<ObjectAnimator>(),
        mutableListOf<ObjectAnimator>(),
        mutableListOf<ObjectAnimator>(),
        mutableListOf<ObjectAnimator>(),
        mutableListOf<ObjectAnimator>(),
    )

    private var lastViewMatrix = mutableListOf(
        mutableListOf<View>(),
        mutableListOf<View>(),
        mutableListOf<View>(),
        mutableListOf<View>(),
        mutableListOf<View>(),
    )

    private var matrix = mutableListOf(
        mutableListOf(0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0),
    )


    private lateinit var betLiveData: MutableLiveData<Int>
    private lateinit var bankLiveData: MutableLiveData<Int>
    private lateinit var totalWinLiveData: MutableLiveData<Int>


    private val sharedPrefs by lazy { getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) }

    private val isSoundOns by lazy { sharedPrefs.getBoolean(prefSoundOnKey, true) }

    private val cardBack1 by lazy { binding.cardBack1 }
    private val cardSelect by lazy { binding.cardChoice }
    private val cardBack2 by lazy { binding.cardBack2 }
    private val pass by lazy { binding.btnPass }
    var isCardGameFinish = false


    private val cards = listOf(
        R.drawable.card_black,
        R.drawable.card_red
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        isSoundOn = isSoundOns

        viewModel = ViewModelProvider(this)[GameScoresModel::class.java]

        betLiveData = viewModel.betLD
        bankLiveData = viewModel.bankLD
        totalWinLiveData = viewModel.totalWinLD



        betLiveData.observe(this) {
            tvBetScore.text = "$it"
            animClickView(tvBetScore, this)
            sharedPrefs.edit().putInt(prefBetKey, betLiveData.value!!).apply()

        }
        bankLiveData.observe(this) {
            tvBankScore.text = "Total Balance \n$it"
            animClickView(tvBankScore, this)
            sharedPrefs.edit().putInt(prefBankKey, bankLiveData.value!!).apply()

        }
        totalWinLiveData.observe(this) {
            tvWinScore.text = "Total WIN \n$it"
            animClickView(tvWinScore, this)
            sharedPrefs.edit().putInt(prefTotalWin, totalWinLiveData.value!!).apply()
        }

        betLiveData.value = sharedPrefs.getInt(prefBetKey, 100)
        bankLiveData.value = sharedPrefs.getInt(prefBankKey, 10000)
        totalWinLiveData.value = sharedPrefs.getInt(prefTotalWin, 0)



        setupScreenSlots()
        setupListeners()
        pulseBtnAnimation(btnSpin)


    }



    private suspend fun cardGame(): Boolean = withContext(Dispatchers.Main) {
        var isLocalCardGameFinish = false
        var buttonPressed = false

        cardBack1.setOnClickListener {
            lifecycleScope.launch {
                val randomCard = cards.random()
                cardBack1.setImageResource(randomCard)

                if (randomCard == R.drawable.card_red) {
                    sharedPrefs.edit().putBoolean(SELECTED_CARD_KEY, true).apply()
                } else {
                    sharedPrefs.edit().putBoolean(SELECTED_CARD_KEY, false).apply()
                }

                delay(2000)
                cardSelect.visibility = View.GONE
                isLocalCardGameFinish = true
                buttonPressed = true
            }
        }

        cardBack2.setOnClickListener {
            lifecycleScope.launch {
                val randomCard = cards.random()
                cardBack2.setImageResource(randomCard)

                if (randomCard == R.drawable.card_red) {
                    sharedPrefs.edit().putBoolean(SELECTED_CARD_KEY, true).apply()
                } else {
                    sharedPrefs.edit().putBoolean(SELECTED_CARD_KEY, false).apply()
                }

                delay(2000)
                cardSelect.visibility = View.GONE
                isLocalCardGameFinish = true
                buttonPressed = true
            }
        }

        pass.setOnClickListener {
            cardSelect.visibility = View.GONE
            isLocalCardGameFinish = true
            sharedPrefs.edit().putBoolean(SELECTED_PASS_KEY, true).apply()
            buttonPressed = true
        }

        return@withContext if (buttonPressed) isLocalCardGameFinish else false
    }



    private fun setupScreenSlots() {

        for (i in 0..4) {
            val parent = holderSlots[i]

            for (j in 0..4) {
                val itemView = layoutInflater.inflate(R.layout.pointersss, parent, false)
                itemView.setBackgroundResource(drawab[Random.nextInt(drawab.size)])
                parent.addView(itemView)
                firstViewsMatrix[j].add(i, itemView)


                val rotationX: ObjectAnimator =
                    ObjectAnimator.ofFloat(firstViewsMatrix[j][i], View.ROTATION_X, 720f)
                rotationX.apply {
                    interpolator = AccelerateDecelerateInterpolator()
                    duration = 1300
                    repeatCount = 2
                }

                animsMatrix[j].add(i, rotationX)


            }

            for (j in 0..100) {
                val itemView = layoutInflater.inflate(R.layout.pointersss, parent, false)
                itemView.setBackgroundResource(drawab[Random.nextInt(drawab.size)])
                parent.addView(itemView)
            }

            for (j in 0..4) {

                val index = Random.nextInt(drawab.size)

                val itemView = layoutInflater.inflate(R.layout.pointersss, parent, false)
                itemView.setBackgroundResource(drawab[index])
                parent.addView(itemView)

                lastViewMatrix[j].add(i, itemView)
                matrix[j][i] = index

            }
        }

    }

    private fun setLastViews() {
        for (i in 0..4) {
            for (j in 0..4) {

                val index = Random.nextInt(drawab.size)

                lastViewMatrix[i][j].setBackgroundResource(drawab[index])
                matrix[i][j] = index
            }
        }
    }

    private fun setResultIconsToTheStart() {
        for (i in 0..4) {
            for (j in 0..4) {
                firstViewsMatrix[i][j].setBackgroundResource(drawab[matrix[i][j]])
            }
        }
    }

    private fun setupListeners() {
        btnSpin.setOnClickListener {

            doSounds(this@GameLogi, SoundConst.SPIN)
            if (betLiveData.value!! > bankLiveData.value!!) {
                Toast.makeText(this, "Small balance", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            bankLiveData.postValue(bankLiveData.value!!.minus(betLiveData.value!!))


            lifecycleScope.launch(Dispatchers.Main) {
                onOffButtons()

                showLinesForOneSecond()
                delay(100)
                doSounds(this@GameLogi, SoundConst.ROLL)



                for (i in 0..7) {
                    withContext(Dispatchers.Main) {
                        setLastViews()
                    }

                    scrolls.forEach {
                        it.isSmoothScrollingEnabled = true
                        it.fullScroll(View.FOCUS_DOWN)
                    }

                    delay(300)

                    withContext(Dispatchers.Main) { setResultIconsToTheStart() }

                    scrolls.forEach {
                        it.isSmoothScrollingEnabled = false
                        it.fullScroll(View.FOCUS_UP)
                    }


                    delay(200)
                }


                checkForWiner()

            }
        }


        btnBetUpp.setOnClickListener {
            doSounds(this@GameLogi, SoundConst.BET)
            animClickView(btnBetUpp, this)
            if (betLiveData.value!! < bankLiveData.value!!) {
                betLiveData.postValue(betLiveData.value!!.plus(100))

            }
        }
        btnBetDowns.setOnClickListener {
            doSounds(this@GameLogi, SoundConst.BET)
            animClickView(btnBetDowns, this)
            if (betLiveData.value!! > 100) {
                betLiveData.postValue(betLiveData.value!!.minus(100))
            }
        }
    }
    private suspend fun showLinesForOneSecond() {
        val lines = listOf(line1, line2, line3, line4, line5, line6, line7)

        for (line in lines) {
            line.visibility = View.VISIBLE
            delay(500)
            line.visibility = View.INVISIBLE
        }
    }
private fun cardView(){
    cardSelect.visibility = View.VISIBLE

    cardBack1.setImageResource(R.drawable.card_back)
    cardBack2.setImageResource(R.drawable.card_back)

}
    private suspend fun checkForWiner() {
        lifecycleScope.launch(Dispatchers.Main) {
            val winCollection = collectWinners()
            val jackpot = jackpotcollectWinners()

            if (winCollection.isNotEmpty() || jackpot.isNotEmpty()) {

                    val cardSel = sharedPrefs.getBoolean(SELECTED_CARD_KEY, false)
                    val passSel = sharedPrefs.getBoolean(SELECTED_PASS_KEY, false)
                val finish = withContext(Dispatchers.Main) { cardGame() }

                    if (finish) {
                        cardView()

                        winCollection.forEach {
                            if (cardSel) {
                                showWinAnim(it)
                                doSounds(this@GameLogi, SoundConst.WIN)

                                bankLiveData.postValue(bankLiveData.value!!.plus(betLiveData.value!! * 4))
                                totalWinLiveData.postValue(totalWinLiveData.value!!.plus(betLiveData.value!! * 4))
                                delay(3800)
                                sharedPrefs.edit().putBoolean(SELECTED_CARD_KEY, false).apply()

                            } else if (passSel) {
                                showWinAnim(it)
                                doSounds(this@GameLogi, SoundConst.WIN)

                                bankLiveData.postValue(bankLiveData.value!!.plus(betLiveData.value!! * 2))
                                totalWinLiveData.postValue(totalWinLiveData.value!!.plus(betLiveData.value!! * 2))
                                delay(3800)

                                sharedPrefs.edit().putBoolean(SELECTED_PASS_KEY, false).apply()
                            }

                        }

                        jackpot.forEach {
                            if (cardSel) {
                                showWinAnim(it)
                                doSounds(this@GameLogi, SoundConst.WIN)

                                bankLiveData.postValue(bankLiveData.value!!.plus(betLiveData.value!! * 20))
                                totalWinLiveData.postValue(totalWinLiveData.value!!.plus(betLiveData.value!! * 20))
                                delay(3800)
                                sharedPrefs.edit().putBoolean(SELECTED_CARD_KEY, false).apply()

                            } else if (passSel) {
                                showWinAnim(it)
                                doSounds(this@GameLogi, SoundConst.WIN)

                                bankLiveData.postValue(bankLiveData.value!!.plus(betLiveData.value!! * 10))
                                totalWinLiveData.postValue(totalWinLiveData.value!!.plus(betLiveData.value!! * 10))
                                delay(3800)

                                sharedPrefs.edit().putBoolean(SELECTED_PASS_KEY, false).apply()
                            }
                        }

                    }else{
                        winCollection.forEach {

                                showWinAnim(it)
                                doSounds(this@GameLogi, SoundConst.WIN)

                                bankLiveData.postValue(bankLiveData.value!!.plus(betLiveData.value!! * 2))
                                totalWinLiveData.postValue(totalWinLiveData.value!!.plus(betLiveData.value!! * 2))
                                delay(3800)

                                sharedPrefs.edit().putBoolean(SELECTED_PASS_KEY, false).apply()


                        }
                        jackpot.forEach {

                                showWinAnim(it)
                                doSounds(this@GameLogi, SoundConst.WIN)

                                bankLiveData.postValue(bankLiveData.value!!.plus(betLiveData.value!! * 10))
                                totalWinLiveData.postValue(totalWinLiveData.value!!.plus(betLiveData.value!! * 10))
                                delay(3800)

                                sharedPrefs.edit().putBoolean(SELECTED_PASS_KEY, false).apply()

                        }
                    }

            }
            onOffButtons()

        }
    }

    private suspend fun showWinAnim(winList: List<Pair<Int, Int>>) {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(200)
            winList.forEach {
                animsMatrix[it.first][it.second].start()
            }
        }
    }

    private suspend fun onOffButtons() {
        withContext(Dispatchers.Main) {
            btnSpin.isClickable = isClickable
            btnBetUpp.isClickable = isClickable
            btnBetDowns.isClickable = isClickable
            isClickable = !isClickable

        }
    }

    private fun is0line() = matrix[0].all { it == matrix[0][0] }
    private fun is1line() = matrix[1].all { it == matrix[1][0] }
    private fun is2line() = matrix[2].all { it == matrix[2][0] }
    private fun is3line() = matrix[3].all { it == matrix[3][0] }
    private fun is4line() = matrix[4].all { it == matrix[4][0] }
    private fun is5line() = matrix[0][1] == matrix[0][2] && matrix[0][2] == matrix[0][3]
    private fun is6line() = matrix[1][1] == matrix[1][2] && matrix[1][2] == matrix[1][3]
    private fun is7line() = matrix[2][1] == matrix[2][2] && matrix[2][2] == matrix[2][3]
    private fun is8line() = matrix[3][1] == matrix[3][2] && matrix[3][2] == matrix[3][3]
    private fun is9line() = matrix[4][1] == matrix[4][2] && matrix[4][2] == matrix[4][3]
    private fun is10line() =
        matrix[0][0] == matrix[1][1] && matrix[1][1] == matrix[2][2] && matrix[2][2] == matrix[3][3] && matrix[3][3] == matrix[4][4]

    private fun is11line() =
        matrix[0][4] == matrix[1][3] && matrix[1][3] == matrix[2][2] && matrix[2][2] == matrix[3][1] && matrix[3][1] == matrix[4][0]


    private fun collectWinners(): List<List<Pair<Int, Int>>> {
        val winCollection = mutableListOf<List<Pair<Int, Int>>>()
        for (c in 0..6) {
            val sameIndexList = mutableListOf<Pair<Int, Int>>()
            for (i in 0..4) {
                for (j in 0..4) {

                    if (matrix[i][j] == c) sameIndexList.add(Pair(i, j))

                }
            }
            if (sameIndexList.size > 5) winCollection.add(sameIndexList)
        }
        return winCollection
    }

    private fun jackpotcollectWinners(): List<List<Pair<Int, Int>>> {
        val jackpotwinCollection = mutableListOf<List<Pair<Int, Int>>>()
        if (is5line()) jackpotwinCollection.add(
            listOf(
                Pair(0, 1),
                Pair(0, 2),
                Pair(0, 3)
            )
        )
        if (is6line()) jackpotwinCollection.add(
            listOf(
                Pair(1, 1),
                Pair(1, 2),
                Pair(1, 3)
            )
        )
        if (is7line()) jackpotwinCollection.add(
            listOf(
                Pair(2, 1),
                Pair(2, 2),
                Pair(2, 3)
            )
        )
        if (is8line()) jackpotwinCollection.add(
            listOf(
                Pair(3, 1),
                Pair(3, 2),
                Pair(3, 3)
            )
        )
        if (is9line()) jackpotwinCollection.add(
            listOf(
                Pair(4, 1),
                Pair(4, 2),
                Pair(4, 3)
            )
        )
        if (is0line()) jackpotwinCollection.add(
            listOf(
                Pair(0, 0),
                Pair(0, 1),
                Pair(0, 2),
                Pair(0, 3),
                Pair(0, 4)
            )
        )
        if (is1line()) jackpotwinCollection.add(
            listOf(
                Pair(1, 0),
                Pair(1, 1),
                Pair(1, 2),
                Pair(1, 3),
                Pair(1, 4)
            )
        )
        if (is2line()) jackpotwinCollection.add(
            listOf(
                Pair(2, 0),
                Pair(2, 1),
                Pair(2, 2),
                Pair(2, 3),
                Pair(2, 4)
            )
        )
        if (is3line()) jackpotwinCollection.add(
            listOf(
                Pair(3, 0),
                Pair(3, 1),
                Pair(3, 2),
                Pair(3, 3),
                Pair(3, 4)
            )
        )
        if (is4line()) jackpotwinCollection.add(
            listOf(
                Pair(4, 0),
                Pair(4, 1),
                Pair(4, 2),
                Pair(4, 3),
                Pair(4, 4)
            )
        )
        if (is10line()) jackpotwinCollection.add(
            listOf(
                Pair(0, 0),
                Pair(1, 1),
                Pair(2, 2),
                Pair(3, 3),
                Pair(4, 4)
            )
        )
        if (is11line()) jackpotwinCollection.add(
            listOf(
                Pair(0, 4),
                Pair(1, 3),
                Pair(2, 2),
                Pair(3, 1),
                Pair(4, 0)
            )
        )
        return jackpotwinCollection
    }

}
