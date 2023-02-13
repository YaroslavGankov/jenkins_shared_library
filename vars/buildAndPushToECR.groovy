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
    awsRegion:            ${config.awsRegion}\n \
    notPushTagLatest:     ${config.notPushTagLatest} (if null - push latest, if not null - not push latest tag to destination repo)\n \
    additionalTag:        ${config.additionalTag}\n \
    pathToDockerfile:     ${config.pathToDockerfile}"
  echo "${string_to_output}"
  
  //pull-push
  sh """
    cd ${config.pathToDockerfile}
    docker build -t ${image_tag_latest} .
    docker tag "${image_repo0}" "${image_tag_latest}";
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