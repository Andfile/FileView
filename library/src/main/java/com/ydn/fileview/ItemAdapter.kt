package com.ydn.fileview

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView

class ItemAdapter : PagedListAdapter<Item, RecyclerView.ViewHolder>(ItemDiffCallback()) {
    var dataSource: FileDataSource? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == R.layout.row_progress) return ProgressViewHolder.create(parent) else return ItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < itemCount) {
            val item = getItem(position)

            if (item!!.isVisible) {
                holder.itemView.visibility = View.VISIBLE
                holder.itemView.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            } else {
                holder.itemView.visibility = View.GONE
                holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
            }

            //if (getItemViewType(position) == R.layout.file_view_item) {
                (holder as ItemViewHolder).bind(getItem(position))
            //} else {
            //    ((ProgressViewHolder) holder).bind(hasExtraRow() && position == getItemCount() - 1);
            //}
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setLayoutRes(layoutRes: Int) {
        Companion.layoutRes = layoutRes
    }

    fun setProgressDlgRes(progressDlgRes: Int) {
        Companion.progressDlgRes = progressDlgRes
    }

    private class ItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var itemTextView = itemView.findViewById<TextView>(R.id.line)

        internal fun bind(item: Item?) {
            itemTextView.setText(item!!.text)
        }

        companion object {

            internal fun create(parent: ViewGroup): ItemViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(if (layoutRes == 0) R.layout.file_item else layoutRes, parent, false)
                return ItemViewHolder(view)
            }
        }
    }

    private class ProgressViewHolder internal constructor(progressView: View) : RecyclerView.ViewHolder(progressView) {
        var progressBar = itemView.findViewById<ProgressBar>(R.id.progressbar)

        fun bind(visible: Boolean) {
            progressBar.visibility = if (visible) View.VISIBLE else View.GONE
        }

        companion object {

            internal fun create(parent: ViewGroup): ProgressViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(if (progressDlgRes == 0) R.layout.row_progress else progressDlgRes, parent, false)
                return ProgressViewHolder(view)
            }
        }
    }

    companion object {

        internal var layoutRes = 0
        internal var progressDlgRes = 0
    }

    private class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {

        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.text == newItem.text
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.text.equals(newItem.text)
        }
    }
}
