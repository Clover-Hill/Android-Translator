package com.newera.neo_translator

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.newera.neo_translator.data.WordItem
import com.newera.neo_translator.databinding.ItemWordBinding

val Comparator = object : DiffUtil.ItemCallback<WordItem>() {
    override fun areItemsTheSame(oldItem: WordItem, newItem: WordItem) : Boolean
    {
        return oldItem.query == newItem.query
    }
    override fun areContentsTheSame(oldItem: WordItem, newItem: WordItem) : Boolean
    {
        return oldItem == newItem
    }
}

class WordListAdapter() : ListAdapter<WordItem, WordListAdapter.ViewHolder>(Comparator)
{
    inner class ViewHolder(private val binding: ItemWordBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(wordItem : WordItem)
        {
            if(wordItem.translation.isEmpty())
            {
                binding.wordTextview.text = "Sorry there are no translation"
                binding.phoneticTextview.text = ""
                binding.webdictTextview.text = ""
            }
            else
            {
                binding.wordTextview.text = wordItem.translation[0]
                binding.phoneticTextview.text = "Phonetic: ["+wordItem.basic.phonetic+"]"
                binding.webdictTextview.text = "Link: "+wordItem.webdict.url
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val binding = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val wordItem = getItem(position)
        holder.bind(wordItem)
    }

}