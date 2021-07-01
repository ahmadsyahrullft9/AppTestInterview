package net.tiap.testappinterview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import net.tiap.testappinterview.adapters.ProductAdapter
import net.tiap.testappinterview.apis.ApiClient
import net.tiap.testappinterview.apis.Users
import net.tiap.testappinterview.binding.BaseActivityBinding
import net.tiap.testappinterview.databinding.ActivityMainBinding
import net.tiap.testappinterview.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivityBinding<ActivityMainBinding>() {

    private val TAG = "MainActivity"
    val retrofit = ApiClient.getClien()
    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var categoryCall: Call<List<String>>
    lateinit var productListCall: Call<List<Product>>
    lateinit var productAdapter: ProductAdapter
    var categori: String = ""

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun setupView(binding: ActivityMainBinding) {
        activityMainBinding = binding

        getCategory()

        getProductList()

        binding.btnFilter.setOnClickListener {
            productListCall?.cancel()
            getProductList()
        }
    }

    private fun getProductList() {
        if (categori != "") {
            productListCall = retrofit.create(Users::class.java).getProducts(categori)
        } else {
            productListCall = retrofit.create(Users::class.java).getProducts()
        }
        productListCall.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (!call.isCanceled) {
                    if (response.isSuccessful) {
                        val productList: List<Product> = response.body()!!
                        productAdapter = ProductAdapter(this@MainActivity, productList)
                        activityMainBinding.apply {
                            rvProduct.layoutManager = LinearLayoutManager(this@MainActivity)
                            rvProduct.setHasFixedSize(true)
                            rvProduct.adapter = productAdapter
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "data produk tidak ditemukan",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@MainActivity, "terjadi kesalahan", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getCategory() {
        categoryCall = retrofit.create(Users::class.java).getCategories()
        categoryCall.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (!call.isCanceled) {
                    if (response.isSuccessful) {
                        val categories: List<String> = response.body()!!
                        val categoriAdapter = ArrayAdapter<String>(
                            this@MainActivity,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            categories
                        )
                        activityMainBinding.apply {
                            spCategory.adapter = categoriAdapter
                            spCategory.onItemSelectedListener = object :
                                AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    categori = parent?.getItemAtPosition(position).toString()
                                    Log.d(TAG, "onItemClick: $categori")
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    TODO("Not yet implemented")
                                }

                            }
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "data kategori tidak ditemukan",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@MainActivity, "terjadi kesalahan", Toast.LENGTH_LONG).show()
            }

        })
    }


    override fun onDestroy() {
        categoryCall?.cancel()
        productListCall?.cancel()
        super.onDestroy()
    }
}