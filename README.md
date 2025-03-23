
## **README for Container 1 Repository**

# **Container 1 - Kubernetes Assignment**

This repository contains the code and configuration for **Container 1**, which is part of the Kubernetes Assignment for **CSCI 5409 - Advanced Topics in Cloud Computing**. Container 1 is a microservice that stores files in a persistent volume and interacts with **Container 2** to calculate the total of a product.

---

### **Table of Contents**
1. [What is Container 1?](#what-is-container-1)
2. [How Does It Work?](#how-does-it-work)
3. [Technologies Used](#technologies-used)
4. [Setup and Deployment](#setup-and-deployment)
5. [API Endpoints](#api-endpoints)
6. [Directory Structure](#directory-structure)
7. [CI/CD Pipeline](#cicd-pipeline)
8. [Important Notes](#important-notes)
9. [Contributing](#contributing)
10. [License](#license)

---

### **What is Container 1?**
Container 1 is a **Spring Boot microservice** that:
1. Accepts a file and stores it in a **persistent volume**.
2. Validates the input and interacts with **Container 2** to calculate the total of a product.
3. Returns the result to the user.

---

### **How Does It Work?**
1. **File Storage:**
   - Container 1 receives a file via the `/store-file` API and stores it in a persistent volume.
2. **Product Calculation:**
   - Container 1 sends the file and product name to **Container 2** via the `/calculate` API.
   - Container 2 calculates the total and returns the result to Container 1.
3. **Response:**
   - Container 1 returns the result to the user.

---

### **Technologies Used**
- **Programming Language:** Java (Spring Boot)
- **Containerization:** Docker
- **Orchestration:** Kubernetes (GKE)
- **CI/CD:** Google Cloud Build
- **Persistent Storage:** PersistentVolume (PV) and PersistentVolumeClaim (PVC)
- **API Testing:** Postman/cURL

---

### **Setup and Deployment**

#### **1. Prerequisites**
Before you begin, ensure you have the following:
- A **Google Cloud Platform (GCP)** account.
- **Google Cloud SDK** (`gcloud`) installed.
- **Terraform** installed (for creating the GKE cluster).
- **Docker** installed.

#### **2. Clone the Repository**
```bash
git clone https://github.com/<your-username>/container1.git
cd container1
```

#### **3. Build the Application**
Build the Spring Boot application using Maven:
```bash
mvn clean package
```

#### **4. Build and Push Docker Image to Google Artifact Registry**
Build the Docker image and push it to the **Google Artifact Registry**:
```bash
docker build -t us-central1-docker.pkg.dev/<project-id>/k8s-container1/container1:latest .
docker push us-central1-docker.pkg.dev/<project-id>/k8s-container1/container1:latest
```

#### **5. Deploy to GKE**
Apply the Kubernetes manifests to deploy the application:
```bash
kubectl apply -f pv.yaml
kubectl apply -f pvc.yaml
kubectl apply -f deployment1.yaml
kubectl apply -f service1.yaml
```

---

### **API Endpoints**

#### **1. Store File**
- **Endpoint:** `POST /store-file`
- **Request:**
  ```json
  {
    "file": "file.dat",
    "data": "product, amount \nwheat, 10\nwheat, 20\noats, 5"
  }
  ```
- **Response:**
  ```json
  {
    "file": "file.dat",
    "message": "Success."
  }
  ```

#### **2. Calculate Product Total**
- **Endpoint:** `POST /calculate`
- **Request:**
  ```json
  {
    "file": "file.dat",
    "product": "wheat"
  }
  ```
- **Response:**
  ```json
  {
    "file": "file.dat",
    "sum": 30
  }
  ```

---

### **Directory Structure**
```
container1/
├── src/                     # Source code for the Spring Boot application
├── target/                  # Compiled JAR file
├── Dockerfile               # Dockerfile for building the container image
├── pv.yaml                  # PersistentVolume manifest
├── pvc.yaml                 # PersistentVolumeClaim manifest
├── deployment1.yaml         # Kubernetes Deployment manifest
├── service1.yaml            # Kubernetes Service manifest
├── README.md                # This file
└── ...                      # Other configuration files
```

---

### **CI/CD Pipeline**
The CI/CD pipeline is built using **Google Cloud Build**. It performs the following steps:
1. Builds the JAR file using Maven.
2. Builds and pushes the Docker image to the **Google Artifact Registry**.
3. Deploys the application to GKE.

---

### **Important Notes**
- **Container 1 depends on Container 2:** Container 1 interacts with **Container 2** to calculate the total of a product. Both services must be deployed and running for the system to work.
- **Persistent Volume:** The persistent volume is mounted at `/SakthiSharan_PV_dir` in the container.
- **Service IP Address:** After deploying the service, get the external IP using:
  ```bash
  kubectl get services
  ```
  Use this IP to access the API endpoints.
- **Image Pull Policy:** The deployment file is configured to pull the image from the **Google Artifact Registry**. Ensure the image is pushed to the registry before deploying.

---
