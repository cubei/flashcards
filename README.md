# flashcards
Simple flash card android app with csv files as input.

The app is meant for stuff like learning vocabulary of different languages.

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png"
     alt="Get it on Google Play"
     height="80">](https://play.google.com/store/apps/details?id=com.quchen.flashcard)
[<img src="https://f-droid.org/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/com.quchen.flashcard/)

[Or get the latest APK directly from the Github repo](app/release/app-release.apk)


### CSV format
* CSV file with `;` (semicolon) as separator.
* First line is the header (e.g. `English;Chinese`)
* Other lines are used as content (e.g. `Thanks;Xie Xie`)
* Content lines without semicolon are ignored (Can be used for comments)
* At the moment only the first columns are used and later columns will be ignored. (e.g. `Thanks;Xie Xie;谢谢`)
