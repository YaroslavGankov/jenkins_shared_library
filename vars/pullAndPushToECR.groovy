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
  image_tag_current_new="${image_repo1_new}:${tag_new}"
  println(image_tag_latest_new)
  println(image_tag_current_new)
  sh """
    echo '\n     sourceImageHub:       ${config.sourceImageHub}\n \
    sourceImageName:      ${config.sourceImageName}\n \
    destinationImageHub:  ${config.destinationImageHub}\n \
    destinationImageName: ${config.destinationImageName}\n \
    awsRegion:            ${config.awsRegion}\n \
    notPushTagLatest:     ${config.notPushTagLatest} (if null - push latest, if not null - not push latest tag to destination repo)'
    image_repo1="${config.destinationImageHub}/${config.destinationImageName}";
    tag=`echo ${config.sourceImageName} | cut -d : -f 2`
    echo "\n     source:      ${image_repo0}\n     destination: \${image_tag_current}\n     destination: \${image_tag_latest}\n"
    aws ecr get-login-password --region ${config.awsRegion} | docker login --username AWS --password-stdin ${config.destinationImageHub}
    docker pull "${image_repo0}"
    docker tag "${image_repo0}" "${image_tag_latest_new}";
    docker tag "${image_tag_latest_new}" "${image_tag_current_new}";
    docker push "${image_tag_current_new}"
  """
  if (config.notPushTagLatest == null) {
    sh """
      echo "${image_tag_latest_new}"
      docker push "${image_tag_latest_new}"
    """
  }
}