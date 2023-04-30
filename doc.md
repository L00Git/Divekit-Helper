## Inhaltsverzeichnis

- [Funktionalität des Divekit-Helpers](#funktionalität-des-divekit-helpers)
- [Divekit-Helper Aufbau](#divekit-helper-aufbau)
- [Test-Methoden Erklärung](#test-methoden-erklärung)
  - [Tabellentests](#tabellentests)
  - [Klassendiagrammtest](#klassendiagrammtests)
  - [Codetests](#codetests)
  - [Beispiele](#beispiele)
- [Implementations-Hinweise](#implementations-hinweise)
  - [Aktueller Stand & notwendige Implementierungen](#aktueller-stand--notwendige-implementierungen)
  - [Integration](#integration)

---

## Funktionalität des Divekit-Helpers

Die Hauptfunktion des Helpers ist es das Erstellen von Tests durch eine DSL zu vereinfachen.
Dies geschieht durch das Übersetzen der DSL in interne Aufrufe der entsprechenden Tests.
Diese Tests generieren eine mit dem Divekit-Report-Visualizer kompatible Datei. 
Der Helper deckt drei Kategorien von Tests ab: Tabellen-Tests, Klassendiagramm-Tests und Code-Tests.
Der Helper enthält jedoch nur eine Implementierung der Code-Testklasse und ist momentan auf externe Implementieren der Tabellen- und Klassendiagramm-Tests angewiesen.

---

## Test-Methoden Erklärung

Die Syntax der DSL basiert auf der Method-Chaining Methode.
Im Folgenden werden die einzelnen Methoden bezüglich ihrer Funktionalität und Bedienung in der "Chain" beschrieben.

### Tabellentests


***table( TABELLENTESTKLASSE )***
- **Funktionalität:** Beginn eines Tabellen-Tests.
- **Parameter:** Tabellen-Test-Klasse = Eine Klasse, welche das [Tabellen-Test-Interface](src/main/java/thkoeln/divekithelper/table/TableTestInterface.java) implementiert.


***rowColumnMismatch()***
- **Funktionalität:** Es wird getestet, ob alle Elemente der Nutzer-Tabelle und Lösungs-Tabelle Spalten und Zeilen genau übereinstimmen.
- **Platzhalter:** `_ELEMENT_`= Hiermit lassen sich falsche Elemente ausgeben.


***column( SPALTENNAME oder SPALTENNUMMER )***
- **Funktionalität:** Spezifiziere eine Spalte, auf welcher tests ausgeführt werden.
- **Parameter:** Spaltenname = Name der Spalte. / Spaltennummer = Nummer der Spalte, beginnend bei null.

***rowMismatch()***
- **Funktionalität:** Teste, ob alle Elemente, welche in derselben Zeile in der Lösung stehen, auch in derselben Zeile in der Nutzer-Tabelle stehen, hierbei ist die Rheinfolge der Zeilen egal.
Hierfür muss zuvor eine Zeile festgelegt worden sein, welche eindeutige Werte enthält.
- **Platzhalter:** `_ELEMENT_`= Hiermit lassen sich falsche Elemente ausgeben.

***missing()***
- **Funktionalität:** Tests, ob ein Element in der angegebenen Spalte fehlt.
- **Platzhalter:** `_ELEMENT_` = Hiermit lassen sich fehlende Elemente ausgeben./ `_COLUMN1_` = Hiermit wird der Name der betroffenen Spalte ausgegeben.

***tooMany()***
- **Funktionalität:** Testet, ob ein element in der angegebenen Spalte überflüssig ist
- **Platzhalter:** `_ELEMENT_` = Hiermit lassen sich überflüssige Elemente ausgeben./ `_COLUMN1_` = Hiermit wird der Name der betroffenen Spalte ausgegeben.

***capitalisation()***
- **Funktionalität:** Testet, ob ein Element nicht der richtigen Groß- und Kleinschreibung entspricht. 
- **Platzhalter:** `_ELEMENT_` = Hiermit lassen sich Elemente ausgeben, welche eine falsche Klein- und Großschreibung aufweisen./ `_COLUMN1_` = Hiermit wird der Name der betroffenen Spalte ausgegeben.

***wrongColumn( SPALTE1, SPALTE2)***
- **Funktionalität:** Testet, ob ein Element welches in Spalte 1 gehört, in Spalte 2 steht.
- **Parameter:** Spalte1 = Der Name oder die Nummer der Spalte, in welcher ein Element stehen sollte. / Spalte2 = Der Name oder die Nummer der Spalte, in welcher ein Element steht.
- **Platzhalter:** `_ELEMENT_` = Hiermit lassen sich Elemente ausgeben, welche in der falschen Spalte stehen. `_COLUMN1_` = Der Name der Spalte, in welche die Elemente gehören. / `_COLUMN2_` = Der Name der Spalte, in welcher die Elemente stehen.

***or()***
- **Funktionalität:** Verknüpfe mehrere Tests derselben Art.

***message( TESTLEVEL, NACHRICHT )***
- **Funktionalität:** Definiere eine Nachricht, welche bei einem Fehler ausgegeben wird, wenn das entsprechende Test-Level erreicht wurde.
- **Parameter:** Test-Level = Das Level, welches erreicht werden muss, damit diese Nachricht ausgegeben wird (das geringste Level is 1)./ Nachricht = Wird ausgeben, wenn der Test fehlschlägt und das Test-Level erreicht ist. Kann Platzhalter enthalten, welche bei bestimmten Tests durch andere Elemente ersetzt werden. 

***combine()***
- **Funktionalität:** Kombiniere mehrere Tests unter einem Testnamen.

***test( TESTNAME, TESTKATEGORIENAME )***
- **Funktionalität:** Führe den Test durch und generiere das Ergebnis.
- **Parameter:** Testname = Der Name, unter welchem der Test den Nutzern angezeigt wird./ Testkategorie = Der Name der Kategorie, unter welche dieser Test einsortiert wird.


### Klassendiagrammtests

***classDiagram( KLASSENDIAGRAMMTESTKLASSE )***
- **Funktionalität:** Beginn eines Klassen-Diagramm-Tests.
- **Parameter:** Klassendiagramm-Test-Klasse =  Eine Klasse, welche das [ClassDiagram-Test-Interface](src/main/java/thkoeln/divekithelper/classDiagram/ClassDiagramTestInterface.java) implementiert.

***missingClasses()***
- **Funktionalität:** Teste, ob eine Klasse im Diagramm fehlt.
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben, welche im Diagramm fehlen.

***missingRelations()***
- **Funktionalität:** Teste, ob eine Relation im Diagramm fehlt.
- **Platzhalter:** `_RELATION_` = Hiermit lassen sich die Relationen ausgeben, welche im Diagramm fehlen.

***mismatch()***
- **Funktionalität:** Teste, ob es einen Unterschied zwischen dem Glossar und dem Diagramm gibt.
- **Platzhalter:** `_MISMATCH_` = Hiermit lassen sich die Unterschiede zwischen dem Diagramm und Glossar ausgeben.

***wrongRelations()***
- **Funktionalität:** Teste, ob eine im Diagramm bestehende Relation überflüssig ist.
- **Platzhalter:** `_RELATION_` = Hiermit lassen sich die Relationen ausgeben, welche im Diagramm überflüssig sind.

***illegalElementes()***
- **Funktionalität:** Teste, ob ein Zeichen im Diagramm verwendet wird, welches von UMLet nicht erkannt wird.
- **Platzhalter:** `_ELEMENT_` = Hiermit lassen sich die Elemente ausgeben, welche im Diagramm vorhanden sind, aber von UMLet nicht erkannt werden.

***message( TESTLEVEL, NACHRICHT )***
- **Funktionalität:** Definiere eine Nachricht, welche bei einem Fehler ausgegeben wird, wenn das entsprechende Test-Level erreicht wurde.
- **Parameter:** Test-Level = Das Level, welches erreicht werden muss, damit diese Nachricht ausgegeben wird (das geringste Level is 1)./ Nachricht = Wird ausgeben, wenn der Test fehlschlägt und das Test-Level erreicht ist. Kann Platzhalter enthalten, welche bei bestimmten Tests durch andere Elemente ersetzt werden.

***combine()***
- **Funktionalität:** Kombiniere mehrere Tests unter einem Testnamen.

***test( TESTNAME, TESTKATEGORIENAME )***
- **Funktionalität:** Führe den Test durch und generiere das Ergebnis.
- **Parameter:** Testname = Der Name, unter welchem der Test den Nutzern angezeigt wird./ Testkategorie = Der Name der Kategorie, unter welche dieser Test einsortiert wird.


### Codetests

*Hinweis*: Im Codetest lassen sich nur Annotation mit der RetentionPolicy Runtime ansprechen.

***classes( PAKETPFAD )***
- **Funktionalität:** Beginn eines Code-Tests.
- **Parameter:** Paketpfad = Der Pfad zum Packet, welches alle Klassen enthält, die getestet werden sollen.

***withName( KLASSENNAME )***
- **Funktionalität:** Teste alle klassen, mit einem bestimmten Namen.
- **Parameter:** Klassenname = Name der Klasse die getestet werden soll.

***withAnnotation( ANNOTATION )***
- **Funktionalität:** Teste alle klassen, mit einer bestimmten Annotation.
- **Parameter:** Annotation = Annotation der Klassen, die getestet werden sollen.

***immutable()***
- **Funktionalität:** Teste, ob die Klassen unveränderbar sind (unveränderbar heißt, dass es keine public Variablen und setter Methoden gibt).
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben, welche veränderbar sind.

***inPackage( PAKETSTRUKTUR )***
- **Funktionalität:** Teste, ob die Klassen in einem Bestimmten Packet sind.
- **Parameter:**  Paketstruktur = Gebe das Ende der Paketstruktur an, in welcher die Klassen liegen sollen. Dies Kann auch nur das letzte Packet sein.
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben, welche nicht im richtigen sind.

***noCircularDependencies()***
- **Funktionalität:** Teste, ob die Klassen zirkulären Abhängigkeiten haben.
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben, welche zirkuläre Abhängigkeiten haben.

***shouldHave()***
- **Funktionalität:** Die Klassen sollten das folgende Attribut haben.

***shouldNotHave()***
- **Funktionalität:** Die Klassen sollten das folgende nicht Attribut haben.

***annotation( ANNOTATION )***
- **Funktionalität:** Teste, ob die Klassen die Annotation besitzen.
- **Parameter:** Annotation = Die Annotation, welche die Klassen haben oder nicht haben sollen.
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben, welche die Annotation entsprechend der festgelegten Regel besitzen oder nicht besitzen.

***otherClass( KLASSENNAME )***
- **Funktionalität:** Teste, ob es für jede Klasse eine andere Klasse mit einem bestimmten Namen gibt.
- **Parameter:** Klassenname = Name einer dritten Klasse, welche alle getesteten Klassen besitzen müssen. Im Namen der dritten Klasse kann `_CLASS_` verwendet werden, um den Namen der zu testenden Klassen einzufügen.
- **Platzhalter:** `_CLASS_` = Hiermit lassen sich die Klassen ausgeben, welche eine andere Klasse entsprechend der festgelegten Regel besitzen oder nicht besitzen.

***stacktrace()***
- **Funktionalität:** Teste, ob der Error Stacktrace eines Surefire-Reports eine Klasse des angegebenen Packets enthält.
- **Platzhalter:** `_LINE_` = Hiermit lassen sich die Zeilen ausgeben, in welcher eine lokale Klasse das erste Mal auftaucht./ `_TEST_` = Hiermit lassen sich die der Name des Tests ausgeben, der dem gefundenen Stacktrace zugeordnet ist.

***message( TESTLEVEL, NACHRICHT )***
- **Funktionalität:** Definiere eine Nachricht, welche bei einem Fehler ausgegeben wird, wenn das entsprechende Test-Level erreicht wurde.
- **Parameter:** Test-Level = Das Level, welches erreicht werden muss, damit diese Nachricht ausgegeben wird (das geringste Level is 1)./ Nachricht = Wird ausgeben, wenn der Test fehlschlägt und das Test-Level erreicht ist. Kann Platzhalter enthalten, welche bei bestimmten Tests durch andere Elemente ersetzt werden.

***test( TESTNAME, TESTKATEGORIENAME )***
- *Funktionalität:* Führe den Test durch und generiere das Ergebnis.
- *Parameter:* Testname = Der Name, unter welchem der Test den Nutzern angezeigt wird./ Testkategorie = Der Name der Kategorie, unter welche dieser Test einsortiert wird.

### Beispiele

Für einen Überblick über die Einsatzmöglichkeiten der DSL gibt es Beispiele zu allen 3 Test-Kategorien:

[Tabellen-Beispieltests](src/test/java/thkoeln/divekithelper/table/DivekitHelperTableBuilderTest.java)

[Klassendiagramm-Beispieltests](src/test/java/thkoeln/divekithelper/classDiagram/DivekitHelperClassDiagramBuilderTest.java)

[Code-Beispieltests](src/test/java/thkoeln/divekithelper/code/DivekitHelperCodeBuilderTest.java)

---

## Divekit-Helper Aufbau


Insgesamt umfasst der Divekit Helper einen allgemeinen und drei spezialisierte Teile.
Die drei Spezialisierungen bestehen aus den verfügbaren Testarten: Tabelle, Klassendiagramm und Code.

Der [common](src/main/java/thkoeln/divekithelper/common)-Ordner enthält die allgemeinen Klassen.
Diese allgemeinen Klassen bestehen aus:
- [DivekitHelper](src/main/java/thkoeln/divekithelper/common/DivekitHelper.java): Implementiert die Methoden, die von allen drei Spezialisierungen genutzt werden.
- [JSONHelper](src/main/java/thkoeln/divekithelper/common/JSONHelper.java): Ermöglicht das Generieren und Speichern der Testergebnisse, in einem vom Divekit-Report-Visualizer kompatiblen Format.
- [XMLHelper](src/main/java/thkoeln/divekithelper/common/XMLHelper.java): Stellt das Extrahieren von Stacktraces aus Surefire-Reports zur verfügung.
- [CommitFrequencyInterface](src/main/java/thkoeln/divekithelper/common/CommitFrequencyInterface.java): Definiert die Funktionalität, welche eine Commit-Frequency-Klasse aufweisen muss. Diese Klasse ist für das Test-Level verantwortlich.

Die drei Spezialisierungen bestehen aus den Testarten: [Tabellen-Tests](src/main/java/thkoeln/divekithelper/table), [Klassendiagramm-Tests](src/main/java/thkoeln/divekithelper/classDiagram) und [Code-Tests](src/main/java/thkoeln/divekithelper/code).
Alle drei sind nach derselben Struktur aufgebaut:
- Im Interface Ordner wird die DSL definiert, indem für jeden Zustand der DSL ein Interface die möglichen nächsten Befehle festgelegt.
- Die Builder-Klasse implementiert diese Interfaces und ist die Klasse, welche beim Benutzen der DSL aufgerufen wird.
- Die Hauptklasse kümmert sich um das Ausführen der Tests und wird vom Builder aufgerufen. Außerdem generiert sie das Ergebnis und gibt es an den JSONHelper weiter. 
- Des Weiteren gibt es noch zwei Test-Interfaces in dem Tabellen- und Klassendiagramm-Ordner, welche für eine Kompatibilität mit dem Divekit-Helper von den entsprechenden Test-Klassen implementiert werden müssen.

Zuletzt gibt es noch einen [Beispiel-Ordner](src/main/java/thkoeln/divekithelper/mock), welcher zum Testen der DSL dient.
Hierfür stellt er folgende Dateien zur verfügung:
- Einen [Ordner](src/main/java/thkoeln/divekithelper/mock/implementations) mit Beispiel-Implementationen der Interfaces.
- Einen [Ordner](src/main/java/thkoeln/divekithelper/mock/repo) mit einem Repository, um den Code-Test testen zu können.
- Einen [Ordner](src/main/java/thkoeln/divekithelper/mock/tables) mit Tabellen, um den Tabellen-Test testen zu können.

## Implementations-Hinweise

### Aktueller Stand & notwendige Implementierungen

Der Helper besitzt noch nicht alle Funktionen um vollständig eingesetzt werden zu können.
Die folgenden Funktionen sind bereits fertig:
- Die DSL (nur die Syntax)
- Der Code-Test
- Das Generieren von Ergebnissen

Während andere Funktionen noch eine Implementierung von Interfaces benötigen:
- Die [Tabellen-](src/main/java/thkoeln/divekithelper/table/TableTestInterface.java) und [Klassendiagramm-Tests]((src/main/java/thkoeln/divekithelper/classDiagram/DiagramTestInterface.java))
- Das Bestimmen der [Test-Level](src/main/java/thkoeln/divekithelper/common/CommitFrequencyInterface.java)

Beim Erstellen der Tabellen-Test-Klasse sollte die momentane Benutzung der Tabellen berücksichtigt werden, so gibt es z.B. Spalten welche einen Platzhalter ("...") besitzen und von Studenten beliebig ausgefüllt werden dürfen oder mehrere Begriffe in einer Zelle, welche in beliebiger Reihenfolge stehen dürfen.

Die momentane Beispiel-Commit-Frequency-Klasse arbeitet mit einer lokalen Datei, welche bei jedem Push auf das Repo in der Pipeline verändert wird. Diese Implementierung dient nur demonstrationszwecken und berücksichtigt nicht die veränderten Dateien, sondern erhöht das Level bei jedem Aufruf eines Tests.
Bei einer richtigen Implementierung sollten die veränderten Dateien jedoch eine Rolle spielen. Außerdem sollte das Speichern der Level nicht über ein Push aus der Pipeline funktionieren, sondern beispielsweise über Caching oder einen Server auf den der Gitlab-Runner zugriff hat.

### Integration
Zuerst sollte man aus dem Divekit-Helper Jar builden und sie dem Projekt hinzufügen.
Im CI/CD-Skript sollte der Surefire-Test vor dem Divekit-Helper ausgeführt werden, da der Surefire-Test vom Helper genutzt wird.
```
    - mvn install:install-file -Dfile=divekit-helper.jar -DgroupId=thkoeln -DartifactId=divekit-helper -Dversion=1.0 -Dpackaging=jar # install Divekit-Helper
    - mvn pmd:pmd  # buildcleancodereport
    - mvn verify -fn # Always return status code 0 => Continue with the next stage
    - mvn compile exec:java # run DivekitHelper
    - mv target/pmd.net.sourceforge.pmd.renderers.JsonRenderer target/Test.pmd.json
    - chmod ugo+x ./divekit-rv
    - ./divekit-rv target/Test.pmd.json target/surefire-reports/TEST-*.xml DivekitHelperResult.custom-test.json
```

Außerdem muss der Divekit-Helper als Dependency der `pom.xml` hinzugefügt werden:
``` 
	<dependency>
		<groupId>thkoeln</groupId>
		<artifactId>divekit-helper</artifactId>
		<version>1.0</version>
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

Die momentane MockCommitFrequency-Klasse speichert das Test-Level, indem sie in der Piepline einen Push durchführt, dies sollte in der fertigen Version eleganter z.B. durch Caching oder einen Server gelöst werden.
Da diese Version jedoch mit einem Push arbeitet, benötigt sie einen Project Access Token und folgendes CI/CD-Skript nach dem Ausführen des Helpers:
```
    - git config user.name "ci-pipeline"  #commit studentTestLevel changes
    - git config user.email "ci-pipeline@example.com"
    - git remote remove project ||true # true => continues if remote doesn't exists
    - git remote add project https://oauth2:$ACCESS_TOKEN@git.st.archi-lab.io/$CI_PROJECT_PATH.git
    - git add src/test/resources/studentTestLevel.json
    - git commit -m "update studentTestLevel"
    - git push project HEAD:master -o ci.skip #ci.skip => prevents trigger of pipeline script
```

Wenn es Probleme mit SLF4J-Dependencies-Dopplungen gibt, kann man die SLF4J Dependency des Divekit-Helpers ausschließen.
