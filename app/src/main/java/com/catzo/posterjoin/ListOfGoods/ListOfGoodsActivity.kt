package com.catzo.posterjoin.ListOfGoods

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.catzo.posterjoin.R
import kotlinx.android.synthetic.main.activity_list_of_goods.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ListOfGoodsActivity : AppCompatActivity() {

    private val url:String ="https://android.joinposter.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val getProducts = "$url/api/menu.getProducts?token=87023814d70d5405d970aa8db0c8b225"
        AsyncTaskHandlerJSON().execute(getProducts)
        setContentView(R.layout.activity_list_of_goods)
    }

    inner class AsyncTaskHandlerJSON : AsyncTask<String,String,String>() {
        override fun doInBackground(vararg url: String?): String {
            val text:String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text = connection.inputStream.use { it.reader().use{reader ->reader.readText()} }
            }finally {
                connection.disconnect()
            }
            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson(result)
        }
    }

    private fun handleJson(jsonString: String?) {
        val response = JSONObject(jsonString)
        val jsonArray = response.getJSONArray("response")
        val list = ArrayList<ProductModel>()
        var x = 0
        while (x < jsonArray.length())
        {
            val jsonObject = jsonArray.getJSONObject(x)
            list.add(ProductModel(
                    jsonObject.getInt("product_id"),
                    jsonObject.getString("product_name"),
                    jsonObject.getInt("menu_category_id"),
                    jsonObject.getString("workshop"),
                    jsonObject.getString("weight_flag"),
                    jsonObject.getString("color"),
                    0,
                    0,
                    jsonObject.getString("barcode"),
                    jsonObject.getString("product_code"),
                    jsonObject.getJSONObject("price").getInt("1"),
                    jsonObject.getString("photo")
            ))
            x++
        }
        val adapter = ListOfProductsAdapter(this, list, url)
        MainList.adapter = adapter
    }
}
