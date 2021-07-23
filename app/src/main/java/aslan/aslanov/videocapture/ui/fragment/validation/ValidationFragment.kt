package aslan.aslanov.videocapture.ui.fragment.validation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import aslan.aslanov.videocapture.databinding.FragmentValidationBinding
import aslan.aslanov.videocapture.model.registerModel.CodeValidationBody
import aslan.aslanov.videocapture.ui.activity.VideoActivity
import aslan.aslanov.videocapture.util.makeToast
import aslan.aslanov.videocapture.util.logApp
import aslan.aslanov.videocapture.util.makeSnackbar
import aslan.aslanov.videocapture.util.setTimer
import aslan.aslanov.videocapture.viewModel.registry.RegisterViewModel


class ValidationFragment : Fragment() {

    private val binding by lazy { FragmentValidationBinding.inflate(layoutInflater) }
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

    @SuppressLint("SetTextI18n")
    private fun bindUI(): Unit = with(binding) {
        lifecycleOwner = this@ValidationFragment

        arguments?.let {
            ValidationFragmentArgs.fromBundle(it).userResponse?.let { userData ->
                buttonValidate.setOnClickListener {
                    val code = editTextConfirmationCode.text.toString().trim()
                    if (code.isEmpty()) {
                        makeToast("code cannot be null", requireContext())
                    } else {

                        val validation = CodeValidationBody(code)
                        viewModel.userValidation(
                            userData.id.toString(),
                            validation
                        ) { validationUser ->
                            validationUser?.let { validUser ->
                                if (validUser.validated) {
                                    val action =
                                        ValidationFragmentDirections.actionValidateFragmentToChildInfoFragment(
                                            validUser
                                        )
                                    findNavController().navigate(action)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "your pasword not correct",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }

                        }
                    }

                }
            }

            textViewResendCode.setOnClickListener {

                setTimer { s: String, b: Boolean ->
                    if (b) {
                        logApp("$s $b")
                        textViewTimer.text = "00:0$s"
                    } else {
                        logApp("$s $b")
                    }
                }
            }

        }


    }

    override fun onStart() {
        super.onStart()
        observeValidation()
    }

    private fun observeValidation(): Unit = with(viewModel) {
        errorMessageValidation.observe(viewLifecycleOwner, { message ->
            message?.let {
                makeSnackbar(it, requireView())
            }
        })
    }

    companion object {
        private const val TAG = "ValidationFragment"
    }
}