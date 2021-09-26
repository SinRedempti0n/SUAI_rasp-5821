package com.sinredemption.raspsuai

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val lessons: ArrayList<LessonsClass>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeText: TextView
        val startText: TextView
        val numText: TextView
        val endText: TextView
        val lessonText: TextView
        val teacherText: TextView
        val auditoryText: TextView


        init {
            // Define click listener for the ViewHolder's View.
            typeText = itemView.findViewById(R.id.typeText)
            startText = itemView.findViewById(R.id.startTimeText)
            numText = itemView.findViewById(R.id.numText)
            endText = itemView.findViewById(R.id.endTimeText)
            lessonText = itemView.findViewById(R.id.lessonText)
            teacherText = itemView.findViewById(R.id.teacherText)
            auditoryText = itemView.findViewById(R.id.auditoryText)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_rasp, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(vh: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        when(lessons[position].type){
            1 -> {vh.typeText.setText(R.string.lab); vh.typeText.setTextColor(Color.parseColor("#1565C0")) }
            2 -> {vh.typeText.setText(R.string.practic); vh.typeText.setTextColor(Color.parseColor("#FF6F00")) }
            3 -> {vh.typeText.setText(R.string.course); vh.typeText.setTextColor(Color.parseColor("#689F38")) }
            else -> {vh.typeText.setText(R.string.lection); vh.typeText.setTextColor(Color.parseColor("#8E24AA")) }
        }

        when(lessons[position].num){
            1 -> {vh.startText.setText(R.string.oneStart); vh.endText.setText(R.string.oneEnd)}

            2 -> {vh.startText.setText(R.string.twoStart); vh.endText.setText(R.string.twoEnd)}

            3 -> {vh.startText.setText(R.string.threeStart); vh.endText.setText(R.string.threeEnd)}

            4 -> {vh.startText.setText(R.string.fourStart); vh.endText.setText(R.string.fourEnd)}

            5 -> {vh.startText.setText(R.string.fiveStart); vh.endText.setText(R.string.fiveEnd)}

            else -> {vh.startText.setText(R.string.sixStart); vh.endText.setText(R.string.sixEnd)}
        }
        vh.numText.text = lessons[position].num.toString()
        vh.lessonText.text = lessons[position].name
        vh.teacherText.text = lessons[position].teacher
        vh.auditoryText.text = lessons[position].auditory
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = lessons.size

}
