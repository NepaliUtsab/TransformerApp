package com.utsab.transformerbattleapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utsab.transformerbattleapp.R
import com.utsab.transformerbattleapp.databinding.ItemTransformerBinding
import com.utsab.transformerbattleapp.helpers.clickListeners.AutobotClickNavigator
import com.utsab.transformerbattleapp.helpers.clickListeners.DecepticonClickNavigator
import com.utsab.transformerbattleapp.models.Transformer


/**
 * Recycle view adapter for Autobot listing in TransformerListActivity
 */
class DecepticonRVAdapter: RecyclerView.Adapter<DecepticonRVAdapter.DecepticonViewHolder>(){

    private var decepticons: ArrayList<Transformer> = arrayListOf()
    private lateinit var navigator : DecepticonClickNavigator
    fun addDecepticons(decepticons: List<Transformer>) {
        this.decepticons = decepticons as ArrayList<Transformer>
        notifyDataSetChanged()
    }


    fun setNavigator(navigator: DecepticonClickNavigator){
        this.navigator = navigator
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DecepticonViewHolder {
        val binding: ItemTransformerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_transformer, parent, false
        )

        return DecepticonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DecepticonViewHolder, position: Int) {
        holder.binding.transformerModel = decepticons[position]
        holder.binding.tvRating.text = decepticons[position].getRating().toString()
        holder.binding.rlItems.setOnClickListener {
            navigator.onDecepticonEditButtonClicked(
                decepticons[position].id,
                decepticons[position].team,
                decepticons[position].team_icon
            )
        }
        Picasso.get().load(decepticons[position].team_icon)
            .into(holder.binding.ivTeamLogo)
    }

    override fun getItemCount(): Int {
        return if (decepticons.isNotEmpty()) decepticons.size
        else 0
    }



    class DecepticonViewHolder(binding: ItemTransformerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemTransformerBinding = binding

    }

}