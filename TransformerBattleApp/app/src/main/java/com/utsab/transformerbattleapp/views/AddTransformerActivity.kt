package com.utsab.transformerbattleapp.views

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.squareup.picasso.Picasso
import com.utsab.transformerbattleapp.R
import com.utsab.transformerbattleapp.databinding.ActivityAddTransformerBinding
import com.utsab.transformerbattleapp.helpers.Constants
import com.utsab.transformerbattleapp.models.Transformer
import com.utsab.transformerbattleapp.viewModels.TransformerViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity for adding new transformer/update or delete existing transformer
 *
 */
@AndroidEntryPoint
class AddTransformerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransformerBinding
    private val viewModel: TransformerViewModel by viewModels()

    companion object {
        fun openAddTransformerActivity(
            context: Context,
            id: String,
            team: String,
            teamIcon: String
        ) {
            val intent = Intent(context, AddTransformerActivity::class.java)
            intent.putExtra(Constants.ID, id)
            intent.putExtra(Constants.TEAM, team)
            intent.putExtra(Constants.TEAM_ICON, teamIcon)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_transformer)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        /*
        * Using a temporary model for storing values from intent
        * */
        val tempTransformer = Transformer()
        tempTransformer.id = intent.getStringExtra(Constants.ID).toString()
        tempTransformer.team = intent.getStringExtra(Constants.TEAM).toString()
        tempTransformer.team_icon = intent.getStringExtra(Constants.TEAM_ICON).toString()

        initUI()

        //region binding ui
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.transformer.value = tempTransformer
        if (tempTransformer.id != "")
            viewModel.getTransformerModelById()
        //endregion binding ui

    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initUI() {
        viewModel.transformer.observe(this, {
//            Showing autobot or decepticon icon based on add button selected on list page
            when (it.team) {
                Constants.TEAM_AUTOBOT -> binding.ivTrasformerIcon.background = resources.getDrawable(R.mipmap.ic_autobot)
                Constants.TEAM_DECEPTICON -> binding.ivTrasformerIcon.background = resources.getDrawable(R.mipmap.ic_decepticon)
            }
        })

//      Showing alert & Closing the activity after success transformer creation
        viewModel.isCreated.observe(this, { isCreated ->
            if (isCreated) {
                showAlert(Constants.CREATED_MESSAGE)
            }
        })

//        Showing alert & Closing the activity after success transformer deletion
        viewModel.isModelDeleted.observe(this, {
            if (it) {
                showAlert(Constants.DELETED_MESSAGE)
            }
        })

        viewModel.isModelUpdated.observe(this, {
            if (it) {
                showAlert(Constants.UPDATED_SUCCESSFULLY)
            }
        })

        viewModel.errorOccured.observe(this, {
            if (it) showAlert(Constants.UNKNOWN_FAILURE)
        })

    }

    /**
     * @param message
     * Shows alert dialog with the appropriate message
     *
     */
    private fun showAlert(message: String?) {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle(R.string.app_name)
                .setMessage(message)
            setPositiveButton(R.string.dismiss) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                when (message) {
                    Constants.CREATED_MESSAGE -> {
                        finish()
                    }
                    Constants.DELETED_MESSAGE -> {
                        finish()
                    }
                }
            }
            setCancelable(false)
            show()
        }
    }
}