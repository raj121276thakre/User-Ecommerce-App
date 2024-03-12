package com.app.userecommerce.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.userecommerce.R
import com.app.userecommerce.databinding.ActivityAddressBinding

class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
    }
}