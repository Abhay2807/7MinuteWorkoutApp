package com.example.a7minuteworkoutapp

import android.graphics.Color
import android.provider.CalendarContract.Colors
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minuteworkoutapp.databinding.ItemExerStatusBinding

class ExerStatusAdapter(val items: ArrayList<ExerciseModel>):
RecyclerView.Adapter<ExerStatusAdapter.ViewHolder>()
{
    class ViewHolder(binding: ItemExerStatusBinding):
            RecyclerView.ViewHolder(binding.root){
                val tvItem=binding.tvItem
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerStatusBinding.inflate(
            LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model:ExerciseModel=items[position]
        holder.tvItem.text=model.getId().toString()

        when{

            model.getIsSelected() ->{

                // When an exercise is selected

                holder.tvItem.background=
                    ContextCompat.getDrawable(holder.itemView.context,
                    R.drawable.item_status_exercise_selected)

                holder.tvItem.setTextColor(Color.parseColor("#000000"))



            }

            model.getIsCompleted() ->{

                // When an exercise is completed

                holder.tvItem.background=
                    ContextCompat.getDrawable(holder.itemView.context,
                        R.drawable.item_status_exercise_selected_completed)

                holder.tvItem.setTextColor(Color.parseColor("#FFFFFF"))

            }

            else ->{

                // When an exercise is still not reached

                holder.tvItem.background=
                    ContextCompat.getDrawable(holder.itemView.context,
                        R.drawable.item_status_exercise)

                holder.tvItem.setTextColor(Color.parseColor("#000000"))


            }



        }

    }

    override fun getItemCount(): Int {
       return items.size
    }

}