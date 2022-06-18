package com.aungbophyoe.space.listvideodownload.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aungbophyoe.space.listvideodownload.R
import com.aungbophyoe.space.listvideodownload.databinding.ActivityMainBinding
import com.aungbophyoe.space.listvideodownload.model.Video
import com.aungbophyoe.space.listvideodownload.recycleradapter.VideoListRecyclerAdapter
import com.aungbophyoe.space.listvideodownload.utility.Constants.REQUEST_STORAGE_READ_WRITE_PERMISSION
import com.aungbophyoe.space.listvideodownload.utility.Utility
import com.aungbophyoe.space.listvideodownload.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),EasyPermissions.PermissionCallbacks,VideoListRecyclerAdapter.DownloadClickListener {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding
    private val viewModel : MainViewModel by viewModels()
    private val videoListRecyclerAdapter  by lazy {
        VideoListRecyclerAdapter(this,this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding!!.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)
            videoListRecyclerAdapter.submitList(emptyList())
            recyclerView.adapter = videoListRecyclerAdapter
            viewModel.getVideoList()
        }
        observeData()
    }

    private fun observeData(){
        try {
            binding!!.apply {
                lifecycleScope.launchWhenStarted {
                    viewModel.videoList.collectLatest {
                        videoListRecyclerAdapter.submitList(it)
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }catch (e:Exception){
            Log.d("error","${e.message}")
        }
    }

    private fun requestPermission(){
        if(Utility.hasStoragePermission(this)){
            return
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            Toast.makeText(this,"Storage Permission Denied.", Toast.LENGTH_SHORT).show()
            val i = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, Uri.parse("package:"+this.packageName))
            i.addCategory(Intent.CATEGORY_DEFAULT)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
        }else{
            EasyPermissions.requestPermissions(
                this,
                "Read and write external storage permission are needed to save download file.",
                REQUEST_STORAGE_READ_WRITE_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }else{
            requestPermission()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDownload(video: Video) {
        Log.d("download","${video.id}")
    }
}