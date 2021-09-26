package com.sinredemption.raspsuai


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.sinredemption.raspsuai.databinding.ActivityMainBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    //Android vars
    lateinit var binding: ActivityMainBinding
    lateinit var recyclerView: RecyclerView
    private lateinit var mDetector: GestureDetectorCompat
    lateinit var gestureListener: View.OnTouchListener

    //Database vars
    val database = Firebase.database
    val myRef = database.getReference("rasp")

    //Lessons vars
    var lessons = ArrayList<LessonsClass>()
    var day = 0
    var week = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(baseContext)

        day = ((Calendar.DAY_OF_WEEK - 1) % 6)
        //Invert week on Sunday
        week = Calendar.WEEK_OF_YEAR % 2 == if(Calendar.DAY_OF_WEEK == 7) 1 else 0

        //Change switch
        if(week)
            binding.switch2.toggle()
        //Change tab
        binding.tabs.getTabAt(day)?.select()
        //Change tab if u pressed on app bar
        binding.appbar.setOnClickListener { binding.switch2.toggle() }


        //Change color of appbar
        colorChange()
        //Read lessons from firebase
        readFromDB()

        //Check switch change
        binding.switch2.setOnCheckedChangeListener { _buttonView, isChecked ->
            week = isChecked
            colorChange()
            setAdapter(filterList())
        }

        //Check tab change
        binding.tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.i("position", tab.position.toString())
                day = tab.position
                setAdapter(filterList())
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        setContentView(binding.root)
    }

    //Reading data from firebase
    private fun readFromDB(): ArrayList<LessonsClass> {
        myRef.get().addOnSuccessListener {
            for (data in it.children) {
                //Log.i("firebase", "Got value ${data.getValue().toString()}")
                var num = data.child("num").getValue().toString().toInt()
                var name = data.child("name").getValue().toString()
                var teacher = data.child("teacher").getValue().toString()
                var auditory = data.child("auditory").getValue().toString()
                var day = data.child("day").getValue().toString().toInt()
                var week = data.child("week").getValue().toString().toBoolean()
                var type = data.child("type").getValue().toString().toInt()
                lessons.add(LessonsClass(num, name, teacher, auditory, day, week, type))
            }
            setAdapter(filterList())
        }.addOnFailureListener { Log.e("firebase", "Error getting data", it) }

        return ArrayList(lessons)
    }

    //Filter by day and week
    fun filterList(): ArrayList<LessonsClass> {
        var filtred = mutableListOf<LessonsClass>()
        for (lesson in lessons) {
            if (lesson.day == day && lesson.week == week)
                filtred.add(lesson)
        }
        binding.nothingText.visibility = if(filtred.isEmpty()) View.VISIBLE else View.INVISIBLE
        return ArrayList(filtred)
    }

    //Show and sort lessons
    fun setAdapter(sorted: ArrayList<LessonsClass>) {
        recyclerView.adapter = RecyclerAdapter(sorted)
        sorted.sortBy { l -> l.num }
    }

    //Changing color of appbar and status bar
    fun colorChange() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(if (week) R.color.red else R.color.blue)
        }
        binding.appbar.setBackgroundColor(this.resources.getColor(if (week) R.color.red else R.color.blue))
    }

    override fun onDown(e: MotionEvent?): Boolean {
        Log.i("Gestures", "onDown: $e ")
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
        Log.i("Gestures", "onShowPress: $e ")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.i("Gestures", "onSingleTapUp: $e ")
        return true
    }

    override fun onScroll(
        event1: MotionEvent,
        event2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if(distanceX > 100) {
            Log.i("Gestures", "onScroll: right")
            if(day < 5) {
                day++
                binding.tabs.getTabAt(day)?.select()
            }
        }
        if(distanceX < -100) {
            Log.i("Gestures", "onScroll: left")
            if(day > 0) {
                day--
                binding.tabs.getTabAt(day)?.select()
            }
        }
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        Log.i("Gestures", "onSingleTapUp: $e ")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.i("Gestures", "onFling: $velocityX")
        return true
    }
}
