provider "google" {
  project = "csci5409-454408"
  region  = "us-central1"
}

# Create the GKE cluster
resource "google_container_cluster" "primary" {
  name     = "k8s-cluster"
  location = "us-central1-c"  # Zone for the GKE cluster

  remove_default_node_pool = true
  initial_node_count       = 1

  network    = "default"
  subnetwork = "default"
}

# Create the node pool for the GKE cluster
resource "google_container_node_pool" "primary_nodes" {
  name       = "gke-node-pool"
  location   = "us-central1-c"  # Zone for the node pool
  cluster    = google_container_cluster.primary.name
  node_count = 1

  node_config {
    preemptible  = false
    machine_type = "e2-medium"
    disk_size_gb = 10
    disk_type    = "pd-standard"
    image_type   = "COS_CONTAINERD"
  }
}

# Create the shared disk
resource "google_compute_disk" "shared_disk" {
  name  = "shared-disk"
  type  = "pd-standard"
  zone  = "us-central1-c"  # Zone for the persistent disk
  size  = 1
}

# Create the first Artifact Registry repository (k8s-container1)
resource "google_artifact_registry_repository" "k8s_container1" {
  location      = "us-central1"
  repository_id = "k8s-container1"
  description   = "Docker repository for k8s-container1"
  format        = "DOCKER"
}

# Create the second Artifact Registry repository (k8s-container2)
resource "google_artifact_registry_repository" "k8s_container2" {
  location      = "us-central1"
  repository_id = "k8s-container2"
  description   = "Docker repository for k8s-container2"
  format        = "DOCKER"
}

# Outputs
output "gke_cluster_name" {
  value = google_container_cluster.primary.name
}

output "gke_cluster_endpoint" {
  value = google_container_cluster.primary.endpoint
}

output "shared_disk_name" {
  value = google_compute_disk.shared_disk.name
}

# Outputs for Artifact Registry repositories
output "k8s_container1_repository_name" {
  value = google_artifact_registry_repository.k8s_container1.name
}

output "k8s_container2_repository_name" {
  value = google_artifact_registry_repository.k8s_container2.name
}