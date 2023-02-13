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
    image_name_to_pull="${source_image_repo}";
    image_hub="${destination_image_repo_hub}";
    image_name="${destination_image_repo}";
    image_repo1="\${image_hub}/\${image_name}";
    tag=`echo \$image_name_to_pull | cut -d : -f 2`
    echo \$image_repo1;
    image_tag_latest="\${image_repo1}:latest"
    image_tag_current="\${image_repo1}:\${tag}"
    echo "image_tag_latest: \${image_tag_latest}";
    echo "image_tag_current: \${image_tag_current}";
    docker pull "\${image_name_to_pull}"
    docker tag "\${image_name_to_pull}" "\${image_tag_latest}";
    docker tag "\${image_tag_latest}" "\${image_tag_current}";
    docker push "\${image_tag_latest}"
    docker push "\${image_tag_current}"
  """
}