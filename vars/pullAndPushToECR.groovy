def call(String source_image_repo, String destination_image_repo_hub, String destination_image_name) {
  // echo Hello World ${name}. It is ${dayOfWeek}."
  sh """
    echo "${source_image_repo}"
    echo "${destination_image_repo_hub}"
    echo "${destination_image_name}"
    docker version
    aws --version
    aws sts get-caller-identity
    aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin ${destination_image_repo_hub}
    image_repo1="\${destination_image_repo_hub}/\${destination_image_name}";
    tag=`echo \$source_image_repo | cut -d : -f 2`
    image_tag_latest="\${image_repo1}:latest"
    image_tag_current="\${image_repo1}:\${tag}"
    echo "=============\nDEBUG:\nimage_tag_latest: \${image_tag_latest}\nimage_tag_current: \${image_tag_current}\n=============";
    docker pull "\${source_image_repo}"
    docker tag "\${source_image_repo}" "\${image_tag_latest}";
    docker tag "\${image_tag_latest}" "\${image_tag_current}";
    docker push "\${image_tag_latest}"
    docker push "\${image_tag_current}"
  """
}