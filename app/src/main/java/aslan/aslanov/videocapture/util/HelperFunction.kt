package aslan.aslanov.videocapture.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import aslan.aslanov.videocapture.R
import aslan.aslanov.videocapture.network.NetworkResult
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import org.json.JSONObject
import java.lang.Exception
import java.time.LocalDate
import java.util.*

private const val TAG = "HELPER_FUNCTION_TAG"
fun logApp(log: String) {
    Log.d(TAG, "logApp: $log")
}

fun addBearer(token: String): String {
    val bearer = " Bearer ".plus(token)
    logApp(bearer)
    return bearer
}

fun setTimer(onComplete: (String, Boolean) -> Unit) {
    var count = 10
    val handler = Handler(Looper.getMainLooper())
    val timer = Timer()
    val doAsyncTimerTask: TimerTask = object : TimerTask() {
        override fun run() {
            handler.post {
                if (count >= 1) {
                    count--
                    onComplete("$count", true)
                } else {
                    onComplete("0", false)
                    timer.cancel()
                }

            }
        }
    }
    timer.schedule(doAsyncTimerTask, 0, 1000L)
}

fun <T> catchServerError(error: ResponseBody?, onCatchError: (NetworkResult<T>) -> Unit) {
    try {
        val jObjError = error?.let {
            JSONObject(it.string())
        }
        jObjError?.let {
            val messageServer = jObjError.getString("error")
            val errorServer = jObjError.getString("message")
            logApp("messageServer$messageServer + errorServer$errorServer")
            onCatchError(NetworkResult.error(errorServer))
        }
    } catch (e: Exception) {
        logApp(e.message ?: " not exception")
        onCatchError(NetworkResult.error(e.message ?: "an error occurred "))

    }
}

fun makeToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun makeSnackBar(message: String, view: View) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}

fun createAlertDialogAny(context: Context, viewId: Int, onComplete: (View, AlertDialog) -> Unit) {
    try {
        val dialog = AlertDialog.Builder(context).create()
        val dialogView = LayoutInflater.from(context).inflate(viewId, null)
        dialog.setView(dialogView)
        onComplete(dialogView, dialog)
        dialog.show()
        dialog.setCancelable(false)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

fun createAlertDialog(context: Context, onComplete: (Boolean, AlertDialog) -> Unit) {
    val dialog = AlertDialog.Builder(context).create()
    val dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_view, null)
    dialog.setView(dialogView)
    val buttonConfirm = dialogView.findViewById<Button>(R.id.button_confirm_dialog)
    val buttonReject = dialogView.findViewById<Button>(R.id.button_reject_dialog)
    buttonConfirm.setOnClickListener {
        onComplete(true, dialog)
    }
    buttonReject.setOnClickListener {
        onComplete(false, dialog)
    }
    dialog.setCancelable(false)
    dialog.show()
}


fun createMaterialDatePicker(activity: FragmentActivity, onComplete: (Long) -> Unit) {
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select date")
        .build()

    datePicker.addOnPositiveButtonClickListener {
        datePicker.selection?.let(onComplete)
    }
    datePicker.addOnNegativeButtonClickListener {
        datePicker.dismiss()
    }
    datePicker.show(activity.supportFragmentManager, "tag")
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("InflateParams")
fun createDialogDatePicker(context: Context, onComplete: (LocalDate?, AlertDialog) -> Unit) {

    var date: LocalDate? = null
    val dialog = AlertDialog.Builder(context).create()
    val dialogView = LayoutInflater.from(context)
        .inflate(R.layout.date_picker_layout, null, false)

    val calendarView = dialogView.findViewById<DatePicker>(R.id.calendarView)
    val confirm = dialogView.findViewById<Button>(R.id.button_confirm_date)

    confirm.setOnClickListener {
        if (date == null) {
            makeToast("please select date", context)
        } else {
            onComplete(date, dialog)
        }
    }
    calendarView.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
        view?.let {
            date = LocalDate.of(year, monthOfYear, dayOfMonth)
        }
    }

    dialog.setView(dialogView)
    dialog.setCancelable(false)
    dialog.show()
}