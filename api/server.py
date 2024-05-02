from flask import Flask, request, jsonify
from kubernetes import client, config
from uuid import uuid4

app = Flask(__name__)

# Load kube config from default location
config.load_kube_config()


def create_kubernetes_job(keyword):
    job_name = f"caller-job-{keyword}-{uuid4()}"
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
            ),
            backoffLimit=4
        )
    )
    batch_v1 = client.BatchV1Api()
    batch_v1.create_namespaced_job(body=job, namespace="default")
    return {"message": "Job created successfully", "job_name": job_name}


@app.route('/create-job', methods=['POST'])
def handle_create_job():
    data = request.json
    keyword = data.get('keyword')
    if not keyword:
        return jsonify({"error": "Keyword is required"}), 400
    
    try:
        response = create_kubernetes_job(keyword)
        return jsonify(response), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500


if __name__ == '__main__':
    app.run(debug=True)
