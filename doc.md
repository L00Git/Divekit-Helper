# Inhaltsverzeichnis

- [Funktionalität des Divekit-Helpers](#funktionalität-des-divekit-helpers)
- [Test Erklärung](#test-erklärung)
  - [Tabellentests](#tabellentest)
  - [Klassendiagrammtest](#klassendiagrammtest)
  - [Codetests](#codetest)
  - [TestLevel](#testlevel)
  - [Beispiele](#beispiele)
- [Divekit-Helper Aufbau](#divekit-helper-aufbau)
- [Integration](#integration)

---

# Funktionalität des Divekit-Helpers

Die Hauptfunktion des Helpers ist es das Erstellen von Tests durch eine DSL zu vereinfachen.
Dies geschieht durch das Übersetzen der DSL in interne Aufrufe der entsprechenden Tests.

Diese Tests generieren eine mit dem Divekit-Report-Visualizer kompatible Datei. 

Der Helper deckt drei Kategorien von Tests ab: Tabellen-Tests, Klassendiagramm-Tests und Code-Tests.

Außerdem wird das Erstellen mehrerer Test-Level ermöglicht, welches z.B. nach Problemen mit der Lösung einer Aufgabe erhöht werden kann.
Durch dieses Test-Level kann unterschiedliches Feedback für einen Fehler erstellt werden, welches erst mit einem erhöhten Test-Level ausgegeben wird.

---

# Test Erklärung

## Tabellentest

### Testart

Im Tabellentest werden Markdown-Dateien eingelesen. Diese müssen dem folgenden Format entsprechen:

```
| Spaltenname 1     | Spaltenname 2    |
|-------------------|------------------|
| Tabelleninhalt 1  | Tabelleninhalt A |
| Tabelleninhalt 2  | Tabelleninhalt B |
| Tabelleninhalt 3  | Tabelleninhalt C |
```

Man gibt dem Test zwei Dateien an, die Tabelle der Studenten und die Lösungs-Tabelle.

Dem Tabellentest wird in der ersten Methode eine Tabellen-Test-Klasse übergeben.
Diese Tabellen-Test-Klasse benötigt die Pfade zu der Tabelle der Studenten und der Lösungs-Tabelle.
Zusätzlich kann optional eine Liste von Spalten über ihre Nummer als case-sensitive definiert werden (beginnend bei Null).
Diese Liste von Spalten wird bei der Erstellung der Tabellen-Test-Klasse als Parameter angegeben.
Alle nicht definierten Spalten sind standardmäßig case-insensitive.

Im Tabellentest lassen sich Spalten über ihren Spaltennamen oder ihre Nummer (bei 0 beginnend) ansprechen.
In der Ausgabe lässt sich der durch den Test betroffene Tabelleninhalt durch `_ELEMENT_` ausgeben, sofern dies vom ausgeführten Test unterstützt wird.
Auf die gleiche Weise lassen sich die Spaltennamen mittels `_COLUMN1_` oder `_COLUMN2_` in den davon unterstützten Tests ausgeben.


Der Tabelleninhalt kann auch aus einer Liste bestehen. Diese wird mittels `,` getrennt, die Reihenfolge ist hierbei egal. 
So kann in der Lösung "Tabelleninhalt A" z.B. "Apfel, Birne" sein, dann wäre es auch richtig, wenn ein Student "Birne, Apfel" schreibt.

Wenn in der Lösungs-Tabelle in einer Spalte der Tabelleninhalt `...` steht, gibt dies an, dass die Spalte von Studenten mit jedem beliebigen Inhalt gefüllt werken darf.

Die Integrität von Zeilen lässt sich auf zwei Arten testen:

Zum einen der "rowColumnMismatch"-Test welcher eine gleiche Positionierung der Studenten-Tabelleninhalte und Lösungs-Tabelleninhalte prüft,
hierfür müsste "Tabelleninhalt 1" an Position (1,1), "Tabelleninhalt A" an Position (1,2) und "Tabelleninhalt 2" an Position (2,1) sein.

Zum anderen über einen "rowMismatch"-Test welcher nur den Zusammenhang von Zeilen prüft.
Hierbei müsste "Tabelleninhalt 1" und "Tabelleninhalt A" immer in derselben Zeile stehen und könnte somit z.B. mit "Tabelleninhalt 3" und "Tabelleninhalt C" die Position getauscht haben.



### Test-Methoden

***table( TABELLENTESTKLASSE )***
- **Funktionalität:** Beginn eines Tabellen-Tests.
- **Parameter:** Tabellen-Test-Klasse = Eine Klasse, welche die Tabellen-Tests implementiert.


***rowColumnMismatch()***
- **Funktionalität:** Es wird getestet, ob alle Elemente der Nutzer-Tabelle und Lösungs-Tabelle Spalten und Zeilen genau übereinstimmen.
- **Platzhalter:** `_ELEMENT_`= Hiermit lassen sich falsche Elemente ausgeben.


***column( SPALTENNAME oder SPALTENNUMMER )***
- **Funktionalität:** Spezifiziert eine Spalte, auf welcher Tests ausgeführt werden.
- **Parameter:** Spaltenname = Name der Spalte. / Spaltennummer = Nummer der Spalte, beginnend bei null.

***rowMismatch()***
- **Funktionalität:** Testet, ob alle Elemente, welche in derselben Zeile in der Lösung stehen, auch in derselben Zeile in der Nutzer-Tabelle stehen, hierbei ist die Rheinfolge der Zeilen egal.
Hierfür muss zuvor eine Spalte festgelegt worden sein, welche eindeutige Werte enthält.
- **Platzhalter:** `_ELEMENT_`= Hiermit lassen sich falsche Elemente ausgeben.

***missing()***
- **Funktionalität:** Testet, ob ein Element in der angegebenen Spalte fehlt.
- **Platzhalter:** `_ELEMENT_` = Hiermit lassen sich fehlende Elemente ausgeben./ `_COLUMN1_` = Hiermit wird der Name der betroffenen Spalte ausgegeben.

***tooMany()***
- **Funktionalität:** Testet, ob ein Element in der angegebenen Spalte überflüssig ist
- **Platzhalter:** `_ELEMENT_` = Hiermit lassen sich überflüssige Elemente ausgeben./ `_COLUMN1_` = Hiermit wird der Name der betroffenen Spalte ausgegeben.

***capitalisation()***
- **Funktionalität:** Testet, ob ein Element nicht der richtigen Groß- und Kleinschreibung entspricht. 
- **Platzhalter:** `_ELEMENT_` = Hiermit lassen sich Elemente ausgeben, welche eine falsche Klein- und Großschreibung aufweisen./ `_COLUMN1_` = Hiermit wird der Name der betroffenen Spalte ausgegeben.

***wrongColumn( SPALTE1, SPALTE2)***
- **Funktionalität:** Testet, ob ein Element welches in Spalte 1 gehört, in Spalte 2 steht.
- **Parameter:** Spalte1 = Der Name oder die Nummer der Spalte, in welcher ein Element stehen sollte. / Spalte2 = Der Name oder die Nummer der Spalte, in welcher ein Element steht.
- **Platzhalter:** `_ELEMENT_` = Hiermit lassen sich Elemente ausgeben, welche in der falschen Spalte stehen. `_COLUMN1_` = Der Name der Spalte, in welche die Elemente gehören. / `_COLUMN2_` = Der Name der Spalte, in welcher die Elemente stehen.

***or()***
- **Funktionalität:** Verknüpft mehrere Tests derselben Art.

***message( TESTLEVEL, NACHRICHT )***
- **Funktionalität:** Definiert eine Nachricht, welche bei einem Fehler ausgegeben wird, wenn das entsprechende Test-Level erreicht wurde.
- **Parameter:** Test-Level = Das Level, welches erreicht werden muss, damit diese Nachricht ausgegeben wird (das geringste Level is 1)./ Nachricht = Wird ausgeben, wenn der Test fehlschlägt und das Test-Level erreicht ist. Kann Platzhalter enthalten, welche bei bestimmten Tests durch andere Elemente ersetzt werden. 

***combine()***
- **Funktionalität:** Kombiniert mehrere unterschiedliche Tests unter einem Testnamen.

***test( TESTNAME, TESTKATEGORIENAME )***
- **Funktionalität:** Führt den Test durch und generiert das Ergebnis.
- **Parameter:** Testname = Der Name, unter welchem der Test den Nutzern angezeigt wird./ Testkategorie = Der Name der Kategorie, unter welche dieser Test einsortiert wird.

---

## Klassendiagrammtest


### Testart

Im Tabellentest werden uxf-Datein eingelesen, welche von UMLet erstellt wurden.
Es werden zwei Diagramme angegeben, ein Studen-Diagramm und ein Lösungs-Diagramm.

Es können die Klassen und Relationen auf ihre Klassen-Namen, Klassen-Attribute, Relations-Typen und Relations-Multiplizitäten geprüft werden.

Zudem lässt sich prüfen, ob alle Klassen vorhanden sind, welche in einem Glossar stehen, dass dem Tabellen-Test Format entspricht.

In dem Lösungs-Diagramm lässt sich eine Relation mittels gepunkteter-Linie als beliebige Relation markieren (`lt=..`).
Eine beliebige Relation muss im Studenten-Diagramm auch die gleichen zwei Klassen verbinden, der Relations-Typ und die Multiplizitäten sind dabei aber egal.

Sollte eine Lösungs-Relation keine Multiplizität angeben, ist hier auch jede Art der Multiplizität erlaubt.

### Test-Methoden

***classDiagram( KLASSENDIAGRAMMTESTKLASSE )***
- **Funktionalität:** Beginn eines Klassen-Diagramm-Tests.
- **Parameter:** Klassendiagramm-Test-Klasse =  Eine Klasse, welche die Klassendiagramm-Tests implementiert.

***missingClasses()***
- **Funktionalität:** Testet, ob eine Klasse im Diagramm fehlt.
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben, welche im Diagramm fehlen.

***wrongClasses()***
- **Funktionalität:** Testet, ob eine Klasse im Diagramm falsch (überflüssig) ist.
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben, welche im Diagramm falsch ist.

***ClassWrongAttributes()***
- **Funktionalität:** Testet, ob eine Klasse im Nutzer-Diagramm andere Attribute als das Lösungs-Diagramm hat.
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben, welche im andere Attribute haben.

***missingRelations()***
- **Funktionalität:** Testet, ob eine Relation im Diagramm fehlt (sollten mehrere Relationen zwischen zwei Klassen bestehen, aber ein Student erstellt nur eine, so werden die Restlichen nicht als fehlend ausgegeben) .
- **Platzhalter:** `_RELATION_` = Hiermit lassen sich die Relationen ausgeben, welche im Diagramm fehlen.

***wrongRelations()***
- **Funktionalität:** Testet, ob eine im Diagramm bestehende Relation überflüssig ist oder die falschen Attribute hat.
- **Platzhalter:** `_RELATION_` = Hiermit lassen sich die Relationen ausgeben, welche im Diagramm überflüssig sind oder die falschen Attribute haben.

***mismatch( GLOSSARPFAD, SPALTENNAME )***
- **Funktionalität:** Testet, ob es einen Unterschied zwischen dem Glossar (welches die Klassen-Namen auflistet) und dem Diagramm gibt.
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben welche nur in einem zwischen Diagramm und Glossar vorhanden sind aber nicht in beiden.
- **Parameter:** GlossarPfad = Der Pfad zum Glossar, welches alle welches dem Diagramm zugehörig ist./ Spaltenname = Name der Spalte in der die Klassennamen stehen.
- *Hinweis:* Sollte das angegebene Glossar nicht geladen werden können (z.B. weil die Markdown-Tabelle nicht existiert), so schlägt der Test fehl.

***illegalElementes()***
- **Funktionalität:** Testet, ob ein Element im Diagramm verwendet wird, welches nicht erlaubt ist (erlaubte Elemente: UMLClass, Relation, UMLNote).
- **Platzhalter:** `_ELEMENT_` = Hiermit lassen sich die Elemente ausgeben, welche im Diagramm vorhanden sind, aber von UMLet nicht erkannt werden.


***message( TESTLEVEL, NACHRICHT )***
- **Funktionalität:** Definiert eine Nachricht, welche bei einem Fehler ausgegeben wird, wenn das entsprechende Test-Level erreicht wurde.
- **Parameter:** Test-Level = Das Level, welches erreicht werden muss, damit diese Nachricht ausgegeben wird (das geringste Level is 1)./ Nachricht = Wird ausgeben, wenn der Test fehlschlägt und das Test-Level erreicht ist. Kann Platzhalter enthalten, welche bei bestimmten Tests durch andere Elemente ersetzt werden.

***combine()***
- **Funktionalität:** Kombiniert mehrere Tests unter einem Testnamen.

***test( TESTNAME, TESTKATEGORIENAME )***
- **Funktionalität:** Führt den Test durch und generiere das Ergebnis.
- **Parameter:** Testname = Der Name, unter welchem der Test den Nutzern angezeigt wird./ Testkategorie = Der Name der Kategorie, unter welche dieser Test einsortiert wird.

---

## Codetest

### Testart

Die Code-Tests testen den Code mittels Reflection.
Dies führt zu der Einschränkung, nur Annotation mit der RetentionPolicy Runtime testbar sind.

Für den "stacktrace"-Test werden die vom SurefireReports generierten xml-Datieren ausgelesen.

### Test-Methoden


***classes( PAKETPFAD )***
- **Funktionalität:** Beginn eines Code-Tests.
- **Parameter:** Paketpfad = Der Pfad zum Paket, welches alle Klassen enthält, die getestet werden sollen.

***withName( KLASSENNAME )***
- **Funktionalität:** Teste alle Klassen, mit einem bestimmten Namen.
- **Parameter:** Klassenname = Name der Klasse die getestet werden soll.

***withAnnotation( ANNOTATION )***
- **Funktionalität:** Teste alle Klassen, mit einer bestimmten Annotation.
- **Parameter:** Annotation = Annotation der Klassen, die getestet werden sollen.

***immutable()***
- **Funktionalität:** Testet, ob die Klassen unveränderbar sind (unveränderbar heißt, dass es keine public Variablen und setter Methoden gibt).
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben, welche veränderbar sind.

***inPackage( PAKETSTRUKTUR )***
- **Funktionalität:** Testet, ob die Klassen in einem bestimmten Paket sind.
- **Parameter:**  Paketstruktur = Gebe das Ende der Paketstruktur an, in welcher die Klassen liegen sollen. Dies kann auch nur das innerste Paket sein.
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben, welche nicht im richtigen Paket sind.

***noCircularDependencies()***
- **Funktionalität:** Testet, ob die Klassen zirkulären Abhängigkeiten haben.
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben, welche zirkuläre Abhängigkeiten haben.

***shouldHave()***
- **Funktionalität:** Die Klassen sollten das folgende Attribut haben.

***shouldNotHave()***
- **Funktionalität:** Die Klassen sollten das folgende nicht Attribut haben.

***annotation( ANNOTATION )***
- **Funktionalität:** Testet, ob die Klassen die Annotation besitzen.
- **Parameter:** Annotation = Die Annotation, welche die Klassen haben oder nicht haben sollen.
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben, welche die Annotation entsprechend der festgelegten Regel besitzen oder nicht besitzen.

***otherClass( KLASSENNAME )***
- **Funktionalität:** Testet, ob es für jede Klasse eine andere Klasse mit einem bestimmten Namen gibt.
- **Parameter:** Klassenname = Name einer dritten Klasse, welche alle getesteten Klassen besitzen müssen. Im Namen der dritten Klasse kann `_CLASS_` verwendet werden, um den Namen der zu testenden Klassen einzufügen.
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben, welche eine andere Klasse entsprechend der festgelegten Regel besitzen oder nicht besitzen.

***stacktrace()***
- **Funktionalität:** Testet, ob der Error Stacktrace eines Surefire-Reports eine Klasse des angegebenen Pakets enthält.
- **Platzhalter:** `_LINE_` = Hiermit lassen sich die Zeilen ausgeben, in welcher eine lokale Klasse das erste Mal auftaucht./ `_TEST_` = Hiermit lassen sich die der Name des Tests ausgeben, der dem gefundenen Stacktrace zugeordnet ist.

***message( TESTLEVEL, NACHRICHT )***
- **Funktionalität:** Definiert eine Nachricht, welche bei einem Fehler ausgegeben wird, wenn das entsprechende Test-Level erreicht wurde.
- **Parameter:** Test-Level = Das Level, welches erreicht werden muss, damit diese Nachricht ausgegeben wird (das geringste Level is 1)./ Nachricht = Wird ausgeben, wenn der Test fehlschlägt und das Test-Level erreicht ist. Kann Platzhalter enthalten, welche bei bestimmten Tests durch andere Elemente ersetzt werden.

***test( TESTNAME, TESTKATEGORIENAME )***
- *Funktionalität:* Führt den Test durch und generiere das Ergebnis.
- *Parameter:* Testname = Der Name, unter welchem der Test den Nutzern angezeigt wird./ Testkategorie = Der Name der Kategorie, unter welche dieser Test einsortiert wird.

---

## TestLevel

Das TestLevel versucht grob zu bestimmen, ob eine Person schwierigkeiten mit einer Aufgabe hat. Hierbei gibt ein erhöhtes Level mehr Schwierigkeiten an.

Hierfür werden die Commits, Pushes betrachtet. Insbesondere werden sich die Commits angeschaut, welche eine Datei eines Testes modifizieren.
Sollte nun eine bestimmte Zeitspanne zwischen zwei Commits vergangen sein, welche Dateien derselben Aufgabe bearbeiten, so wird davon ausgegangen,
dass die Person an dieser Aufgabe festhängt und das TestLevel dieses Tests wird erhöht.

Jedoch kann Level kann sich pro Push-Vorgang nur um eins erhöhen, da das TestLevel das Feedback der GitHub-Pages Seite beeinflusst und dies nur durch einen Push und nicht durch einen Commit aktualisiert wird.
Hierdurch soll verhindert werden, dass TestLevel übersprungen werden und das Feedback eines Levels nicht gelesen wurde.

Die TestLevel lassen sich über die [TestLevel](src/main/java/thkoeln/divekithelper/common/testlevel/TestLevel.java) generieren.
Hierzu muss eine Konfigurations-Datei im JSON-Format erstellt werden, welche folgenden Informationen enthält:

*Hinweis:* default = Diese Werte werden in den Tests übernommen, wenn sie nicht explizit angegeben sind
- defaultDelay : Anzahl der Minuten zwischen zwei Commits, bis das testLevel erhöht wird.
- defaultMaxLevel : Anzahl der Level die maximal erreicht werden kann.
- tests : List von **Tests** (Ein Test muss einen testName, und path haben. Zusätzlich können noch Test spezifische delay und maxLevel angeben werden.)
  - testName : Name dieses Tests, um das TestLevel einem Test beim Benutzen der DSL zuzuordnen. Der testName muss einem der beiden Parameter der *test( TESTNAME, TESTKATEGORIENAME )* Methode entsprechen. Hierbei wird der *TESTNAME* priorisiert, sollte kein Level zu diesem gefunden werden, wird nach einem Level zu dem *TESTKATEGORIENAME* gesucht.
  - path : Dateipfad zu dem Ordner oder der Datei, welche dem Test enspricht.
  - delay : Anzahl der Minuten zwischen zwei Commits, bis das testLevel erhöht wird.
  - maxLevel : Anzahl der Level die maximal erreicht werden kann.

Ein Beispiel einer Konfigurations-Datei wäre:
``` 
{
  "defaultDelay":15,
  "defaultMaxLevel": 10,
    {"testName":"E1","path":"src/main/resources/E1.md","delay":20,"maxLevel": 3},
    {"testName":"E2","path":"src/main/resources/E2.md","delay":20},
    {"testName":"E3","path":"src/main/java/thkoeln/st/st2praktikum/E3"},
    {"testName":"E3b","path":"src/main/java/thkoeln/st/st2praktikum/E3/E3b.java","delay":10},
    {"testName":"E4","path":"src/main/java/thkoeln/st/st2praktikum/E4.java","delay":10}]
}
```

Wenn man diese Datei beispielsweise im Hauptverzeichnis als `testLevelConfig.json` speichert, lassen sich die TestLevel über folgenden Befehl generieren:
`TestLevel.generateTestLevel("testLevelConfig.json")`

Dieser Befehl muss einmal vor dem Ausführen der DSL bzw. dem Nutzen der TestLevel ausgeführt werden.
Sollte dies nicht geschehen oder es Probleme beim Generieren der Level geben, so werden die Tests standardmäßig mit einem TestLevel von 0 ausgeführt.

---

## Beispiele

Für einen Überblick über die Einsatzmöglichkeiten der DSL gibt es Beispiele zu allen 3 Test-Kategorien:

[Tabellen-Beispieltests](src/test/java/thkoeln/divekithelper/table/DivekitHelperTableBuilderTest.java)

[Klassendiagramm-Beispieltests](src/test/java/thkoeln/divekithelper/classDiagram/DivekitHelperClassDiagramBuilderTest.java)

[Code-Beispieltests](src/test/java/thkoeln/divekithelper/code/DivekitHelperCodeBuilderTest.java)

---

# Divekit-Helper Aufbau


Insgesamt umfasst der Divekit-Helper einen allgemeinen und drei spezialisierte Teile.
Die drei Spezialisierungen bestehen aus den verfügbaren Testarten: Tabelle, Klassendiagramm und Code.

Der [common](src/main/java/thkoeln/divekithelper/common)-Ordner enthält die allgemeinen Klassen.
Diese allgemeinen Klassen bestehen aus:
- [DivekitHelper](src/main/java/thkoeln/divekithelper/common/DivekitHelper.java): Implementiert die Methoden, die von allen drei Spezialisierungen genutzt werden.
- [JSONHelper](src/main/java/thkoeln/divekithelper/common/JSONHelper.java): Ermöglicht das Generieren und Speichern der Testergebnisse, in einem vom Divekit-Report-Visualizer kompatiblen Format.
- [TestLevel](src/main/java/thkoeln/divekithelper/common/testlevel): In diesen Ordner liegen alle Klassen, welche für das Bestimmen des TestLevels verantwortlich sind.

Die drei Spezialisierungen bestehen aus den Testarten: [Tabellen-Tests](src/main/java/thkoeln/divekithelper/table), [Klassendiagramm-Tests](src/main/java/thkoeln/divekithelper/classDiagram) und [Code-Tests](src/main/java/thkoeln/divekithelper/code).
Alle drei sind nach derselben Struktur aufgebaut:
- Im Interface Ordner wird die DSL definiert, indem für jeden Zustand der DSL ein Interface die möglichen nächsten Befehle festgelegt.
- Die Builder-Klasse implementiert diese Interfaces und ist die Klasse, welche beim Benutzen der DSL aufgerufen wird.
- Die Hauptklasse kümmert sich um das Ausführen der Test-Klasse und wird vom Builder aufgerufen. Außerdem generiert sie das Ergebnis und gibt es an den JSONHelper weiter. 

Des Weiteren haben die Tabellen- und Klassendiagramm-Tests eigene Parser, welche das Auslesen der UMLet und Markdown Dateien übernehmen, sowie Test Klassen welche die Tests auf den durch das Auslesen generierten Java-Klassen ausführen.

Der Code-Test hat keinen eignen Parser oder Test-Klasse, sondern führt die Tests direkt in der Hauptklasse mittels Reflection aus.

Zuletzt gibt es noch einen [Mock-Ordner](src/main/java/thkoeln/divekithelper/mock), welcher zum Testen der DSL dient.
Hierfür stellt er folgende Dateien zur verfügung:
- [Repository](src/main/java/thkoeln/divekithelper/mock/repo) mit einem Repository, um den Code-Test testen zu können.
- [Tabellen](src/main/java/thkoeln/divekithelper/mock/tables) mit Tabellen, um den Tabellen-Test testen zu können.
- [Diagramme](src/main/java/thkoeln/divekithelper/mock/diagrams) mit Klassen-Diagrammen, um den Klassendiagramm-Test testen zu können.



---

# Integration

Der Divekit-Helper muss als Dependency der `pom.xml` hinzugefügt werden (sollte es schon ein SLF4J Binding geben, kann die betroffene Dependency ausgeschlossen werden):
``` 
		<dependency>
			<groupId>com.github.L00Git</groupId>
			<artifactId>divekit-helper</artifactId>
			<version>1.0.5</version>
			<exclusions>
				<exclusion>
					<groupId>org.slf4j</groupId>
					<artifactId>slf4j-simple</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
```

Folgende Befehle sollten im Build teil der `pom.xml` stehen, damit die Stacktraces erhalten bleiben und der Helper ausgeführt wird:

Stacktrace
```			
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <configuration>
        <trimStackTrace>false</trimStackTrace>
      </configuration>
    </plugin>
```

Ausführen (in diesem Beispiel befinden sich die Divekit-Helper Befehle in der Klasse: `thkoeln.divekitHelper.DivekitHelperTest` im Test Verzeichnis)
```
	<plugin>
		<groupId>org.codehaus.mojo</groupId>
		<artifactId>exec-maven-plugin</artifactId>
		<version>1.6.0</version>
		<configuration>
		  <mainClass>thkoeln.divekitHelper.DivekitHelperTest</mainClass>
		  <classpathScope>test</classpathScope>
		</configuration>
	</plugin>
```

Im CI/CD-Skript sollte der Surefire-Test vor dem Divekit-Helper ausgeführt werden, da der Surefire-Test vom Helper genutzt wird.
```
    - mvn exec:java || true # run DivekitHelper
```

Beim Aufrufen des report-visualizers, muss der Report als Parameter angegeben werden.
```
    - ./divekit-rv target/Test.pmd.json target/surefire-reports/TEST-*.xml DivekitHelperResult.custom-test.json
```
