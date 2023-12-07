#!/bin/bash

# helm install default bitnami/postgresql --set persistence.existingClaim=postgres-pv-claim --set volumePermissions.enabled=true

helm install default -f values.yml bitnami/postgresql
