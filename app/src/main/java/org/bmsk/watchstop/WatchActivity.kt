package org.bmsk.watchstop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import org.bmsk.watchstop.databinding.ActivityWatchBinding
import org.bmsk.watchstop.databinding.DialogCountdownSettingBinding
import kotlin.concurrent.timer

class WatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWatchBinding
    private var countDownSecond = 10
    private var currentDeciSecond = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initButton()
    }

    private fun initButton() {
        initStartButton()
        initStopButton()
        initPauseButton()
        initLapButton()

        initCountDownTextView()
    }

    private fun initCountDownTextView() {
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
        timer(initialDelay = 0, period = 100) {
            currentDeciSecond += 1

            val minutes = currentDeciSecond.div(10) / 60
            val second = currentDeciSecond.div(10) % 60
            val deciSeconds = currentDeciSecond % 10

            runOnUiThread {
                binding.tvTime.text =
                    String.format("%02d:%02d", minutes, second)
                binding.tvTick.text = deciSeconds.toString()
            }
        }
    }

    private fun stop() {
        binding.btnStart.isVisible = true
        binding.btnStop.isVisible = true
        binding.btnPause.isVisible = false
        binding.btnLap.isVisible = false
    }

    private fun pause() {

    }

    private fun lap() {

    }

    private fun showCountdownSettingDialog() {
        AlertDialog.Builder(this).apply {
            val dialogBinding = DialogCountdownSettingBinding.inflate(layoutInflater)
            with(dialogBinding.npCountdownSecond) {
                maxValue = 20
                minValue = 0
                value = countDownSecond
            }
            setTitle("카운트다운 설정")
            setView(dialogBinding.root)
            setPositiveButton("확인") { _, _ ->
                countDownSecond = dialogBinding.npCountdownSecond.value
                binding.tvCountdown.text = String.format("%02d", countDownSecond)
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