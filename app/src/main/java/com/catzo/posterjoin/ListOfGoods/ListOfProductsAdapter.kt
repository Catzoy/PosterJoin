package com.catzo.posterjoin.ListOfGoods

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.catzo.posterjoin.R
import com.catzo.posterjoin.UpdateProduct.UpdateProductActivity
import java.net.URL


class ListOfProductsAdapter(val context: Context, val list:ArrayList<ProductModel>, val url:String):BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.product_card, parent, false)
        val pname = view.findViewById<TextView>(R.id.ProductNameTextView)
        pname.text = list[position].product_name
        val ppicture = view.findViewById<ImageView>(R.id.ProductImage)
        DownloadImageTask(ppicture).execute((url+list[position].photo))
        view.setOnClickListener()
        {
            val intent = Intent(context, UpdateProductActivity::class.java)
            intent.putExtra("Product", list[position])
            context.startActivity(intent)
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    private inner class DownloadImageTask(var bmImage: ImageView) : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            val url_display = urls[0]
            var mIcon11: Bitmap? = null
            val stream = URL(url_display).openStream()
            mIcon11 = BitmapFactory.decodeStream(stream)
            return mIcon11
        }

        override fun onPostExecute(result: Bitmap) {
            bmImage.setImageBitmap(result)
        }
    }
}