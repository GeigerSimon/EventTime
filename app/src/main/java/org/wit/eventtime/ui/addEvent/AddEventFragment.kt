package org.wit.eventtime.ui.addEvent

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import org.wit.eventtime.MainActivity
import org.wit.eventtime.R
import org.wit.eventtime.databinding.FragmentAddEventBinding
import org.wit.eventtime.models.EventModel
import timber.log.Timber
import java.util.*

class AddEventFragment: Fragment() {
    private var _binding: FragmentAddEventBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var event = EventModel()
    lateinit var app: MainActivity
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private val calendar: Calendar = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addEventViewModel =
            ViewModelProvider(this).get(AddEventViewModel::class.java)

        _binding = FragmentAddEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var edit = false

        var sYear = -1
        var sMonth = -1
        var sDay = -1
        var sHour = -1
        var sMinute = -1
        var eYear = -1
        var eMonth = -1
        var eDay = -1
        var eHour = -1
        var eMinute = -1

        if (event.startYear == -1) {
            sYear = calendar.get(Calendar.YEAR)
            sMonth = calendar.get(Calendar.MONTH)
            sDay = calendar.get(Calendar.DAY_OF_MONTH)
            sHour = calendar.get(Calendar.HOUR)
            sMinute = 0
            eYear = sYear
            eMonth = sMonth
            eDay = sDay
            eHour = sHour + 1
            eMinute = 0
        }
        else {
            sYear = event.startYear
            sMonth = event.startMonth
            sDay = event.startDay
            sHour = event.startHour
            sMinute = event.startMinute
            eYear = event.endYear
            eMonth = event.endMonth
            eDay = event.endDay
            eHour = event.endHour
            eMinute = event.endMinute
        }

        var sDate = "${sDay}.${sMonth + 1}.${sYear}"
        binding.btnChooseStartDate.text = sDate
        var eDate = "${eDay}.${eMonth + 1}.${eYear}"
        binding.btnChooseEndDate.text = eDate

        binding.btnChooseStartDate.setOnClickListener() {
            val datePickerDialog = DatePickerDialog(this@AddEventFragment, DatePickerDialog.OnDateSetListener {
                    _, pickedYear, pickedMonth, pickedDay ->
                if (pickedYear > eYear
                    || (pickedYear == eYear && pickedMonth > eMonth)
                    || (pickedYear == eYear && pickedMonth == eMonth && pickedDay > eDay)) {
                    eYear = pickedYear
                    eMonth = pickedMonth
                    eDay = pickedDay
                }
                sYear = pickedYear
                sMonth = pickedMonth
                sDay = pickedDay
                sDate = "$sDay.${sMonth + 1}.$sYear"
                eDate = "$eDay.${eMonth + 1}.$eYear"
                binding.btnChooseStartDate.text = sDate
                binding.btnChooseEndDate.text = eDate
            }, sYear, sMonth, sDay)
            datePickerDialog.show()
        }

        binding.btnChooseEndDate.setOnClickListener() {
            val datePickerDialog = DatePickerDialog(this@AddEventFragment, DatePickerDialog.OnDateSetListener {
                    _, pickedYear, pickedMonth, pickedDay ->
                if (pickedYear < sYear
                    || (pickedYear == sYear && pickedMonth < sMonth)
                    || (pickedYear == sYear && pickedMonth == sMonth && pickedDay < sDay)) {
                    Snackbar.make(it, R.string.enter_valid_end_date, Snackbar.LENGTH_LONG)
                        .show()
                }
                else {
                    eYear = pickedYear
                    eMonth = pickedMonth
                    eDay = pickedDay
                }
                eDate = "$eDay.${eMonth + 1}.$eYear"
                binding.btnChooseEndDate.text = eDate
            }, eYear, eMonth, eDay)
            datePickerDialog.show()
        }

        binding.btnAdd.setOnClickListener() {
            event.title = binding.eventTitle.text.toString()
            event.description = binding.description.text.toString()
            event.startDay = sDay
            event.startMonth = sMonth
            event.startYear = sYear
            event.endDay = eDay
            event.endMonth = eMonth
            event.endYear = eYear
            if (event.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_event_title, Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
                if (edit) {
                    app.events.update(event.copy())
                } else {
                    app.events.create(event.copy())
                }
            }
            Timber.i("add Button Pressed: $event")
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()
    }



        val textView: TextView = binding.textAddEvent
        addEventViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}