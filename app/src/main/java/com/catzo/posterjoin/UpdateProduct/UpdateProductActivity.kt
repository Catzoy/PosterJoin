package com.catzo.posterjoin.UpdateProduct

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.beust.klaxon.Klaxon
import com.catzo.posterjoin.ListOfGoods.ProductModel
import com.catzo.posterjoin.R
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class UpdateProductActivity : AppCompatActivity() {

    private val url:String ="https://android.joinposter.com"
    private var product:ProductModel?=null
    lateinit var THE_item:MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_product)
        product = intent.extras.getSerializable("Product") as ProductModel

        ProductName.setText(product!!.product_name)
        ProductBarcode.setText(product!!.barcode)
        ProductCost.setText(product!!.price.toString())
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.action_Save ->
            {
                THE_item=item
                item.isEnabled = false
                product!!.barcode = if(!ProductBarcode.text.toString().isEmpty())ProductBarcode.text.toString() else product!!.barcode
                product!!.product_name = if(!ProductBarcode.text.toString().isEmpty())ProductName.text.toString() else product!!.product_name
                product!!.price = if(!ProductCost.text.toString().isEmpty())ProductCost.text.toString().toInt() else product!!.price
                val updateProducts = "$url/api/menu.updateProduct?token=87023814d70d5405d970aa8db0c8b225"
                AsyncTaskHandlerJSON(product!!, this).execute(updateProducts)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class AsyncTaskHandlerJSON(val product: ProductModel, val context: Context) : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg url: String?): String {
            val text : String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true

            val jsonstring = Klaxon().toJsonString(product)

            connection.setRequestProperty("charset", "utf-8")
            connection.setRequestProperty("Content-Type", "application/json")
            try {
                connection.connect()
                connection.outputStream.write(jsonstring.toByteArray())
                connection.outputStream.flush()

                text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            }
            finally {
                connection.disconnect()
            }

            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val response = JSONObject(result)
            if(response.has("error")) {
                Toast.makeText(
                        context,
                        "error: " + response.getString("error") + "\nmessage: " + response.getString("message"),
                        Toast.LENGTH_SHORT)
                        .show()
                THE_item.isEnabled = true
            }
            else finish()
        }
    }
}
