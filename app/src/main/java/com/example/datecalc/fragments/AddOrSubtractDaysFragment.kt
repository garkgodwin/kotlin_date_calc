package com.example.datecalc.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.datecalc.R
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*
import kotlin.math.abs

import android.R.string.no
import android.util.Log
import android.widget.*
import java.text.SimpleDateFormat


class AddOrSubtractDaysFragment : Fragment() {

    private lateinit var btnDate: Button

    private lateinit var rgOperators: RadioGroup

    private lateinit var spYears: Spinner
    private lateinit var spMonths: Spinner
    private lateinit var spDays: Spinner

    private lateinit var txtNewDate: TextView

    val months = arrayListOf<String>(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November","December",
    )
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)


    //constants
    val ADDITION = "ADDITION"
    val SUBTRACTION = "SUBTRACTION"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(com.example.datecalc.R.layout.fragment_add_or_subtract_days, container, false)

        initViews(view)
        populateSpinners()
        reset()
        btnDate.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDay ->
                        btnDate.text = "${mDay} ${months[mMonth]} $mYear"
                        Log.d("DATE_BUTTON", "${btnDate.text}")
                        calculateNewDate()
                    }, year, month, day).show()
            }
        }

        rgOperators.setOnCheckedChangeListener{ _, checkedId ->
            operator = if(checkedId == R.id.r_btn_add){
                ADDITION
            } else{
                SUBTRACTION
            }
            calculateNewDate()
        }


        //years,months,years
        spYears.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                calculateNewDate()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        spMonths.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                calculateNewDate()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        spDays.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                calculateNewDate()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        return view
    }

    private fun initViews(view: View){
        btnDate = view.findViewById(R.id.btn_date)
        rgOperators = view.findViewById(R.id.rg_operators)
        spYears = view.findViewById(R.id.sp_years)
        spMonths = view.findViewById(R.id.sp_months)
        spDays = view.findViewById(R.id.sp_days)
        txtNewDate = view.findViewById(R.id.txt_new_date)
    }
    private fun populateSpinners(){
        //spYears, spMonths, spDays
        val numbers = arrayListOf<Int>()
        for(num in 0..999){
            numbers.add(num)
        }
        val adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, numbers) }
        adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spYears.adapter = adapter
        spMonths.adapter = adapter
        spDays.adapter = adapter

    }

    private fun reset(){
        var defString = "${day} ${months[month]} $year"
        btnDate.text = defString
        calculateNewDate()
    }

    var operator = ADDITION
    @SuppressLint("NewApi", "SimpleDateFormat")
    private fun calculateNewDate(){
        val defaultZoneId:ZoneId = ZoneId.systemDefault()
        val dTxt = btnDate.text
        val years = spYears.selectedItem.toString().toInt()
        val months = spMonths.selectedItem.toString().toInt()
        val days = spDays.selectedItem.toString().toInt()
        //string to date
        val df: DateTimeFormatter =
            DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("d MMMM yyyy")
                .toFormatter(Locale.ENGLISH)
        val date: Date = Date.from(LocalDate.parse(dTxt, df).atStartOfDay(defaultZoneId).toInstant())

        val c: Calendar = Calendar.getInstance()
        c.time = date
        val x = if (operator == ADDITION) 1 else -1
        c.add(Calendar.YEAR, years * x)
        c.add(Calendar.MONTH, months * x)
        c.add(Calendar.DAY_OF_MONTH, days * x)

        val newDate = c.time
        val dayOfWeek = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)
        val day = c.getDisplayName(Calendar.DAY_OF_MONTH, Calendar.LONG, Locale.ENGLISH)
        val month =  c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
        val year = c.getDisplayName(Calendar.YEAR, Calendar.LONG, Locale.ENGLISH)

        val dateFormatted = SimpleDateFormat("EEEE, d MMMM yyyy").format(newDate)
        val displayNewDate = "${dayOfWeek}, $day $month $year"
        txtNewDate.text = dateFormatted
    }



}