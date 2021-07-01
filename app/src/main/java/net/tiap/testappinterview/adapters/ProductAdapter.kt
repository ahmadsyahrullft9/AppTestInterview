package net.tiap.testappinterview.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.tiap.testappinterview.R
import net.tiap.testappinterview.models.Product

class ProductAdapter(
    val context: Context,
    var productList: List<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.onbind(productList.get(position))
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtProductName: TextView
        var txtCategory: TextView
        var txtDescription: TextView
        var ivProduct: ImageView

        init {
            txtProductName = itemView.findViewById(R.id.txt_product_name)
            txtCategory = itemView.findViewById(R.id.txt_category)
            txtDescription = itemView.findViewById(R.id.txt_description)
            ivProduct = itemView.findViewById(R.id.iv_product)
        }

        fun onbind(product: Product) {
            txtProductName.text = product.title
            txtCategory.text = product.category
            txtDescription.text = product.description
            Glide.with(itemView).load(product.image).into(ivProduct)
        }
    }
}