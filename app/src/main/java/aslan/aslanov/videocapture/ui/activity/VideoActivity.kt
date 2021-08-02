package aslan.aslanov.videocapture.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import aslan.aslanov.videocapture.R
import aslan.aslanov.videocapture.databinding.ActivityViedeoBinding
import aslan.aslanov.videocapture.local.manager.SharedPreferenceManager
import aslan.aslanov.videocapture.util.logApp
import aslan.aslanov.videocapture.util.makeToast

class VideoActivity : AppCompatActivity() {

    private val binding by lazy { ActivityViedeoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkUserIsLogin()
        bindUI()
    }


    private fun bindUI(): Unit = with(binding) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(fragmentContainerView.id) as NavHostFragment
        val navController = navHostFragment.navController

       NavigationUI.setupWithNavController(bottomNavigationView,navController)
    }


    private fun checkUserIsLogin() {
        if (!SharedPreferenceManager.isLogin && SharedPreferenceManager.token == null) {
            SharedPreferenceManager.token?.let { logApp(it) }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}