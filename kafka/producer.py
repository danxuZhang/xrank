import time
from kafka import KafkaProducer
import json

producer = KafkaProducer(bootstrap_servers=['localhost:9093'],
                         value_serializer=lambda v: json.dumps(v).encode('utf-8'))

cnt = 0
while True:
    if cnt % 2 == 0:
        producer.send('nvidia', {'content': f"nvidia content{cnt // 2}"})
    else:
        producer.send('tesla', {'content': f"tesla content{cnt // 2}"})
    producer.flush()
    print(f"Messages sent successfully @ timestamp {cnt}")
    cnt += 1;
    time.sleep(1)

