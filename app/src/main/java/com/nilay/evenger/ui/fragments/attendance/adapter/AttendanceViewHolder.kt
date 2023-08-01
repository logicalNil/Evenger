package com.nilay.evenger.ui.fragments.attendance.adapter

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.color.MaterialColors
import com.nilay.evenger.R
import com.nilay.evenger.databinding.RowAttendanceBinding
import com.nilay.evenger.core.db.attendance.AttendanceModel
import com.nilay.evenger.ui.fragments.attendance.utils.calculatedDays
import com.nilay.evenger.ui.fragments.attendance.utils.findPercentage
import com.nilay.evenger.ui.fragments.attendance.utils.setResources
import kotlin.math.ceil

sealed class AttendanceViewHolder(
    binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    class AttendanceHolder(
        private val binding: RowAttendanceBinding,
        private val minPercentage: Int,
        private val onItemClickListener: ((attendance: Int) -> Unit)? = null,
        private val onCheckClick:( (attendance: Int) -> Unit)? = null,
        private val onWrongClick:( (attendance: Int) -> Unit)? = null,
        private val onLongClick: ((attendance: Int) -> Unit)? = null
    ) : AttendanceViewHolder(binding) {

        private fun RowAttendanceBinding.viewChanges(isSave: Boolean) = this.apply {
            val redColor = ContextCompat.getColor(
                binding.root.context, R.color.red
            )
            val primaryColor = MaterialColors.getColor(
                binding.root, androidx.appcompat.R.attr.colorPrimary, Color.WHITE
            )
            imageView.setColorFilter(if (isSave) primaryColor else redColor)
            textViewPercentage.setTextColor(if (isSave) primaryColor else redColor)
            textViewClass.setTextColor(if (isSave) primaryColor else redColor)
            linearProgressIndicator.setIndicatorColor(if (isSave) primaryColor else redColor)
        }

        init {
            binding.apply {
                binding.root.setOnClickListener {
                    onItemClickListener?.invoke(adapterPosition)
                }
                materialButtonPresent.setOnClickListener {
                    onCheckClick?.invoke(adapterPosition)
                }
                materialButtonAbsent.setOnClickListener {
                    onWrongClick?.invoke(adapterPosition)
                }
                root.setOnLongClickListener {
                    onLongClick?.invoke(adapterPosition)
                    true
                }
            }
        }

        fun bind(attendance: AttendanceModel) {
            binding.apply {
                textViewSubject.text = attendance.subject
                textViewTeacher.text =
                    (attendance.teacher ?: "").ifBlank { String.format("No Teacher") }
                imageView.setImageResource(
                    if (attendance.subject.contains(
                            "lab", true
                        )
                    ) R.drawable.round_computer_24
                    else R.drawable.round_assignment_24
                )
//                set Progress
                val percentage = findPercentage(
                    attendance.present.toFloat(), attendance.total.toFloat()
                ) { present, total ->
                    when (total) {
                        0.0F -> 0.0F
                        else -> (present / total) * 100
                    }
                }

                textViewPercentage.text = String.format("%d %%", percentage.toInt())
                linearProgressIndicator.progress = percentage.toInt()
                textViewClass.text = String.format("%d/%d", attendance.present, attendance.total)
//                status
                setResources(percentage.toInt()) { per ->
                    when {
                        per == 0 -> {
                            binding.viewChanges(true)
                            binding.textViewStatus.text = binding.root.context.resources.getString(
                                R.string.status,
                            )
                        }

                        per >= minPercentage -> {
                            val day = calculatedDays(
                                attendance.present, attendance.total
                            ) { present, total ->
                                (((100 * present) - (minPercentage * total)) / minPercentage)
                            }.toInt()
                            binding.viewChanges(true)
                            binding.textViewStatus.text = when {
                                per == minPercentage || day <= 0 -> binding.root.context.resources.getString(
                                    R.string.on_track
                                )

                                day != 0 -> binding.root.context.resources.getString(
                                    R.string.leave_class, day.toString()
                                )

                                else -> binding.root.context.resources.getString(R.string.error)
                            }
                        }

                        per < minPercentage -> {
                            val day = calculatedDays(
                                attendance.present, attendance.total
                            ) { present, total ->
                                (((minPercentage * total) - (100 * present)) / (100 - minPercentage))
                            }
                            binding.viewChanges(false)
                            textViewStatus.text = binding.root.context.resources.getString(
                                R.string.attend_class,
                                (ceil(day).toInt()).toString()
                            )
                        }
                    }
                }
            }
        }
    }
}