import jenkins.model.*
jenkins = Jenkins.instance
def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end login