package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShopViewModel(
    private val repository: ProductRepository = LocalProductRepository()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Все")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(listOf("Все"))
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedProductForDetail = MutableStateFlow<Product?>(null)
    val selectedProductForDetail: StateFlow<Product?> = _selectedProductForDetail.asStateFlow()

    val filteredProducts: StateFlow<List<Product>> = combine(
        _allProducts,
        _searchQuery,
        _selectedCategory
    ) { products, query, category ->
        products.filter { product ->
            val matchesSearch = query.isBlank() ||
                    product.title.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true)

            val matchesCategory = category == "Все" || product.category.equals(category, ignoreCase = true)

            matchesSearch && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _categories.value = repository.getCategories()
            repository.getProducts().collect { products ->
                _allProducts.value = products
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun onProductClicked(product: Product) {
        _selectedProductForDetail.value = product
    }

    fun onDismissProductDetail() {
        _selectedProductForDetail.value = null
    }
}
