def call(Map config = [:]) {
  if (config.awsRegion == null) {
    config.awsRegion="eu-west-1"
  }
  if (config.sourceImageHub == null) {
      image_repo0="${config.sourceImageName}"
  } else {
      image_repo0="${config.sourceImageHub}/${config.sourceImageName}"
  }
  image_repo1_new="${config.destinationImageHub}/${config.destinationImageName}"
  tag_new = config.sourceImageName.replaceAll(".*:","")
  image_tag_latest_new="${image_repo1_new}:latest"
  if (config.additionalTag == null) {
    image_tag_current_new="${image_repo1_new}:${tag_new}"
  } else {
    image_tag_current_new="${image_repo1_new}:${tag_new}-${additionalTag}"
  }
  echo "Input values:\n     sourceImageHub:       ${config.sourceImageHub}\n \
    sourceImageName:      ${config.sourceImageName}\n \
    destinationImageHub:  ${config.destinationImageHub}\n \
    destinationImageName: ${config.destinationImageName}\n \
    awsRegion:            ${config.awsRegion}\n \
    notPushTagLatest:     ${config.notPushTagLatest} (if null - push latest, if not null - not push latest tag to destination repo)\n \
    additionalTag:        ${config.additionalTag}"
  string_to_output="Pull-push image parameters:\n     source:      ${image_repo0}\n     destination: ${image_tag_current_new}\n"
  if (config.notPushTagLatest == null) {
    string_to_output="${string_to_output}     destination: ${image_tag_latest_new}\n"
  }
  echo "${string_to_output}"
  sh """
    aws ecr get-login-password --region ${config.awsRegion} | docker login --username AWS --password-stdin ${config.destinationImageHub}
    docker pull "${image_repo0}"
    docker tag "${image_repo0}" "${image_tag_latest_new}";
    docker tag "${image_tag_latest_new}" "${image_tag_current_new}";
    docker push "${image_tag_current_new}"
  """
  if (config.notPushTagLatest == null) {
    sh """
      docker push "${image_tag_latest_new}"
    """
  }
}