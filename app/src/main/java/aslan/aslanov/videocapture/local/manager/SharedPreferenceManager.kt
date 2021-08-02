package aslan.aslanov.videocapture.local.manager

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object SharedPreferenceManager {
    private const val sharedPreferenceManagerKey = "aslan.aslanov.videocapture.local.manager"

    private lateinit var sharedPreferences: SharedPreferences

    private var IS_LOGIN = Pair("isLogin", false)
    private var TOKEN = Pair("token", null)

    fun instance(context: Context) {
        sharedPreferences = context.getSharedPreferences(sharedPreferenceManagerKey, MODE_PRIVATE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var isLogin: Boolean
        get() = sharedPreferences.getBoolean(IS_LOGIN.first, IS_LOGIN.second)
        set(value) = sharedPreferences.edit {
            it.putBoolean(IS_LOGIN.first, value)
        }

    var token: String?
        get() = sharedPreferences.getString(TOKEN.first, TOKEN.second)
        set(value) = sharedPreferences.edit {
            it.putString(TOKEN.first, value)
        }

}