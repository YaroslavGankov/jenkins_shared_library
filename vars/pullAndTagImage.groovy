//pull image from private repo and tag it as image name is
def call(Map config = [:]) {
  echo "DEBUG FUNCTION: config.dockerhub_private: ${config.dockerhub_private}"
  echo "DEBUG FUNCTION: config.image: ${config.image}"
  //tip - private_image = image
  sh "docker pull ${config.dockerhub_private}/${config.image} && docker tag ${config.dockerhub_private}/${config.image} ${config.image};"
  sh "docker images" //debug
}
