package org.bmsk.watchstop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import org.bmsk.watchstop.databinding.ActivityWatchBinding
import org.bmsk.watchstop.databinding.DialogCountdownSettingBinding
import java.util.*
import kotlin.concurrent.timer

class WatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWatchBinding
    private var countdownSecond = 10
    private var currentDeciSecond = 0
    private var currentCountdownDeciSecond = countdownSecond * 10
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initButton()
        initTextView()
    }

    private fun initButton() {
        initStartButton()
        initStopButton()
        initPauseButton()
        initLapButton()
    }

    private fun initTextView() {
        initCountdown()
        initCountdownTextView()
    }

    private fun initCountdown() {
        binding.tvCountdown.text = String.format("%02d", countdownSecond)
        binding.pbCountdown.progress = 100
    }

    private fun initCountdownTextView() {
        binding.tvCountdown.setOnClickListener {
            showCountdownSettingDialog()
        }
    }

    private fun initStartButton() {
        binding.btnStart.setOnClickListener {
            start()
            binding.btnStart.isVisible = false
            binding.btnStop.isVisible = false
            binding.btnPause.isVisible = true
            binding.btnLap.isVisible = true
        }
    }

    private fun initStopButton() {
        binding.btnStop.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun initPauseButton() {
        binding.btnPause.setOnClickListener {
            pause()
            binding.btnStart.isVisible = true
            binding.btnStop.isVisible = true
            binding.btnPause.isVisible = false
            binding.btnLap.isVisible = false
        }
    }

    private fun initLapButton() {
        binding.btnLap.setOnClickListener {
            lap()
        }
    }

    private fun start() {
        timer = timer(initialDelay = 0, period = 100) {
            if (currentCountdownDeciSecond == 0) {
                currentDeciSecond += 1

                val minutes = currentDeciSecond.div(10) / 60
                val second = currentDeciSecond.div(10) % 60
                val deciSeconds = currentDeciSecond % 10

                runOnUiThread {
                    binding.tvTime.text =
                        String.format("%02d:%02d", minutes, second)
                    binding.tvTick.text = deciSeconds.toString()

                    binding.groupCountdown.isVisible = false
                }
            } else {
                currentCountdownDeciSecond -= 1
                val seconds = currentCountdownDeciSecond / 10
                val progress = (currentCountdownDeciSecond / (countdownSecond * 10f)) * 100

                binding.root.post {
                    binding.tvCountdown.text = String.format("%02d", seconds)
                    binding.pbCountdown.progress = progress.toInt()
                }
            }
        }
    }

    private fun stop() {
        binding.btnStart.isVisible = true
        binding.btnStop.isVisible = true
        binding.btnPause.isVisible = false
        binding.btnLap.isVisible = false

        currentDeciSecond = 0
        binding.tvTime.text = "00:00"
        binding.tvTick.text = "0"

        binding.groupCountdown.isVisible = true
        initCountdown()
    }

    private fun pause() {
        timer?.cancel()
        timer = null
    }

    private fun lap() {

    }

    private fun showCountdownSettingDialog() {
        AlertDialog.Builder(this).apply {
            val dialogBinding = DialogCountdownSettingBinding.inflate(layoutInflater)
            with(dialogBinding.npCountdownSecond) {
                maxValue = 20
                minValue = 0
                value = countdownSecond
            }
            setTitle("카운트다운 설정")
            setView(dialogBinding.root)
            setPositiveButton("확인") { _, _ ->
                countdownSecond = dialogBinding.npCountdownSecond.value
                currentDeciSecond = countdownSecond * 10
                binding.tvCountdown.text = String.format("%02d", countdownSecond)
            }
            setNegativeButton("취소", null)
        }.show()
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("종료하시겠습니까?")
            setPositiveButton("네") { _, _ ->
                stop()
            }
            setNegativeButton("아니오", null)
        }.show()
    }
}