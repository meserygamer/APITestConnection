package com.example.apitest

import APIConnect
import News
import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.RecyclerView1ItemBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null;

    public val binding : ActivityMainBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        APIConnect.GetRetrofit().GetAllNews().enqueue(object : Callback<MutableList<News>>{
            override fun onResponse(
                call: Call<MutableList<News>>,
                response: Response<MutableList<News>>
            )
            {
                Log.println(Log.INFO, "Quarry code", response.code().toString())
                SetAdapter(response.body()!!);
            }

            override fun onFailure(call: Call<MutableList<News>>, t: Throwable) {
                Log.println(Log.ERROR, "Пиздец", "Апи не работает!")
            }

        })

        binding.MainPhoto.setOnClickListener(object : View.OnClickListener
        {
            override fun onClick(v: View?) {
                if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(this@MainActivity,arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 101)
                }
                else
                {
                    val photoPickerIntent = Intent(Intent.ACTION_PICK);
                    photoPickerIntent.type = "image/*";
                    startActivityForResult(photoPickerIntent, 1);
                    //val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    //changeImage.launch(pickImg)
                }
            }
        })
    }

    fun SetAdapter(collection: MutableList<News>)
    {
        binding.MainRecycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.MainRecycleView.adapter = AdapterRecyclerView(collection)
    }

    /*
    private val changeImage = registerForActivityResult( ActivityResultContracts.StartActivityForResult() )
    {
        if (it.resultCode == PackageManager.PERMISSION_DENIED) {
            val data = it.data
            val imgUri = data?.data
            binding.MainPhoto.setImageURI(imgUri)
        }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data);

        var bitmap: Bitmap? = null

        when (requestCode) {
            1 -> if (resultCode === RESULT_OK) {
                val selectedImage = data?.data
                try {
                    bitmap = getBitmap(contentResolver, selectedImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                binding.MainPhoto.setImageBitmap(bitmap)
            }
        }

    }

}

public class AdapterRecyclerView(var NewsCollections : MutableList<News>) : RecyclerView.Adapter<AdapterRecyclerViewViewHolder>()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterRecyclerViewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerView1ItemBinding.inflate(inflater, parent, false)
        return AdapterRecyclerViewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return NewsCollections.size;
    }

    override fun onBindViewHolder(holder: AdapterRecyclerViewViewHolder, position: Int) {
        holder.binding.IdNews.text = NewsCollections[position].id.toString();
        holder.binding.NameNews.text = NewsCollections[position].name.toString();
        holder.binding.DescriptionNews.text = NewsCollections[position].description.toString();
        holder.binding.PriceNews.text = NewsCollections[position].price.toString();
        Picasso.get().load(NewsCollections[position].image).into(holder.binding.PictureNews);
    }
}

public class AdapterRecyclerViewViewHolder(var binding: RecyclerView1ItemBinding) : RecyclerView.ViewHolder(binding.root)