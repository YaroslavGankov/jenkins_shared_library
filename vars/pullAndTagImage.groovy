// // pull image from private repo and tag it as image name is
// Map parameters:
// 'dockerhub_private' - ECR private repo to login
// 'image' - image in the repo without repo-prefix. Example: 'docker:dind'
//
// // example of usage with variables from params:
// pullAndTagImage(dockerhub_private:params.ecr_repo,image:params.image_to_pull)
def call(Map config = [:]) {
  //echo "DEBUG FUNCTION: config.dockerhub_private: ${config.dockerhub_private}"
  //echo "DEBUG FUNCTION: config.image: ${config.image}"
  //tip - private_image = image
  sh "docker pull ${config.dockerhub_private}/${config.image} && docker tag ${config.dockerhub_private}/${config.image} ${config.image};"
  //sh "docker images" //debug
}
