package com.ajkerdeal.app.ajkerdealadmin.ui.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.ajkerdeal.app.ajkerdealadmin.broadcast.ReminderBroadcast
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentRemainderBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import timber.log.Timber
import java.util.*

@SuppressLint("SetTextI18n")
class RemainderFragment : Fragment() {

    private var binding: FragmentRemainderBinding? = null

    private var selectedHour24: String = ""
    private var selectedTimeStamp: Long = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentRemainderBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initLister()
    }

    private fun initView() {
        if (SessionManager.isRemainder) {
            activeState()
            binding?.msg?.text = "Current work session remainder at ${SessionManager.remainderTime} everyday"
            binding?.timePickerLayout?.isVisible = false
        } else {
            inActiveState()
        }
    }

    private fun initLister() {

        binding?.timePickerLayout?.setOnClickListener {
            timePicker()
        }

        binding?.submitBtn?.setOnClickListener {
            if (SessionManager.isRemainder) {
                cancelReminder()
                SessionManager.isRemainder = false
                SessionManager.remainderTime = ""
                inActiveState()
                binding?.timePickerLayout?.isVisible = true
                binding?.msg?.text = ""
                binding?.timeRangePicker?.text = ""
            } else {
                if (selectedTimeStamp > 0L) {
                    createReminder(selectedTimeStamp)
                    SessionManager.isRemainder = true
                    SessionManager.remainderTime = selectedHour24
                    activeState()
                    context?.toast("Remainder set")
                } else {
                    context?.toast("Select time first")
                }
            }
        }
    }

    private fun activeState() {
        binding?.submitBtn?.text = "Cancel"
        binding?.submitBtn?.setBackgroundColor(Color.RED)
    }

    private fun inActiveState() {
        binding?.submitBtn?.text = "Submit"
        binding?.submitBtn?.setBackgroundColor(Color.parseColor("#056608"))
    }

    private fun timePicker() {
        val calender = Calendar.getInstance()
        TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
            calender.apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            selectedTimeStamp = calender.timeInMillis
            selectedHour24 = "$hourOfDay:$minute"
            binding?.timeRangePicker?.text = selectedHour24
            binding?.msg?.text = "Work session remainder at $selectedHour24 everyday"
        }, calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE), false)
            .show()
    }

    private fun createReminder(alarmTime: Long) {
        val intent = Intent(requireContext(), ReminderBroadcast::class.java).apply {
            putExtra("title", "Work session remainder")
            putExtra("body", "Tap to open app and start today's session")
        }
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 21403, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager: AlarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //val alarmTime = System.currentTimeMillis() + (1000 * 60 * 60 * 9) // 9 hours
        //alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTime, AlarmManager.INTERVAL_DAY, pendingIntent)
        Timber.d("reminderDebug createReminder for ${SessionManager.userId} $alarmTime")
    }

    private fun cancelReminder() {
        val intent = Intent(requireContext(), ReminderBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 21403, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager: AlarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            Timber.d("reminderDebug cancelReminder for ${SessionManager.userId}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}