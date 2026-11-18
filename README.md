# Shop App — Android E-Commerce Application 🛒🎮

Современное мобильное приложение интернет-магазина игровых аксессуаров и комплектующих на **Jetpack Compose** и **Material 3**.

---

## 📱 Возможности (Features)

- 🛍️ **Каталог товаров**: Удобная сетка товаров с фотографиями, категориями, ценами и рейтингами ⭐.
- 🔍 **Поиск и фильтрация**: Живой поиск по названию и быстрые фильтры-чипсы по категориям (*Запчасти, Геймпады, Аудио, Кабели, Накопители, Периферия*).
- 👁️ **Детальный просмотр товара**: Модальное окно с увеличенной фотографией, подробным описанием и таблицей характеристик.
- 🛒 **Интерактивная корзина**:
  - Анимация полёта миниатюры товара в корзину.
  - Счётчик товаров в верхней панели.
  - Управление количеством (`+` / `-`), удаление отдельных позиций, очистка корзины и подсчёт итоговой суммы.
- 🇰🇿 **Форматирование цен**: Отображение цен в тенге (`₸`) с разделением тысяч.
- 🎨 **Темизация**: Стильное оформление Material 3 с поддержкой тёмной и светлой темы.

---

## 🛠️ Технологический стек (Tech Stack)

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Язык**: [Kotlin](https://kotlinlang.org/)
- **Архитектура**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Асинхронность и реактивность**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [StateFlow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/)
- **Загрузка изображений**: [Coil Compose](https://coil-kt.github.io/coil/compose/)
- **Сборка**: Gradle (Kotlin DSL, Version Catalogs `libs.versions.toml`)

---

## 🚀 Инструкция по запуску (Getting Started)

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/ВАШ_ЛОГИН/shop-app.git
   ```
2. Откройте проект в **Android Studio**.
3. Дождитесь окончания **Gradle Sync**.
4. Запустите приложение на эмуляторе или реальном устройстве (требуется Android 10+ / `minSdk 29`).
