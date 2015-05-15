import java.time.ZonedDateTime

class GitLogApp {

    public static void main(String[] args) {
        def (token, owner, project, base, head) = args;
        def result = new Github(token, owner, project).log(base, head)
        def commits = result.data['commits']
        commits
                .findAll { it['parents'].size() == 1 } // remove PR/merge commits
                .collect(rawCommitToCardLogEntry)
                .groupBy { entry -> entry.card.board }
                .sort()
                .each(print)
    }

    def static rawCommitToCardLogEntry = { row ->
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


    def static print = { val ->
        def board = val.key
        def cardEntries = val.value.sort()

        if (board == Card.MISC) {
            println "## Misc ##\n\n"
        } else {
            println "## Board $board##\n\n"
        }

        cardEntries.each { entry -> println "  ${entry.card.markdowned()} - [${entry.commitDate.toLocalDate()}] \"${entry.fullMessage.split("\n").first()}\" $entry.commitId" }
        println ""
    }

}
