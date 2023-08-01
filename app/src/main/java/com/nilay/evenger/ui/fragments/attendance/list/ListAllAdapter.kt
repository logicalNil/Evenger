package com.nilay.evenger.ui.fragments.attendance.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nilay.evenger.core.api.SubjectUIModel
import com.nilay.evenger.core.db.attendance.AttendanceModel
import com.nilay.evenger.databinding.RowSubjectSelectBinding

class ListAllAdapter(
    private val onClick: (AttendanceModel, Boolean) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<ListAllAdapter.ListAllViewHolder>() {

    var list = mutableListOf<SubjectUIModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ListAllViewHolder(
        private val binding: RowSubjectSelectBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    binding.checkBox.isChecked = !binding.checkBox.isChecked
                    onClick.invoke(
                        list[adapterPosition].attendanceModel,
                        binding.checkBox.isChecked
                    )
                }
            }
        }

        fun bind(
            model: SubjectUIModel
        ) {
            binding.apply {
                tvSubjectName.text = model.attendanceModel.subject
                tvSubjectCode.text = model.attendanceModel.teacher
                checkBox.isClickable = false
                checkBox.isChecked = model.isSelected
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAllViewHolder =
        ListAllViewHolder(
            RowSubjectSelectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int =
        list.size

    override fun onBindViewHolder(holder: ListAllViewHolder, position: Int) {
        holder.bind(list[position])
    }
}