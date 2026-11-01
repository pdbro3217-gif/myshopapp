package com.example.myapplication

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import kotlin.math.sin

data class ActiveFlyingItem(
    val id: Long,
    val imageUrl: String,
    val emoji: String,
    val startPos: Offset,
    val animatable: Animatable<Float, AnimationVector1D>
)

@Composable
fun rememberImageRequest(url: String): ImageRequest? {
    val context = LocalContext.current
    return remember(url) {
        if (url.isNotBlank()) {
            ImageRequest.Builder(context)
                .data(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
                .addHeader("Referer", "https://www.mechta.kz/")
                .addHeader("Accept", "image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
                .crossfade(true)
                .build()
        } else null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    viewModel: ShopViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedProductForDetail by viewModel.selectedProductForDetail.collectAsState()

    val cartItems = remember { mutableStateMapOf<Product, Int>() }
    var showCartSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var cartButtonCoordinates by remember { mutableStateOf<Offset?>(null) }
    val cartScale = remember { Animatable(1f) }
    val flyingItems = remember { mutableStateListOf<ActiveFlyingItem>() }

    val totalCount = cartItems.values.sum()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Магазин", fontWeight = FontWeight.Bold)
                    },
                    actions = {
                        Button(
                            onClick = { showCartSheet = true },
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .graphicsLayer {
                                    scaleX = cartScale.value
                                    scaleY = cartScale.value
                                }
                                .onGloballyPositioned { coordinates ->
                                    cartButtonCoordinates = coordinates.positionInRoot()
                                },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (totalCount > 0) "🛒 Корзина ($totalCount)" else "🛒 Корзина",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    placeholder = { Text("Поиск товаров...") },
                    leadingIcon = { Text("🔍", fontSize = 18.sp) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                Text("❌", fontSize = 14.sp)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )

                // Category Chips Filter
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = category == selectedCategory
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.onCategorySelected(category) },
                            label = { Text(text = category) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                // Main Content
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (filteredProducts.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "🔍", fontSize = 56.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Ничего не найдено",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Попробуйте изменить поиск или категорию",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(filteredProducts, key = { it.id }) { item ->
                            ProductCard(
                                product = item,
                                onProductClick = { product ->
                                    viewModel.onProductClicked(product)
                                },
                                onAddToCart = { product, startPos ->
                                    coroutineScope.launch {
                                        val anim = Animatable(0f)
                                        val flyingItem = ActiveFlyingItem(
                                            id = System.currentTimeMillis() + (0..1000).random(),
                                            imageUrl = product.imageUrl,
                                            emoji = product.imageEmoji,
                                            startPos = startPos,
                                            animatable = anim
                                        )
                                        flyingItems.add(flyingItem)

                                        anim.animateTo(
                                            targetValue = 1f,
                                            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                                        )

                                        cartItems[product] = (cartItems[product] ?: 0) + 1
                                        flyingItems.remove(flyingItem)

                                        launch {
                                            cartScale.animateTo(1.25f, animationSpec = tween(100))
                                            cartScale.animateTo(
                                                1.0f,
                                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Product Detail Sheet
            selectedProductForDetail?.let { product ->
                ProductDetailSheet(
                    product = product,
                    onDismiss = { viewModel.onDismissProductDetail() },
                    onAddToCart = { prod, startPos ->
                        coroutineScope.launch {
                            val anim = Animatable(0f)
                            val flyingItem = ActiveFlyingItem(
                                id = System.currentTimeMillis() + (0..1000).random(),
                                imageUrl = prod.imageUrl,
                                emoji = prod.imageEmoji,
                                startPos = startPos,
                                animatable = anim
                            )
                            flyingItems.add(flyingItem)

                            anim.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                            )

                            cartItems[prod] = (cartItems[prod] ?: 0) + 1
                            flyingItems.remove(flyingItem)

                            launch {
                                cartScale.animateTo(1.25f, animationSpec = tween(100))
                                cartScale.animateTo(
                                    1.0f,
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                                )
                            }
                        }
                    }
                )
            }

            // Cart Bottom Sheet
            if (showCartSheet) {
                CartBottomSheet(
                    cartItems = cartItems,
                    onDismiss = { showCartSheet = false },
                    onIncreaseQuantity = { product ->
                        cartItems[product] = (cartItems[product] ?: 0) + 1
                    },
                    onDecreaseQuantity = { product ->
                        val current = cartItems[product] ?: 0
                        if (current > 1) {
                            cartItems[product] = current - 1
                        } else {
                            cartItems.remove(product)
                        }
                    },
                    onRemoveItem = { product ->
                        cartItems.remove(product)
                    },
                    onClearCart = {
                        cartItems.clear()
                    },
                    onCheckout = {
                        cartItems.clear()
                        showCartSheet = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Заказ успешно оформлен! 🎉")
                        }
                    }
                )
            }
        }

        // Overlay for flying item animations
        val targetCartOffset = cartButtonCoordinates ?: Offset(800f, 80f)
        flyingItems.forEach { flyingItem ->
            val progress = flyingItem.animatable.value
            val startX = flyingItem.startPos.x + 30f
            val startY = flyingItem.startPos.y + 30f
            val endX = targetCartOffset.x + 50f
            val endY = targetCartOffset.y + 25f

            val currentX = startX + (endX - startX) * progress
            val arcHeight = 200f
            val arc = (sin(progress * Math.PI.toFloat()) * arcHeight).toFloat()
            val currentY = startY + (endY - startY) * progress - arc

            val scale = 1f - (progress * 0.65f)
            val alpha = if (progress > 0.85f) (1f - progress) / 0.15f else 1f

            val imageReq = rememberImageRequest(flyingItem.imageUrl)

            Box(
                modifier = Modifier
                    .offset { IntOffset(currentX.toInt(), currentY.toInt()) }
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha.coerceIn(0f, 1f)
                    }
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (imageReq != null) {
                    SubcomposeAsyncImage(
                        model = imageReq,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        error = {
                            Text(text = flyingItem.emoji, fontSize = 24.sp)
                        }
                    )
                } else {
                    Text(text = flyingItem.emoji, fontSize = 24.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartBottomSheet(
    cartItems: Map<Product, Int>,
    onDismiss: () -> Unit,
    onIncreaseQuantity: (Product) -> Unit,
    onDecreaseQuantity: (Product) -> Unit,
    onRemoveItem: (Product) -> Unit,
    onClearCart: () -> Unit,
    onCheckout: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val totalPrice = cartItems.entries.sumOf { it.key.price * it.value }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Корзина",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (cartItems.isNotEmpty()) {
                    OutlinedButton(
                        onClick = onClearCart,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Очистить")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (cartItems.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🛒", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Корзина пуста",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Добавьте товары из каталога",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartItems.entries.toList(), key = { it.key.id }) { entry ->
                        val product = entry.key
                        val quantity = entry.value
                        val imageReq = rememberImageRequest(product.imageUrl)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.secondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (imageReq != null) {
                                        SubcomposeAsyncImage(
                                            model = imageReq,
                                            contentDescription = product.title,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize(),
                                            error = {
                                                Text(text = product.imageEmoji, fontSize = 24.sp)
                                            }
                                        )
                                    } else {
                                        Text(text = product.imageEmoji, fontSize = 24.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = product.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${product.price.toInt()} ₽",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { onDecreaseQuantity(product) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Text(
                                        text = "$quantity",
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        fontWeight = FontWeight.Bold
                                    )

                                    IconButton(
                                        onClick = { onIncreaseQuantity(product) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    }

                                    IconButton(
                                        onClick = { onRemoveItem(product) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text("❌", fontSize = 14.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Итого:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${totalPrice.toInt()} ₽",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onCheckout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Оформить заказ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
