def fnr = {

def modelKeyId=''
def jobId=''
def status=''
def reservationId=''
def execId=''
def token = login()
echo token

modelKeyId = findData(2387, 2388, 212, 206)
echo modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete

  while (status != 'Completed') {
    // Check job status
    sleep(500)
    status=CheckStatus(jobid)
    }
  // generate complete, find Data again
  modelKeyId = findData(2387, 2388, 212, 206)
   echo modelKeyId
}
// reserve the data
reservationId = reserveData(2387, 2388, 212, 206, modelKeyId)

// get the login (email address) to return
execId = fetchData(2387, 2388, reservationId)

return execId
}//end fnr
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
def findData(String projectId, String versionId, String modelId, String environmentId){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' ]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: '''{
                              "environmentId": '''+ environmentId +''',
 ,"filters":[{"attributeName":''' + id +''',"entityName":"user_profile","schema":"dbo","dataSource":"SDS","operator":"GREATER_THAN_OR_EQUAL_TO","values":["1000"]}],
                              "includeReservedRecords": false,
                              "startAfterValues": {}
                              }''',
              url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
              def body = readJSON file: '', text: response.content
              echo response.content
// parse for modelKeyId
	modelKeyId = body['id']
	return modelKeyId
    }//end findData
def publishData(String projectId, String versionId, String generatorId){
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
}//end publishData
def CheckStatus(String jobId)
{
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}//end CheckStatus
def reserveData(String projectId, String versionId, String modelId, String environmentId, String modelKeyId){
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
}//end reserveData
