package com.phntechnology.exoplayerdownloader.util

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.Service
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.phntechnology.exoplayerdownloader.util.Constants.DOWNLOAD_NOTIFICATION_CHANNEL_ID
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern


@OptIn(markerClass = [UnstableApi::class])
@Synchronized
fun Service.getDownloadNotificationHelper(): DownloadNotificationHelper {
    return DownloadNotificationHelper(this, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
}

fun Service.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}



fun RecyclerView.getRecyclerViewHeight(): Int {
    val adapter = this.adapter
    val layoutManager = this.layoutManager

    if (adapter == null || layoutManager == null) {
        // Adapter or LayoutManager is not set
        return 0
    }

    var totalHeight = 0

    for (i in 0 until adapter.itemCount) {
        val childView = layoutManager.findViewByPosition(i)
        childView?.let {
            val widthSpec =
                View.MeasureSpec.makeMeasureSpec(this.width, View.MeasureSpec.EXACTLY)
            val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            it.measure(widthSpec, heightSpec)
            totalHeight += it.measuredHeight
        }
    }

    return totalHeight
}

fun TextView.getTextViewHeight(): Int {
    val widthMeasureSpec =
        View.MeasureSpec.makeMeasureSpec(this.width, View.MeasureSpec.EXACTLY)
    val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

    this.measure(widthMeasureSpec, heightMeasureSpec)

    return this.measuredHeight
}

fun View.hideView() {
    visibility = View.GONE
}

fun View.showView() {
    visibility = View.VISIBLE
}

fun <VB : ViewBinding> Fragment.setBottomSheet(
    bindingInflater: (LayoutInflater) -> VB,
    onBind: (VB, BottomSheetDialog) -> Unit
) {
    val bottomSheetDialog = BottomSheetDialog(requireContext())
    val bottomSheetBinding = bindingInflater(layoutInflater)
    onBind(bottomSheetBinding, bottomSheetDialog)
    bottomSheetDialog.setContentView(bottomSheetBinding.root)
    bottomSheetDialog.show()
}

fun <VB : ViewBinding> Fragment.showCustomDialog(
    bindingInflater: (LayoutInflater) -> VB,
    onBind: (VB, Dialog) -> Unit
) {
    val dialog = Dialog(requireContext())
    val dialogBinding = bindingInflater(layoutInflater)
    onBind(dialogBinding, dialog)
    dialog.setContentView(dialogBinding.root)
    dialog.window?.setBackgroundDrawableResource(R.color.transparent)
    dialog.show()
}

fun getFormattedDate(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd MMM EEEE", Locale.getDefault())
    val date = dateFormat.format(calendar.time).split(" ")[0]
    val month = dateFormat.format(calendar.time).split(" ")[1]
    val day = dateFormat.format(calendar.time).split(" ")[2].uppercase()
    return "$date $month $day"
}


//fragment back pressed handler
fun Fragment.backPressedHandle(handle: () -> Unit) {
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            handle()
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
}

fun Fragment.hideSoftKeyboard() {
    requireActivity().currentFocus?.let {
        val inputMethodManager =
            ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)!!
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

@SuppressLint("ClickableViewAccessibility")
fun Fragment.setupUI(view: View, handle: () -> Unit) {

    // Set up touch listener for non-text box views to hide keyboard.
    if (view !is EditText) {
        view.setOnTouchListener { v, event ->
            hideSoftKeyboard()
            handle()
            false
        }
    }

    //If a layout container, iterate over children and seed recursion.
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val innerView = view.getChildAt(i)
            setupUI(innerView, handle)
        }
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun getFileSize(uri: Uri?, context: Context): Long {
    if (uri != null) {
        return context.contentResolver.openAssetFileDescriptor(uri, "r")!!.length
    }
    return 0
}

fun Fragment.orientationPortrait() {
    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

fun Fragment.orientationLandscape() {
    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

fun TextInputEditText.textChange(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(text: Editable?) {
            afterTextChanged.invoke(text.toString())
        }
    })
}

fun AutoCompleteTextView.textChange(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(text: Editable?) {
            afterTextChanged.invoke(text.toString())
        }
    })
}

fun Fragment.pickDate(
    _day: Int, _month: Int, _year: Int, _date: (Int, Int, Int) -> Unit
) {
    val datePickerDialog = DatePickerDialog(
        requireContext(), { view, year, monthOfYear, dayOfMonth ->
            _date.invoke(year, monthOfYear, dayOfMonth)
        }, _year, _month, _day
    )
    datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
    datePickerDialog.show()
}

fun Fragment.pickTime(
    _hour: Int, _minute: Int, is24Hour: Boolean, _time: (hourOfDay: Int, minute: Int) -> Unit
) {
    val timePickerDialog = TimePickerDialog(
        requireContext(), { view, hourOfDay, minute ->
            _time.invoke(hourOfDay, minute)
        }, _hour, _minute, is24Hour
    )
    timePickerDialog.show()
}

fun isNotNullOrZero(number: String, errorMessage: String): String? {
    if (number.trim().isNullOrEmpty() || number.trim() == "0") {
        return errorMessage
    }
    return null
}

fun isValidVideoUrl(url: String): Boolean {
    val urlPattern = Regex("^((https?|ftp|file):\\/\\/)?([\\da-z.-]+)\\.([a-z.]{2,6})([\\/\\w .-]*)*\\/(.*\\.(mp4|avi|mov|wmv|flv|mkv))\$", RegexOption.IGNORE_CASE)
    return urlPattern.matches(url)
}

fun isValidName(name: String, errorMessage: String = ""): String? {
    if (name.trim().isNullOrEmpty()) {
        return errorMessage
    }
    return null
}

fun isValidEmail(email: String, errorMessage: String = ""): String? {
    if (email.trim().isNullOrEmpty()) {
        return errorMessage
    } else if (!Pattern.compile("[a-zA-Z0-9+_.-]+@[a-zA-Z0-9]+[.-][a-zA-Z][a-z.A-Z]+")
            .matcher(email).matches()
    ) {
        return errorMessage
    }
    return null
}

fun isValidMobileNumber(number: String, errorMessage: String): String? {
    if (number.trim().isNullOrEmpty()) {
        return errorMessage
    } else if (number.trim().length != 10) {
        return errorMessage
    } else if (!number.trim().matches("^[6-9][0-9]{9}$".toRegex())) {
        return errorMessage
    }
    return null
}

fun isValidNumber(number: String, errorMessage: String): String? {
    if (number.trim().isNullOrEmpty()) {
        return errorMessage
    } else if (number.trim().length != 10) {
        return errorMessage
    }
    return null
}

fun Fragment.toastMsg(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun Activity.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}