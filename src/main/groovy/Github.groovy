import groovyx.net.http.RESTClient

class Github {

    private final token
    private final owner
    private final repo
    private rest

    Github(token, owner, repo) {
        this.token = token
        this.owner = owner
        this.repo = repo
        this.rest = new RESTClient("https://api.github.com/")
    }

    private def headers() {
        [
                "Authorization": "token $token",
                "User-Agent"   : owner
        ]
    }

    def log(base, head) {
        rest.get(
                path: "repos/${this.owner}/${this.repo}/compare/${base}...${head}",
                headers: headers()
        )
    }

}
