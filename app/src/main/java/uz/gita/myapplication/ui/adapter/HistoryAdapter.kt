package uz.gita.myapplication.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.gita.myapplication.R
import uz.gita.myapplication.data.source.remote.response.HistoryItem
import uz.gita.myapplication.databinding.ItemHistoryIncomeBinding
import uz.gita.myapplication.databinding.ItemHistoryOutcomeBinding
import java.text.SimpleDateFormat
import java.util.*


class HistoryAdapter :
    PagingDataAdapter<HistoryItem, HistoryAdapter.ViewHolder>(MyDiffUtil) {
    private var itemClick: ((HistoryItem) -> Unit)? = null
    private val INCOME_VIEW = 1
    private val OUTCOME_VIEW = 2

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind()
    }

    inner class IncomeViewHolder(private val itemIncomeBinding: ItemHistoryIncomeBinding) :
        ViewHolder(itemIncomeBinding.root) {

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        override fun bind() {
            val data = getItem(absoluteAdapterPosition)!!
            data.apply {

                val sdf = SimpleDateFormat("d MMM yyyy HH:mm:ss")
                val netDate = Date(this.time)
                val time = sdf.format(netDate)

                itemIncomeBinding.dateTv.text = time
                itemIncomeBinding.nameTv.text = this.owner
                itemIncomeBinding.amountTv.text = this.amount.toString()
                itemIncomeBinding.feeTv.text = "${itemIncomeBinding.root.context.getString(R.string.fee)} ${this.fee}"
            }

            itemIncomeBinding.root.setOnClickListener {
                getItem(absoluteAdapterPosition)?.let { it1 ->
                    itemClick!!.invoke(it1)
                }
            }

        }
    }

    inner class OutcomeViewHolder(private val itemOutcomeBinding: ItemHistoryOutcomeBinding) :
        ViewHolder(itemOutcomeBinding.root) {
        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        override fun bind() {
            val data = getItem(absoluteAdapterPosition)!!

            data.apply {
                val sdf = SimpleDateFormat("d MMM yyyy HH:mm:ss")
                val netDate = Date(this.time)
                val time = sdf.format(netDate)

                itemOutcomeBinding.dateTv.text = time
                itemOutcomeBinding.nameTv.text = this.owner
                itemOutcomeBinding.amountTv.text = this.amount.toString()
                itemOutcomeBinding.feeTv.text = "${itemOutcomeBinding.root.context.getString(R.string.fee)} ${this.fee}"
            }

            itemOutcomeBinding.root.setOnClickListener {
                getItem(absoluteAdapterPosition)?.let { it1 ->
                    itemClick!!.invoke(it1)
                }
            }

        }
    }

    object MyDiffUtil : DiffUtil.ItemCallback<HistoryItem>() {
        override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemViewType(position: Int): Int {
        val sender = getItem(position)?.sender
        return if (sender == null) {
            OUTCOME_VIEW
        } else INCOME_VIEW

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            INCOME_VIEW -> IncomeViewHolder(
                ItemHistoryIncomeBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            else -> OutcomeViewHolder(
                ItemHistoryOutcomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    fun setItemClick(block: (HistoryItem) -> Unit) {
        itemClick = block
    }

}