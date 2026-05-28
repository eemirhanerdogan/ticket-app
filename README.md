# TicketApp

TicketApp, Kotlin ve Jetpack Compose ile geliştirilen, kullanıcıların etkinlikleri görüntüleyip bilet satın alabildiği, ödeme sonrası biletlerini QR kod ile görüntüleyebildiği Android uygulamasıdır.

## Kullanılan Teknolojiler

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **Retrofit & OkHttp** (Ağ işlemleri)
- **Kotlinx Serialization** (JSON ayrıştırma)
- **Koin** (Dependency Injection)
- **Coroutines & StateFlow** (Asenkron işlemler ve state yönetimi)
- **Navigation Compose** (Ekranlar arası geçiş)
- **QR Code Rendering Library** (QR kod üretimi)
- **Çoklu Modül Mimarisi**: app, core, data

## Mimari Yapı

Projede kurumsal mimari standartlarına uygun olarak üç temel modül kullanılmıştır:

- **app**: UI bileşenleri, Composable ekranlar, ViewModel'ler, Navigasyon ve Dependency Injection (DI) tanımlamalarını içerir.
- **core**: Domain modelleri, Repository arayüzleri ve projenin her yerinde kullanılan ortak util sınıflarını (DateFormatter, ErrorMessages vb.) barındırır.
- **data**: DTO'lar, API arayüzleri, Mapper'lar, Repository implementasyonları ve token yönetimi (TokenStore, Authenticator) bu katmanda yer alır.

### Veri Akışı
`Screen -> ViewModel -> Repository -> API`

*Not: Composable fonksiyonlar içerisinde doğrudan API veya Repository çağrısı yapılmamış, tüm veri akışı ViewModel'ler üzerinden yönetilmiştir.*

## Tamamlanan Ana Özellikler

- **Kullanıcı İşlemleri**: Giriş yapma (Login), kayıt olma (Register), güvenli çıkış yapma (Logout).
- **Güvenlik**: Token refresh mekanizması ve 401 Unauthorized durum yönetimi.
- **Etkinlik Yönetimi**: Yaklaşan etkinliklerin listelenmesi ve detay ekranı.
- **Satın Alma Akışı**: Bilet türü/adet seçimi, satın alma oluşturma, ödeme onayı ve işlemin tamamlanması.
- **Biletlerim**: Satın alınan biletlerin listelenmesi ve detaylı QR kod ekranı.
- **QR Kod**: Bilet bilgilerinin gerçek zamanlı QR görseline dönüştürülmesi.
- **UX Geliştirmeleri**: 
    - QR ekranında otomatik maksimum parlaklık seviyesi.
    - ISO formatlı tarihlerin kullanıcı dostu formata dönüştürülmesi.
    - Hata (Error), yüklenme (Loading), boş (Empty) ve içerik (Content) state'lerinin yönetimi.
- **Pull-to-refresh**: Bilet listesi için yenileme desteği.

## API Akışı

Uygulama aşağıdaki API uç noktalarını kullanarak uçtan uca akışı sağlar:

- `GET /events`: Etkinlik listesi.
- `GET /events/{id}`: Etkinlik detayları ve bilet türleri.
- `POST /purchases`: Satın alma isteği oluşturma.
- `POST /purchases/{id}/pay`: Ödeme işlemini tamamlama.
- `GET /me/tickets`: Kullanıcıya ait bilet listesi.
- `GET /me/tickets/{id}`: Tekil bilet detayı ve QR payload.
- `GET /me/purchases` (Bonus): Satın alma geçmişi.

## Bonus Özellik: Satın Alımlarım

Uygulamaya eklenen **"Satın Alımlarım"** ekranı sayesinde:
- Kullanıcılar geçmişteki tüm satın alma işlemlerini (ID, durum, tutar) görebilir.
- `PENDING` (Beklemede) durumundaki satın almalar için "Ödemeye devam et" butonu ile ödeme işlemi sonradan tamamlanabilir.
- Ödeme başarılı olduğunda kullanıcı otomatik olarak güncel biletlerini görmek üzere "Biletlerim" ekranına yönlendirilir.

## Test Akışı

Uygulamayı test etmek için aşağıdaki adımları izleyebilirsiniz:

1. Uygulamayı başlatın ve giriş yapın (veya yeni hesap oluşturun).
2. Ana sayfadaki etkinliklerden birine tıklayın.
3. Etkinlik detay ekranında bilet türlerini inceleyin, adet seçin ve **Satın Al** butonuna basın.
4. Açılan diyalogda ödemeyi onaylayın.
5. Başarılı ödeme sonrası yönlendirildiğiniz **Biletlerim** ekranında yeni biletinizi görün.
6. Bilete tıklayarak detaylı bilet bilgilerini ve **QR kodunu** görüntüleyin.
7. Ana sayfadaki logout ikonu ile oturumu güvenli şekilde kapatın.

## Build

Projeyi temizleyip derlemek için aşağıdaki komutu kullanabilirsiniz:

```powershell
.\gradlew clean build
```
