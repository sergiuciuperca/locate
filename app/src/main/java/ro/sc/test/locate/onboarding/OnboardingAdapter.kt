package ro.sc.test.locate.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ro.sc.test.locate.databinding.ItemOnboardingBinding

class OnboardingAdapter : RecyclerView.Adapter<OnboardingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = 3

    class ViewHolder(binding: ItemOnboardingBinding) : RecyclerView.ViewHolder(binding.root)
}