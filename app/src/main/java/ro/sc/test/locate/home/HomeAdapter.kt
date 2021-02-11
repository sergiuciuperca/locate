package ro.sc.test.locate.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ro.sc.test.locate.R
import ro.sc.test.locate.data.entities.ui.HomeItemData
import ro.sc.test.locate.databinding.ItemHomeBinding

class HomeAdapter(private val cornerSize: Int) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root)

    private val items = mutableListOf<HomeItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.itemTitle.text = item.title
        holder.binding.infoHour.text = item.time
        holder.binding.infoAvailable.text = item.info
        holder.binding.infoPrice.text = item.price

        Glide.with(holder.binding.itemImg)
            .load(item.imageUrl)
            .transform(RoundedCorners(cornerSize))
            .into(holder.binding.itemImg)

        val resources = holder.binding.root.resources
        holder.binding.starImg1.setImageDrawable(
            if (item.stars >= 1) ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_star,
                null
            ) else
                ResourcesCompat.getDrawable(resources, R.drawable.ic_star_disabled, null)
        )

        holder.binding.starImg2.setImageDrawable(
            if (item.stars >= 2) ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_star,
                null
            ) else
                ResourcesCompat.getDrawable(resources, R.drawable.ic_star_disabled, null)
        )

        holder.binding.starImg3.setImageDrawable(
            if (item.stars >= 3) ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_star,
                null
            ) else
                ResourcesCompat.getDrawable(resources, R.drawable.ic_star_disabled, null)
        )

        holder.binding.starImg4.setImageDrawable(
            if (item.stars >= 4) ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_star,
                null
            ) else
                ResourcesCompat.getDrawable(resources, R.drawable.ic_star_disabled, null)
        )

        holder.binding.starImg5.setImageDrawable(
            if (item.stars >= 5) ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_star,
                null
            ) else
                ResourcesCompat.getDrawable(resources, R.drawable.ic_star_disabled, null)
        )
    }

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(data: List<HomeItemData>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }
}
