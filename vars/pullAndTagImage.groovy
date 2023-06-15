def call(Map config = [:]) {
  echo "DEBUG FUNCTION: config.dockerhub_private: ${config.dockerhub_private}"
  echo "DEBUG FUNCTION: config.image: ${config.image}"
  //tip - private_image = image
  sh "docker pull ${dockerhub_private}/${image} && docker tag ${dockerhub_private}/${image} ${image};"
}

