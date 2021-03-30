package com.utsab.transformerbattleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.utsab.transformerbattleapp.databinding.ActivityMainBinding
import com.utsab.transformerbattleapp.viewModels.TransformersTokenViewModel
import com.utsab.transformerbattleapp.views.TransformerListActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //    private val viewModel by viewModels<TransformersViewModel>()
    private val viewModel: TransformersTokenViewModel by viewModels()


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        subscribeUi()

    }

    private fun subscribeUi() {
        viewModel.isFirstTime.observe(this, Observer { result ->
            if (!result) TransformerListActivity.openTransformerListActivity(this@MainActivity)
        })
    }


    fun beginClicked(view: View) {
        when (view.id) {
            R.id.btnBegin -> {
                TransformerListActivity.openTransformerListActivity(this@MainActivity)
            }
        }
    }
}