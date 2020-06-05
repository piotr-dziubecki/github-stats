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
def userRepos = []
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
pageCount = "";
pattern = ~/page=(\d+)/

def getData(HTTPBuilder http, String url, onlyHeaders = false) {

    def ur
    http.request( url, GET, JSON ) { req ->

    headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"
    headers.'Authorization' = "token $authToken"
    headers.Accept = 'application/json'

    response.success = { resp, json ->
        if( onlyHeaders == false ){
             ur = json
             rateLimitCount = resp.headers['X-RateLimit-Remaining'].value
        } else {
            linkHeader = resp.headers['Link']

            if( linkHeader != null){
                def match = linkHeader.value =~ pattern
                ur = match[1][1]
            } else {
                ur = 1;
            }
        }
    }
 
   response.failure = { resp ->
        println "Something went wrong:\n"
        println resp.getAllHeaders()
        System.exit(1)
   }
}

return ur

}

repoURL = 'http://api.github.com/users/' + userName + '/repos'
println "Querying: " + repoURL + "\n"



pageCount = getData(http, repoURL, true)

userRepos.add(getData(http, repoURL) )
for (int i = 2; i <= pageCount; ++i){

    repoURL = 'http://api.github.com/users/' + userName + '/repos?page=' + i
    userRepos.add(getData(http, repoURL))
}

saveDate = new Date().format( 'yyyy-MM-dd' )

fRepoOut = new File("data/" + saveDate + "-" + userName + "-repos.csv")
fRepoOut.write("date, repo, language, contributors, forks, stars\n")

fOut = new File("data/" + saveDate + "-" + userName + "-contributors.csv")
fOut.write("login, html-link\n")

repoCounter = 0
userRepos.each { repoEntry -> 
    
    repoEntry.each { repo ->
        String repoName =  repo.name

        def contributors = getData(http, 'https://api.github.com/repos/' + userName + '/' + repoName + '/contributors')

        int countContr = 0;
            contributors.each { contr -> 
                loginMap.put(contr.login, contr.html_url)
                countContr++;
            }

        println "Repo: " + repoName + " contributors: " + countContr
        fRepoOut.append(saveDate + ',' + repoName + ',' + repo.language + ',' + countContr + ',' + repo.forks_count + ',' + repo.stargazers_count + "\n")
        repoCounter++;
    }
}

println "\nFor " + userName + " " + repoCounter + " repositories queried"



loginMap.each{
    fOut.append(it.key + ',' + it.value + "\n")
}

println "\nData succesfully exported to:\n"
println "data/" + saveDate + "-" + userName + "-repos.csv file"
println "data/" + saveDate + "-" + userName + "-contributors.csv file"
println "\nYou have " + rateLimitCount + " free requests left for next 60 minutes"
