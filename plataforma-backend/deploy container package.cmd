docker login --username %GITHUB_USERNAME% --password %GITHUB_PACKAGE_PW% ghcr.io
docker build . -t ghcr.io/%GITHUB_USERNAME%/gamecms-backend-platform:latest
docker push ghcr.io/%GITHUB_USERNAME%/gamecms-backend-platform:latest
