# 🎯 ReactMoment

<div align="center">

![VibeQuest Logo](https://img.shields.io/badge/ReactMoment-Daily%20Challenges-orange?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNCAyNCI+PHBhdGggZD0iTTEyIDJDNi40OCAyIDIgNi40OCAyIDEyczQuNDggMTAgMTAgMTAgMTAtNC40OCAxMC0xMFMxNy41MiAyIDEyIDJ6bTAgMThjLTQuNDEgMC04LTMuNTktOC04czMuNTktOCA4LTggOCAzLjU5IDggOC0zLjU5IDgtOCA4eiIgZmlsbD0id2hpdGUiLz48cGF0aCBkPSJNMTIgNmMtMy4zMSAwLTYgMi42OS02IDZzMi42OSA2IDYgNiA2LTIuNjkgNi02LTIuNjktNi02LTZ6bTAgMTBjLTIuMjEgMC00LTEuNzktNC00czEuNzktNCA0LTQgNCAxLjc5IDQgNC0xLjc5IDQtNCA0eiIgZmlsbD0id2hpdGUiLz48L3N2Zz4=)
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)](https://developer.android.com/studio)
[![Material Design](https://img.shields.io/badge/Material_Design-757575?style=for-the-badge&logo=material-design&logoColor=white)](https://material.io/design)

**TH Köln | Medieninformatik | MOCO Module**  
**Verantwortlicher Dozent: Prof. Dr. Matthias Böhmer**

</div>

## 📱 Über VibeQuest

VibeQuest ist ein digitaler Treffpunkt für Freundesgruppen, die täglich neue Mini-Challenges erhalten und diese innerhalb von 24 Stunden lösen. Dabei steht nicht das Reden, sondern das Reagieren im Mittelpunkt: Nutzer*innen teilen Erlebnisse und Gefühle rein visuell und emotional – über Emojis, Sounds und Reactions. Keine Chats, keine Kommentare, keine klassischen Nachrichten. Die App schafft eine lebendige, spontane Atmosphäre, die Nähe trotz Distanz ermöglicht.

## ✨ Kern-Features

### 🎯 Tägliche Challenges
- Jeden Tag automatisch eine neue Challenge für Freundesgruppen
- Abwechslungsreiche Aufgaben wie:
  - "Zeig dein Frühstück in 5 Sekunden."
  - "Mach ein verrücktes Selfie an einem ungewöhnlichen Ort."
  - "Erfinde einen eigenen Victory-Dance."

### ⏱️ Zeitlimit
- 24 Stunden Zeit zum Erfüllen und Hochladen der Challenge
- Flexibilität beim Medienformat: Foto, kurzes Video, Tonaufnahme – je nach Aufgabe

### 🔄 Reaktive Bewertung
- Keine schriftlichen Kommentare oder Bewertungen
- Freunde reagieren ausschließlich mit:
  - 😀 Emojis
  - 🌟 Stickern
  - 🔊 Sounds (z.B. Lachen, Applaus, Herzklopfen)
- Schnelles, leichtes Feedback ohne klassische Textkommentare

### 🏆 Wettbewerb & Belohnungen
- Die Person mit den meisten oder speziellsten Reaktionen gewinnt den Tag
- Punktesystem für gesammelte Reaktionen
- Punkte können gegen digitale Goodies eingetauscht werden:
  - Besondere Sticker
  - Einzigartige Reaktions-Sounds
  - Neue Challenge-Packs

## 🛠️ Technologie-Stack

- **Programmiersprache:** Kotlin
- **UI-Framework:** Jetpack Compose
- **IDE:** Android Studio
- **Design-System:** Material Design 3
- **Architektur:** MVVM (Model-View-ViewModel)
- **Asynchrone Operationen:** Kotlin Coroutines & Flow
- **Dependency Injection:** Hilt
- **Bildverarbeitung:** CameraX & MediaPipe
- **Datenspeicherung:** Room Database & DataStore

## 📋 Anforderungen

- Android 8.0 (API Level 26) oder höher
- Kamera- und Mikrofonzugriff
- Internetzugang

## 🚀 Installation für Entwickler

1. Clone das Repository:
   ```bash
   git clone https://github.com/yourusername/vibequest.git
   ```

2. Öffne das Projekt in Android Studio

3. Sync Gradle Files

4. Starte die App auf einem Emulator oder physischen Gerät

## 🧪 Testdaten & Debugging

Für Testzwecke gibt es einen speziellen Debug-Modus, der aktiviert werden kann:
```kotlin
// In DebugConfig.kt
val ENABLE_DEBUG_MODE = true
```

## 📝 Projektdokumentation

Die vollständige Projektdokumentation mit UML-Diagrammen, Wireframes und User Journey Maps ist im `docs/` Verzeichnis zu finden.

# 🔮 Roadmap

- [ ] Integration des Radial-Reaktionsmenüs für Beiträge und Challenges
- [ ] Implementierung der Datenbank für Reaktionsspeicherung
- [ ] Erweiterung der Challenge-Karten mit dynamischen Inhalten
- [x] Erweiterung mit Gruppen-Challenges (mehrere Teilnehmer pro Challenge)
- [ ] Freundschaftssystem mit Benachrichtigungen für neue Challenges
- [ ] Verbessertes UI/UX für die Reaktionen-Ansicht
- [ ] Offline-Modus mit lokaler Datenspeicherung
- [ ] Persönliche Statistiken über Challenge-Teilnahme und Reaktionen
- [ ] Community-erstellte Challenge-Vorlagen
- [ ] Integration mit Social Media zum Teilen von Challenge-Ergebnissen

## 👥 Team

- [Imran Moujtahid] - Konzept & Entwicklung
- [Daniel Budashev] - UI/UX Design
- [Teammitglied 3] - Backend & Datenverwaltung

## 🎓 Akademischer Kontext

Dieses Projekt wurde im Rahmen des MOCO-Moduls (Mobile Computing) im Studiengang Medieninformatik an der TH Köln entwickelt.

## 📄 Lizenz

Dieses Projekt ist lizenziert unter der MIT Lizenz - siehe die [LICENSE](LICENSE) Datei für Details.

---

<div align="center">
<p>Entwickelt mit ❤️ an der TH Köln</p>

![TH Köln Logo](https://img.shields.io/badge/TH_Köln-Medieninformatik-red?style=for-the-badge)
</div>
