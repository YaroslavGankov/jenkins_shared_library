def call() {
    GIT_COMMIT_HASH_SHORT = sh(script: "git rev-parse HEAD | cut -c1-7", returnStdout: true).trim()
    DATE_PART =  sh(script: "git show -s --format=%ci | cut -d ' ' -f1 | tr -d -", returnStdout: true).trim() //format like 20230615
    GIT_BRANCH = sh(script: "git rev-parse --abbrev-ref HEAD | cut -d / -f 2- | tr '\\/' '_'", returnStdout: true).trim()
    tagValue = "${DATE_PART}-${GIT_BRANCH}_${GIT_COMMIT_HASH_SHORT}"
    return tagValue
}