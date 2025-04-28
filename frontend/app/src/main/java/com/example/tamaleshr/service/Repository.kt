package com.example.tamaleshr.service

interface Repository<Service> {
    val provider: ServiceProvider<Service>
}