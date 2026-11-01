package com.example.myapplication

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val category: String,
    val description: String,
    val fullDescription: String,
    val rating: Double,
    val reviewsCount: Int,
    val imageUrl: String,
    val imageEmoji: String = "🎮",
    val specifications: Map<String, String> = emptyMap()
)

val sampleProducts = listOf(
    Product(
        id = 1,
        title = "Стики с эффектом холла",
        price = 7990.0,
        category = "Запчасти",
        description = "Стики с эффектом холла дают бесконечную работу без дрифта",
        fullDescription = "Модули стиков с датчиками Холла полностью устраняют проблему дрифта стиков. В них используются магнитные сенсоры вместо механических потенциометров, что обеспечивает непревзойденную точность и долговечность.",
        rating = 4.9,
        reviewsCount = 42,
        imageUrl = "https://images.unsplash.com/photo-1600080972464-8e5f35f63d08?w=500&q=80",
        imageEmoji = "🕹️",
        specifications = mapOf(
            "Тип сенсора" to "Магнитные датчики Холла",
            "Совместимость" to "DualSense, DualShock 4, Xbox Controller",
            "Гарантия" to "24 месяца",
            "Ресурс" to "Свыше 10 млн нажатий"
        )
    ),
    Product(
        id = 2,
        title = "Беспроводной геймпад",
        price = 5490.0,
        category = "Геймпады",
        description = "Эргономичный контроллер с виброотдачей и аккумулятором на 12 часов",
        fullDescription = "Продвинутый беспроводной геймпад с импульсными триггерами, встроенным аккумулятором увеличенной емкости и эргономичным прорезиненным хватом для длительных игровых сессий.",
        rating = 4.8,
        reviewsCount = 128,
        imageUrl = "https://images.unsplash.com/photo-1592840496694-26d035b52b48?w=500&q=80",
        imageEmoji = "🎮",
        specifications = mapOf(
            "Подключение" to "Bluetooth 5.2 / USB Type-C",
            "Время работы" to "до 12 часов",
            "Совместимость" to "PC, Android, iOS, Nintendo Switch",
            "Вес" to "245 г"
        )
    ),
    Product(
        id = 3,
        title = "Игровая гарнитура 7.1",
        price = 4290.0,
        category = "Аудио",
        description = "Объемный звук, съёмный микрофон и мягкие амбушюры",
        fullDescription = "Полноразмерные игровые наушники с виртуальным объемным звуком 7.1. Оснащены 50-мм динамиками с неодимовыми магнитами, съёмным микрофоном с шумоподавлением и дышащими тканевыми амбушюрами.",
        rating = 4.7,
        reviewsCount = 89,
        imageUrl = "https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=500&q=80",
        imageEmoji = "🎧",
        specifications = mapOf(
            "Динамики" to "50 мм неодимовые",
            "Диапазон частот" to "20 Гц - 20 000 Гц",
            "Разъем" to "3.5 мм / USB звуковая карта",
            "Микрофон" to "Двунаправленный, съёмный"
        )
    ),
    Product(
        id = 4,
        title = "Зарядная станция Dual",
        price = 2990.0,
        category = "Зарядные устройства",
        description = "Быстрая одновременная зарядка двух контроллеров со светодиодом",
        fullDescription = "Компактная подставка для одновременной быстрой зарядки двух геймпадов. Светодиодные индикаторы наглядно показывают статус заряда (красный — идет зарядка, синий — заряжено).",
        rating = 4.9,
        reviewsCount = 64,
        imageUrl = "https://images.unsplash.com/photo-1612287230202-1ff1d85d1bdf?w=500&q=80",
        imageEmoji = "🔋",
        specifications = mapOf(
            "Питание" to "USB Type-C (5V/2A)",
            "Время полной зарядки" to "2.5 часа",
            "Защита" to "От перегрева и короткого замыкания"
        )
    ),
    Product(
        id = 5,
        title = "Кабель USB-C Type-C 3m",
        price = 1290.0,
        category = "Кабели",
        description = "Прочный нейлоновый кабель высокой длины для игры во время зарядки",
        fullDescription = "Сверхпрочный кабель в нейлоновой оплетке длиною 3 метра с поддержкой быстрой зарядки до 60 Вт и высокоскоростной передачи данных. Позволяет комфортно играть на диване во время зарядки.",
        rating = 4.6,
        reviewsCount = 53,
        imageUrl = "",
        imageEmoji = "🔌",
        specifications = mapOf(
            "Длина" to "3 метра",
            "Материал" to "Нейлоновая оплетка + алюминиевый коннектор",
            "Мощность" to "до 60W (20V/3A)"
        )
    ),
    Product(
        id = 6,
        title = "Карта памяти 256 ГБ",
        price = 3190.0,
        category = "Накопители",
        description = "Высокоскоростная карта памяти MicroSDXC для быстрых загрузок",
        fullDescription = "Высокоскоростная карта MicroSDXC UHS-I U3 A2 с высокой скоростью чтения до 160 МБ/с. Идеально подходит для консолей Steam Deck, Nintendo Switch и современных смартфонов.",
        rating = 4.8,
        reviewsCount = 210,
        imageUrl = "",
        imageEmoji = "💾",
        specifications = mapOf(
            "Объем" to "256 ГБ",
            "Класс скорости" to "Class 10, UHS-I U3, V30, A2",
            "Скорость чтения" to "до 160 МБ/с"
        )
    ),
    Product(
        id = 7,
        title = "Механическая клавиатура",
        price = 6890.0,
        category = "Периферия",
        description = "RGB подсветка, переключатели Red switch и компактный корпус",
        fullDescription = "Компактная механика 75% формата с смазанными линейными переключателями Red Switch, настраиваемой миллионом цветов RGB-подсветкой и съемным Type-C кабелем.",
        rating = 4.9,
        reviewsCount = 95,
        imageUrl = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500&q=80",
        imageEmoji = "⌨️",
        specifications = mapOf(
            "Формат" to "75% (82 клавиши)",
            "Переключатели" to "Linear Red Switches (смазаны)",
            "Подсветка" to "RGB 16.8 млн цветов"
        )
    ),
    Product(
        id = 8,
        title = "Игровая мышь 16000 DPI",
        price = 3990.0,
        category = "Периферия",
        description = "Легкий корпус, точный оптический сенсор и программируемые кнопки",
        fullDescription = "Ультралегкая эргономичная мышь весом всего 59 г с топовым оптическим сенсором PixArt, тефлоновыми ножками PTFE и 6 программируемыми кнопками.",
        rating = 4.7,
        reviewsCount = 76,
        imageUrl = "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=500&q=80",
        imageEmoji = "🖱️",
        specifications = mapOf(
            "Сенсор" to "PixArt 3389 (16 000 DPI)",
            "Вес" to "59 грамм",
            "Переключатели" to "Omron (50 млн кликов)"
        )
    )
)
