@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.6')
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET


if (args.size() < 1) {
    println("Missing filename")
    System.exit(1)
}

def userName = args[0]
def http = new HTTPBuilder()
def userRepos
def loginMap = [:]


def Properties properties = new Properties()
File propertiesFile = new File('auth.properties')
propertiesFile.withInputStream {
    properties.load(it)
}


authToken = properties.token

if ( authToken == null || authToken == "" ){
    println("Provide valid token in auth.properties")
    System.exit(1)
}

rateLimitCount = 0;

def getData(HTTPBuilder http, String url) {

    def ur
    http.request( url, GET, JSON ) { req ->

    headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"
    headers.'Authorization' = "token $authToken"
    headers.Accept = 'application/json'

    response.success = { resp, json ->
     ur = json
     rateLimitCount = resp.headers['X-RateLimit-Remaining'].value
    }
 
   response.failure = { resp ->
        println resp.getAllHeaders()
   }
}

return ur

}

repoURL = 'http://api.github.com/users/' + userName + '/repos'
println "Querying: " + repoURL + "\n"

userRepos = getData(http, repoURL)

userRepos.any { repo ->
    String repoName =  repo.name

    def contributors = getData(http, 'https://api.github.com/repos/' + userName + '/' + repoName + '/contributors')

    int countContr = 0;
        contributors.each { contr -> 
            loginMap.put(contr.login, contr.html_url)
            countContr++;
        }

    println "Repo: " + repoName + " contributors: " + countContr
    
}

fOut = new File(userName + ".csv")
fOut.write("login, html-link\n")

loginMap.each{
    fOut.append(it.key + ',' + it.value + "\n")
}

println "\nData succesfully exported to: " + userName + ".csv file"
println "You have " + rateLimitCount + " free requests left for next 60 minutes"
