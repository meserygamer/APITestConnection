package com.example.apitest

import APIConnect
import News
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.RecyclerView1ItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    }

    fun SetAdapter(collection: MutableList<News>)
    {
        binding.MainRecycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.MainRecycleView.adapter = AdapterRecyclerView(collection)
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
    }
}

public class AdapterRecyclerViewViewHolder(var binding: RecyclerView1ItemBinding) : RecyclerView.ViewHolder(binding.root)