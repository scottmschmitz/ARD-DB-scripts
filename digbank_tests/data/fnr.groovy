import groovy.json.JsonSlurper

def fnr() {

def modelKeyId=''
def jobId=''
def status=''
def reservationId=''
def execId=''
def token = login()
echo 'Token: ' +token

syncData(2383, 2384, 212, token)
sleep(2)
modelKeyId = findData(2383, 2384, 212, 206, token)
echo 'modelKeyId returned: '+modelKeyId

if (modelKeyId == 0) {
// no record found, must Generate new record
   jobId = publishData(2383, 2384, 2398, token)     
// loop checking status of jobId until Complete

  while (status != 'Completed') {
    // Check job status
    sleep(2)
    status=CheckStatus(jobid)
    }
  // generate complete, pre-fetch data into Find & Reserve cache
  syncData(2383, 2384, 212, token)
  sleep(2)
  // generate complete, find Data again
  modelKeyId = findData(2383, 2384, 212, 206, token)
   echo modelKeyId
}
// reserve the data
reservationId = reserveData(2383, 2384, 212, 206, modelKeyId, token)
echo 'reservationId returned: '+reservationId
// intermittent 404 error if no delay
sleep(2)
// get the login (email address) to return
execId = fetchData(2383, 2384, token, reservationId)

return execId
}//end fnr

return this


def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              token = body['token']
              return token
    } //end login
def findData(projectId, versionId, modelId,  environmentId, token){
    def request = '{"environmentId":"'+environmentId+'","filters":[{"attributeName":"id","entityName":"user_profile","schema":"dbo","dataSource":"SDS","operator":"GREATER_THAN_OR_EQUAL_TO","values":["1000"]}]}'           
    echo request
def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' +token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
                requestBody: request,
              url: 'https://scotts-tdm-serv:8443/TDMFindReserveService/api/ca/v1/testDataModels/'+ modelId +'/actions/find?projectId='+ projectId +'&versionId='+ versionId
			  echo response.content
              def json = new JsonSlurper().parseText(response.content)
              modelKeyId = json.records.modelKeys[0].id
              echo 'mymodelKeyId: '+modelKeyId
              return modelKeyId
    }//end findData
def publishData(projectId, versionId, generatorId, token){
    def request = '{"name":"Generate new records","description":"Publish using api","projectId":'+projectId+',"versionId":'+versionId+',"type":"PUBLISHJOB","origin":"generation","jobs":[],"parameters":{"generatorId":'+generatorId+',"jobType":"PUBLISH","title":"Publish using Jenkins","publishTo":"TGT","target":"dbo","dataTargetProfile":"SDS","repeatCount":1}}'

    def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs'
    def body = readJSON file: '', text: response.content
    def jobId = body['jobId']
    return jobId
}//end publishData
def CheckStatus(jobId)
{
 def request='{}'
 def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',
    requestBody: request, url: 'https://scotts-tdm-serv:8443/TDMJobService/api/ca/v1/jobs/'+jobId
    def body = readJSON file: '', text: response.content
    def status = body['status']
    return status
}//end CheckStatus
def reserveData(projectId, versionId, modelId, environmentId, modelKeyId, token){
	def request = '{"reservationName":"EphemeralReservation","dataModelId":'+modelId+',"environmentId":'+environmentId+',"resources":[{"dataModelId":'+modelId+',"modelKeys":{"id":"'+modelKeyId+'"}}]}'
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer ' +token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: request, 
url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/reservations?projectId='+projectId+'&versionId='+ versionId
                def body = readJSON file: '', text: response.content
                reservationId = body['reservationId']
                echo 'reservationId:  '+reservationId
	return reservationId
}//end reserveData
def fetchData(projectId, versionId, reservationId, token){
	def request = '{"page":1,"size":100,"attributes":[{"attributeName":"id","entityName":"user_profile","schema":"dbo","dataSource":"SDS"}]}'
                def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'GET', responseHandle: 'LEAVE_OPEN',requestBody: request,
    url: 'https://scotts-tdm-serv:8443/TDMFindReserveService/api/ca/v1/reservations/'+reservationId+'/reservedData/actions/fetch?projectId='+projectId+'&versionId='+ versionId
// extract emailID attribute from response  
  def json = new JsonSlurper().parseText(response.content)
  execId = json.records.attributes[0].value[0]
  echo 'execId: '+execId
	return execId
}//end fetchData
def syncData(projectId, versionId, modelId, token){
	def request = '{}'
	def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Bearer '+token]],contentType: 'APPLICATION_JSON', httpMode: 'POST', responseHandle: 'LEAVE_OPEN',requestBody: request,
    url: 'https://scotts-tdm-serv:8443/TDMDataReservationService/api/ca/v1/testDataModels/'+modelId+'/startSync?projectId='+projectId+'&versionId='+ versionId
//	https://localhost:8443/TDMDataReservationService/api/ca/v1/testDataModels/212/syncTasks/actions/startSync?projectId=2383&versionId=2384
	}
