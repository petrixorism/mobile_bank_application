package uz.gita.myapplication.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import uz.gita.myapplication.R
import uz.gita.myapplication.data.source.remote.request.ProfileInfoRequest
import uz.gita.myapplication.databinding.FragmentProfileBinding
import uz.gita.myapplication.ui.viewmodel.ProfileViewModel
import uz.gita.myapplication.util.showSnackBar
import uz.gita.myapplication.util.showToast
import java.io.File
import java.io.IOException


@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile), EasyPermissions.PermissionCallbacks {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: ProfileViewModel by viewModels()
    private var photoFile = File("")

    @SuppressLint("IntentReset")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launchWhenCreated {
            viewModel.loadingFlow.collect {
                binding.loadingPb.isVisible = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.avatarUrlFlow.collect {
                val split = it.replaceFirst(":8080", "")
                val url = "https:" + split.substring(5)
                Glide
                    .with(requireContext())
                    .load(url)
                    .error(R.drawable.img)
                    .centerCrop()
                    .placeholder(R.drawable.img)
                    .into(binding.profilePic);
                Glide
                    .with(requireContext())
                    .load(url)
                    .error(R.drawable.img)
                    .centerCrop()
                    .placeholder(R.drawable.img)
                    .into(binding.avatarBackgroundIv)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.messageFlow.collect {
                showToast(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.infoFlow.collect {
                binding.firstNameEt.setText(it.firstName)
                binding.lastNameEt.setText(it.lastName)
                binding.passwordTet.setText(it.password)
                binding.phoneEt.text = it.phone
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.updateFlow.collect {
                showSnackBar(getString(R.string.info_updated))
                binding.firstNameEt.setText(it.firstName)
                binding.lastNameEt.setText(it.lastName)
                binding.passwordTet.setText(it.password)
                binding.phoneEt.text = it.phone
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.successAvatarFlow.collect {
                showSnackBar(it)
                viewModel.getAvatar()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.logoutFlow.collect {
                findNavController().navigate(ProfileFragmentDirections.actionGlobalSplashFragment())
            }
        }

        binding.addPhotoButton.setOnClickListener {

            if (hasPermission()) {
                picImage()
            } else {
                requestPermission()
            }

        }
        binding.buttonEditProfile.clicks()
            .onEach {
                viewModel.updateProfile(
                    ProfileInfoRequest(
                        binding.firstNameEt.text.toString(),
                        binding.lastNameEt.text.toString(),
                        binding.passwordTet.text.toString(),
                    )
                )
            }.debounce(3000L)
            .launchIn(lifecycleScope)


        binding.logoutButton.setOnClickListener {
            logoutDialog()
        }

        viewModel.getAvatar()
        viewModel.getProfileInfo()

    }

    @SuppressLint("IntentReset")
    private fun picImage() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    private fun logoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        //set title for alert dialog
        builder.setTitle(getString(R.string.logout))
        //set message for alert dialog
        builder.setMessage(getString(R.string.logout_quote))

        //performing positive action
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            viewModel.logout()
        }
        //performing cancel action
        builder.setNeutralButton(getString(R.string.cancel)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(true)
        alertDialog.show()

    }

    private fun hasPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions, grantResults, grantResults, this
        )
    }

    private fun requestPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This application cannot work without permission",
            1,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestPermission()
        }


    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        picImage()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                try {
                    val path = getRealPathFromURI(it, requireActivity())
                    if (path != null) {
                        photoFile = File(path)


                        val builder = AlertDialog.Builder(requireContext())
                        //set title for alert dialog
                        builder.setTitle(getString(R.string.image_uploaded))
                        //set message for alert dialog
                        builder.setMessage(getString(R.string.image_quote))

                        //performing positive action
                        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                            viewModel.uploadAvatar(photoFile)
                        }
                        //performing cancel action
                        builder.setNeutralButton(getString(R.string.cancel)) { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }

                        val alertDialog: AlertDialog = builder.create()
                        // Set other dialog properties
                        alertDialog.setCancelable(true)
                        alertDialog.show()


                    } else {
                        showToast("Try again")
                    }
                } catch (e: IOException) {
                    showToast(e.message.toString())
                }
            }
        }
    }

    private fun getRealPathFromURI(contentURI: Uri?, context: Activity): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.managedQuery(
            contentURI, projection, null,
            null, null
        ) ?: return null
        val columnIndex = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        return if (cursor.moveToFirst()) cursor.getString(columnIndex) else null
    }


}