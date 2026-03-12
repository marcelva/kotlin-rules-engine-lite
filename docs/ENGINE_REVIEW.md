# Engine-Bewertung und Verbesserungsplan

## Kurzfazit
Die Engine ist bewusst klein, leicht lesbar und damit gut für einfache Domänenregeln geeignet. Die Kernidee – `Rule`, `RuleSet`, Provider/Supplier und optionale JSON-DSL – ist klar und verständlich.

Für „einfach **und** robust gegenüber Erweiterungen“ fehlen aktuell vor allem:
1. strukturierte Fehlermeldungen,
2. erklärbare Auswertung (Warum true/false?),
3. ausbaufähige Komposition (mehr als nur `allOf` und spezialisiertes `OR`),
4. entfernbare technische Seiteneffekte (`println`),
5. stabilere Konfiguration (typsichere Actions/Filter).

---

## Stärken der aktuellen Architektur

- Sehr schlanker Kern (`Rule<T>`, `RuleSet<T>`) mit geringer Einstiegshürde.
- Gute Trennung zwischen API und Domänenimplementierung (Modulstruktur).
- JSON-Einstiegspunkt vorhanden, um Regeln ohne Recompile zu konfigurieren.

---

## Hauptrisiken für Erweiterbarkeit

### 1) Fehlende Diagnosefähigkeit bei Entscheidungen
`evaluate` liefert nur `Boolean`. Bei komplexeren Rule-Sets ist nicht sichtbar, welche Regel gefeuert hat oder fehlgeschlagen ist.

**Folge:** Debugging, Fachbereichs-Transparenz und Testbarkeit werden mit wachsender Regelanzahl schwierig.

### 2) Komposition ist aktuell asymmetrisch
`RuleSet` ist implizit `AND` (`all`), `OR` ist über einen separaten Builder gelöst.

**Folge:** Bei künftigen Operatoren (`NOT`, verschachtelte Gruppen, Prioritäten) steigt die Komplexität in DSL und Implementierung unnötig.

### 3) Nebenwirkungen im Core/Provider (`println`)
Mehrere Stellen loggen direkt auf STDOUT.

**Folge:** Schwer testbar, unklare Observability-Strategie, schwierig in produktiven Umgebungen mit zentralem Logging.

### 4) String-basierte Schlüssel für kritische Schnittstellen
`action` und `filterBy` sind Strings.

**Folge:** Tippfehler erst zur Laufzeit; Refactorings sind fehleranfällig.

### 5) Caching auf `evaluate(action, fact)`-Ebene
Caching auf der Supplier-Methode kann bei variierenden Facts hohe Kardinalität erzeugen.

**Folge:** Cache-Nutzen kann kippen (Speicherverbrauch/geringe Hit-Rate), sobald Facts weniger wiederverwendet werden.

---

## Priorisierte Verbesserungen (einfach + robust)

## P0 – Sofortmaßnahmen (kleiner Eingriff, hoher Nutzen)

1. **`println` durch strukturiertes Logging ersetzen**
   - z. B. Quarkus Logger + einheitliche Log-Events.
   - Keine Seiteneffekte in DSL-Buildern.

2. **Typsichere Eingaben an den Außenkanten**
   - `action` als `enum class PersonAction` statt String in öffentlichen APIs.
   - Optional `Country` statt `filterBy: String` intern.

3. **Fehlermeldungen vereinheitlichen**
   - Eigene Exceptions (`RuleSetNotFound`, `UnsupportedAction`) mit Kontext.

## P1 – Erweiterungsfähigkeit der Engine

4. **EvaluationResult einführen (statt nur Boolean)**
   - Enthält `passed`, `failedRules`, `matchedRules`, optional `trace`.
   - Für UI/Monitoring und Tests Gold wert.

5. **Einheitliches Ausdrucksmodell für Logikoperatoren**
   - Interne AST-Knoten (`AllOf`, `AnyOf`, `Not`, `LeafRule`) statt Spezialfall-OR.
   - Macht JSON-DSL und Kotlin-DSL konsistent erweiterbar.

6. **Regel-Metadaten standardisieren**
   - Jede Regel mit stabiler `id`, `description`, optional `severity`.
   - Wichtig für Migrationen, Audits und Telemetrie.

## P2 – Betriebssicherheit und Governance

7. **Versionierung von Rule-Sets**
   - Regelset-Version + Activation-Strategie (z. B. feature flag).

8. **JSON-DSL validieren vor Ausführung**
   - Schema-/Semantikprüfung vor Mapping.
   - Fehler früh melden, nicht erst zur Laufzeit beim Evaluate.

9. **Gezielte Lasttests + Metriken**
   - p95/p99-Latenzen, Cache-Hit-Rate, Fehlerquote pro Regelset.

---

## Zielbild (minimal-invasiv)

- **Core bleibt klein:** `Rule`, `RuleSet`, plus `EvaluationResult`.
- **Komposition generisch:** über Operator-Knoten statt Spezial-Builder.
- **Provider bleiben domänennah:** liefern nur RuleSet-Definitionen.
- **Engine liefert Diagnostik:** immer nachvollziehbar, warum eine Entscheidung fiel.

So bleibt die Engine weiterhin einfach, wächst aber kontrolliert mit neuen Anforderungen.
