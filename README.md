# Fitness Diary
Fitness Diary是一個減重紀錄APP
透過**飲食紀錄**、**運動紀錄(Strava)**、**體態紀錄**以幫助使用者了解自身身體變化
* 飲食紀錄
  俗話說七分吃三分動，將飲食拍照記錄下來，定期回顧飲食是否有問題
* 運動紀錄(Strava)
  減重就是管住嘴，邁開腿，APP可以連結Strava取得運動紀錄
* 體態紀錄
  紀錄體重、體脂率變化，亦可拍照記錄身體變化

## Tech Stack & Open-source libraries
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - declarative UI Toolkit
- [Kotlin](https://kotlinlang.org/)
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [Kotlin Flow](https://kotlinlang.org/docs/flow.html)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)   - DI
- [MVVM Architecture](https://developer.android.com/topic/architecture#recommended-app-arch)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - state holder
- [Room](https://developer.android.com/training/data-storage/room)
- [ktor](https://ktor.io/docs/getting-started-ktor-client-multiplatform-mobile.html) - implementing OAuth
- [Serialization](https://kotlinlang.org/docs/serialization.html) - parse JSON
- [Timber](https://github.com/JakeWharton/timber) - logger
- [Material Design 3](https://m3.material.io/)
- [kizitonwose/Calendar](https://github.com/kizitonwose/Calendar) Compose-based Calendar
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) - View-based Chart
- [coil](https://coil-kt.github.io/coil/) - image loading library

## Dependency Management
- [Gradle version catalogs](https://developer.android.com/build/migrate-to-catalogs)

## Screenshot
![home](/images/home.png)
![drawer](/images/drawer.png)
![sport](/images/sport_record.png)