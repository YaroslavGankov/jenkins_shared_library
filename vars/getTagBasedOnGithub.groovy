def call() {
    GIT_COMMIT_HASH_SHORT = sh(script: "git rev-parse HEAD | cut -c1-7", returnStdout: true).trim()
    DATE_PART =  sh(script: "git show -s --format=%ci | cut -d ' ' -f1 | tr -d -", returnStdout: true).trim() //format like 20230615
    println("DEBUG FROM FUNCTION getTagBasedOnGithub: env.GIT_BRANCH: "+env.GIT_BRANCH)//debug
    if ( env.GIT_BRANCH == null || env.GIT_BRANCH == '' ) {
        GIT_BRANCH_IN_FUNCTION = sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()
    } else {
        GIT_BRANCH_IN_FUNCTION = env.GIT_BRANCH
    }
    // GIT_BRANCH_IN_FUNCTION = 'origin/main' //for debug
    start_position_for_cut=GIT_BRANCH_IN_FUNCTION.indexOf('/')
    // println("start_position_for_cut: "+start_position_for_cut) //debug
    GIT_BRANCH_IN_FUNCTION = GIT_BRANCH_IN_FUNCTION.substring(start_position_for_cut+1)
    GIT_BRANCH_IN_FUNCTION = GIT_BRANCH_IN_FUNCTION.replaceAll('/','_')
    // println("1: "+GIT_BRANCH_IN_FUNCTION) //debug
    tagValue = "${DATE_PART}-${GIT_BRANCH_IN_FUNCTION}_${GIT_COMMIT_HASH_SHORT}"
    return tagValue
}