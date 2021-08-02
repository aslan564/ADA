package aslan.aslanov.videocapture.ui.fragment.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import aslan.aslanov.videocapture.databinding.FragmentRegisterBinding
import aslan.aslanov.videocapture.model.registerModel.PhoneRequestBody
import aslan.aslanov.videocapture.util.logApp
import aslan.aslanov.videocapture.util.makeSnackBar
import aslan.aslanov.videocapture.util.makeToast
import aslan.aslanov.videocapture.viewModel.registry.RegisterViewModel

class RegisterFragment : Fragment() {

    private val binding by lazy { FragmentRegisterBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<RegisterViewModel>()
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
        lifecycleOwner = this@RegisterFragment
        binding.buttonRegister.setOnClickListener {
            val code = editTextCountryCode.text.trim()
            val number = editTextPhoneNumber.text.trim()
            if (code.isEmpty() || number.isEmpty()) {
                logApp("country code cannot be  null ")
                makeSnackBar("Country  code and number cannot be  null!! ", mainContainer)
                return@setOnClickListener
            } else {
                logApp("$code")
                logApp("$number")
                val registerRequestBody = PhoneRequestBody("$code$number")
                viewModel.registerUser(registerRequestBody){userResponse->
                    userResponse?.let {
                        val action =
                            RegisterFragmentDirections.actionRegisterFragmentToValidationFragment(
                                it
                            )
                        findNavController().navigate(action)
                    }
                }
                logApp("$registerRequestBody")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        observeRegistry()
    }

    private fun observeRegistry(): Unit = with(viewModel) {

        errorMessageRegister.observe(viewLifecycleOwner, { message ->
            message?.let {
                makeToast(it, requireContext())
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    companion object {
        private const val TAG = "RegisterFragment"
    }
}