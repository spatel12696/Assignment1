package com.example.assignment1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private lateinit var etLoanAmount: EditText
    private lateinit var etInterestRate: EditText
    private lateinit var etTenure: EditText
    private lateinit var btnCalculateEmi: Button
    private lateinit var tvEmiResult: TextView

    private lateinit var etIncome: EditText
    private lateinit var etExpense: EditText
    private lateinit var btnAddExpense: Button
    private lateinit var tvExpenseList: TextView
    private lateinit var tvBalance: TextView

    private var emiValue = 0.0
    private var expenses = mutableListOf<Double>()
    private var income = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind UI elements
        etLoanAmount = findViewById(R.id.etLoanAmount)
        etInterestRate = findViewById(R.id.etInterestRate)
        etTenure = findViewById(R.id.etTenure)
        btnCalculateEmi = findViewById(R.id.btnCalculateEmi)
        tvEmiResult = findViewById(R.id.tvEmiResult)

        etIncome = findViewById(R.id.etIncome)
        etExpense = findViewById(R.id.etExpense)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        tvExpenseList = findViewById(R.id.tvExpenseList)
        tvBalance = findViewById(R.id.tvBalance)

        // Calculate EMI button click
        btnCalculateEmi.setOnClickListener {
            val loan = etLoanAmount.text.toString().toDoubleOrNull() ?: 0.0
            val annualRate = etInterestRate.text.toString().toDoubleOrNull() ?: 0.0
            val months = etTenure.text.toString().toIntOrNull() ?: 0

            emiValue = calculateEmi(loan, annualRate, months)
            tvEmiResult.text = "Monthly EMI: %.2f".format(emiValue)
            updateBalance()
        }

        // Add expense button click
        btnAddExpense.setOnClickListener {
            val expense = etExpense.text.toString().toDoubleOrNull()
            if (expense != null) {
                expenses.add(expense)
                etExpense.text.clear()
                showExpenses()
                updateBalance()
            }
        }
    }

    private fun calculateEmi(principal: Double, annualRate: Double, months: Int): Double {
        if (principal <= 0.0 || months <= 0) return 0.0
        val monthlyRate = annualRate / 12 / 100
        if (monthlyRate == 0.0) return principal / months
        val pow = (1 + monthlyRate).pow(months.toDouble())
        return (principal * monthlyRate * pow) / (pow - 1)
    }

    private fun showExpenses() {
        if (expenses.isEmpty()) {
            tvExpenseList.text = "No expenses yet."
        } else {
            val listText = expenses.mapIndexed { i, e -> "${i + 1}. $e" }.joinToString("\n")
            tvExpenseList.text = listText
        }
    }

    private fun updateBalance() {
        income = etIncome.text.toString().toDoubleOrNull() ?: 0.0
        val totalExpenses = expenses.sum()
        val totalOutflow = emiValue + totalExpenses
        val balance = income - totalOutflow
        tvBalance.text = if (balance >= 0) {
            "Savings: %.2f".format(balance)
        } else {
            "Deficit: %.2f".format(-balance)
        }
    }
}
