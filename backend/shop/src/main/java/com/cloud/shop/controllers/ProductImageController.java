package com.cloud.shop.controllers;

import com.cloud.shop.entities.Product;
import com.cloud.shop.entities.ProductImage;
import com.cloud.shop.repositories.ProductImageRepository;
import com.cloud.shop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
