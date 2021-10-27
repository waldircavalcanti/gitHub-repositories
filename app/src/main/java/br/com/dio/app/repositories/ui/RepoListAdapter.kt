package br.com.dio.app.repositories.ui

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.dio.app.repositories.core.DotColor
import br.com.dio.app.repositories.data.model.Repo
import br.com.dio.app.repositories.databinding.ItemRepoBinding
import com.bumptech.glide.Glide

class RepoListAdapter(private val onItemClicked: (Repo) -> Unit) :
    ListAdapter<Repo, RepoListAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRepoBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClicked)
    }


    inner class ViewHolder(
        private val binding: ItemRepoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Repo, onItemClicked: (Repo) -> Unit) {
            binding.tvRepoName.text = item.name
            binding.tvRepoDescription.text = item.description
            binding.tvRepoLanguage.text = item.language
            binding.chipStar.text = item.stargazersCount.toString()
            val url = "https://ui-avatars.com/api/?background=random&name=${item.name}"
            Glide.with(binding.root.context)
                .load(Uri.parse(url)).into(binding.ivOwner)

            listColors().forEach { colors ->
                if (colors.nameLanguage == item.language) {
                    binding.tvRepoLanguage.setTextColor(colors.colorDot)
                }

                Log.e("TAG", "${colors.colorDot}")
            }

            itemView.setOnClickListener {
                onItemClicked(item)
            }
        }
    }
}


private fun listColors(): MutableList<DotColor> {
    val colors: MutableList<DotColor> = mutableListOf()
    DotColor.values().forEach { color -> colors.add(color) }
    return colors
}


class DiffCallback : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Repo, newItem: Repo) = oldItem.id == newItem.id
}