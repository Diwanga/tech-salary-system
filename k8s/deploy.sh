#!/bin/bash
set -e

echo "☸️  Deploying TechSalary to Minikube..."

# # ── 1. Point Docker to Minikube daemon
# echo "→ Pointing Docker to Minikube..."
# eval $(minikube docker-env)

# ── 2. Build image inside Minikube
echo "→ Building salary-service image..."
docker build -t techsalary/salary-service:latest ./services/salary-service

# ── 3. Apply namespaces first
echo "→ Creating namespaces..."
kubectl apply -f k8s/00-namespaces.yaml

# ── 4. Apply configs and secrets
echo "→ Applying ConfigMaps and Secrets..."
kubectl apply -f k8s/01-configmap.yaml
kubectl apply -f k8s/02-secret.yaml
kubectl apply -f k8s/03-postgres-secret.yaml

# ── 5. Deploy Postgres
echo "→ Deploying PostgreSQL..."
kubectl apply -f k8s/data/postgres-pvc.yaml
kubectl apply -f k8s/data/postgres-init-configmap.yaml
kubectl apply -f k8s/data/postgres-deployment.yaml

# ── 6. Wait for Postgres
echo "→ Waiting for Postgres to be ready..."
kubectl wait --for=condition=ready pod \
  -l app=postgres \
  -n data \
  --timeout=120s

# ── 7. Deploy app services
echo "→ Deploying salary-service..."
kubectl apply -f k8s/app/salary-service-deployment.yaml
## Here can add other services ##

# ── 8. Apply Ingress
echo "→ Applying Ingress..."
kubectl apply -f k8s/ingress.yaml

# ── 9. Wait for salary-service
echo "→ Waiting for salary-service to be ready..."
kubectl wait --for=condition=ready pod \
  -l app=salary-service \
  -n app \
  --timeout=120s

echo ""
echo "✅ Deployment complete!"
echo ""
echo "📋 Status:"
kubectl get pods -n data
kubectl get pods -n app
kubectl get ingress -n app
echo ""
echo ""
echo "▶️  Test:"
echo "   curl http://techsalary.local/api/v1/salaries"
