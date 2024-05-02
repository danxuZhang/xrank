Build the docker image
```bash
docker build -t caller-function .
```

Test docker image locally
```bash
docker run caller-function python caller.py "tesla"
```

Push to remote repo
```bash
docker tag caller-function char-1ee/caller-function:latest
docker push char-1ee/caller-function:latest
```

Deploy the job
```bash
kubectl apply -f caller-job.yaml
```

Config the k8s client
```bash
python k8s_init.py
```

Start a local server 
```
FLASK_APP=server.py flask run
```
After that can trigger k8s job creation by sending POST request to http://127.0.0.1:5000/create-job` with JSON body
```json
{
  "keyword": "tesla"
}
```

## TODO
- [ ] Test k8s
- [ ] Integration with kafka
- [ ] Monitoring and Logging
