package ru.mail.fancywork.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mail.fancywork.databinding.ViewFancyworkBinding
import ru.mail.fancywork.model.datatype.Fancywork
import ru.mail.fancywork.ui.primary.MainActivity
import ru.mail.fancywork.ui.primary.ShowcaseActivity

class FancyworkAdapter(
    private var worksList: List<Fancywork>
) : RecyclerView.Adapter<FancyworkAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewFancyworkBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ViewFancyworkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(worksList[position]) {
                binding.name.text = title
                binding.difficulty.rating = 4F
                binding.colorText.text = "Цветов: $colors"
                binding.sizeText.text = "Размер: ${width} x $height"

                if (bitmap != null) {
                    binding.image.setImageBitmap(bitmap)
                }
                binding.layout.setOnClickListener {
                    itemView.context.startActivity(
                        Intent(
                            itemView.context,
                            ShowcaseActivity::class.java
                        ).apply {
                            putExtra(MainActivity.FANCYWORK_MESSAGE, worksList[position])
                        })
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return worksList.size
    }
}
