def fnr() {

//def modelKeyId=''
def jobId=''
def status=''
def reservationId=''
def execId=''
def token = login()
echo 'Token: ' +token

echo 'Call findData()'
def modelKeyId = findData()
echo 'modelKeyId: ' +modelKeyId

//if (modelKeyId == 0) {
// no record found, must Generate new record
//   jobId = publishData(2387, 2388, 2398)     
// loop checking status of jobID until Complete

//  while (status != 'Completed') {
    // Check job status
//    sleep(500)
//    status=CheckStatus(jobid)
//    }
  // generate complete, find Data again
//  modelKeyId = findData(2387, 2388, 212, 206)
//   echo modelKeyId
//}
// reserve the data
//reservationId = reserveData(2387, 2388, 212, 206, modelKeyId)

// get the login (email address) to return
//execId = fetchData(2387, 2388, reservationId)

execId = '9988'
return execId
}//end fnr

return this
 
def findData(){
 	modelKeyId = '5767'
	return modelKeyId
    }//end findData
    
def login(){
              def response = httpRequest customHeaders: [[maskValue: false, name: 'Authorization', value: 'Basic QWRtaW5pc3RyYXRvcjptYXJtaXRl']], httpMode: 'POST', outputFile: 'test.txt',
              url: 'https://scotts-tdm-serv:8443/TestDataManager/user/login'
              def body = readJSON file: '', text: response.content
              //echo body['token']
              //def timestamp = EXECUTION_TIME.getTime()
              //echo timestamp.toString()
              token = body['token']
              echo 'Within login() from fnr'
              return token
    } //end login