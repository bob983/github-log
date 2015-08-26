import groovyx.net.http.RESTClient

import java.time.ZonedDateTime

import static groovyx.net.http.ContentType.JSON

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
        def result = rest.get(
                path: "repos/${this.owner}/${this.repo}/compare/${base}...${head}",
                headers: headers()
        )

        def commits = result.data['commits']
        commits
                .findAll { it['parents'].size() == 1 } // remove PR/merge commits
                .collect(rawCommitToCardLogEntry)
                .groupBy { entry -> entry.card.board }
                .sort()
                .collect(print)
                .join("\n")

    }

    def getReleaseForTag(tag) {
        try {
            def result = rest.get(
                    path: "repos/${this.owner}/${this.repo}/releases/tags/${tag}",
                    headers: headers()
            )
            result.data
        } catch (ex) {
            if(ex.response.status == 404) {
                return null
            } else {
                throw new RuntimeException(ex.response.text)
            }
        }
    }

    def updateRelease(releaseId, text) {
        try {
            rest.patch(
                    path: "repos/${this.owner}/${this.repo}/releases/${releaseId}",
                    headers: headers(),
                    requestContentType: JSON,
                    body : [body: text]
            )
        } catch (ex) {
            ex.printStackTrace()
        }
    }

    private rawCommitToCardLogEntry = { row ->
        def commit = row['commit']
        def author = commit['author']
        def params = [
                authorEmail: author['email'],
                fullMessage: commit['message'],
                card       : Card.DUMMY,
                commitId   : row['sha'],
                commitDate : ZonedDateTime.parse(author['date'].toString())
        ]

        def res = params.fullMessage =~ /(?s)^([A-Za-z]+)[- :]?(\d+)(.*)/
        if (res.matches()) {
            def card = new Card(board: res[0][1].toUpperCase(), cardNumber: res[0][2])
            params['card'] = card;
        }
        new CardLogEntry(params)
    }


    private print = { val ->
        def board = val.key
        def cardEntries = val.value.sort()

        def result = ""
        if (board == Card.MISC) {
            result += "## Misc ##\n\n"
        } else {
            result += "## Board $board ##\n\n"
        }

        def cards = cardEntries.collect { entry ->
            "${entry.card.markdowned()} - [${entry.commitDate.toLocalDate()}] \"${entry.fullMessage.split("\n").first()}\" $entry.commitId"
        }
        result += cards.join("\n")
        result
    }


}
