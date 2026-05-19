# iTravel Istanbul App

<img width="2000" height="1200" alt="iTravel Istanbul" src="https://github.com/user-attachments/assets/b28a5466-b78c-4526-9f5f-0a5f51239c3c" />


iTravel, kullanıcıların İstanbul’daki tarihi, kültürel, doğal ve popüler noktaları keşfetmesini sağlayan modern bir mobil gezi uygulamasıdır.  
Uygulama; harita tabanlı keşif sistemi, rota oluşturma, kullanıcı yorumları, popüler mekan önerileri ve yönetici paneli gibi birçok özelliği bir arada sunmaktadır.

---

# Proje Amacı

Bu projenin amacı, İstanbul’u ziyaret eden veya şehirde yaşayan kullanıcıların:
- yeni yerler keşfetmesini,
- mekanlar hakkında detaylı bilgi almasını,
- harita üzerinden konumları görüntülemesini,
- rota oluşturmasını,
- deneyimlerini yorumlayarak paylaşmasını
  sağlayan kullanıcı dostu bir şehir keşif platformu geliştirmektir.

---

# Uygulama Özellikleri

## Kullanıcı Sistemi
- Kullanıcı kayıt olma ve giriş sistemi
- Firebase Authentication entegrasyonu
- Profil sayfası oluşturma
- Profil fotoğrafı güncelleme
- Kullanıcı yorumlarını görüntüleme

## İstanbul Keşif Sistemi
Kullanıcılar:
- tarihi yerleri,
- müzeleri,
- doğal alanları,
- ücretsiz gezilebilecek mekanları,
- popüler kafeleri ve restoranları
  kategori bazlı şekilde keşfedebilir.

# Akıllı Arama Sistemi
Kategori ekranlarında kullanıcı deneyimini geliştirmek amacıyla arama sistemi eklenmiştir.

Kullanıcılar:
- mekan adına göre arama yapabilir,
- kategoriler içerisindeki yerleri hızlıca filtreleyebilir,
- yazdıkça anlık sonuç görüntüleyebilir.

Arama sonucu bulunamadığında kullanıcıya bilgilendirici mesaj gösterilmektedir.

## Harita ve Konum Servisleri
- Google Maps API entegrasyonu
- Mekanların harita üzerinde görüntülenmesi
- Kategoriye göre marker sistemi
- Kullanıcının konumuna göre yakın mekan önerileri
- Konum bazlı keşif ekranı

## Rota Oluşturma
Kullanıcı mevcut konumundan seçilen mekana:
- gerçek yol rotası oluşturabilir,
- mesafe bilgisi görüntüleyebilir,
- tahmini ulaşım süresini görebilir,
- harita üzerinden yol güzergahını takip edebilir.

Rota sistemi gerçek yol verileri kullanılarak geliştirilmiştir.

## Mekan Detay Sistemi
Her mekan için:
- görsel,
- açıklama,
- kategori,
- puan,
- konum bilgileri,
- benzer önerilen yerler
  sunulmaktadır.

## Yorum ve Puanlama Sistemi
Firebase Realtime Database kullanılarak:
- yorum ekleme,
- yorum güncelleme,
- yorum silme,
- yıldız puanlama
  işlemleri gerçekleştirilmiştir.

## Yönetici Paneli
Yönetici sistemi sayesinde:
- yeni mekan ekleme,
- mekan düzenleme,
- mekan silme,
- kategori filtreleme
  işlemleri yapılabilmektedir.

Bu bölümde CRUD (Create, Read, Update, Delete) işlemleri aktif olarak kullanılmaktadır.

---

# Kullanılan Teknolojiler

- Java
- Android Studio
- Firebase Authentication
- Firebase Realtime Database
- Firebase Storage
- Google Maps API
- Material Design Components
- RecyclerView
- CardView
- ConstraintLayout
- OSRM Routing API

---

# Firebase Kullanımı

Projede Firebase aktif olarak kullanılmıştır:

- Firebase Authentication → kullanıcı giriş/kayıt sistemi
- Firebase Realtime Database → mekanlar, yorumlar ve kullanıcı verileri
- Firebase Storage → profil ve mekan görselleri

Realtime Database üzerinde:
- veri ekleme,
- veri çekme,
- veri güncelleme,
- veri silme
  işlemleri gerçekleştirilmiştir.

---

# Uygulama Ekranları

## Giriş ve Karşılama Ekranları
- Modern onboarding ekranı
- Kullanıcı ve yönetici giriş sistemi

## Keşfet Sayfası
- İstanbul tanıtımı
- Kategori bazlı keşif sistemi
- Mekan önerileri

## Harita Sistemi
- Mekan markerları
- Yakındaki yerleri keşfetme
- Konum bazlı görüntüleme

## Mekan Detay Sayfası
- Detaylı açıklamalar
- Yorum sistemi
- Puanlama sistemi
- Benzer mekan önerileri
- Rota oluşturma

## Yönetici Paneli
- Mekan ekleme
- Mekan düzenleme
- Mekan silme
- Firebase veri yönetimi

---

# APK

Uygulamayı denemek isteyen kullanıcılar için:

🔗 [APK İndir](https://github.com/sumeyyegull/iTravel_Project/releases/download/v1.0/app-debug.apk)

Not:
- Android cihazlarda bilinmeyen kaynaklara izin verilmesi gerekmektedir.
- Android 8.0 (API 26) ve üzeri cihazlarda test edilmiştir.

---

# Geliştiriciler

- Sümeyye Gül
- Fatma Yaşar
- Ceren Özkan

Bilgisayar Mühendisliği Öğrencileri

Android | Java | Firebase | Google Maps
