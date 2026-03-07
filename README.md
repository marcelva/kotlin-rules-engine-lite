# Kotlin Rules Engine Lite

A lightweight, Kotlin-first rules engine for modeling business decisions with simple, composable rule sets.

It is designed for teams that want to:
- keep decision logic readable,
- avoid complex rule frameworks,
- and get productive quickly with plain Kotlin.

## What this engine gives you

- **Small core API** (`Rule`, `RuleSet`, DSL builders)
- **Composable rules** using `all-of` semantics by default
- **`OR` composition** across multiple rule sets
- **JSON-based rule set loading** via `JsonRuleSetDSL`
- **Provider pattern** for selecting rule sets dynamically (e.g., by country)

---

## Quick start (2 minutes)

### 1) Define a domain object

```kotlin
data class Person(
    val age: Int,
    val country: Country,
    val lastName: String,
    val hairType: String = "",
    val eyeColor: String = ""
)
```

### 2) Create reusable rules

```kotlin
class MinAgeRule(private val minAge: Int) : Rule<Person> {
    override fun evaluate(fact: Person): Boolean = fact.age >= minAge
}

class LastNamePrefixRule(private val prefix: String) : Rule<Person> {
    override fun evaluate(fact: Person): Boolean = fact.lastName.startsWith(prefix)
}
```

### 3) Build a rule set with the Kotlin DSL

```kotlin
val canDrinkBeerDE = ruleSet<Person> {
    rule(MinAgeRule(16))
    rule(LastNamePrefixRule("S"))
}

val result = canDrinkBeerDE.evaluate(Person(17, Country.DE, "Schmidt"))
// true
```

`RuleSet` uses **AND logic** for all rules (`rules.all { ... }`).

---

## Core concepts

### `Rule<T>`
A single condition.

```kotlin
interface Rule<T> {
    fun evaluate(fact: T): Boolean
}
```

### `RuleSet<T>`
A list of rules evaluated as **all-of**.

```kotlin
class RuleSet<T>(val rules: List<Rule<T>>) {
    fun evaluate(fact: T): Boolean = rules.all { it.evaluate(fact) }
}
```

### `ruleSet { ... }`
Type-safe Kotlin DSL to define a rule set.

```kotlin
val adultsOnly = ruleSet<Person> {
    rule(MinAgeRule(18))
}
```

### `combineWithOr { ... }`
Combine multiple rule sets with **OR** semantics.

```kotlin
val germanRule = ruleSet<Person> {
    rule(MinAgeRule(18))
}

val usaRule = ruleSet<Person> {
    rule(MinAgeRule(21))
}

val canBuySpecialProduct = combineWithOr<Person> {
    add(germanRule)
    add(usaRule)
}
```

---

## JSON-based rules

If you want editable rule configurations, use `JsonRuleSetDSL`.

### Expected JSON format

```json
{
  "ruleSets": [
    {
      "name": "can_drive_right",
      "allOf": [
        { "attribute": "country", "equals": "DE" },
        { "attribute": "minAge", "equals": "18" }
      ]
    }
  ]
}
```

### Load and map JSON to executable rules

```kotlin
val json = """
{
  "ruleSets": [
    {
      "name": "onboarding_segment_a",
      "allOf": [
        { "attribute": "country", "equals": "DE" },
        { "attribute": "eyeColor", "equals": "brown" }
      ]
    }
  ]
}
""".trimIndent()

val factories: Map<String, (String) -> Rule<Person>> = mapOf(
    "country" to { expected -> object : Rule<Person> {
        override fun evaluate(fact: Person): Boolean = fact.country.name == expected
    } },
    "eyeColor" to { expected -> object : Rule<Person> {
        override fun evaluate(fact: Person): Boolean =
            fact.eyeColor.equals(expected, ignoreCase = true)
    } }
)

val ruleSetsByName = JsonRuleSetDSL.fromJson(json, factories)
val passed = ruleSetsByName["onboarding_segment_a"]!!
    .evaluate(Person(22, Country.DE, "Sommer", eyeColor = "Brown"))
```

You can find additional JSON examples in `examples/json/`.

---

## Dynamic rule selection with providers

For larger systems, use `RuleSetProvider<T>` implementations and a central `RuleSetSupplier<T>`.

This repository includes an example with:
- `GermanyRuleSetProvider`
- `USARuleSetProvider`
- `PersonRuleSetSupplier` (selects provider by `country`)

This pattern keeps decision logic modular and easy to extend.

---

## Build and test

From the repository root:

```bash
mvn test
```

Modules:
- `api` – core contracts and DSL
- `domain` – concrete rules and providers
- `config` – configuration module
- `rest` – integration module dependencies

---

## Typical extension workflow

1. Add a new `Rule<T>` implementation.
2. Register it inside a `ruleSet { ... }` or `RuleSetProvider`.
3. (Optional) Expose it in `JsonRuleSetDSL` via `ruleFactories`.
4. Add tests for positive and negative cases.

---

## Why this project is useful

This engine is intentionally simple: no custom rule language, no heavy runtime, just Kotlin objects and predictable evaluation semantics.

That makes it a strong fit for:
- onboarding checks,
- eligibility/entitlement decisions,
- fraud signal pre-filtering,
- campaign targeting logic,
- country- or tenant-specific rule packs.

If you want fast, understandable business rules in Kotlin, this gives you a clean starting point.
