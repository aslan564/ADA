package aslan.aslanov.videocapture.ui.fragment.childInfo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import aslan.aslanov.videocapture.R
import aslan.aslanov.videocapture.databinding.FragmentChildInfoBinding
import aslan.aslanov.videocapture.local.manager.SharedPreferenceManager
import aslan.aslanov.videocapture.model.registerModel.Child
import aslan.aslanov.videocapture.model.registerModel.UserRequestBody
import aslan.aslanov.videocapture.model.user.child.GENDER
import aslan.aslanov.videocapture.ui.activity.VideoActivity
import aslan.aslanov.videocapture.util.*
import aslan.aslanov.videocapture.util.NetworkConstant.emailPattern
import aslan.aslanov.videocapture.viewModel.registry.UserViewModel
import java.sql.Date
import java.util.*


class ChildInfoFragment : Fragment() {

    private val bindingChild by lazy { FragmentChildInfoBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<UserViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return bindingChild.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    override fun onResume() {
        super.onResume()
        observeChildData()
    }

    private fun observeChildData(): Unit = with(viewModel) {
        childUserCreate.observe(viewLifecycleOwner, { childResponse ->
            childResponse?.let { response ->
                logApp("ChildInfoFragment 1: observeChildData ${response.child}")
                arguments?.let {
                    ChildInfoFragmentArgs.fromBundle(it).validationResponse?.let { validationResp ->
                        if (!SharedPreferenceManager.isLogin) {
                            SharedPreferenceManager.token = validationResp.token
                            SharedPreferenceManager.isLogin = true
                            logApp(SharedPreferenceManager.token!!)
                            val intent = Intent(requireActivity(), VideoActivity::class.java)
                            requireActivity().startActivity(intent)
                        }
                    }
                }
            }
        })
        errorMessageConfirmChildInfo.observe(viewLifecycleOwner, { error ->
            error?.let {
                makeSnackBar(it, requireView())
            }
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindUI(): Unit = with(bindingChild) {

        arguments?.let {
            ChildInfoFragmentArgs.fromBundle(it).validationResponse?.let { validationResp ->
                logApp("***********************${validationResp.token}")

                textViewRealBirthday.setOnClickListener {

                    createMaterialDatePicker(requireActivity()) { longDate ->
                        logApp(longDate.toString())

                        val date = Date(longDate)

                        textViewRealBirthday.text = date.toString()

                    }

                }
                textViewWaitingBrthday.setOnClickListener {
                    createMaterialDatePicker(requireActivity()) { longDate ->
                        logApp(longDate.toString())
                        val date = Date(longDate)
                        textViewWaitingBrthday.text = date.toString()
                    }
                }
                buttonNext.setOnClickListener {
                    val childName = editTextNameSurname.text.toString().trim()
                    val childWeight = editTextBirthdayWeight.text.toString().trim()
                    val emailAddress = editTextEmailAddress.text.toString().trim()
                    val referringDoctor = editTextReferringDoctor.text.toString().trim()
                    val estimatedBirthDate = textViewWaitingBrthday.text.toString().trim()
                    val childRealBirthday = textViewRealBirthday.text.toString().trim()
                    val gender = radioGroupGender.checkedRadioButtonId
                    val privacy = checkboxPrivacyPolicy
                    val contract = checkboxContractApp

                    when ("") {
                        childName -> {
                            createEditTextError(editTextNameSurname, getString(R.string.required))
                            return@setOnClickListener
                        }
                        estimatedBirthDate -> {
                            textViewWaitingBrthday.error = getString(R.string.required)
                            textViewRealBirthday.requestFocus()
                            return@setOnClickListener
                        }
                        childRealBirthday -> {
                            textViewRealBirthday.error = getString(R.string.required)
                            textViewRealBirthday.requestFocus()
                            // createEditTextError(editTextRealBirthday, getString(R.string.required))
                            return@setOnClickListener
                        }
                        childWeight -> {
                            createEditTextError(
                                editTextBirthdayWeight,
                                getString(R.string.required)
                            )
                            return@setOnClickListener
                        }
                        emailAddress -> {
                            createEditTextError(editTextEmailAddress, getString(R.string.required))
                            return@setOnClickListener
                        }


                    }

                    if (emailAddress.matches(emailPattern)) {
                        logApp("ChildInfoFragment : $emailAddress")
                    } else {
                        createEditTextError(
                            editTextEmailAddress,
                            getString(R.string.email_not_valid)
                        )
                        return@setOnClickListener
                    }

                    if (!privacy.isChecked) {
                        privacy.error = getString(R.string.required)
                        privacy.requestFocus()
                        return@setOnClickListener
                    } else if (!contract.isChecked) {
                        contract.error = getString(R.string.required)
                        contract.requestFocus()
                        return@setOnClickListener
                    }
                    createAlertDialogAny(
                        requireContext(),
                        R.layout.alert_dialog_view
                    ) { view, alertDialog ->
                        val buttonConfirm = view.findViewById<Button>(R.id.button_confirm_dialog)
                        val buttonReject = view.findViewById<Button>(R.id.button_reject_dialog)
                        buttonConfirm.setOnClickListener {
                            val childGender = if (gender == radioButtonMan.id) {
                                GENDER.MALE
                            } else {
                                GENDER.FEMALE
                            }
                            val child = Child(
                                doctorName = referringDoctor,
                                estimatedBirthDate = estimatedBirthDate,
                                grams = childWeight.toInt(),
                                name = childName,
                                realBirthDate = childRealBirthday,
                                sexuality = childGender.name
                            )
                            val childRequestBody = UserRequestBody(
                                child = child,
                                email = emailAddress,
                                privacyContract = privacy.isChecked,
                                reportContract = contract.isChecked
                            )
                            logApp("ChildInfoFragment : $childRequestBody")
                            if (validationResp.token.isNotEmpty()) {
                                viewModel.createUser(validationResp.token, childRequestBody)
                            } else {
                                logApp("ChildInfoFragment : ${validationResp.token}")
                            }

                            alertDialog.dismiss()
                        }
                        buttonReject.setOnClickListener {
                            alertDialog.dismiss()
                        }
                    }
                }
            }
        }


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createEditTextError(editText: EditText, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.requestFocus()
            editText.setError(
                message,
                requireContext().getDrawable(R.drawable.ic_baseline_info_24)
            )
        } else {
            editText.error = getString(R.string.required)
            editText.requestFocus()
        }

    }


    companion object {
        private const val TAG = "ChildInfoFragment"
    }
}