package com.example.zombiestepv01.Activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.zombiestepv01.R
import kotlinx.android.synthetic.main.activity_store.*

class StoreFragment: AppCompatActivity(){ // Fragment() {
   /* override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_store,container,false)
    }*/
override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_store)

   }

    override fun onBackPressed() {

    }

}