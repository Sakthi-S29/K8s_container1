provider "google" {
  project = "csci5409-454408"
  region  = "us-central1"
}

resource "google_container_cluster" "gke_cluster" {
  name               = "k8s-cluster"
  location           = "us-central1-c"
  remove_default_node_pool = true
  deletion_protection = false
}

resource "google_container_node_pool" "default_pool" {
  name       = "default-pool"
  location   = "us-central1-c"
  cluster    = google_container_cluster.gke_cluster.name
  node_count = 1

  node_config {
    machine_type = "e2-medium"
    disk_size_gb = 10
    disk_type    = "pd-standard"
    image_type   = "cos_containerd"
  }

  depends_on = [google_container_cluster.gke_cluster]
}

resource "google_compute_disk" "shared-disk" {
  name = "shared-disk"
  type = "pd-standard"
  zone = "us-central1-c"
  size = 1
}