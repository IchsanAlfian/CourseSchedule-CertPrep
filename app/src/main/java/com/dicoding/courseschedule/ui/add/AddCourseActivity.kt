package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private var startTimePicker = ""
    private var endTimePicker = ""
    private lateinit var viewModel: AddCourseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.title = resources.getString(R.string.add_course)
        val factory = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)
        viewModel.saved.observe(this){
            if (it.getContentIfNotHandled() == true){
                onBackPressed()
            }
        }
    }
    fun startPicker(view: View) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, START_PICKER_TAG)
    }
    fun endPicker(view: View) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, END_PICKER_TAG)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                val name = findViewById<TextInputEditText>(R.id.ed_course_name).text.toString()
                val lecturer = findViewById<TextInputEditText>(R.id.ed_lecture).text.toString()
                val note = findViewById<TextInputEditText>(R.id.ed_note).text.toString()
                val day = findViewById<Spinner>(R.id.spinner_day).selectedItemPosition
                viewModel.insertCourse(name,day,startTimePicker,endTimePicker,lecturer,note)
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int){
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        when(tag){
            START_PICKER_TAG->{
                startTimePicker = dateFormat.format(calendar.time)
                val startTextView = findViewById<TextView>(R.id.tv_start_time)
                startTextView.text = startTimePicker
            }
            END_PICKER_TAG->{
                endTimePicker = dateFormat.format(calendar.time)
                val endTextView = findViewById<TextView>(R.id.tv_end_time)
                endTextView.text = endTimePicker
            }
        }
    }
    companion object {
        private const val START_PICKER_TAG = "StartTimePicker"
        private const val END_PICKER_TAG = "EndTimePicker"

    }
}
