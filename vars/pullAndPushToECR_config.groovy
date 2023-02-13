def call(Map config = [:]) {
  sh """
    echo '\n     sourceImageHub: ${config.sourceImageHub}\n \
    sourceImageName: ${config.sourceImageName}\n \
    destinationImageHub: ${config.destinationImageHub}\n \
    destinationImageName: ${config.destinationImageName}\n'
  """
  if (config.sourceImageHub == null) {
      image_repo0="${config.sourceImageName}"
  } else {
      image_repo0="${config.sourceImageHub}/${config.sourceImageName}"
  }
  sh """
    image_repo1="${config.destinationImageHub}/${config.destinationImageName}";
    tag=`echo ${config.sourceImageName} | cut -d : -f 2`
    image_tag_latest="\${image_repo1}:latest"
    image_tag_current="\${image_repo1}:\${tag}"
    echo "\n     source:      ${image_repo0}\n     destination: \${image_tag_current}\n     destination: \${image_tag_latest}\n"
    docker pull "${image_repo0}"
    docker tag "${image_repo0}" "\${image_tag_latest}";
    docker tag "\${image_tag_latest}" "\${image_tag_current}";
    docker push "\${image_tag_latest}"
    docker push "\${image_tag_current}"
  """
}