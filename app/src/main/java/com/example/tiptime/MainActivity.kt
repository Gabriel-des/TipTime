package com.example.tiptime

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState != null) {
            binding.tipResult.text = savedInstanceState.getString("tip")
        } else {
            val formattedTip = NumberFormat.getCurrencyInstance().format(0)
            binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
        }

        binding.calculateButton.setOnClickListener {
            calculateTip()
        }
    }

    private fun calculateTip() {
        val stringTextField = binding.costOfService.text.toString()
        val cost = stringTextField.toDoubleOrNull() ?: return
        val selectedId = binding.tipOption.checkedRadioButtonId
        val roundUp = binding.roundUpSwitch.isChecked

        val tipPercentage = when(selectedId) {
            R.id.option_20_percent -> 0.2
            R.id.option_18_percent -> 0.18
            else -> 0.15
        }

        var tip = cost * tipPercentage

        if (roundUp) {
            tip = kotlin.math.ceil(tip)
        }

        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("tip", binding.tipResult.text.toString())
    }
}