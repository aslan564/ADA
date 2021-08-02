package aslan.aslanov.videocapture.ui.fragment.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import aslan.aslanov.videocapture.R
import aslan.aslanov.videocapture.databinding.FragmentProfileBinding
import aslan.aslanov.videocapture.util.createAlertDialogAny
import aslan.aslanov.videocapture.util.makeSnackBar
import aslan.aslanov.videocapture.util.makeToast
import aslan.aslanov.videocapture.viewModel.registry.UserViewModel


class ProfileFragment : Fragment() {

    private val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI(): Unit = with(binding) {
        lifecycleOwner = this@ProfileFragment
        viewModelChildInfo = this@ProfileFragment.viewModel
        buttonReportable.setOnClickListener {
            viewModel.reportRequest { report, message ->
                textViewReportable.text = message
            }
        }
        buttonContactOur.setOnClickListener {
            /*createAlertDialogAny(
                requireContext(),
                R.layout.layout_alert_warn
            ) { view, alertDialog ->
                val button = view.findViewById<Button>(R.id.custom_alert_dialog_button)
            }*/
        }
    }

    override fun onStart() {
        super.onStart()
        observeData()
    }

    private fun observeData(): Unit = with(viewModel) {
        getChildUserDate()
        errorMessageConfirmChildInfo.observe(viewLifecycleOwner, { error ->
            error?.let {
                makeToast(it, requireContext())
            }
        })
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}