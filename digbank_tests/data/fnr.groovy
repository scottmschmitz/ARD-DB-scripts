import jenkins.model.*
jenkins = Jenkins.instance

def token = login()
echo token