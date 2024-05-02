from kubernetes import client, config
from uuid import uuid4

# Load kube config from default location
config.load_kube_config()

# Configure the keyword and dynamically set the job name
keyword = "tesla"
job_name = f"caller-job-{keyword}-{uuid4()}"

# Define the job
job = client.V1Job(
    api_version="batch/v1",
    kind="Job",
    metadata=client.V1ObjectMeta(name=job_name),
    spec=client.V1JobSpec(
        template=client.V1PodTemplateSpec(
            spec=client.V1PodSpec(
                containers=[client.V1Container(
                    name="caller",
                    image="char-1ee/caller-function:latest",
                    args=["python", "caller.py", keyword]
                )],
                restartPolicy="Never",
            )
        )
    )
)

# Create the job
batch_v1 = client.BatchV1Api()
batch_v1.create_namespaced_job(body=job, namespace="default")
