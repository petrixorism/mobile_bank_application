package uz.gita.myapplication.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.myapplication.R
import uz.gita.myapplication.data.source.remote.response.CardResponse
import uz.gita.myapplication.databinding.ItemCardBinding

class CardTransferAdapter : ListAdapter<CardResponse, CardTransferAdapter.ViewHolder>(DiffItem) {

    private var selectCardListener: ((CardResponse) -> Unit)? = null

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val binding by viewBinding(ItemCardBinding::bind)
        private val cardNameTextView = binding.cardsName
        private val cardBalanceTextView = binding.cardSum
        private val panTextView = binding.cardPan
        private val ownerNameTextView = binding.cardOwnerName
        private val expTextView = binding.cardExp
        private val cardColor = binding.cardColor

        private val editButton = binding.editCard
        private val deleteButton = binding.deleteCard
        private val ignoreBalanceButton = binding.ignoreBalanceCard


        init {
            binding.root.setOnClickListener {
                selectCardListener?.invoke(
                    getItem(absoluteAdapterPosition)
                )
            }

        }

        @SuppressLint("SetTextI18n")
        fun bind() {
            deleteButton.isVisible = false
            editButton.isVisible = false
            ignoreBalanceButton.isVisible = false
            getItem(absoluteAdapterPosition).apply {
                when (color) {
                    0 -> cardColor.setImageResource(R.drawable.ic_card8)
                    1 -> cardColor.setImageResource(R.drawable.ic_card1)
                    2 -> cardColor.setImageResource(R.drawable.ic_card2)
                    3 -> cardColor.setImageResource(R.drawable.ic_card3)
                    4 -> cardColor.setImageResource(R.drawable.ic_card4)
                    5 -> cardColor.setImageResource(R.drawable.ic_card5)
                    6 -> cardColor.setImageResource(R.drawable.ic_card6)
                    7 -> cardColor.setImageResource(R.drawable.ic_card7)
                    else -> cardColor.setImageResource(R.drawable.ic_card8)

                }
                cardBalanceTextView.text = balance.toString()
                cardNameTextView.text = cardName
                ownerNameTextView.text = owner
                expTextView.text = exp
                panTextView.text =
                    pan.substring(0, 4) + " " + pan.substring(4, 8) + " " + pan.substring(
                        8,
                        12
                    ) + " " + pan.substring(12)

            }
        }

    }

    object DiffItem : DiffUtil.ItemCallback<CardResponse>() {
        override fun areItemsTheSame(oldItem: CardResponse, newItem: CardResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CardResponse, newItem: CardResponse): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    fun setClickCardListener(block: (CardResponse) -> Unit) {
        selectCardListener = block
    }


}