#!/snap/bin/groovy
//for tests purposes
println("hello")
def GIT_BRANCH;
GIT_BRANCH='';
println("before: "+GIT_BRANCH)
if ( GIT_BRANCH == null || GIT_BRANCH == '' ) {
    GIT_BRANCH = 'origin/main'
    start_position_for_cut=GIT_BRANCH.indexOf('/')
    // println("start_position_for_cut: "+start_position_for_cut) //debug
    GIT_BRANCH = GIT_BRANCH.substring(start_position_for_cut+1)
    GIT_BRANCH = GIT_BRANCH.replaceAll('/','_')
    // println("1: "+GIT_BRANCH) //debug

    GIT_BRANCH = 'origin/ololo/DEV_1506'
    start_position_for_cut=GIT_BRANCH.indexOf('/')
    println("start_position_for_cut: "+start_position_for_cut)
    GIT_BRANCH = GIT_BRANCH.substring(start_position_for_cut+1)
    GIT_BRANCH = GIT_BRANCH.replaceAll('/','_')
    println("2: "+GIT_BRANCH)

    GIT_BRANCH = 'origin/ololo/DEV_1506/fix'
    start_position_for_cut=GIT_BRANCH.indexOf('/')
    println("start_position_for_cut: "+start_position_for_cut)
    GIT_BRANCH = GIT_BRANCH.substring(start_position_for_cut+1)
    GIT_BRANCH = GIT_BRANCH.replaceAll('/','_')
    println("3: "+GIT_BRANCH)

    GIT_BRANCH = 'master'
    start_position_for_cut=GIT_BRANCH.indexOf('/')
    println("start_position_for_cut: "+start_position_for_cut)
    GIT_BRANCH = GIT_BRANCH.substring(start_position_for_cut+1)
    GIT_BRANCH = GIT_BRANCH.replaceAll('/','_')
    println("4: "+GIT_BRANCH)
    //GIT_BRANCH = sh(script: "git rev-parse --abbrev-ref HEAD | cut -d / -f 2- | tr '\\/' '_'", returnStdout: true).trim()
}