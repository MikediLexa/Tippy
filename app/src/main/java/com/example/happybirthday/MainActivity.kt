package com.example.happybirthday

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AbsSeekBar
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat


private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {

    private lateinit var etBaseAmount: EditText
    private lateinit var etSeekBar: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var etTipAmount: TextView
    private lateinit var etTotalAmount: TextView
    private lateinit var tvTipDescription: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBaseAmount        = findViewById(R.id.etBaseAmount)
        etSeekBar           = findViewById(R.id.etSeekBar)
        tvTipPercentLabel   = findViewById(R.id.tvTipPercentLabel)
        etTipAmount         = findViewById(R.id.etTipAmount)
        etTotalAmount       = findViewById(R.id.etTotalAmount)
        tvTipDescription    = findViewById(R.id.tvTipDescription)


        etSeekBar.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT %"
        updateTipDescription(INITIAL_TIP_PERCENT)
        etSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")

                tvTipPercentLabel.text = "$p1 %"
                computeTipAndTotal()
                updateTipDescription(p1)

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged $p0")
                computeTipAndTotal()
            }

        })


    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20 .. 24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription
        // Update the color based on the tipPercent
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / etSeekBar.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        tvTipDescription.setTextColor(color)

    }

    private fun computeTipAndTotal() {
        if (etBaseAmount.text.isEmpty()) {
            etTipAmount.text = ""
            etTotalAmount.text = ""
            return
        }
        // 1. Get the value of the base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = etSeekBar.progress
        // 2. Compute the tip and total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        // 3. Update the UI
        etTipAmount.text = "%.2f".format(tipAmount)
        etTotalAmount.text = "%.2f".format(totalAmount)
    }


}