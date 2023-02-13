def call(String source_image_repo, String destination_image_repo_hub, String destination_image_repo) {
  // echo Hello World ${name}. It is ${dayOfWeek}."
  sh """
    echo "${source_image_repo}"
    echo "${destination_image_repo_hub}"
    echo "${destination_image_repo}"
    docker version
    aws --version
    aws sts get-caller-identity
    aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin ${destination_image_repo_hub}
  """
}