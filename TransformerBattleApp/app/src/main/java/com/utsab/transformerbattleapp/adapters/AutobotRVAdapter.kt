package com.utsab.transformerbattleapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utsab.transformerbattleapp.R
import com.utsab.transformerbattleapp.databinding.ItemTransformerBinding
import com.utsab.transformerbattleapp.helpers.clickListeners.AutobotClickNavigator
import com.utsab.transformerbattleapp.models.Transformer

/**
 * Recycle view adapter for Autobot listing in TransformerListActivity
 */
class AutobotRVAdapter: RecyclerView.Adapter<AutobotRVAdapter.AutobotViewHolder>() {

    private var autobots: ArrayList<Transformer> = arrayListOf()
    private lateinit var navigator : AutobotClickNavigator
    fun addAutobots(autobots: List<Transformer>) {
        this.autobots = autobots as ArrayList<Transformer>
        notifyDataSetChanged()
    }

    fun setNavigator(navigator: AutobotClickNavigator){
        this.navigator = navigator
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutobotViewHolder {
        val binding: ItemTransformerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_transformer, parent, false
        )

        return AutobotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AutobotViewHolder, position: Int) {
        holder.binding.transformerModel = autobots[position]
        holder.binding.tvRating.text = autobots[position].getRating().toString()
        holder.binding.rlItems.setOnClickListener {
            navigator.onAutobotEditButtonClicked(autobots[position].id,
                autobots[position].team,
                autobots[position].team_icon
                )
        }
//        Using Picasso to load the images from the network
        Picasso.get().load(autobots[position].team_icon)
            .into(holder.binding.ivTeamLogo)
    }

    override fun getItemCount(): Int {
        return if (autobots.isNotEmpty()) autobots.size
        else 0
    }


    class AutobotViewHolder(binding: ItemTransformerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemTransformerBinding = binding

    }


}