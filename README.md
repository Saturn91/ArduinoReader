# Anwendungsdokumentation zum Programm Calbact-Peltier-Test V1.0

Dieses Programm wurde im Rahmen eines Praktikums entwickelt. Sinn und Zweck ist dabei die grafische Auswertung und Aufbereitung von Messdaten eines Arduinos an einem Rechner. Dabei fungiert der Arduino als I/O-Schnittstelle (vgl. Bild). Insbesondere zeitlich veränderliche Signale die in irgendeiner Form am Arduino gemessen werden bieten sich dafür an.
Protokoll und Anwendung auf Seite des Arduinos:

![Prinzip](https://abload.de/img/arduino_pc4jb30.png) 

Grundsätzlich kann das Programm alle Daten auslesen die mit folgendem Protokoll vom Arduino gesendet werden. Dazu muss das USB-Kabel des Arduinos mit dem Rechner verbunden sein. 
Im Arduino Programm:
 
Ohne Änderungen im Java-Programm können bis zu 16 Kanäle verwendet werden. Dies ist nur begrenzt weil nur 16 verschiedene Farben für die unterschiedlichen Graphen definiert wurden. Grundsätzlich wäre die Kanalanzahl erweiterbar. Bitte beachten, dass nur nummerische Werte (Int, float, long etc…) ausgelesen werden (PC-seitig wird in float umgewandelt).
Installation
Um das Programm zu verwenden müssen zwei Dinge vorab auf dem Rechner installiert sein.
1.	Java 1.8 -Runtime environment 
2.	Rtxt herunterladen: http://mfizz.com/oss/rxtx-for-java (Readme beschreibt installation)

# Anwendung des Programms
Damit das Programm sauber arbeitet (Voraussetzung ist Punkt Installation) muss wie hier beschrieben vorgegangen werden. 
1.	Arduino mit entsprechendem Programm starten.
2.	Computer mit USB mit dem Arduino verbinden (mit USB als einziger Spannungsquelle entfällt Schritt 1!)
3.	Doppelklick auf JAR 
4.	Fenster «Visual-Plot of Arduino Signals» öffnet sich
5.	Im schwarzen Ausgabefeld sollte eine Ausgabe wie in Abbildung 1 Beschrieben stehen (Wenn nicht siehe Punkt 6), wenn ja weiter zu 7
6.	Die Meldung “unexpected Error while opening Serial communication” (vgl. Abbildung 2) kann zwei Ursachen haben: 1. Programm läuft bereits in zweiter Anwendung; 2. Ein anderes Programm greift auf den Arduino zu (Arduino IDE, Mathlab…); 3. Der Arduino wurde nicht angeschlossen. Je nach Ursache alle anderen Programme die auf den Arduino zugreifen schliessen oder den Arduino mit USB verbinden. Dann dieses Programm schliessen und erneut starten. 
7.	Im oberen Drittel des Bildes Abbildung 1: GUI sollten nun Graphen sichtbar werden. Mit den Pfeiltasten links und unter dem Graphen kann die Ansicht verschoben werden. Die Textfelder dazwischen erlauben den Abstand zwischen den einzelnen Indikatoren zu konfigurieren (Eingabe z.B. 1.5). 
8.	Unten links ist der Einstellungsbereich. Mit der Taste «Save Data now» wird pro Kanal eine Textdatei erstellt, die die Daten in einer Form abspeichert welche z.B. mit einem Excel-Programm ausgewertet werden können (alle 60s werden diese Daten ebenfalls gespeichert).
9.	Im nächsten Textfeld muss ein numerischer INT-Wert eingegeben werden der bestimmt wie tief die LOG-Ausgabe rechts Nachrichten ausgeben soll (vgl. Tabelle).
-1	nichts
0	Nur Start und End – Meldungen des Programms
1	Fehler die Laufkritisch sind
2	Laufunkritische Fehler
3-5	Details des Programms
6	Direkt Plot von empfangenen Serial-Daten
7	Informationen zur Laufgeschwindigkeit des Programms

10.	Die unterste Linie erlaubt die Anzeige eines einzelnen Kanals, dabei werden die anderen Kanäle zwar noch gespeichert jedoch nicht im Graph geplotet. Zur Anzeige des beispielsweise ersten Kanals im Textfeld 0 eingeben – ENTER – Häckchen «Record all Data» entfernen und Button «start» betätigen. Durch resetten des Häckchens und erneutem betätigen von «start» werden wieder alle Kanäle angezeigt. 
11.	Wichtig: Durch betätigen von «start» wird der Graph bei der aktuellen Zeit auf 0 gesetzt und die Einstellungen zur Kanalauswahl übernommen. Die alten Daten werden jedoch nicht gelöscht, nur nicht mehr angezeigt (d.h. sie können immer noch aus den Textfiles gelesen werden).

![GUI-Darstellung](https://abload.de/img/arduino_pc_guikustw.png)

Abbildung 1: GUI

![Arduino nicht gefunden Fehler](https://abload.de/img/arduino_pc_error8fjju.png)
 
Abbildung 2: Fehlermeldung - Arduino nicht gefunden!


# Downloads
-	Github Repository: https://github.com/Saturn91/Arduino-reader
-	Releases (jar): https://github.com/Saturn91/Arduino-reader/releases

