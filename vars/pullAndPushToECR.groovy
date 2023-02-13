//Examples of usage:
//Example 1. Description: pull Jenkins Image and push it with tags: '2.375.2-jdk11' (don't change) and 'latest':
//pullAndPushToECR(sourceImageName:"jenkins/jenkins:2.375.2-jdk11",destinationImageHub:"<aws_account_id>.dkr.ecr.<region>.amazonaws.com",destinationImageName:"devops/jenkins")
//
//Example 2. //Description: pull docker:dind image and push it with tag: 'dind' (don't change) and don't push with tag 'latest':
//pullAndPushToECR(sourceImageName:"docker:dind",destinationImageHub:"<aws_account_id>.dkr.ecr.<region>.amazonaws.com",destinationImageName:"devops/docker",notPushTagLatest:true)
//
//Example 3. //Description: pull docker:dind image and push it with tag: 'dind-20220213-r1456' (additional tag) and don't push with tag 'latest':
//pullAndPushToECR(sourceImageName:"docker:dind",destinationImageHub:"<aws_account_id>.dkr.ecr.<region>.amazonaws.com",destinationImageName:"devops/docker",notPushTagLatest:true,additionalTag:"20220213-r1456")

def call(Map config = [:]) {
  //init
  if (config.awsRegion == null) {
    config.awsRegion="eu-west-1"
  }
  if (config.sourceImageHub == null) {
      image_repo0="${config.sourceImageName}"
  }
  else {
      image_repo0="${config.sourceImageHub}/${config.sourceImageName}"
  }
  image_repo1="${config.destinationImageHub}/${config.destinationImageName}"
  tag = config.sourceImageName.replaceAll(".*:","")
  image_tag_latest="${image_repo1}:latest"
  if (config.additionalTag == null) {
    image_tag_current="${image_repo1}:${tag}"
  }
  else {
    image_tag_current="${image_repo1}:${tag}-${config.additionalTag}"
  }
  string_to_output="Pull-push image parameters:\n     source:      ${image_repo0}\n     destination: ${image_tag_current}\n"
  if (config.notPushTagLatest == null) {
    string_to_output="${string_to_output}     destination: ${image_tag_latest}\n"
  }
  
  //info and debug
  echo "Input values:\n     sourceImageHub:       ${config.sourceImageHub}\n \
    sourceImageName:      ${config.sourceImageName}\n \
    destinationImageHub:  ${config.destinationImageHub}\n \
    destinationImageName: ${config.destinationImageName}\n \
    additionalTag:        ${config.additionalTag}"
    awsRegion:            ${config.awsRegion}\n \
    notPushTagLatest:     ${config.notPushTagLatest} (if null - push latest, if not null - not push latest tag to destination repo)\n \
  echo "${string_to_output}"
  
  //pull-push
  sh """
    aws ecr get-login-password --region ${config.awsRegion} | docker login --username AWS --password-stdin ${config.destinationImageHub}
    docker pull "${image_repo0}"
    docker tag "${image_repo0}" "${image_tag_latest}";
    docker tag "${image_tag_latest}" "${image_tag_current}";
    docker push "${image_tag_current}"
  """
  if (config.notPushTagLatest == null) {
    sh """
      docker push "${image_tag_latest}"
    """
  }
}