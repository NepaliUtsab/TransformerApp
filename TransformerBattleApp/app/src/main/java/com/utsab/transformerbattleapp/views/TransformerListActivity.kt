package com.utsab.transformerbattleapp.views

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.utsab.transformerbattleapp.R
import com.utsab.transformerbattleapp.adapters.AutobotRVAdapter
import com.utsab.transformerbattleapp.adapters.DecepticonRVAdapter
import com.utsab.transformerbattleapp.databinding.ActivityTransformerListBinding
import com.utsab.transformerbattleapp.helpers.Constants
import com.utsab.transformerbattleapp.helpers.clickListeners.AutobotClickNavigator
import com.utsab.transformerbattleapp.helpers.clickListeners.DecepticonClickNavigator
import com.utsab.transformerbattleapp.viewModels.TransformerViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity for listing the autobot and decepticon, go to create activity, go to edit page, start battle
 */
@AndroidEntryPoint
class TransformerListActivity : AppCompatActivity(), AutobotClickNavigator, DecepticonClickNavigator {

    companion object {
        fun openTransformerListActivity(context: Context) {
            val intent = Intent(context, TransformerListActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val viewModel: TransformerViewModel by viewModels()
    private lateinit var binding: ActivityTransformerListBinding

    private lateinit var autobotAdapter: AutobotRVAdapter
    private lateinit var decepticonAdapter: DecepticonRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transformer_list)

        initUI()
        subscribeUI()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTransformersList()
    }

    override fun onBackPressed() {
        finishAffinity()
    }



    private fun subscribeUI() {
//region  updating the empty autobot and decepticon placeholders and recycle view
        viewModel.autobots.observe(this, { result ->
            autobotAdapter.addAutobots(result)
            if (result.isNullOrEmpty()) {
                binding.tvEmptyAutobotPlaceholder.visibility = View.VISIBLE
                binding.rvAutobots.visibility =View.INVISIBLE
            }else{
                binding.tvEmptyAutobotPlaceholder.visibility = View.GONE
                binding.rvAutobots.visibility =View.VISIBLE
            }
        })

        viewModel.decepticons.observe(this, { result ->
            decepticonAdapter.addDecepticons(result)
            if (result.isNullOrEmpty()) {
                binding.tvEmptyDecepticonPlaceholder.visibility = View.VISIBLE
                binding.rvDecepticons.visibility = View.INVISIBLE
            }else{
                binding.tvEmptyDecepticonPlaceholder.visibility = View.GONE
                binding.rvDecepticons.visibility = View.VISIBLE
            }
        })

        viewModel.winner.observe(this, {winner ->
            showWinnerAlert(winner)
        })
//endregion  updating the empty autobot and decepticon placeholders and recycle view

    }

    private fun showWinnerAlert(winner: String?) {
        val builder = AlertDialog.Builder(this)
//        Displaying approprate message based on the result
        when(winner){
            Constants.TEAM_AUTOBOT -> builder.setMessage(R.string.winner_message_autobot)
            Constants.TEAM_DECEPTICON -> builder.setMessage(R.string.winner_message_decepticon)
            Constants.NO_BATTLE -> builder.setMessage(R.string.error_message_no_battle)
            else -> builder.setMessage(R.string.winner_message_draw)
        }
        with(builder){
            setTitle(R.string.winner_title)
            setPositiveButton(R.string.dismiss) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            setCancelable(true)
            show()
        }
    }

    private fun initUI() {
//        initializing recycleviews and adapters for autobot and decepticon
        binding.rvAutobots.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            autobotAdapter = AutobotRVAdapter()
            autobotAdapter.setNavigator(this@TransformerListActivity)
            val layoutManager = binding.rvAutobots.layoutManager as LinearLayoutManager
            this.addItemDecoration(
                DividerItemDecoration(
                    binding.rvAutobots.context,
                    layoutManager.orientation
                )
            )
            this.adapter = autobotAdapter
        }

        binding.rvDecepticons.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            decepticonAdapter = DecepticonRVAdapter()
            decepticonAdapter.setNavigator(this@TransformerListActivity)
            val layoutManager = binding.rvDecepticons.layoutManager as LinearLayoutManager
            this.addItemDecoration(
                DividerItemDecoration(
                    binding.rvDecepticons.context,
                    layoutManager.orientation
                )
            )
            this.adapter = decepticonAdapter
        }


    }


    //region click_listeners


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem) =  when(item.itemId) {
        R.id.action_battle -> {
            /**
             * Checking if both teams have at least one member to start a battle
             */
            if(viewModel.autobots.value!!.size > 1 && viewModel.decepticons.value!!.size > 1){
                viewModel.startBattle()
                true
            }else{
                showWinnerAlert(Constants.NO_BATTLE)
                false
            }
        }
        android.R.id.home -> {
            finish()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    //region click listener implementation for recycle view adapters
    override fun onAutobotEditButtonClicked(id: String, team: String, teamIcon: String) {
        AddTransformerActivity.openAddTransformerActivity(this, id, team, teamIcon)
    }

    override fun onDecepticonEditButtonClicked(id: String, team: String, teamIcon: String) {
        AddTransformerActivity.openAddTransformerActivity(this, id, team, teamIcon)
    }

    //endregion click listener implementation for recycle view adapters

    fun addButtonClicked(view: View) {
        when(view.id){
            R.id.ibCreateAutobot -> {
                viewModel.createAutobot()
                AddTransformerActivity.openAddTransformerActivity(this, "", Constants.TEAM_AUTOBOT, viewModel.transformer.value!!.team_icon)
            }
            R.id.ibCreateDecepticon -> {
                viewModel.createDecepticon()
                AddTransformerActivity.openAddTransformerActivity(this, "", Constants.TEAM_DECEPTICON, viewModel.transformer.value!!.team_icon)
            }
        }
    }

    //endregion click listeners
}