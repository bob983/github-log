class GitLogApp {

    public static void main(String[] args) {
        def (token, owner, project, base, head) = args;
        def github = new Github(token, owner, project)

        def changelog = github.log(base, head)

        println "\n\n$changelog\n\n"

        def release = github.getReleaseForTag(head)
        if(release) {
            println "Found release ${release['name']} from ${release['created_at']}, ID=${release['id']}"
            github.updateRelease(release['id'], changelog)
        } else {
            def res = head =~ /v(.*)/
            if(res.matches()) {
                def releaseName = res[0][1]
                println "Creating release $releaseName"
                github.createRelease(releaseName, head, changelog)
            } else {
                println "Tag $head does not have expected format `vSomething`"
            }
        }
    }

}
