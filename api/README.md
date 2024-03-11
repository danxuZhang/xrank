Build the docker image
```
docker build -t caller-function .
```

Test docker image locally
```
docker run caller-function python caller.py "tesla"
```

Push to remote repo
```
docker tag caller-function char-1ee/caller-function:latest
docker push char-1ee/caller-function:latest
```

Deploy the job
```
kubectl apply -f caller-job.yaml
```

Run the init.py