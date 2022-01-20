package com.example.datecalc.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.datecalc.R
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*
import kotlin.math.abs
import kotlin.math.floor

class DifferenceBetweenDatesFragment : Fragment() {

    //buttons
    private lateinit var btnFrom: Button
    private lateinit var btnTo: Button


    //text_views
    private lateinit var txtDifference: TextView
    private lateinit var txtTotalDays: TextView

    val months = arrayListOf<String>(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November","December",
    )
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_difference_between_dates, container, false)
        initViews(view)
        reset()

        btnFrom.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDay ->
                        btnFrom.text = "${mDay} ${months[mMonth]} $mYear"
                        calculateDifference()
                    }, year, month, day).show()
            }
        }
        btnTo.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDay ->
                        btnTo.text = "${mDay} ${months[mMonth]} $mYear"
                        calculateDifference()
                    }, year, month, day).show()
            }
        }


        return view
    }

    private fun reset(){
        btnFrom.text = "${day} ${months[month]} $year"
        btnTo.text = "${day} ${months[month]} $year"
        txtDifference.text = "Same dates"
        txtTotalDays.text = ""
    }

    private fun initViews(view: View){
        btnFrom = view.findViewById(R.id.btn_date_from)
        btnTo = view.findViewById(R.id.btn_date_to)
        txtDifference = view.findViewById(R.id.txt_difference)
        txtTotalDays = view.findViewById(R.id.txt_total_days)

    }


    var remainingDays: Double = 0.0
    var diffString: String = ""
    @SuppressLint("NewApi")
    private fun calculateDifference(){
        val defaultZoneId: ZoneId = ZoneId.systemDefault()
        val dTxt1 = btnFrom.text
        val dTxt2 = btnTo.text
        //string to date
        val df: DateTimeFormatter =
            DateTimeFormatterBuilder() // case insensitive to parse JAN and FEB
                .parseCaseInsensitive() // add pattern
                .appendPattern("d MMMM yyyy") // create formatter (use English Locale to parse month names)
                .toFormatter(Locale.ENGLISH)
        val d1: Date = Date.from(LocalDate.parse(dTxt1, df).atStartOfDay(defaultZoneId).toInstant())
        val d2: Date = Date.from(LocalDate.parse(dTxt2, df).atStartOfDay(defaultZoneId).toInstant())

        //convert

        //calculate difference
        val difference = abs(d1.time - d2.time)
        val seconds = difference / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        Log.d("DAYS", "$days")
        remainingDays = days.toDouble()
        diffString = ""
        stringifyDifference() // set difference text
        txtDifference.text = diffString
        var ex = "day"
        if(days > 1.0) ex = "days"
        txtTotalDays.text = "$days $ex"
    }

    //recursive function to check for the remaining days
    private fun stringifyDifference(){
        when {
            remainingDays >= 365.0 -> {
                //remainDays != 0 means there is still a day left
                //calculate years
                val totalYears = floor((remainingDays/365))
                remainingDays =  abs(remainingDays - (totalYears * 365))
                var ex = "year"
                if(totalYears > 1.0) ex = "years"
                diffString += "${totalYears.toInt()} $ex "
                stringifyDifference()
            }
            remainingDays >= 30.0 -> {
                val totalMonths = floor(remainingDays/30)
                remainingDays = abs(remainingDays - (totalMonths*30))
                var ex = "month"
                if(totalMonths > 1.0) ex = "months"
                diffString += "${totalMonths.toInt()} $ex "
                stringifyDifference()
            }
            remainingDays >= 7.0 -> {
                val totalWeeks = floor(remainingDays/7)
                remainingDays = abs(remainingDays - (totalWeeks*7))
                var ex = "week"
                if(totalWeeks > 1.0) ex = "weeks"
                diffString += "${totalWeeks.toInt()} $ex "
                stringifyDifference()
            }
            remainingDays >= 0.0 -> {
                if(remainingDays != 0.0){
                    var ex = "day"
                    if(remainingDays > 1.0) ex = "days"
                    diffString += "${remainingDays.toInt()} $ex "
                }
                else{
                    //check first if string is not empty, then don't add 0 days
                    //if string is empty, set string to Same day
                    if(diffString == ""){
                        diffString = "Same dates"
                        txtTotalDays.text = "" //set total days to empty if the same dates
                    }
                }
            }

        }
    }

}