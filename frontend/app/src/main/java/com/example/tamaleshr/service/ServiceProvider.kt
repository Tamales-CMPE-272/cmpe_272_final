package com.example.tamaleshr.service

import retrofit2.Retrofit

interface ServiceProvider<Service> {
    val retrofit: Retrofit
    fun service(): Service
}