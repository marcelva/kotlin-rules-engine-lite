# JSON-Beispiele für die Rules Engine

Diese Sammlung enthält **10 eigenständige JSON-Beispiele**, die zeigen, wie Regelwerke mit `JsonRuleSetDSL` modelliert werden können.

## Hinweise zur Verwendung

* Das JSON-Format erwartet pro Eintrag:
  * `name`: Name des RuleSets
  * `allOf`: Liste von Bedingungen, die **alle** erfüllt sein müssen
  * `attribute` + `equals`: Attributname und Vergleichswert
* Die tatsächliche Bedeutung von `attribute` hängt von den registrierten `ruleFactories` in der Anwendung ab.

## Enthaltene Beispiele

1. `01-onboarding-segmentierung.json`
2. `02-risiko-check.json`
3. `03-campaign-targeting.json`
4. `04-identity-sanity.json`
5. `05-day-based-activation.json`
6. `06-event-access.json`
7. `07-fraud-signal-bundle.json`
8. `08-gaming-reward-tier.json`
9. `09-legacy-migration.json`
10. `10-complex-business-pack.json`
