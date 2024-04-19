import azure.functions as func
import logging
import json
import os
import pyodbc
from datetime import datetime
import firebase_admin
from firebase_admin import credentials
from firebase_admin import messaging

key = os.getenv('FCMAccountKey')
key = json.loads(key)
cred = credentials.Certificate(key)
firebase_admin.initialize_app(cred)
app = func.FunctionApp(http_auth_level=func.AuthLevel.FUNCTION)

topics = {
    1: "Blockchain",
    2: "Semiconductor",
    3: "Biotechnology",
    4: "Artificial Intelligence",
    5: "Vehicle"
}

@app.route(route="UpdateUserTopic")
def UpdateUserTopic(req: func.HttpRequest) -> func.HttpResponse:
    logging.info('Update user topic triggered')

    try:
        reqBody = req.get_json()
        topics = reqBody['topics']
        email = reqBody["email"]
        logging.info('Receive user: ' + str(email) + ", topics: " + str(topics))
        connectString = "Driver={ODBC Driver 18 for SQL Server};Server=tcp:stockimpact.database.windows.net,1433;Database=StockImpact;Uid=paimon;Pwd=GenshinImpact,Start;Encrypt=yes;TrustServerCertificate=no;Connection Timeout=30;"
        with pyodbc.connect(connectString) as conn:
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM Topic WHERE email='" + email + "'")
            rows = cursor.fetchall()
            results = [dict(zip([column[0] for column in cursor.description], row)) for row in rows]
            logging.info(results)
            if (len(results) == 0):
                for topic in topics:
                    cursor.execute("INSERT INTO [dbo].[Topic] VALUES('" + email + "', " + str(topic) + ")")
                    logging.info(topic)
            else:
                #[{'ID': 1, 'Email': 'test@example.com', 'Topic': 1}]
                for row in results:
                    if row["Topic"] in topics:
                        topics.remove(row["Topic"])
                    else:
                        if len(topics) > 0:
                            cursor.execute("UPDATE [dbo].[Topic] SET Topic = " + str(topics[0]) + " WHERE ID = " + str(row["ID"]))
                            topics.remove(topics[0])
                        else:
                            cursor.execute("DELETE FROM [dbo].[Topic] WHERE ID = " + str(row["ID"]))
                for topic in topics:
                    cursor.execute("INSERT INTO [dbo].[Topic] VALUES('" + email + "', " + str(topic) + ")")
                    logging.info(topic)
            return func.HttpResponse("Update successfully", status_code=200)
    except Exception as e:
        return func.HttpResponse(str(e), status_code=500)
    

@app.route(route="GetUserTopic")
def GetUserTopic(req: func.HttpRequest) -> func.HttpResponse:
    logging.info('Get user topic triggered')    

    email = req.params.get('email')

    if not email:
        return func.HttpResponse(
             "Please pass an email on the query string",
             status_code=400
        )

    try:
        connectString = "Driver={ODBC Driver 18 for SQL Server};Server=tcp:stockimpact.database.windows.net,1433;Database=StockImpact;Uid=paimon;Pwd=GenshinImpact,Start;Encrypt=yes;TrustServerCertificate=no;Connection Timeout=30;"
        with pyodbc.connect(connectString) as conn:
            cursor = conn.cursor()
            query = "SELECT * FROM [dbo].[Topic] WHERE Email = '" + email+"'"
            cursor.execute(query)
            rows = cursor.fetchall()
            results = [dict(zip([column[0] for column in cursor.description], row)) for row in rows]
            data = json.dumps(results)

            return func.HttpResponse(data, mimetype="application/json", status_code=200)
    except Exception as e:
        return func.HttpResponse(f"Error: {str(e)}", status_code=500)
    

@app.route(route="UpdateHistory")
def UpdateHistory(req: func.HttpRequest) -> func.HttpResponse:
    logging.info('Update history triggered')
    try:
        reqBody = req.get_json()
        summary = reqBody['summary']
        time = reqBody['time']
        topic = reqBody["topic"]
        dateFormat = "%Y-%m-%d %H:%M"
        dateObject = datetime.strptime(time, dateFormat)
        logging.info('Receive topic: ' + str(topic) + ", summary: " + str(summary) + ", time: " + str(time))
        connectString = "Driver={ODBC Driver 18 for SQL Server};Server=tcp:stockimpact.database.windows.net,1433;Database=StockImpact;Uid=paimon;Pwd=GenshinImpact,Start;Encrypt=yes;TrustServerCertificate=no;Connection Timeout=30;"
        with pyodbc.connect(connectString) as conn:
            cursor = conn.cursor()
            cursor.execute("INSERT INTO [dbo].[History] VALUES(" + str(topic) + ", '" + str(dateObject.strftime("%Y-%m-%d %H:%M"))+ "', '" + str(summary) + "')")
            message = messaging.Message(
            notification=messaging.Notification(
                title='Latest news available!',
                body='Click to check news'
                ),
                data={
                    'topic': topic,
                },
                topic=topics[topic],
                android=messaging.AndroidConfig(
                    priority='high'
                )
            )
            try:
                response = messaging.send(message)
                print('Successfully sent message:', response)
            except firebase_admin.exceptions.FirebaseError as e:
                print('Failed to send message:', e)
            return func.HttpResponse("Update successfully", status_code=200)
    except Exception as e:
        return func.HttpResponse(str(e), status_code=500)
    

@app.route(route="GetHistory")
def GetHistory(req: func.HttpRequest) -> func.HttpResponse:
    logging.info('Get hisotry triggered')    

    topic = req.params.get('topic')

    if not topic:
        return func.HttpResponse(
             "Please pass a topic on the query string",
             status_code=400
        )

    try:
        connectString = "Driver={ODBC Driver 18 for SQL Server};Server=tcp:stockimpact.database.windows.net,1433;Database=StockImpact;Uid=paimon;Pwd=GenshinImpact,Start;Encrypt=yes;TrustServerCertificate=no;Connection Timeout=30;"
        with pyodbc.connect(connectString) as conn:
            cursor = conn.cursor()
            query = "SELECT * FROM [dbo].[History] WHERE Topic = " + str(topic)
            cursor.execute(query)
            rows = cursor.fetchall()
            results = [dict(zip([column[0] for column in cursor.description], row)) for row in rows]
            for row in results:
                row['Time'] = row['Time'].isoformat()
            data = json.dumps(results)

            return func.HttpResponse(data, mimetype="application/json", status_code=200)
    except Exception as e:
        return func.HttpResponse(f"Error: {str(e)}", status_code=500)
    

@app.route(route="Test")
def Test(req: func.HttpRequest) -> func.HttpResponse:
    
    message = messaging.Message(
        notification=messaging.Notification(
            title='New Article Available!',
            body='Click to read the latest news.'
        ),
        data={
            'topic': 'value1',
            'key2': 'value2'
        },
        topic='BitCoin',
        android=messaging.AndroidConfig(
            priority='high'
        )
    )
    try:
        response = messaging.send(message)
        print('Successfully sent message:', response)
    except firebase_admin.exceptions.FirebaseError as e:
        print('Failed to send message:', e)
    return func.HttpResponse("1", mimetype="application/json", status_code=200)