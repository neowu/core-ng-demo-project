include(
    "demo-service-interface",
    "demo-service",
    "demo-service-db-migration",
    "demo-site",
    "demo-es-migration",
    "demo-mongo-migration",
    "kibana-generator",
    "benchmark"
)

include(
    "parent:test-client"
)

includeBuild("../core-ng-project")
