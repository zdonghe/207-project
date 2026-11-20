# RentSafeTO Explorer

## Overview

We are building RentSafeTO Explorer for renters in Toronto who want to check a building before signing a lease. The City publishes RentSafeTO registration and evaluation data, but it is split across datasets and hard to search. Students and first-time renters are the most likely to miss it before signing a lease. Our app puts it in one place.

RentSafeTO Explorer lets a tenant search by address, ward, or building ID; view evaluation scores, deficiencies, and history; compare two buildings; and save a favourites list or export a report. The project domain is civic technology, housing, and open-data analysis.

The project is a Java 11 Swing desktop application built with Clean Architecture. It will read the City of Toronto Open Data CKAN API and keep a local cache, so it can still show data when the network is unavailable. It is an independent student project, not affiliated with the City of Toronto.

## Quick start

You need Java 11 and Maven.

```bash
mvn clean test
mvn package
```

Checkstyle runs during `validate`, and the JUnit 5 tests run under Surefire. There is no `Main` class on `main` yet, so `java -jar` does not work. The runnable jar comes with the search use case.

## Team

- Dong He
- Nicholas Cheng
- Belina Zhu
- Sophie Pigott

## User stories

| #   | Story                                                                                                                                                                  | Owner          |
| --- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------- |
| 1   | As a prospective tenant, I want to search for a rental building by address, ward, or building ID so that I can check its evaluation history before booking a viewing.  | Dong He        |
| 2   | As a prospective tenant, I want to view a building's evaluation scores, deficiencies, and score history so that I can understand its condition and enforcement record. | Belina Zhu     |
| 3   | As a prospective tenant, I want to compare two buildings side by side, including their scores and ward benchmark, so that I can choose between them.                   | Nicholas Cheng |
| 4   | As a prospective tenant, I want to save buildings to a favourites list and export a report so that I can revisit my options and share them with roommates.             | Sophie Pigott  |

## MVP

| Use case                            | Owner          |
| ----------------------------------- | -------------- |
| Search for a rental building        | Dong He        |
| View building evaluation history    | Belina Zhu     |
| Compare two buildings               | Nicholas Cheng |
| Save favourites and export a report | Sophie Pigott  |

## Use cases

### Use Case 1: Search for a Rental Building

| Field            | Value                                                                                                              |
| ---------------- | ------------------------------------------------------------------------------------------------------------------ |
| Primary actor    | Prospective tenant                                                                                                 |
| Supporting actor | City of Toronto Open Data service, when live data is used                                                          |
| Preconditions    | The application is running. Building data is available from the live service or the local cache.                   |
| Trigger          | The tenant enters a search term and asks to search.                                                                |
| Success outcome  | A ranked list of matching buildings is displayed with address, ward, latest evaluation score, and evaluation year. |

**Main success scenario**

1. The tenant enters an address, ward, or building ID.
2. The system checks that the request is valid.
3. The system finds matching buildings from the live service or the local cache.
4. The system combines registration and evaluation information.
5. The system keeps the most recent evaluation for each building.
6. The system ranks the results and displays them.

<details>
<summary>Extensions</summary>

- **2a. The search request is empty or invalid.** The system explains what is needed and does not search.
- **3a. The live service is unavailable.** The system uses cached data and tells the tenant the results may be out of date.
- **3b. No buildings match.** The system says so and suggests removing filters or broadening the search.
- **4a. Some registration or evaluation records are missing.** The system still shows the building and marks the missing information.

</details>

### Use Case 2: View Building Evaluation History

| Field           | Value                                                                                                                                  |
| --------------- | -------------------------------------------------------------------------------------------------------------------------------------- |
| Primary actor   | Prospective tenant                                                                                                                     |
| Preconditions   | The tenant has selected a building from the search results or favourites.                                                              |
| Trigger         | The tenant opens the building's detail view.                                                                                           |
| Success outcome | The building profile, latest scores, history, category results, deficiencies, and changes since the previous evaluation are displayed. |

**Main success scenario**

1. The tenant selects a building.
2. The system retrieves its registration and evaluation records.
3. The system orders the evaluations from oldest to newest.
4. The system identifies the latest evaluation and any changes since the previous one.
5. The system displays the profile, score history, category breakdown, deficiencies, and changes.

<details>
<summary>Extensions</summary>

- **2a. No registration record exists.** The system shows the evaluation data and marks the registration as missing.
- **2b. No evaluation records exist.** The system shows the registration data and explains that no evaluations are available.
- **3a. Some scores are not applicable.** The system shows them as not applicable and leaves them out of comparisons.
- **4a. There is only one evaluation.** The system hides the change comparison.

</details>

### Use Case 3: Compare Two Buildings

| Field           | Value                                                                                                                                 |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| Primary actor   | Prospective tenant                                                                                                                    |
| Preconditions   | The tenant has selected two buildings.                                                                                                |
| Trigger         | The tenant asks to compare the selected buildings.                                                                                    |
| Success outcome | A side-by-side comparison of the two buildings is displayed, including scores, differences, a comparison chart, and a ward benchmark. |

**Main success scenario**

1. The tenant selects two buildings.
2. The system retrieves both evaluation histories.
3. The system aligns evaluations by year and calculates differences.
4. The system calculates a ward benchmark.
5. The system displays the comparison table, chart, differences, benchmark, and summary.

<details>
<summary>Extensions</summary>

- **2a. One building has no evaluation data.** The system marks it as having insufficient data and compares what is available.
- **3a. The evaluation years do not overlap.** The system compares the closest available years and labels the mismatch.
- **4a. The ward has too few evaluated buildings.** The system hides the benchmark and explains why.

</details>

### Use Case 4: Save Favourites and Export a Report

| Field           | Value                                                                                |
| --------------- | ------------------------------------------------------------------------------------ |
| Primary actor   | Prospective tenant                                                                   |
| Preconditions   | At least one building is selected.                                                   |
| Trigger         | The tenant asks to save a favourite or export a report.                              |
| Success outcome | The favourite or report is saved and can be reopened after the application restarts. |

**Main success scenario**

1. The tenant selects one or more buildings.
2. The tenant chooses to save a favourite or export a report.
3. The system checks the selection and report options.
4. The system saves the favourite or the report.
5. The system confirms the result and shows where the report was saved.

<details>
<summary>Extensions</summary>

- **2a. No buildings are selected.** The system explains that a selection is required and does not save.
- **4a. The file cannot be written.** The system explains the problem and keeps the selection so the tenant can try again.
- **4b. A report with the same name exists.** The system asks whether to overwrite it or choose a new name.

</details>

## Entities

The Entity layer contains the program's model and enterprise rules. It has no dependency on Swing, data access, or the CKAN API.

| Entity             | Instance variables                                                                                                                                 | Relevant methods                                                                                                  |
| ------------------ | -------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------- |
| `Building`         | `rsn`, `address`, `ward`, `wardName`, `yearBuilt`, `storeys`, `units`, `propertyType`, `managementCompany`, `latitude`, `longitude`, `evaluations` | `addEvaluation`, `getLatestEvaluation`, `getEvaluationHistory`, `getAddressKey`, `isRegistered`, `getDisplayName` |
| `Evaluation`       | `rsn`, `year`, `completedOn`, `currentScore`, `proactiveScore`, `reactiveScore`, `items`                                                           | `getDeficiencies`, `getItem`, `getItemsInCategory`, `isImprovementOver`, `getChangedItems`, `getSummary`          |
| `EvaluationItem`   | `name`, `category`, `score`, `weight`, `description`                                                                                               | `isApplicable`, `isDeficiency`, `getSeverity`, `getNormalizedScore`                                               |
| `EvaluationChange` | `itemName`, `previousScore`, `currentScore`                                                                                                        | `getDelta`, `isImprovement`, `getDescription`                                                                     |
| `Severity`         | Enum constants: `UNKNOWN`, `LOW`, `MEDIUM`, `HIGH`                                                                                                 | `values`, `valueOf`                                                                                               |

Relationships: a `Building` has zero or more `Evaluation` objects. An `Evaluation` contains zero or more `EvaluationItem` objects. `EvaluationChange` records the movement of one item between two evaluations.

Planned entities, not yet implemented: `Score`, `ScoreType`, `RiskLevel`, `Favourite`, and `Report`. The current `Evaluation` stores scores as `Integer` fields rather than `Score` objects.

A UML class diagram is in [`docs/design.md`](docs/design.md).

## API

**API:** City of Toronto Open Data CKAN API.

**Documentation:**

- CKAN API guide: https://docs.ckan.org/en/latest/api/
- Evaluation dataset: https://open.toronto.ca/dataset/apartment-building-evaluation/
- Registration dataset: https://open.toronto.ca/dataset/apartment-building-registration/

**Resources:**

- Apartment Building Evaluation, 2023-current: `244f7a02-da5c-425b-b55f-fbdd133dd732`
- Apartment Building Registration: `3ad76a8c-0518-4df2-b94e-8c747d62f8c1`

**How we will use it:** The data access layer will call `datastore_search` on the evaluation resource to search by address, ward, or building ID, and cache responses locally. We verified the evaluation and registration queries manually.

**Successful call:** Yes. The evaluation query was verified on 2026-11-20 and reports a total of 20 records for 10 buildings. The registration query was also verified manually and returns matching BALLIOL records.

Example request:

```
GET https://ckan0.cf.opendata.inter.prod-toronto.ca/api/3/action/datastore_search?resource_id=244f7a02-da5c-425b-b55f-fbdd133dd732&q=BALLIOL&limit=2
```

Field mapping, observed responses, and registration details are in [`docs/api.md`](docs/api.md).

## Data and licence

This project uses City of Toronto open data under the Open Government Licence - Toronto. RentSafeTO Explorer is an independent student project, not affiliated with or endorsed by the City of Toronto. It is for information only and is not legal, rental, or safety advice.
