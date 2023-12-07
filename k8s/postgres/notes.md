https://phoenixnap.com/kb/postgresql-kubernetes
https://adamtheautomator.com/postgres-to-kubernetes/

helm uninstall default

kubectl delete pvc [pvc-name]

kubectl get services
kubectl get pods
kubectl get pvc
kubectl get pv
kubectl logs <pod-name>

kubectl delete <name.example:pvc> <name>

kubectl port-forward --namespace default svc/default-postgresql 5432:5432

kubectl logs default-postgresql-0
