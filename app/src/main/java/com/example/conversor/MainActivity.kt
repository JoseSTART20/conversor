package com.example.conversor

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.android.volley.Response.Listener
import com.android.volley.Response.ErrorListener

class MainActivity : AppCompatActivity() {
    private lateinit var amountEditText: EditText
    private lateinit var fromCurrencySpinner: Spinner
    private lateinit var toCurrencySpinner: Spinner
    private lateinit var convertButton: Button
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        amountEditText = findViewById(R.id.amountEditText)
        fromCurrencySpinner = findViewById(R.id.fromCurrencySpinner)
        toCurrencySpinner = findViewById(R.id.toCurrencySpinner)
        convertButton = findViewById(R.id.convertButton)
        resultTextView = findViewById(R.id.resultTextView)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.currencies_array, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromCurrencySpinner.adapter = adapter
        toCurrencySpinner.adapter = adapter

        convertButton.setOnClickListener { convertCurrency() }
    }

    private fun convertCurrency() {
        val amountStr = amountEditText.text.toString()
        val fromCurrency = fromCurrencySpinner.selectedItem.toString()
        val toCurrency = toCurrencySpinner.selectedItem.toString()

        if (amountStr.isNotEmpty()) {
            val amount = amountStr.toDouble()
            val url = "https://v6.exchangerate-api.com/v6/your_api_key/latest/$fromCurrency"

            val queue: RequestQueue = Volley.newRequestQueue(this)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                Listener<JSONObject> { response ->
                    val rate = response.getJSONObject("conversion_rates").getDouble(toCurrency)
                    val convertedAmount = amount * rate
                    resultTextView.text = String.format(
                        "%.2f %s = %.2f %s",
                        amount,
                        fromCurrency,
                        convertedAmount,
                        toCurrency
                    )
                },
                ErrorListener { error: VolleyError? ->
                    resultTextView.text = "Error en la conversión"
                })

            queue.add(jsonObjectRequest)
        }
    }
}
