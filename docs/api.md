# API

## Source

City of Toronto Open Data CKAN API.

- CKAN API guide: https://docs.ckan.org/en/latest/api/
- Evaluation dataset: https://open.toronto.ca/dataset/apartment-building-evaluation/
- Registration dataset: https://open.toronto.ca/dataset/apartment-building-registration/

## Resources

- Apartment Building Evaluation, 2023-current: `244f7a02-da5c-425b-b55f-fbdd133dd732`, 7,158 records, refreshed 2026-11-19.
- Apartment Building Registration: `3ad76a8c-0518-4df2-b94e-8c747d62f8c1`, 3,611 records, refreshed 2026-07-05.

## Evaluation endpoint

The current client uses `datastore_search` on the evaluation resource. It searches by address, ward, or building ID and caches the response locally.

Example request:

```
GET https://ckan0.cf.opendata.inter.prod-toronto.ca/api/3/action/datastore_search?resource_id=244f7a02-da5c-425b-b55f-fbdd133dd732&q=BALLIOL&limit=2
```

Observed result:

- `success: true`
- `total: 20`
- Records include `RSN`, `SITE ADDRESS`, `WARD`, `WARDNAME`, `YEAR EVALUATED`, `CURRENT BUILDING EVAL SCORE`, and the category fields.

Field mapping:

| CKAN field | Project field |
| --- | --- |
| `RSN` | `Building.rsn` |
| `SITE ADDRESS` | `Building.address` |
| `WARD` | `Building.ward` |
| `WARDNAME` | `Building.wardName` |
| `YEAR BUILT` | `Building.yearBuilt` |
| `CONFIRMED STOREYS` | `Building.storeys` |
| `CONFIRMED UNITS` | `Building.units` |
| `PROPERTY TYPE` | `Building.propertyType` |
| `LATITUDE` | `Building.latitude` |
| `LONGITUDE` | `Building.longitude` |
| `YEAR EVALUATED` | `Evaluation.year` |
| `EVALUATION COMPLETED ON` | `Evaluation.completedOn` |
| `CURRENT BUILDING EVAL SCORE` | `Evaluation.currentScore` |
| `PROACTIVE BUILDING SCORE` | `Evaluation.proactiveScore` |
| `CURRENT REACTIVE SCORE` | `Evaluation.reactiveScore` |

Each category column becomes an `EvaluationItem`. The value is 0 to 3, or `N/A` when the category was not evaluated.

## Registration endpoint

The client does not call the registration resource live yet. The bundled fixture was built from:

```
GET https://ckan0.cf.opendata.inter.prod-toronto.ca/api/3/action/datastore_search?resource_id=3ad76a8c-0518-4df2-b94e-8c747d62f8c1&q=BALLIOL&limit=20
```

Observed result:

- `success: true`
- `total: 10`
- Records include `RSN`, `SITE_ADDRESS`, and `PROP_MANAGEMENT_COMPANY_NAME`.
- The query returns the same buildings as the evaluation cache, including RSN `4153770` and `4244920`.

A live join between evaluation and registration data is planned.

## Endpoints not used

`package_search`, `package_show`, `datastore/dump`, and CKAN field metadata are available on this instance, but the current client does not call them. `datastore_search_sql` returns 404 on this instance, so aggregation and joining are done locally.

## Notes

- The API is keyless.
- The public dataset page may render the dataset as "Retired," but the CKAN API reports `is_retired: false`, `state: active`, and `last_refreshed: 2026-11-19`. The API metadata is the source of truth for this project.
- The bundled cache contains 10 BALLIOL buildings for development and demonstration. It is not the full dataset.
- Evaluation data may be incomplete, delayed, or revised. The 2023 evaluation process changed the categories and weights, so pre-2023 data is not directly comparable.
