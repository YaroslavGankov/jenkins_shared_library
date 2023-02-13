//Examples of usage:
//Example 1. Description: build docker image from Dockerfile and push it to repo
//buildAndPushToECR(destinationImageHub:"<aws_account_id>.dkr.ecr.<region>.amazonaws.com",destinationImageName:"devops/jenkins-agent",notPushTagLatest:"true",additionalTag:"4.11-1-jdk11",pathToDockerfile:"docker/jenkins/agents/jnlp4.11-1-jdk11")
//buildAndPushToECR(destinationImageHub:"<aws_account_id>.dkr.ecr.<region>.amazonaws.com",destinationImageName:"devops/jenkins-agent",notPushTagLatest:"true",tag:"4.11-1-jdk11",pathToDockerfile:"docker/jenkins/agents/jnlp4.11-1-jdk11")
//
//'tag' and 'additionalTag' - are the same - tag for the image. If 'tag' is defined then 'additionalTag' well be ignored. If tag isn't defined, 'additionalTag' will become 'tag'

def call(Map config = [:]) {
  //init
  if (config.awsRegion == null) {
    config.awsRegion="eu-west-1"
  }
  image_repo1="${config.destinationImageHub}/${config.destinationImageName}"
  if (config.tag == null) {
    tag = "${config.additionalTag}"
  }
  image_tag_latest="${image_repo1}:latest"
  image_tag_current="${image_repo1}:${tag}"
  string_to_output="Pull-push image parameters:\n     destination: ${image_tag_current}\n"
  if (config.notPushTagLatest == null) {
    string_to_output="${string_to_output}     destination: ${image_tag_latest}\n"
  }
  
  //info and debug
  echo "Input values:\n     sourceImageHub:       ${config.sourceImageHub}\n \
    destinationImageHub:  ${config.destinationImageHub}\n \
    destinationImageName: ${config.destinationImageName}\n \
    tag/additionalTag:    ${config.additionalTag}\n \
    awsRegion:            ${config.awsRegion}\n \
    notPushTagLatest:     ${config.notPushTagLatest} (if null - push latest, if not null - not push latest tag to destination repo)\n \
    pathToDockerfile:     ${config.pathToDockerfile}"
  echo "${string_to_output}"

  //pull-push
  sh """
    cd ${config.pathToDockerfile}
    docker build -t ${image_tag_latest} .
    docker tag "${image_tag_latest}" "${image_tag_current}";
    aws ecr get-login-password --region ${config.awsRegion} | docker login --username AWS --password-stdin ${config.destinationImageHub}
    docker push "${image_tag_current}"
  """
  if (config.notPushTagLatest == null) {
    sh """
      docker push "${image_tag_latest}"
    """
  }
}