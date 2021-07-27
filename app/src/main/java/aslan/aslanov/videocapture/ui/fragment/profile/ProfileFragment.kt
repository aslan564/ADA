package aslan.aslanov.videocapture.ui.fragment.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import aslan.aslanov.videocapture.R
import aslan.aslanov.videocapture.databinding.FragmentProfileBinding
import aslan.aslanov.videocapture.util.makeSnackBar
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
        viewModelChildInfo=this@ProfileFragment.viewModel

    }

    override fun onStart() {
        super.onStart()
        observeData()
    }

    private fun observeData(): Unit = with(viewModel) {
        getChildUserDate()
        errorMessageConfirmChildInfo.observe(viewLifecycleOwner, { error ->
            error?.let {
                makeSnackBar(it, requireView())
            }
        })
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}