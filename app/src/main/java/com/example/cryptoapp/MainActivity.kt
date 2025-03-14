package com.example.cryptoapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Header
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cryptoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvAdapter: RvAdapter
    private lateinit var data: ArrayList<Model>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        supportActionBar?.hide()
        data = ArrayList<Model>()
        apiData
        rvAdapter = RvAdapter(this, data)
        binding.Rv.layoutManager = LinearLayoutManager(this)
        binding.Rv.adapter = rvAdapter

        }
         var apiData: Unit = Unit
             get() {
                val url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/trending/latest"
                 val queue=Volley.newRequestQueue(this)
                 val jsonObjectRequest:JsonObjectRequest=
                     @SuppressLint("NotifyDataSetChanged")
                     object: JsonObjectRequest(Method.GET,url,null,Response.Listener { response  ->
                         try {
                             val dataArray=response.getJSONArray("data")
                             for (i in 0 until dataArray.length())
                             {
                                 val dataObject=dataArray.toJSONObject(i)
                                 val symbol=dataObject.getString("symbol")
                                 val name = dataObject.getString("name")
                                 val USD=dataObject.getJSONObject("USD")
                                 val price=USD.getDouble("price")
                                 data.add(Model(name,symbol,price.toString()))

                             }

                                rvAdapter.notifyDataSetChanged()
                         }catch (e:Exception){
                             Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()

                         }

                     } , Response.ErrorListener {
                         Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
                     })
                     {

                         override fun getHeaders(): Map<String, String> {

                             val header=HashMap<String, String>()
                             header["X-CMC_PRO_API_KEY"]= "041e32b6-fc40-41aa-8fe3-b19cd06f48a1"
                             return header
                         }
                     }
                           queue.add(jsonObjectRequest)


            }


        }

