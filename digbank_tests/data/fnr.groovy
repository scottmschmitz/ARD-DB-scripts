def fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveDatadef fnr = {

def token = login()
echo token

def modelKeyId = findData(2387, 2388, 212, DigitalBank)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   def jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete
  def status = FALSE
  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  def modelKeyId = findData(2387, 2388, 212, DigitalBank)
   echo modelKeyId
}
// reserve the data
def reservationId = reserveData(2387, 2388, 212, DigitalBank, modelKeyId)

// get the login (email address) to return
def execId = fetchData(2387, 2388, reservationId)

return execId
}

def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              echo body['token']
              def timestamp = EXECUTION_TIME.getTime()
              echo timestamp.toString()
              token = body['token']
              return token
    } //end logindef findData(String projectId, String versionId, String modelId, String environmentId){
      stage('find data'){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + name +''',"entityName":"account","schema":"dbo","dataSource":"SDS","operator":"IN_VALUES","values":["Family Checking"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// To Do: parse for modelKeyId
	return modelKeyId
      }
    }//end findDatadef publishData(String projectId, String versionId, String generatorId){
    def request = '''{
      "name":"Generate new records",
      "description":"Publish using api",
      "projectId":''' + 2387 + ''',
      "versionId":''' + 2388 + ''',
      "type":"PUBLISHJOB",
      "origin":"generation",
      "scheduledTime":''' + System.currentTimeMillis() + ''',
      "jobs":[],
      "parameters":{
      "generatorId":''' + generatorId + ''',
      "jobType":"PUBLISH",
      "title":"Publish using Jenkins",
      "publishTo":"TGT",
      "target":"dbo",
      "dataTargetProfile":"SDS",
      "repeatCount":1
      }
     }'''

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishDatadef CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
                //def authorization = 'Bearer '+token
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: '''{
      "dataModelId": '''+ modelId +''',
      "environmentId": '''+ environmentId +''',
{"reservationName":"User with Family Checking","dataModelId":'''+modelId +''',"environmentId":'''+environmentId +''',"resources":[{"dataModelId":'''+ modelId +''',"modelKeys":{"id":'''+modelKeyId+'''}}]
    }''', url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo response.content + ' ' + reservationId
	return reservationId
        }
}//end reserveData